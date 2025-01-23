package ui.envio;

import com.jfoenix.controls.*;
import exception.SelectException;
import factories.EnvioFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import models.Envio;
import logicInterface.EnvioManager;
import utils.UtilsMethods;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ListChangeListener;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javax.ws.rs.core.GenericType;
import models.EnvioRutaVehiculo;
import models.Estado;
import models.Ruta;
import models.User;
import models.Vehiculo;
import ui.paquete.PaqueteController;

public class EnvioController {

    private static final Logger LOGGER = Logger.getLogger(EnvioController.class.getName());

    @FXML
    private JFXDatePicker fromDatePicker;

    @FXML
    private JFXDatePicker toDatePicker;

    @FXML
    private JFXComboBox<String> estadoFilterComboBox;

    @FXML
    private JFXTextField numeroPaquetesTextField;

    @FXML
    private TableView<Envio> table;

    @FXML
    private TableColumn<Envio, Integer> idColumn;

    @FXML
    private TableColumn<Envio, String> fechaEnvioColumn;

    @FXML
    private TableColumn<Envio, String> fechaEntregaColumn;

    @FXML
    private TableColumn<Envio, String> estadoColumn;

    @FXML
    private TableColumn<Envio, Integer> numPaquetesColumn;

    @FXML
    private TableColumn<Envio, String> creadorEnvioColumn;

    @FXML
    private TableColumn<Envio, String> rutaColumn;

    @FXML
    private TableColumn<Envio, String> vehiculoColumn;

    @FXML
    private JFXButton applyDateFilterButton, applyEstadoFilterButton, applyNumPaquetesFilterButton;

    @FXML
    private JFXButton addShipmentBtn, removeShipmentBtn, printReportBtn;

    private EnvioManager envioService;
    private ObservableList<Envio> envioList;

    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void initStage(Parent root) {
        LOGGER.info("Initialising Envio window.");
        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.setTitle("Envio");
        stage.setResizable(false);
        stage.centerOnScreen();

        envioService = EnvioFactory.getEnvioInstance();
        envioList = FXCollections.observableArrayList();

        table.setEditable(true);
        idColumn.setEditable(false);
        estadoColumn.setEditable(false);
        rutaColumn.setEditable(false);

        estadoFilterComboBox.getItems().setAll(Estado.ENTREGADO.toString(),
                Estado.EN_REPARTO.toString(),
                Estado.PREPARACION.toString());

        setUpTableColumns();
        table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        configureRemoveButton();

        loadInitialData();
        stage.show();
    }

    private void setUpTableColumns() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        estadoColumn.setCellValueFactory(new PropertyValueFactory<>("estado"));
        numPaquetesColumn.setCellValueFactory(new PropertyValueFactory<>("numPaquetes"));
        creadorEnvioColumn.setCellValueFactory(new PropertyValueFactory<>("creadorEnvio"));
        rutaColumn.setCellValueFactory(new PropertyValueFactory<>("ruta"));
        vehiculoColumn.setCellValueFactory(new PropertyValueFactory<>("vehiculo"));

        fechaEnvioColumn.setCellValueFactory(cellData -> {
            Date fechaEnvio = cellData.getValue().getFechaEnvio();
            if (fechaEnvio != null) {
                return new SimpleStringProperty(new SimpleDateFormat("dd/MM/yyyy").format(fechaEnvio));
            } else {
                return new SimpleStringProperty("");
            }
        });
        
