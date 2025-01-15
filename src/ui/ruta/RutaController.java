package ui.ruta;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import exception.SelectException;
import factories.RutaManagerFactory;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.GenericType;
import logicInterface.RutaManager;
import models.Ruta;
import service.RutaRESTClient;
import utils.UtilsMethods;

public class RutaController {

    private static final Logger logger = Logger.getLogger(RutaController.class.getName());

    @FXML
    private JFXDatePicker fromDatePicker;

    @FXML
    private JFXDatePicker toDatePicker;

    @FXML
    private JFXButton applyFilterButton;

    @FXML
    private JFXComboBox<String> sizeFilterComboBox;

    @FXML
    private JFXComboBox<String> sizeFilterComboBox1;

    @FXML
    private JFXTextField filterValueField;

    @FXML
    private JFXTextField searchTextField;

    @FXML
    private JFXButton searchButton;

    @FXML
    private JFXButton searchButton1;

    @FXML
    private TableColumn<Ruta, Integer> localizadorColumn;

    @FXML
    private TableColumn<Ruta, String> origenColumn;

    @FXML
    private TableColumn<Ruta, String> destinoColumn;

    @FXML
    private TableColumn<Ruta, Float> distanciaColumn;

    @FXML
    private TableColumn<Ruta, Integer> tiempoColumn;

    @FXML
    private TableColumn<Ruta, Date> fechaColumn;

    @FXML
    private TableColumn<Ruta, Integer> numeroVehiculosColumn;

    @FXML
    private JFXButton addShipmentBtn;

    @FXML
    private JFXButton removeShipmentBtn;

    @FXML
    private JFXButton printReportBtn;

    private RutaManager rutaManager;

    private ObservableList<Ruta> rutaData;

    private Stage stage;

    @FXML
    private TableView<Ruta> rutaTable;

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
            fechaColumn.setCellValueFactory(new PropertyValueFactory<>("FechaCreacion"));
            numeroVehiculosColumn.setCellValueFactory(new PropertyValueFactory<>("numVehiculos"));

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
    
     /*@FXML
    private void applyDateFilter(ActionEvent event) {
        try {
            Date from = fromDatePicker.getValue() != null ? java.sql.Date.valueOf(fromDatePicker.getValue()) : null;
            Date to = toDatePicker.getValue() != null ? java.sql.Date.valueOf(toDatePicker.getValue()) : null;

            if (from == null && to == null) {
                throw new IllegalArgumentException("Debe llenar al menos uno de los campos de fecha.");
            }

            List<Ruta> rutas = RutaManagerFactory.getRutaManager().filterBy2Dates_XML(new GenericType<List<Ruta>>() {
            }, from.toString(), to.toString());
            rutas.setAll(rutas);
        } catch (IllegalArgumentException e) {
            new UtilsMethods().showAlert("Error de filtro", e.getMessage());
        } catch (Exception e) {
            new UtilsMethods().showAlert("Error al filtrar por fechas", e.getMessage());
        }
    }*/

    private void addShipment() {
        // Lógica para agregar una nueva ruta
    }

    private void removeShipment() {
        // Lógica para eliminar una ruta
    }

    private void printReport() {
        // Lógica para imprimir un informe
    }
}
