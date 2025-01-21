package ui.ruta;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import exception.SelectException;
import factories.RutaManagerFactory;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.util.converter.FloatStringConverter;
import javafx.util.converter.IntegerStringConverter;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.GenericType;
import logicInterface.RutaManager;
import models.Ruta;
import service.RutaRESTClient;
import utils.UtilsMethods;

public class RutaController {

    private static final Logger logger = Logger.getLogger(RutaController.class.getName());

    @FXML
    private JFXDatePicker fromDatePicker, toDatePicker;
    @FXML
    private JFXButton applyFilterButton, searchButton, searchButton1, addShipmentBtn, removeShipmentBtn, printReportBtn;
    @FXML
    private JFXComboBox<String> sizeFilterComboBox, sizeFilterComboBox1;
    @FXML
    private JFXTextField filterValueField, searchTextField;
    @FXML
    private TableView<Ruta> rutaTable;
    @FXML
    private TableColumn<Ruta, Integer> localizadorColumn, tiempoColumn, numeroVehiculosColumn;
    @FXML
    private TableColumn<Ruta, String> origenColumn, destinoColumn;
    @FXML
    private TableColumn<Ruta, Float> distanciaColumn;
    @FXML
    private TableColumn<Ruta, Date> fechaColumn;
    private RutaManager rutaManager;

    private ObservableList<Ruta> rutaData;