        fechaEntregaColumn.setCellValueFactory(cellData -> {
            Date fechaEntrega = cellData.getValue().getFechaEntrega();
            if (fechaEntrega != null) {
                return new SimpleStringProperty(new SimpleDateFormat("dd/MM/yyyy").format(fechaEntrega));
            } else {
                return new SimpleStringProperty("");
            }
        });
    }

    private void loadInitialData() {
        try {
            List<Envio> envios = envioService.findAll_XML(new GenericType<List<Envio>>() {
            });
            if (envios != null) {
                envioList.setAll(envios);
                table.setItems(envioList);
                
                for (Envio e : envioList) {
                    System.out.println(e);
                }
                LOGGER.info("Insertando datos.");
            }
        } catch (Exception ex) {
            Logger.getLogger(EnvioController.class.getName()).log(Level.SEVERE, "Error cargando los datos iniciales.", ex);
            new UtilsMethods().showAlert("Error", "Error al cargar los datos iniciales.");
        }
    }

    @FXML
    private void applyDateFilter(ActionEvent event) {
        try {
            Date from = fromDatePicker.getValue() != null ? java.sql.Date.valueOf(fromDatePicker.getValue()) : null;
            Date to = toDatePicker.getValue() != null ? java.sql.Date.valueOf(toDatePicker.getValue()) : null;

            if (from == null && to == null) {
                throw new IllegalArgumentException("Debe llenar al menos uno de los campos de fecha.");
            }

            List<Envio> filteredData = envioService.filterByDates_XML(new GenericType<List<Envio>>() {
            }, from.toString(), to.toString());
            envioList.setAll(filteredData);
        } catch (IllegalArgumentException e) {
            new UtilsMethods().showAlert("Error de filtro", e.getMessage());
        } catch (Exception e) {
            new UtilsMethods().showAlert("Error al filtrar por fechas", e.getMessage());
        }
    }

    @FXML
    private void applyEstadoFilter(ActionEvent event) {
        try {
            String estado = estadoFilterComboBox.getValue();
            if (estado == null || estado.isEmpty()) {
                throw new IllegalArgumentException("El campo de estado está vacío.");
            }

            List<Envio> filteredData = envioService.filterEstado_XML(new GenericType<List<Envio>>() {
            }, estado);
            envioList.setAll(filteredData);
        } catch (IllegalArgumentException e) {
            new UtilsMethods().showAlert("Error de filtro", e.getMessage());
        } catch (Exception e) {
            new UtilsMethods().showAlert("Error al filtrar por estado", e.getMessage());
        }
    }

    @FXML
    private void applyNumPaquetesFilter(ActionEvent event) {
        try {
            String numPaquetes = numeroPaquetesTextField.getText();
            if (numPaquetes == null || numPaquetes.isEmpty()) {
                throw new IllegalArgumentException("El campo está vacío, inserta un número de paquetes.");
            }

            int num = Integer.parseInt(numPaquetes);
            if (num <= 0) {
                throw new IllegalArgumentException("El número de paquetes debe ser mayor a 0.");
            }

            List<Envio> filteredData = envioService.filterNumPaquetes_XML(new GenericType<List<Envio>>() {
            }, num);
            envioList.setAll(filteredData);
        } catch (IllegalArgumentException e) {
            new UtilsMethods().showAlert("Error de filtro", e.getMessage());
        } catch (Exception e) {
            new UtilsMethods().showAlert("Error al filtrar por número de paquetes", e.getMessage());
        }
    }

    @FXML
    private void addEnvio(ActionEvent event) {
        try {
            Envio nuevoEnvio = new Envio();
//            EnvioRutaVehiculo fk = new EnvioRutaVehiculo();
//            Ruta fkRuta = new Ruta();
//            Vehiculo fkVehiculo = new Vehiculo();
//            fk.setRuta(fkRuta);
//            fk.setVehiculo(fkVehiculo);
//            nuevoEnvio.setEnvioRutaVehiculo(fk);
            envioList.add(nuevoEnvio);
            envioService.create_XML(nuevoEnvio);
        } catch (Exception e) {
            new UtilsMethods().showAlert("Error al añadir envío", e.getMessage());
        }
    }

    @FXML
    private void removeEnvio(ActionEvent event) {
        try {
            ObservableList<Envio> selectedEnvios = table.getSelectionModel().getSelectedItems();
            if (selectedEnvios.isEmpty()) {
                throw new IllegalArgumentException("Debes seleccionar al menos un envío para eliminar.");
            }

            for (Envio envio : selectedEnvios) {
                envioService.remove(envio.getId());
                envioList.remove(envio);
            }
        } catch (IllegalArgumentException e) {
            new UtilsMethods().showAlert("Error", e.getMessage());
        } catch (Exception e) {
            new UtilsMethods().showAlert("Error al eliminar envío(s)", e.getMessage());
        }
    }

    private void configureRemoveButton() {
        ObservableList<Envio> selectedItems = table.getSelectionModel().getSelectedItems();

        selectedItems.addListener((ListChangeListener<Envio>) change -> {
            removeShipmentBtn.setDisable(selectedItems.isEmpty());
        });

        removeShipmentBtn.setDisable(true);
    }

    @FXML
    private void printReport(ActionEvent event) {
        new UtilsMethods().showAlert("Reporte", "Funcionalidad de impresión aún no implementada.");
    }
}
