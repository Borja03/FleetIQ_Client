package ui.envio;

import cellFactories.EnvioDateEditingCell;
import com.jfoenix.controls.*;
import exception.SelectException;
import exception.UpdateException;
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ListChangeListener;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.cell.ChoiceBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import javax.ws.rs.core.GenericType;
import logicInterface.*;
import models.*;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;
import utils.ThemeManager;

/**
 * Controlador para la gestión de envíos en la aplicación. Proporciona
 * funcionalidades para filtrar, agregar, eliminar y visualizar envíos, así como
 * generar reportes.
 */
public class EnvioController {

    /**
     * Logger para registrar eventos y errores.
     */
    private static final Logger LOGGER = Logger.getLogger(EnvioController.class.getName());

    /**
     * Selector de fecha de inicio para el filtro.
     */
    @FXML
    private JFXDatePicker fromDatePicker;

    /**
     * Selector de fecha de fin para el filtro.
     */
    @FXML
    private JFXDatePicker toDatePicker;

    /**
     * ComboBox para filtrar envíos por estado.
     */
    @FXML
    private JFXComboBox<String> estadoFilterComboBox;

    /**
     * Campo de texto para filtrar envíos por número de paquetes.
     */
    @FXML
    private JFXTextField numeroPaquetesTextField;

    /**
     * Tabla de envíos.
     */
    @FXML
    private TableView<Envio> table;

    /**
     * Columna de ID del envío.
     */
    @FXML
    private TableColumn<Envio, Integer> idColumn;

    /**
     * Columna de fecha de envío.
     */
    @FXML
    private TableColumn<Envio, Date> fechaEnvioColumn;

    /**
     * Columna de fecha de entrega.
     */
    @FXML
    private TableColumn<Envio, Date> fechaEntregaColumn;

    /**
     * Columna de estado del envío.
     */
    @FXML
    private TableColumn<Envio, Estado> estadoColumn;

    /**
     * Columna de número de paquetes en el envío.
     */
    @FXML
    private TableColumn<Envio, Integer> numPaquetesColumn;

    /**
     * Columna con el nombre del creador del envío.
     */
    @FXML
    private TableColumn<Envio, String> creadorEnvioColumn;

    /**
     * Columna con la ruta del envío.
     */
    @FXML
    private TableColumn<Envio, String> rutaColumn;

    /**
     * Columna con el vehículo asignado al envío.
     */
    @FXML
    private TableColumn<Envio, String> vehiculoColumn;

    /**
     * Botón para aplicar el filtro por fecha.
     */
    @FXML
    private JFXButton applyDateFilterButton, applyEstadoFilterButton, applyNumPaquetesFilterButton;

    /**
     * Botón para agregar un nuevo envío.
     */
    @FXML
    private JFXButton addShipmentBtn;

    /**
     * Botón para eliminar un envío seleccionado.
     */
    @FXML
    private JFXButton removeShipmentBtn;

    /**
     * Botón para imprimir un reporte de envíos.
     */
    @FXML
    private JFXButton printReportBtn;

    /**
     * Servicio para la gestión de envíos.
     */
    private EnvioManager envioService;

    /**
     * Servicio para la gestión de paquetes.
     */
    private PaqueteManager paqueteService;

    /**
     * Servicio para la gestión de usuarios.
     */
    private UserManager userService;

    /**
     * Servicio para la gestión de vehículos.
     */
    private VehicleManager vehicleService;

    /**
     * Servicio para la gestión de rutas de envíos con vehículos.
     */
    private EnvioRutaVehiculoManager envioRutaVehiculoService;

    /**
     * Lista observable de envíos.
     */
    private ObservableList<Envio> envioList;

    /**
     * Lista de paquetes.
     */
    private ArrayList<Paquete> paqueteList;

    /**
     * Lista de usuarios.
     */
    private ArrayList<User> userList = new ArrayList<>();

    /**
     * Lista de vehículos.
     */
    private List<Vehiculo> vehiculoList = new ArrayList<>();

