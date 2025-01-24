package ui.envio;

import cellFactories.EnvioDateEditingCell;
import com.jfoenix.controls.*;
import exception.SelectException;
import factories.EnvioFactory;
import factories.EnvioRutaVehiculoFactory;
import factories.SignableFactory;
import factories.VehicleFactory;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ListChangeListener;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.cell.ChoiceBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javax.ws.rs.core.GenericType;
import logicInterface.EnvioRutaVehiculoManager;
import logicInterface.PaqueteManager;
import logicInterface.UserManager;
import logicInterface.VehicleManager;
import models.EnvioRutaVehiculo;
import models.Estado;
import models.Paquete;
import models.Ruta;
import models.User;
import models.Vehiculo;

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
    private TableColumn<Envio, Date> fechaEnvioColumn;

    @FXML
    private TableColumn<Envio, Date> fechaEntregaColumn;

    @FXML
    private TableColumn<Envio, Estado> estadoColumn;

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
    private PaqueteManager paqueteService;
    private UserManager userService;
    private VehicleManager vehicleService;
    private EnvioRutaVehiculoManager envioRutaVehiculoService;
    private ObservableList<Envio> envioList;
    private ArrayList<Paquete> paqueteList;
    private ArrayList<User> userList = new ArrayList<>();
    private List<Vehiculo> vehiculoList = new ArrayList<>();
    private List<EnvioRutaVehiculo> envioRutaVehiculoList = new ArrayList<>();

    private List<String> userNamesList = new ArrayList<>();
    private List<String> vehiculoMatriculaList = new ArrayList<>();

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
        userService = SignableFactory.getSignable();
        vehicleService = VehicleFactory.getVehicleInstance();
        envioRutaVehiculoService = EnvioRutaVehiculoFactory.getEnvioRutaVehiculoInstance();

        table.setEditable(true);
        idColumn.setEditable(false);
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
        try {
            idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
            estadoColumn.setCellValueFactory(new PropertyValueFactory<>("estado"));
            estadoColumn.setCellFactory(ChoiceBoxTableCell.forTableColumn(Estado.values()));
            estadoColumn.setOnEditCommit(event -> {
                Envio envio = event.getRowValue();
                Estado nuevoEstado = event.getNewValue();
                envio.setEstado(nuevoEstado);
                try {
                    envioService.edit_XML(envio, envio.getId().toString());
                } catch (Exception e) {
                    LOGGER.severe("Error al actualizar el estado del envío: " + e.getMessage());
                    new UtilsMethods().showAlert("Error al actualizar estado", e.getMessage());
                }
            });

            numPaquetesColumn.setCellValueFactory(new PropertyValueFactory<>("numPaquetes"));

            creadorEnvioColumn.setCellValueFactory(new PropertyValueFactory<>("creadorEnvio"));
            userList = userService.findAll();

            for (User user : userList) {
                userNamesList.add(user.getName());
            }
            ObservableList<String> userNamesObservableList = FXCollections.observableArrayList(userNamesList);
            creadorEnvioColumn.setCellFactory(ChoiceBoxTableCell.forTableColumn(userNamesObservableList));
            creadorEnvioColumn.setOnEditCommit(event -> {
                Envio envio = event.getRowValue();
                String nombre = event.getNewValue();
                envio.setCreadorEnvio(nombre);
                try {
                    envioService.edit_XML(envio, envio.getId().toString());
                } catch (Exception e) {
                    LOGGER.severe("Error al actualizar el creador del envío: " + e.getMessage());
                    new UtilsMethods().showAlert("Error al actualizar estado", e.getMessage());
                }
            });

            rutaColumn.setCellValueFactory(new PropertyValueFactory<>("ruta"));

            vehiculoColumn.setCellValueFactory(new PropertyValueFactory<>("vehiculo"));
            vehiculoList = vehicleService.findAllVehiculos();

            for (Vehiculo vehiculo : vehiculoList) {
                vehiculoMatriculaList.add(vehiculo.getMatricula());
            }
            ObservableList<String> vehiclesNamesObservableList = FXCollections.observableArrayList(vehiculoMatriculaList);
            vehiculoColumn.setCellFactory(ChoiceBoxTableCell.forTableColumn(vehiclesNamesObservableList));
            vehiculoColumn.setOnEditCommit(event -> {
                Envio envio = event.getRowValue();
                String matricula = "";
                Integer idEnvioRutaVehiculo = 0; 
               if (event.getNewValue() != null) {
                    matricula = event.getNewValue();
                } else {
                    return;
                }
                String localizador = "";
                try {
                    envioRutaVehiculoList = envioRutaVehiculoService.findAll_XML(new GenericType<List<EnvioRutaVehiculo>>() {
                    });

                    int i = 0;
                    boolean encontrado = false;
                    while (i < envioRutaVehiculoList.size() || !encontrado) {
                        
                        //Error en los getters de vehiculo y ruta
                        List<Vehiculo> vComprobar = vehicleService.findAllVehiculosByPlate(matricula);
                        if (vComprobar.get(0).getMatricula().equalsIgnoreCase(matricula)) {
                            localizador = envioRutaVehiculoService.getRutaId(vComprobar.get(0).getId());
                            idEnvioRutaVehiculo = envioRutaVehiculoService.getId(vComprobar.get(0).getId());
                            encontrado = true;
                        }
                    }

                } catch (Exception ex) {
                    Logger.getLogger(EnvioController.class.getName()).log(Level.SEVERE, null, ex);
                }

                envio.setRuta(localizador);
                envio.setVehiculo(matricula);
                try {
                    envioService.edit_XML(envio, envio.getId().toString());
                } catch (Exception e) {
                    LOGGER.severe("Error al actualizar el creador del envío: " + e.getMessage());
                    new UtilsMethods().showAlert("Error al actualizar estado", e.getMessage());
                }
            });

            fechaEntregaColumn.setCellValueFactory(new PropertyValueFactory<>("fechaEntrega"));
            fechaEntregaColumn.setCellFactory(column -> new EnvioDateEditingCell());
            fechaEntregaColumn.setOnEditCommit(event -> {
                Envio envio = event.getRowValue();
                Date newDate = event.getNewValue();
                envio.setFechaEntrega(newDate);
                try {
                    envioService.edit_XML(envio, envio.getId().toString());
                } catch (Exception e) {
                    LOGGER.severe("Error al actualizar el estado del envío: " + e.getMessage());
                    new UtilsMethods().showAlert("Error al actualizar estado", e.getMessage());
                }
            });

            fechaEnvioColumn.setCellValueFactory(new PropertyValueFactory<>("fechaEnvio"));
            fechaEnvioColumn.setCellFactory(column -> new EnvioDateEditingCell());
            fechaEnvioColumn.setOnEditCommit(event -> {
                Envio envio = event.getRowValue();
                Date newDate = event.getNewValue();
                envio.setFechaEnvio(newDate);
                try {
                    envioService.edit_XML(envio, envio.getId().toString());
                } catch (Exception e) {
                    LOGGER.severe("Error al actualizar el estado del envío: " + e.getMessage());
                    new UtilsMethods().showAlert("Error al actualizar estado", e.getMessage());
                }
            });
        } catch (SelectException ex) {
            Logger.getLogger(EnvioController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void loadInitialData() {
        try {
            List<Envio> envios = envioService.findAll_XML(new GenericType<List<Envio>>() {
            });
            if (envios != null) {
                envioList.setAll(envios);
                table.setItems(envioList);
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
            envioList.add(nuevoEnvio);
            envioService.create_XML(nuevoEnvio);
            loadInitialData();
            table.refresh();
        } catch (Exception e) {
            LOGGER.severe("Error al añadir un nuevo envío: " + e.getMessage());
            new UtilsMethods().showAlert("Error al añadir envío", e.getMessage());
        }
    }

    @FXML
    private void removeEnvio(ActionEvent event) {
        try {
            ObservableList<Envio> selectedEnvios = table.getSelectionModel().getSelectedItems();
            LOGGER.info("Iniciando la eliminación de los envíos seleccionados.");
            for (Envio envio : selectedEnvios) {
                if (envio.getEnvioRutaVehiculo() != null) {
                    envioService.remove(envio.getId());
                }
                envioService.remove(envio.getId());
                envioList.remove(envio);
                table.refresh();
            }

        } catch (IllegalArgumentException e) {
            LOGGER.warning("Error en la selección de envíos: " + e.getMessage());
            new UtilsMethods().showAlert("Error", e.getMessage());
        } catch (Exception e) {
            LOGGER.severe("Error al eliminar envío(s): " + e.getMessage());
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