    private Stage stage;

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    public void initialize(Parent root) {
        rutaManager = RutaManagerFactory.getRutaManager();

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Ruta");
        stage.setResizable(false);
        stage.centerOnScreen();
        stage.getIcons().add(new Image("/image/fleet_icon.png"));
        removeShipmentBtn.setDisable(true);

        sizeFilterComboBox.setItems(FXCollections.observableArrayList("Filter by Time", "Filter by Distance"));
        sizeFilterComboBox1.setItems(FXCollections.observableArrayList(">", "<", "="));

        sizeFilterComboBox.setOnAction(event -> updateUnitLabel());
        searchButton1.setOnAction(event -> applyFilterButtonAction());
        searchButton.setOnAction(event -> searchByLocalizador());

        addShipmentBtn.setOnAction(event -> addShipment());
        removeShipmentBtn.setOnAction(event -> removeShipment());
        printReportBtn.setOnAction(event -> printReport());

        rutaTable.setEditable(true);

        rutaTable.getSelectionModel().setSelectionMode(javafx.scene.control.SelectionMode.MULTIPLE);
        rutaTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            removeShipmentBtn.setDisable(rutaTable.getSelectionModel().getSelectedItems().isEmpty());
        });

        configureEditableColumns();

        try {
            loadRutaData();
        } catch (SelectException ex) {
            Logger.getLogger(RutaController.class.getName()).log(Level.SEVERE, null, ex);
        }

        stage.show();
    }

    private void loadRutaData() throws SelectException {
        try {
            List<Ruta> rutas = RutaManagerFactory.getRutaManager().findAll_XML(new GenericType<List<Ruta>>() {
            });

            rutaData = FXCollections.observableArrayList(rutas);

            localizadorColumn.setCellValueFactory(new PropertyValueFactory<>("localizador"));
            origenColumn.setCellValueFactory(new PropertyValueFactory<>("origen"));
            destinoColumn.setCellValueFactory(new PropertyValueFactory<>("destino"));
            distanciaColumn.setCellValueFactory(new PropertyValueFactory<>("distancia"));
            tiempoColumn.setCellValueFactory(new PropertyValueFactory<>("tiempo"));
            //fechaColumn.setCellValueFactory(new PropertyValueFactory<>("FechaCreacion"));
            numeroVehiculosColumn.setCellValueFactory(new PropertyValueFactory<>("numVehiculos"));

            fechaColumn.setCellValueFactory(new PropertyValueFactory<>("FechaCreacion"));
            fechaColumn.setCellFactory(column -> {
                return new javafx.scene.control.TableCell<Ruta, Date>() {
                    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

                    @Override
                    protected void updateItem(Date item, boolean empty) {
                        super.updateItem(item, empty);

                        if (empty || item == null) {
                            setText(null);
                        } else {
                            setText(dateFormat.format(item));
                        }
                    }
                };
            });

            rutaTable.setItems(rutaData);
        } catch (WebApplicationException e) {
            logger.log(Level.SEVERE, "Error loading ruta data", e);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unexpected error", e);
        }
    }

    private void updateUnitLabel() {
        String selectedFilter = sizeFilterComboBox.getValue();
        if ("Filter by Time".equals(selectedFilter)) {
            sizeFilterComboBox1.setPromptText("Horas");
        } else if ("Filter by Distance".equals(selectedFilter)) {
            sizeFilterComboBox1.setPromptText("Km");
        } else {
            sizeFilterComboBox1.setPromptText("Unit");
        }
    }

    private void applyFilterButtonAction() {
        String filterType = sizeFilterComboBox.getValue();
        String comparisonOperator = sizeFilterComboBox1.getValue();
        String filterValue = filterValueField.getText().trim();

        // Si el campo de valor del filtro está vacío, recargar todas las rutas
        if (filterValue.isEmpty()) {
            try {
                loadRutaData();
                logger.info("Recargando todas las rutas porque el campo de filtro está vacío.");
            } catch (SelectException e) {
                logger.log(Level.SEVERE, "Error al recargar las rutas", e);
            }
            return;
        }

        try {
            double value = Double.parseDouble(filterValue);
            if (value < 0) {
                showAlert("Error", "El valor del filtro no puede ser negativo.");
                return;
            }
        } catch (NumberFormatException e) {
            showAlert("Error", "El valor del filtro debe ser un número válido.");
            return;
        }

        if (comparisonOperator == null || comparisonOperator.isEmpty()) {
            showAlert("Error", "Debe seleccionar un operador de comparación (>, <, =).");
            return;
        }

        try {
            if ("Filter by Time".equals(filterType)) {
                applyTimeFilter(comparisonOperator, filterValue);
            } else if ("Filter by Distance".equals(filterType)) {
                applyDistanceFilter(comparisonOperator, filterValue);
            }
        } catch (WebApplicationException e) {
            logger.log(Level.SEVERE, "Error applying filter", e);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unexpected error while applying filter", e);
        }
    }

    private void applyTimeFilter(String comparisonOperator, String filterValue) {
        switch (comparisonOperator) {
            case ">":
                rutaData.clear();
                rutaData.addAll(rutaManager.filterTiempoMayor_XML(new GenericType<List<Ruta>>() {
                }, filterValue));
                break;
            case "<":
                rutaData.clear();
                rutaData.addAll(rutaManager.filterTiempoMenor_XML(new GenericType<List<Ruta>>() {
                }, filterValue));
                break;
            case "=":
                rutaData.clear();
                rutaData.addAll(rutaManager.filterTiempoIgual_XML(new GenericType<List<Ruta>>() {
                }, filterValue));
                break;
            default:
                logger.warning("Invalid comparison operator for Time filter.");
                break;
        }
        rutaTable.setItems(rutaData);
    }

    private void applyDistanceFilter(String comparisonOperator, String filterValue) {
        switch (comparisonOperator) {
            case ">":
                rutaData.clear();
                rutaData.addAll(rutaManager.filterDistanciaMayor_XML(new GenericType<List<Ruta>>() {
                }, filterValue));
                break;
            case "<":
                rutaData.clear();
                rutaData.addAll(rutaManager.filterDistanciaMenor_XML(new GenericType<List<Ruta>>() {
                }, filterValue));
                break;
            case "=":
                rutaData.clear();
                rutaData.addAll(rutaManager.filterDistanciaIgual_XML(new GenericType<List<Ruta>>() {
                }, filterValue));
                break;
            default:
                logger.warning("Invalid comparison operator for Distance filter.");
                break;
        }
        rutaTable.setItems(rutaData);
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void searchByLocalizador() {
        String searchText = searchTextField.getText().trim();
        if (searchText.isEmpty()) {
            try {
                loadRutaData();
                logger.info("Recargando todas las rutas.");
            } catch (SelectException e) {
                logger.log(Level.SEVERE, "Error al recargar las rutas", e);
            }
            return;
        }

        try {
            Integer localizador = Integer.parseInt(searchText);

            Ruta ruta = rutaManager.findByLocalizadorInteger_XML(Ruta.class, localizador);

            if (ruta != null) {
                rutaData.clear();
                rutaData.add(ruta);
                rutaTable.setItems(rutaData);
                logger.info("Ruta filtrada cargada correctamente.");
            } else {
                logger.info("No se encontró ninguna ruta con el localizador proporcionado.");
            }
        } catch (NumberFormatException e) {
            logger.warning("El texto de búsqueda no es un número válido.");
        } catch (WebApplicationException e) {
            logger.log(Level.SEVERE, "Error al buscar por localizador", e);
        }
    }

    private void addShipment() {
        try {
            // Crear una nueva ruta vacía
            Ruta nuevaRuta = new Ruta();
            nuevaRuta.setOrigen("");
            nuevaRuta.setDestino("");
            nuevaRuta.setDistancia(0.0f);
            nuevaRuta.setTiempo(0);
            nuevaRuta.setNumVehiculos(0);
            nuevaRuta.setFechaCreacion(new Date());

            // Enviar la nueva ruta al servidor
            rutaManager.edit_XML(nuevaRuta, "0"); // Usar "0" o el ID correspondiente para el servidor

            // Refrescar la tabla recargando los datos desde el servidor
            loadRutaData();

            logger.info("Nueva ruta vacía añadida y tabla actualizada correctamente.");
        } catch (WebApplicationException e) {
            logger.log(Level.SEVERE, "Error al añadir una nueva ruta", e);
            showAlert("Error", "No se pudo añadir la nueva ruta.");
        } catch (SelectException e) {
            logger.log(Level.SEVERE, "Error al refrescar la tabla después de añadir la ruta", e);
            showAlert("Error", "No se pudo actualizar la tabla después de añadir la ruta.");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error inesperado", e);
            showAlert("Error", "Error inesperado al añadir la nueva ruta.");
        }
    }

    private void removeShipment() {
        List<Ruta> selectedRutas = rutaTable.getSelectionModel().getSelectedItems();

        try {
            for (Ruta ruta : selectedRutas) {
                rutaManager.remove(String.valueOf(ruta.getLocalizador())); // Llama al método REST con el localizador
            }
            rutaData.removeAll(selectedRutas); // Elimina las rutas seleccionadas de la tabla
            logger.info("Rutas eliminadas correctamente.");
        } catch (WebApplicationException e) {
            logger.log(Level.SEVERE, "Error al eliminar rutas", e);
            showAlert("Error", "Ocurrió un error al eliminar las rutas.");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error inesperado", e);
            showAlert("Error", "Error inesperado al eliminar rutas.");
        }
    }

    private void printReport() {
        // Lógica para imprimir un informe
    }

    private void configureEditableColumns() {
    // Editable "Origen" column
    origenColumn.setCellFactory(TextFieldTableCell.forTableColumn());
    origenColumn.setOnEditCommit(
            new EventHandler<CellEditEvent<Ruta, String>>() {
        @Override
        public void handle(CellEditEvent<Ruta, String> t) {
            Ruta ruta = t.getTableView().getItems().get(t.getTablePosition().getRow());
            ruta.setOrigen(t.getNewValue());

            try {
                rutaManager.edit_XML(ruta, String.valueOf(ruta.getLocalizador()));
                logger.info("Origen actualizado en el servidor: " + t.getNewValue());
            } catch (WebApplicationException e) {
                logger.log(Level.SEVERE, "Error al actualizar origen en el servidor", e);
                showAlert("Error", "No se pudo actualizar el origen.");
            }
        }
    });

    // Editable "Destino" column
    destinoColumn.setCellFactory(TextFieldTableCell.forTableColumn());
    destinoColumn.setOnEditCommit(
            new EventHandler<CellEditEvent<Ruta, String>>() {
        @Override
        public void handle(CellEditEvent<Ruta, String> t) {
            Ruta ruta = t.getTableView().getItems().get(t.getTablePosition().getRow());
            ruta.setDestino(t.getNewValue());

            try {
                rutaManager.edit_XML(ruta, String.valueOf(ruta.getLocalizador()));
                logger.info("Destino actualizado en el servidor: " + t.getNewValue());
            } catch (WebApplicationException e) {
                logger.log(Level.SEVERE, "Error al actualizar destino en el servidor", e);
                showAlert("Error", "No se pudo actualizar el destino.");
            }
        }
    });

    // Editable "Distancia" column (corrected for Float)
    distanciaColumn.setCellFactory(TextFieldTableCell.forTableColumn(new FloatStringConverter()));
    distanciaColumn.setOnEditCommit(
            new EventHandler<CellEditEvent<Ruta, Float>>() {
        @Override
        public void handle(CellEditEvent<Ruta, Float> t) {
            Ruta ruta = t.getTableView().getItems().get(t.getTablePosition().getRow());
            Float nuevaDistancia = t.getNewValue();

            // Validar si la distancia es un valor positivo
            if (nuevaDistancia < 0) {
                showAlert("Error", "La distancia no puede ser negativa.");
                return;
            }

            ruta.setDistancia(nuevaDistancia);

            try {
                rutaManager.edit_XML(ruta, String.valueOf(ruta.getLocalizador()));
                logger.info("Distancia actualizada en el servidor: " + nuevaDistancia);
            } catch (WebApplicationException e) {
                logger.log(Level.SEVERE, "Error al actualizar distancia en el servidor", e);
                showAlert("Error", "No se pudo actualizar la distancia.");
            }
        }
    });

    // Editable "Tiempo" column (corrected for Integer)
    tiempoColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
    tiempoColumn.setOnEditCommit(
            new EventHandler<CellEditEvent<Ruta, Integer>>() {
        @Override
        public void handle(CellEditEvent<Ruta, Integer> t) {
            Ruta ruta = t.getTableView().getItems().get(t.getTablePosition().getRow());
            Integer nuevoTiempo = t.getNewValue();

            // Validar si el tiempo es un valor positivo
            if (nuevoTiempo < 0) {
                showAlert("Error", "El tiempo no puede ser negativo.");
                return;
            }

            ruta.setTiempo(nuevoTiempo);

            try {
                rutaManager.edit_XML(ruta, String.valueOf(ruta.getLocalizador()));
                logger.info("Tiempo actualizado en el servidor: " + nuevoTiempo);
            } catch (WebApplicationException e) {
                logger.log(Level.SEVERE, "Error al actualizar tiempo en el servidor", e);
                showAlert("Error", "No se pudo actualizar el tiempo.");
            }
        }
    });
    }

}