    /**
     * Lista de rutas de envíos con vehículos.
     */
    private List<EnvioRutaVehiculo> envioRutaVehiculoList = new ArrayList<>();

    /**
     * Lista de nombres de usuarios.
     */
    private List<String> userNamesList = new ArrayList<>();

    /**
     * Lista de matrículas de vehículos.
     */
    private List<String> vehiculoMatriculaList = new ArrayList<>();

    /**
     * Ventana principal de la aplicación.
     */
    private Stage stage;

    /**
     * Establece la ventana principal de la aplicación.
     *
     * @param stage La ventana principal.
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Inicializa la ventana de gestión de envíos.
     *
     * @param root El nodo raíz de la interfaz gráfica.
     */
    public void initStage(Parent root) {
        LOGGER.info("Initialising Envio window.");
        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.setTitle("Envio");
        stage.setResizable(false);
        stage.getIcons().add(new Image("/image/fleet_icon.png"));
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
        setUpContextMenu();

        ThemeManager.getInstance().applyTheme(stage.getScene());
        loadInitialData();
        stage.show();
    }

    /**
     * Configura el menú contextual de la tabla de envíos.
     */
    private void setUpContextMenu() {
        ContextMenu contextMenu = new ContextMenu();

        MenuItem addItem = new MenuItem("Add New Envio");
        addItem.setOnAction(this::addEnvio);

        MenuItem deleteItem = new MenuItem("Delete Envio");
        deleteItem.setOnAction(this::removeEnvio);

        MenuItem printItem = new MenuItem("Print Report");
        printItem.setOnAction(this::printReport);

        contextMenu.getItems().addAll(addItem, deleteItem, printItem);
        table.setContextMenu(contextMenu);
    }

    /**
     * Configura las columnas de la tabla en la interfaz de usuario.
     * <p>
     * Este método establece los valores de las celdas, define editores
     * personalizados y maneja los eventos de edición para las columnas de la
     * tabla. Además, realiza validaciones y actualizaciones en la base de datos
     * cuando se editan ciertos campos.
     * </p>
     *
     * 
     * base de datos.
     */
    private void setUpTableColumns() {
        try {
            idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
            estadoColumn.setCellValueFactory(new PropertyValueFactory<>("estado"));
            estadoColumn.setCellFactory(ChoiceBoxTableCell.forTableColumn(Estado.values()));
            estadoColumn.setOnEditCommit(event -> {
                Envio envio = event.getRowValue();
                Estado nuevoEstado = event.getNewValue();
                Estado originalEstado = envio.getEstado();
                try {
                    Envio envioClone = envio.clone();
                    envioClone.setEstado(nuevoEstado);

                    envioService.edit_XML(envioClone, envioClone.getId().toString());
                    envio.setEstado(nuevoEstado);
                } catch (Exception e) {
                    envio.setEstado(originalEstado);
                    table.refresh();
                    LOGGER.severe("Error al actualizar el estado del envío: " + e.getMessage());
                    new UtilsMethods().showAlert("Error al actualizar estado", e.getMessage());
                }
            });

            numPaquetesColumn.setCellValueFactory(new PropertyValueFactory<>("numPaquetes"));
            numPaquetesColumn.setCellFactory(TextFieldTableCell.forTableColumn(
                    new StringConverter<Integer>() {
                @Override
                public String toString(Integer object) {
                    return object != null ? object.toString() : "";
                }

                @Override
                public Integer fromString(String string) {
                    try {
                        return Integer.valueOf(string);
                    } catch (NumberFormatException e) {
                        LOGGER.warning("Numero de paquetes invalido: " + string);
                        return null;
                    }
                }
            }));

            numPaquetesColumn.setOnEditCommit(event -> event.getRowValue().setNumPaquetes(event.getNewValue()));
            numPaquetesColumn.setOnEditCommit(event -> {
                Envio envio = event.getRowValue();
                Integer nuevosPaquetes = event.getNewValue();
                Integer paquetesAntiguos = envio.getNumPaquetes();

                try {
                    Envio envioClone = envio.clone();
                    // Validate the new receiver value
                    if (nuevosPaquetes <= 0) {
                        throw new IllegalArgumentException("En numero de paquetes debe ser mayor o igual a cero");
                    }
                    envioClone.setNumPaquetes(nuevosPaquetes);

                    envioService.edit_XML(envioClone, envioClone.getId().toString());
                    envio.setNumPaquetes(nuevosPaquetes);

                } catch (Exception ex) {
                    envio.setNumPaquetes(paquetesAntiguos);
                    table.refresh();
                    UtilsMethods.showAlert("Error", ex.getMessage());
                    LOGGER.warning("Error al actualizar el numero de paquetes: " + nuevosPaquetes);
                    numPaquetesColumn.getTableView().refresh();

                }

            });

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
                String nombreAntiguo = envio.getCreadorEnvio();
                try {
                    Envio envioClone = envio.clone();
                    envioClone.setCreadorEnvio(nombre);
                    envioService.edit_XML(envio, envio.getId().toString());
                    envio.setCreadorEnvio(nombre);
                } catch (Exception e) {
                    envio.setCreadorEnvio(nombreAntiguo);
                    table.refresh();
                    LOGGER.severe("Error al actualizar el creador del envío: " + e.getMessage());
                    new UtilsMethods().showAlert("Error al actualizar estado", e.getMessage());
                }
            });

            rutaColumn.setCellValueFactory(new PropertyValueFactory<>("ruta"));

            vehiculoColumn.setCellValueFactory(new PropertyValueFactory<>("vehiculo"));
            vehiculoList = vehicleService.findAllVehiculos();
            for (Vehiculo vehiculo : vehiculoList) {
                if (!vehiculo.isActivo()) {
                    Integer idVehiculo = vehiculo.getId();
                    List<Ruta> rList = envioRutaVehiculoService.getRutaId(new GenericType<List<Ruta>>() {
                    }, idVehiculo.toString());
                    if (!rList.isEmpty() && rList.get(0) != null) {
                        vehiculoMatriculaList.add(vehiculo.getMatricula());
                    }
                }
            }
            ObservableList<String> vehiclesNamesObservableList = FXCollections.observableArrayList(vehiculoMatriculaList);
            vehiculoColumn.setCellFactory(ChoiceBoxTableCell.forTableColumn(vehiclesNamesObservableList));
            vehiculoColumn.setOnEditCommit(event -> {
                Envio envio = event.getRowValue();
                String matricula = "";
                Vehiculo vActualizado = new Vehiculo();
                if (event.getNewValue() != null) {
                    matricula = event.getNewValue();
                } else {
                    return;
                }
                String localizador = "";
                EnvioRutaVehiculo erv = new EnvioRutaVehiculo();
                try {
                    envioRutaVehiculoList = envioRutaVehiculoService.findAll_XML(new GenericType<List<EnvioRutaVehiculo>>() {
                    });

                    int i = 0;
                    boolean encontrado = false;
                    while (i < envioRutaVehiculoList.size() || !encontrado) {

                        List<Vehiculo> vComprobar = vehicleService.findAllVehiculosByPlate(matricula);
                        if (vComprobar.get(0).getMatricula().equalsIgnoreCase(matricula)) {
                            vActualizado = vComprobar.get(0);
                            Integer idVehiculo = vComprobar.get(0).getId();
                            List<Ruta> rList = envioRutaVehiculoService.getRutaId(new GenericType<List<Ruta>>() {
                            }, idVehiculo.toString());
                            localizador = rList.get(0).getLocalizador().toString();
                            List<EnvioRutaVehiculo> ervList = envioRutaVehiculoService.getId(new GenericType<List<EnvioRutaVehiculo>>() {
                            }, idVehiculo.toString());
                            erv = ervList.get(0);
                            encontrado = true;
                        }
                        i++;
                    }

                } catch (IndexOutOfBoundsException ex) {
                    LOGGER.severe("El vehiculo seleccionado no está asignado a ninguna ruta" + ex.getMessage());
                    new UtilsMethods().showAlert("El vehiculo seleccionado no está asignado a ninguna ruta", ex.getMessage());
                } catch (Exception ex) {
                    Logger.getLogger(EnvioController.class.getName()).log(Level.SEVERE, null, ex);
                }

                try {
                    String matriculaVAntiguo = envio.getVehiculo();
                    if (matriculaVAntiguo != null) {
                        List<Vehiculo> vComprobar = vehicleService.findAllVehiculosByPlate(matriculaVAntiguo);
                        Vehiculo vAntiguo = vComprobar.get(0);
                        vAntiguo.setActivo(false);
                        vehicleService.updateVehiculo(vAntiguo);
                        vActualizado.setActivo(true);
                        vehicleService.updateVehiculo(vActualizado);
                    }
                    envio.setRuta(localizador);
                    envio.setVehiculo(matricula);
                    envio.setEnvioRutaVehiculo(erv);
                } catch (UpdateException | SelectException ex) {
                    Logger.getLogger(EnvioController.class.getName()).log(Level.SEVERE, null, ex);
                }
                table.refresh();
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
                try {
                    if (envio.getFechaEnvio() == null) {
                        throw new IllegalArgumentException("Debes insertar la fecha de envio antes de insertar la fecha de entrega");
                    } else {
                        if (newDate.before(envio.getFechaEnvio())) {
                            throw new IllegalArgumentException("La fecha entrega debe ser posterior a la fecha envio");
                        }
                    }
                    envio.setFechaEnvio(newDate);
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
                try {
                    Envio envioClone = envio.clone();
                    envioClone.setFechaEnvio(newDate);
                    envioService.edit_XML(envio, envio.getId().toString());
                    envio.setFechaEnvio(newDate);
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

    /**
     * Aplica un filtro de fechas a la lista de envíos.
     * <p>
     * Este método obtiene las fechas seleccionadas en los selectores de fecha y
     * filtra los envíos en función del rango especificado. Si no se proporciona
     * ninguna fecha, se lanza una excepción. Los resultados filtrados se
     * actualizan en la lista de envíos mostrada en la interfaz.
     * </p>
     *
     * @param event Evento de acción que activa el filtro.
     */
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

    /**
     * Aplica un filtro por número de paquetes a la lista de envíos.
     * <p>
     * Obtiene el valor ingresado en el campo de texto y filtra los envíos que
     * coincidan con ese número de paquetes. Si el campo está vacío, se carga la
     * lista inicial. Si el valor ingresado es menor o igual a 0, se muestra una
     * alerta de error.
     * </p>
     *
     * @param event Evento de acción que activa el filtro.
     */
    @FXML
    private void applyNumPaquetesFilter(ActionEvent event) {
        try {
            String numPaquetes = numeroPaquetesTextField.getText();
            if (numPaquetes == null || numPaquetes.isEmpty()) {
                loadInitialData();
            } else {

                int num = Integer.parseInt(numPaquetes);
                if (num <= 0) {
                    throw new IllegalArgumentException("El número de paquetes debe ser mayor a 0.");
                }

                List<Envio> filteredData = envioService.filterNumPaquetes_XML(new GenericType<List<Envio>>() {
                }, num);
                envioList.setAll(filteredData);
            }
        } catch (IllegalArgumentException e) {
            new UtilsMethods().showAlert("Error de filtro", e.getMessage());
        } catch (Exception e) {
            new UtilsMethods().showAlert("Error al filtrar por número de paquetes", e.getMessage());
        }
    }

    /**
     * Añade un nuevo envío a la lista y lo guarda en la base de datos.
     * <p>
     * Crea un nuevo objeto {@code Envio}, lo añade a la lista y lo envía al
     * servicio para su almacenamiento. Luego, recarga los datos iniciales y
     * actualiza la tabla. En caso de error, se muestra una alerta.
     * </p>
     *
     * @param event Evento de acción que activa la adición del envío.
     */
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

    /**
     * Elimina los envíos seleccionados de la lista y de la base de datos.
     * <p>
     * Verifica si los envíos seleccionados están vinculados a un vehículo o una
     * ruta antes de eliminarlos. Si el vehículo estaba activo, se desactiva
     * antes de la eliminación. Se refresca la tabla tras la operación.
     * </p>
     *
     * @param event Evento de acción que activa la eliminación del envío.
     */
    @FXML
    private void removeEnvio(ActionEvent event) {
        try {
            ObservableList<Envio> selectedEnvios = table.getSelectionModel().getSelectedItems();
            LOGGER.info("Iniciando la eliminación de los envíos seleccionados.");
            for (Envio envio : selectedEnvios) {
                LOGGER.info(selectedEnvios.toString());
                if (envio.getEnvioRutaVehiculo() != null) {
                    envioService.remove(envio.getId());
                }
                if (envio.getVehiculo() == null) {

                } else {
                    String matriculaVAntiguo = envio.getVehiculo();
                    List<Vehiculo> vComprobar = vehicleService.findAllVehiculosByPlate(matriculaVAntiguo);
                    Vehiculo vAntiguo = vComprobar.get(0);
                    vAntiguo.setActivo(false);
                    vehicleService.updateVehiculo(vAntiguo);
                }
                envioService.remove(envio.getId());
                envioList.remove(envio);

            }
            table.refresh();
        } catch (UpdateException ex) {
            Logger.getLogger(EnvioController.class.getName()).log(Level.SEVERE, null, ex);
            new UtilsMethods().showAlert("Error al añadir envío", ex.getMessage());
        } catch (SelectException ex) {
            Logger.getLogger(EnvioController.class.getName()).log(Level.SEVERE, null, ex);
            new UtilsMethods().showAlert("Error al añadir envío", ex.getMessage());
        } catch (Exception ex) {
            new UtilsMethods().showAlert("Error al añadir envío", ex.getMessage());
        }
    }

    /**
     * Configura el botón de eliminación para habilitarse o deshabilitarse
     * dinámicamente.
     * <p>
     * Escucha cambios en la selección de la tabla de envíos y activa o
     * desactiva el botón de eliminación dependiendo de si hay elementos
     * seleccionados.
     * </p>
     */
    private void configureRemoveButton() {
        ObservableList<Envio> selectedItems = table.getSelectionModel().getSelectedItems();

        selectedItems.addListener((ListChangeListener<Envio>) change -> {
            removeShipmentBtn.setDisable(selectedItems.isEmpty());
        });

        removeShipmentBtn.setDisable(true);
    }

    /**
     * Genera e imprime un reporte de los envíos actuales utilizando
     * JasperReports.
     * <p>
     * Compila y llena un reporte con los datos actuales de la tabla de envíos,
     * luego lo muestra en una ventana de visualización. En caso de error, se
     * muestra una alerta con el mensaje correspondiente.
     * </p>
     *
     * @param event Evento de acción que activa la impresión del reporte.
     */
    @FXML
    private void printReport(ActionEvent event) {
        try {
            JasperReport report = JasperCompileManager.compileReport(getClass().getResourceAsStream("/ui/report/RutaReport.jrxml"));
            //Data for the report: a collection of UserBean passed as a JRDataSource 
            //implementation 
            JRBeanCollectionDataSource dataItems = new JRBeanCollectionDataSource((Collection<Envio>) this.table.getItems());
            //Map of parameter to be passed to the report
            Map<String, Object> parameters = new HashMap<>();
            //Fill report with data
            JasperPrint jasperPrint = JasperFillManager.fillReport(report, parameters, dataItems);
            //Create and show the report window. The second parameter false value makes 
            //report window not to close app.
            JasperViewer jasperViewer = new JasperViewer(jasperPrint, false);
            jasperViewer.setVisible(true);
            // jasperViewer.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        } catch (JRException ex) {
            //If there is an error show message and
            //log it.
            utils.UtilsMethods.showAlert("Error al imprimir: ", ex.getMessage());
        }
    }

}


