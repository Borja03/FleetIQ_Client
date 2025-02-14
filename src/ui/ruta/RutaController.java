package ui.ruta;

import cellFactories.RutaDateEditingCell;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXListCell;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import exception.SelectException;
import factories.EnvioRutaVehiculoFactory;
import factories.RutaManagerFactory;
import factories.VehicleFactory;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;

import java.util.Set;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.converter.FloatStringConverter;
import javafx.util.converter.IntegerStringConverter;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.GenericType;
import logicInterface.EnvioRutaVehiculoManager;
import logicInterface.RutaManager;
import logicInterface.VehicleManager;
import models.EnvioRutaVehiculo;
import models.Ruta;
import models.Vehiculo;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;
import utils.ThemeManager;
import service.EnvioRutaVehiculoRESTClient;
import utils.UtilsMethods;

/**
 * Controller class responsible for managing the user interface of the "Ruta"
 * view.
 * <p>
 * This controller initializes and configures various JavaFX components such as
 * date pickers, buttons, combo boxes, text fields, and a table view that
 * displays route information. It sets up event handlers for filtering routes by
 * date, searching by identifier, adding or removing shipments, printing
 * reports, and managing context menus and editable table columns.
 * </p>
 * The class interacts with several managers:
 * <ul>
 * <li>{@code RutaManager} - Handles the retrieval and management of route
 * data.</li>
 * <li>{@code VehicleManager} - Manages vehicle-related operations.</li>
 * <li>{@code EnvioRutaVehiculoManager} - Oversees the assignment of shipments
 * to routes and vehicles.</li>
 * <li>{@code ThemeManager} - Applies a visual theme to the user interface.</li>
 * </ul>
 *
 * <p>
 * The controller is integrated with FXML, and its UI components are injected
 * via the {@code @FXML} annotation. The stage is configured with a fixed size,
 * centered on the screen, and decorated with an icon.
 * </p>
 *
 * @author Borja
 * @version 1.0
 */
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
    @FXML
    private ChoiceBox<String> vehiculoChoiceBox;

    private RutaManager rutaManager;
    private VehicleManager vehicleManager;
    private EnvioRutaVehiculoManager ervManager;

    private ObservableList<Ruta> rutaData;

    private Stage stage;

    /**
     * Retrieves the current {@link Stage} associated with this controller.
     *
     * @return the stage used by this controller
     */
    public Stage getStage() {
        return stage;
    }

    /**
     * Sets the {@link Stage} for this controller.
     *
     * @param stage the stage to be used by this controller
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Initializes the controller with the provided root node.
     * <p>
     * This method sets up the scene, configures the stage properties (such as
     * title, icon, and position), and registers event handlers for various UI
     * components including buttons, combo boxes, and table views. It also
     * prepares the table for editing, loads the initial route data, and applies
     * the current UI theme.
     * </p>
     *
     * @param root the root node of the scene graph to be displayed in the stage
     */
    @FXML
    public void initialize(Parent root) {
        rutaManager = RutaManagerFactory.getRutaManager();
        vehicleManager = VehicleFactory.getVehicleInstance();
        ervManager = EnvioRutaVehiculoFactory.getEnvioRutaVehiculoInstance();

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Ruta");
        stage.setResizable(false);
        stage.centerOnScreen();
        stage.getIcons().add(new Image("/image/fleet_icon.png"));
        removeShipmentBtn.setDisable(true);

        applyFilterButton.setOnAction(event -> filterByDates());

        sizeFilterComboBox.setItems(FXCollections.observableArrayList("Filtro por Tiempo", "Filtro por Distancia"));
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

        setupContextMenu();
        loadRutaData();

        ThemeManager.getInstance().applyTheme(stage.getScene());
        stage.show();
    }

    /**
     * Loads the route data into the table view.
     * <p>
     * This method retrieves the route data from the {@code RutaManager} and
     * populates the table view, ensuring that the displayed data is current.
     * </p>
     */
    private void loadRutaData() {
        try {
            List<Ruta> rutas = RutaManagerFactory.getRutaManager().findAll_XML(new GenericType<List<Ruta>>() {
            });

            // Obtener y actualizar el conteo de vehículos para cada ruta
            for (Ruta ruta : rutas) {
                try {
                    String countStr = ervManager.countByRutaId(String.valueOf(ruta.getLocalizador()));
                    int vehicleCount = Integer.parseInt(countStr.trim());
                    ruta.setNumVehiculos(vehicleCount);
                } catch (NumberFormatException e) {
                    logger.log(Level.SEVERE, "Error al convertir el conteo de vehículos para la ruta " + ruta.getLocalizador(), e);
                    ruta.setNumVehiculos(0);
                } catch (WebApplicationException e) {
                    logger.log(Level.SEVERE, "Error al obtener el conteo de vehículos para la ruta " + ruta.getLocalizador(), e);
                    ruta.setNumVehiculos(0);
                }
            }

            rutaData = FXCollections.observableArrayList(rutas);
            // DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy MMMMM dd", Locale.US);

            localizadorColumn.setCellValueFactory(new PropertyValueFactory<>("localizador"));
            origenColumn.setCellValueFactory(new PropertyValueFactory<>("origen"));
            destinoColumn.setCellValueFactory(new PropertyValueFactory<>("destino"));
            distanciaColumn.setCellValueFactory(new PropertyValueFactory<>("distancia"));
            tiempoColumn.setCellValueFactory(new PropertyValueFactory<>("tiempo"));
            numeroVehiculosColumn.setCellValueFactory(new PropertyValueFactory<>("numVehiculos"));
            fechaColumn.setCellValueFactory(new PropertyValueFactory<>("FechaCreacion"));
            
            
           // DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT, yourLocale);

            

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MMMMM.dd", Locale.US);
            
           
            
            rutaTable.setItems(rutaData);
        } catch (WebApplicationException e) {
            logger.log(Level.SEVERE, "Error cargando datos de rutas", e);
            UtilsMethods.showAlert("Error", "No se pudieron cargar las rutas.");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error inesperado", e);
            UtilsMethods.showAlert("Error", "Ocurrió un error inesperado.");
        }
    }

    /**
     * Actualiza el texto del campo de filtro secundario según la selección del
     * filtro principal.
     * <p>
     * Si el usuario elige "Filter by Time", se muestra "Horas". Si selecciona
     * "Filter by Distance", se muestra "Km". En otros casos, se establece
     * "Unit" como valor predeterminado.
     */
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

    /**
     * Actualiza el texto del campo de filtro secundario según la selección del
     * filtro principal.
     * <p>
     * Si el usuario elige "Filter by Time", se muestra "Horas". Si selecciona
     * "Filter by Distance", se muestra "Km". En otros casos, se establece
     * "Unit" como valor predeterminado.
     */
    @FXML
    private void filterByDates() {
        if (fromDatePicker.getValue() == null || toDatePicker.getValue() == null) {
            logger.log(Level.WARNING, "Both dates must be selected.");
            return;
        }

        // Convertir las fechas seleccionadas a String en formato ISO (yyyy-MM-dd)
        String fromDate = fromDatePicker.getValue().toString();
        String toDate = toDatePicker.getValue().toString();

        try {
            List<Ruta> filteredRutas = rutaManager.filterBy2Dates_XML(new GenericType<List<Ruta>>() {
            }, fromDate, toDate);

            rutaData = FXCollections.observableArrayList(filteredRutas);
            rutaTable.setItems(rutaData);

            logger.log(Level.INFO, "Routes filtered successfully by dates: {0} to {1}", new Object[]{fromDate, toDate});
        } catch (WebApplicationException e) {
            logger.log(Level.SEVERE, "Error filtering routes by dates from REST service", e);
            showAlert("Error de servidor", "Error al filtrar por fechas");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unexpected error while filtering routes by dates", e);
        }
    }

    /**
     * Aplica un filtro según el tipo seleccionado (tiempo o distancia) y un
     * operador de comparación.
     * <p>
     * Valida que el valor ingresado sea un número positivo y que se haya
     * seleccionado un operador de comparación. Luego, aplica el filtro
     * correspondiente y actualiza la tabla con los resultados. En caso de
     * error, muestra un mensaje al usuario y registra la excepción.
     */
    private void applyFilterButtonAction() {
        String filterType = sizeFilterComboBox.getValue();
        String comparisonOperator = sizeFilterComboBox1.getValue();
        String filterValue = filterValueField.getText().trim();

        if (filterValue.isEmpty()) {
            loadRutaData();
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

    /**
     * Aplica un filtro de tiempo a la lista de rutas basado en un operador de
     * comparación.
     * <p>
     * Dependiendo del operador seleccionado consulta el servicio REST para
     * obtener las rutas que cumplen con la condición y actualiza la tabla con
     * los resultados. Si el operador no es válido, se registra una advertencia
     * en los logs.
     *
     * @param comparisonOperator El operador de comparación aram filterValue El
     * valor de tiempo a filtrar.
     */
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

    /**
     * Aplica un filtro de distancia a la lista de rutas basado en un operador
     * de comparación.
     * <p>
     * Dependiendo del operador seleccionado consulta el servicio REST para
     * obtener las rutas que cumplen con la condición y actualiza la tabla con
     * los resultados. Si el operador no es válido, se registra una advertencia
     * en los logs.
     *
     * @param comparisonOperator El operador de comparación aram filterValue El
     * valor de distancia a filtrar.
     */
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

    /**
     * Muestra una alerta emergente con un mensaje de error.
     *
     * @param title Título de la alerta.
     * @param content Mensaje a mostrar en el cuerpo de la alerta.
     */
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * Busca una ruta por su localizador y actualiza la tabla con el resultado.
     * <p>
     * Convierte el texto ingresado en un número entero y realiza la búsqueda en
     * el servicio REST. Si no se encuentra ninguna ruta con el localizador
     * ingresado, se muestra una alerta. Si el formato del texto no es válido,
     * también se informa al usuario.
     */
    private void searchByLocalizador() {
        String searchText = searchTextField.getText().trim();
        if (searchText.isEmpty()) {
            loadRutaData();
            return;
        }

        try {
            Integer localizador = Integer.parseInt(searchText);
            Ruta ruta = rutaManager.findByLocalizadorInteger_XML(Ruta.class, localizador);

            rutaData.clear();
            rutaData.add(ruta);
            rutaTable.setItems(rutaData);
            logger.info("Ruta filtrada cargada correctamente.");
        } catch (NumberFormatException e) {
            logger.warning("El texto de búsqueda no es un número válido.");
            showAlert("Error de formato", "El texto de búsqueda no es un número válido.");
        } catch (WebApplicationException e) {
            logger.log(Level.SEVERE, "Error al buscar por localizador", e);
            showAlert("Error de búsqueda", "Error al buscar por localizador.");
        } catch (Exception e) {
            logger.info(e.getMessage());
            showAlert("Ruta no encontrada", e.getMessage());
        }
    }

    /**
     * Añade una nueva ruta vacía con valores predeterminados.
     * <p>
     * Crea una nueva instancia de {@code Ruta} con valores por defecto y la
     * envía al servicio REST para su almacenamiento. Luego, actualiza la tabla
     * de rutas con los datos más recientes. En caso de error, se muestra una
     * alerta con la información correspondiente.
     */
    private void addShipment() {
        try {
            Ruta nuevaRuta = new Ruta();
            nuevaRuta.setOrigen("");
            nuevaRuta.setDestino("");
            nuevaRuta.setDistancia(0.0f);
            nuevaRuta.setTiempo(0);
            nuevaRuta.setNumVehiculos(0);
            nuevaRuta.setFechaCreacion(new Date());

            rutaManager.edit_XML(nuevaRuta, "0");

            loadRutaData();

            logger.info("Nueva ruta vacía añadida y tabla actualizada correctamente.");
        } catch (WebApplicationException e) {
            logger.log(Level.SEVERE, "Error al añadir una nueva ruta", e);
            showAlert("Error", "No se pudo añadir la nueva ruta.");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error inesperado", e);
            showAlert("Error", "Error inesperado al añadir la nueva ruta.");
        }
    }

    /**
     * Elimina las rutas seleccionadas de la tabla y del sistema.
     * <p>
     * Verifica que al menos una ruta haya sido seleccionada antes de proceder.
     * Muestra un cuadro de confirmación para validar la acción del usuario. Si
     * el usuario confirma, intenta eliminar cada ruta del sistema a través del
     * servicio REST y actualiza la tabla eliminando las rutas seleccionadas. Si
     * ocurre un error, se muestra una alerta y se registra la excepción.
     */
    private void removeShipment() {
        List<Ruta> selectedRutas = rutaTable.getSelectionModel().getSelectedItems();

        if (selectedRutas.isEmpty()) {
            showAlert("Error", "Debe seleccionar al menos una ruta para eliminar.");
            return;
        }

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirmación de eliminación");
        confirmAlert.setHeaderText("¿Está seguro de que desea eliminar las rutas seleccionadas?");
        confirmAlert.setContentText("Esta acción no se puede deshacer.");

        confirmAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    for (Ruta ruta : selectedRutas) {
                        rutaManager.remove(String.valueOf(ruta.getLocalizador()));
                    }
                    rutaData.removeAll(selectedRutas);
                    rutaTable.getSelectionModel().clearSelection();
                    logger.info("Rutas eliminadas correctamente.");
                } catch (WebApplicationException e) {
                    logger.log(Level.SEVERE, "Error al eliminar rutas", e);
                    showAlert("Error", "Ocurrió un error al eliminar las rutas.");
                } catch (Exception e) {
                    logger.log(Level.SEVERE, "Error inesperado", e);
                    showAlert("Error", "Error inesperado al eliminar rutas.");
                }
            } else {
                logger.info("Eliminación de rutas cancelada.");
            }
        });
    }

    /**
     * Genera e imprime un informe de las rutas mostradas en la tabla.
     * <p>
     * Compila y carga un informe JasperReport desde un archivo de diseño
     * (.jrxml). Luego, llena el informe con los datos de las rutas actuales en
     * la tabla y lo muestra en una ventana de vista previa. Si ocurre un error
     * durante el proceso, se muestra una alerta y se registra la excepción.
     */
    private void printReport() {
        logger.info("Print Report clicked");
        try {
            logger.info("Beginning printing action...");
            JasperReport report = JasperCompileManager.compileReport(getClass().getResourceAsStream("/ui/report/RutaReport.jrxml"));
            //Data for the report: a collection of UserBean passed as a JRDataSource 
            //implementation 
            JRBeanCollectionDataSource dataItems = new JRBeanCollectionDataSource((Collection<Ruta>) this.rutaTable.getItems());
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
            logger.log(Level.SEVERE, "UI GestionUsuariosController: Error printing report: {0}", ex.getMessage());
        }
    }

    /**
     * Configura las columnas de la tabla como editables y define la lógica de
     * actualización de datos.
     * <p>
     * Permite la edición en línea de los campos: origen, destino, distancia,
     * tiempo y fecha de creación. Al editar una celda, se clona la ruta, se
     * actualiza el valor correspondiente y se envía al servidor. Si la
     * actualización es exitosa, el valor modificado se refleja en la tabla. En
     * caso de error, se muestra una alerta y se restaura el valor original. Se
     * incluyen validaciones para evitar valores nulos o negativos en distancia
     * y tiempo.
     */
    private void configureEditableColumns() {
        origenColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        origenColumn.setOnEditCommit(event -> {
            Ruta originalRuta = event.getRowValue();
            String newOrigen = event.getNewValue();
            String originalOrigen = originalRuta.getOrigen();

            try {
                if (newOrigen == null || newOrigen.trim().isEmpty()) {
                    throw new IllegalArgumentException("El origen no puede estar vacio.");
                }

                Ruta clonedRuta = originalRuta.clone();
                clonedRuta.setOrigen(newOrigen);

                rutaManager.edit_XML(clonedRuta, String.valueOf(clonedRuta.getLocalizador()));
                logger.info("Origen actualizado en el servidor: " + newOrigen);

                originalRuta.setOrigen(newOrigen);
            } catch (Exception e) {
                logger.log(Level.SEVERE, "Error al actualizar origen en el servidor: " + e.getMessage(), e);
                showAlert("Error del servidor", e.getMessage());
                originalRuta.setOrigen(originalOrigen);
                rutaTable.refresh();
            }
        });

        destinoColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        destinoColumn.setOnEditCommit(event -> {
            Ruta originalRuta = event.getRowValue();
            String newDestino = event.getNewValue();
            String originalDestino = originalRuta.getDestino();

            try {

                if (newDestino == null || newDestino.trim().isEmpty()) {
                    throw new IllegalArgumentException("El destino no puede estar vacio.");
                }

                Ruta clonedRuta = (Ruta) originalRuta.clone();
                clonedRuta.setDestino(newDestino);

                rutaManager.edit_XML(clonedRuta, String.valueOf(clonedRuta.getLocalizador()));
                logger.info("Destino actualizado en el servidor: " + newDestino);

                originalRuta.setDestino(newDestino);
            } catch (Exception e) {
                logger.log(Level.SEVERE, "Error al actualizar destino en el servidor", e);
                showAlert("Error del servidor", "No se pudo actualizar el destino.");
                originalRuta.setDestino(originalDestino);
                event.getTableView().refresh();
                rutaTable.refresh();
            }
        });

        FloatStringConverter customFloatConverter = new FloatStringConverter() {
            @Override
            public Float fromString(String value) {
                try {
                    return super.fromString(value);
                } catch (NumberFormatException e) {
                    logger.log(Level.SEVERE, "Error al convertir distancia", e);
                    showAlert("Error", "El valor ingresado no es un número válido.");
                    return null;
                }
            }
        };

        distanciaColumn.setCellFactory(TextFieldTableCell.forTableColumn(customFloatConverter));
        distanciaColumn.setOnEditCommit(event -> {

            Ruta originalRuta = event.getRowValue();
            Float originalDistancia = originalRuta.getDistancia();

            try {
                if (event.getNewValue() == null) {
                    event.getTableView().refresh();
                    throw new IllegalArgumentException("La distancia no puede estar vacia.");
                }

                Float nuevaDistancia = event.getNewValue();
                if (nuevaDistancia < 0) {
                    showAlert("Error", "La distancia no puede ser negativa.");
                    event.getTableView().refresh();
                    throw new IllegalArgumentException("La distancia no puede ser negativa.");
                }

                Ruta clonedRuta = (Ruta) originalRuta.clone();
                clonedRuta.setDistancia(nuevaDistancia);

                rutaManager.edit_XML(clonedRuta, String.valueOf(clonedRuta.getLocalizador()));
                logger.info("Distancia actualizada en el servidor: " + nuevaDistancia);

                originalRuta.setDistancia(nuevaDistancia);
            } catch (Exception e) {
                logger.log(Level.SEVERE, "Error al actualizar distancia en el servidor", e);
                showAlert("Error del servidor", "No se pudo actualizar la distancía.");
                originalRuta.setDistancia(originalDistancia);
                event.getTableView().refresh();
                rutaTable.refresh();
            }
        });

        IntegerStringConverter customIntegerConverter = new IntegerStringConverter() {
            @Override
            public Integer fromString(String value) {
                try {
                    return super.fromString(value);
                } catch (NumberFormatException e) {
                    logger.log(Level.SEVERE, "Error al convertir tiempo", e);
                    showAlert("Error", "El valor ingresado no es un número válido.");
                    return null;
                }
            }
        };

        tiempoColumn.setCellFactory(TextFieldTableCell.forTableColumn(customIntegerConverter));
        tiempoColumn.setOnEditCommit(event -> {

            Ruta originalRuta = event.getRowValue();
            Integer originalTiempo = originalRuta.getTiempo();

            try {
                if (event.getNewValue() == null) {
                    event.getTableView().refresh();
                    throw new IllegalArgumentException("El tiempo no puede estar vacio.");
                }

                Integer nuevoTiempo = event.getNewValue();
                if (nuevoTiempo < 0) {
                    showAlert("Error", "El tiempo no puede ser negativo.");
                    event.getTableView().refresh();
                    throw new IllegalArgumentException("El tiempo no puede ser negativo.");
                }

                Ruta clonedRuta = (Ruta) originalRuta.clone();
                clonedRuta.setTiempo(nuevoTiempo);

                rutaManager.edit_XML(clonedRuta, String.valueOf(clonedRuta.getLocalizador()));
                logger.info("Tiempo actualizado en el servidor: " + nuevoTiempo);

                originalRuta.setTiempo(nuevoTiempo);
            } catch (Exception e) {
                logger.log(Level.SEVERE, "Error al actualizar tiempo en el servidor", e);
                showAlert("Error del servidor", "No se pudo actualizar el tiempo.");
                originalRuta.setTiempo(originalTiempo);
                event.getTableView().refresh();
                rutaTable.refresh();
            }
        });

        //  fechaColumn.setCellValueFactory(new PropertyValueFactory<>("FechaCreacion"));
        fechaColumn.setCellFactory(column -> new RutaDateEditingCell());
        fechaColumn.setOnEditCommit(event -> {
            Ruta originalRuta = event.getRowValue();
            Date newDate = event.getNewValue();
            Date originalDate = originalRuta.getFechaCreacion();

            try {
                Ruta clonedRuta = (Ruta) originalRuta.clone();
                clonedRuta.setFechaCreacion(newDate);

                rutaManager.edit_XML(clonedRuta, String.valueOf(clonedRuta.getLocalizador()));
                logger.info("Fecha actualizada en el servidor: " + newDate);

                originalRuta.setFechaCreacion(newDate);
            } catch (Exception e) {
                logger.log(Level.SEVERE, "Error al actualizar fecha en el servidor", e);
                showAlert("Error del servidor", "No se pudo actualizar la fecha.");
                originalRuta.setFechaCreacion(originalDate);
                event.getTableView().refresh();
                rutaTable.refresh();
            }
        });
    }

    /**
     * Configura el menú contextual de la tabla de rutas.
     * <p>
     * Agrega una opción de "Añadir Vehículo" que permite asignar vehículos a
     * una ruta seleccionada. Al hacer clic en la opción, se abre un diálogo de
     * selección de vehículos disponibles.
     */
    private void setupContextMenu() {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem addVehicleMenuItem = new MenuItem("Añadir Vehículo");

        addVehicleMenuItem.setOnAction(event -> {
            Ruta selectedRuta = rutaTable.getSelectionModel().getSelectedItem();
            if (selectedRuta != null) {
                showVehicleSelectionDialog(selectedRuta);
            }
        });

        contextMenu.getItems().add(addVehicleMenuItem);

        rutaTable.setContextMenu(contextMenu);
    }

    /**
     * Muestra un cuadro de diálogo para seleccionar y asignar vehículos a la
     * ruta especificada.
     * <p>
     * Carga dinámicamente los vehículos disponibles, excluyendo los ya
     * asignados a la misma ruta. Los usuarios pueden seleccionar múltiples
     * vehículos y confirmar la asignación. Si la asignación es exitosa, se
     * actualiza el número de vehículos en la ruta y se sincroniza con el
     * servidor.
     *
     * @param ruta La ruta a la que se asignarán los vehículos.
     */
    private void showVehicleSelectionDialog(Ruta ruta) {
        Stage vehicleStage = new Stage();
        vehicleStage.setTitle("Seleccionar Vehículos");
        JFXListView<String> vehicleListView = new JFXListView<>();
        ObservableList<String> selectedMatriculas = FXCollections.observableArrayList();
        ObservableList<String> matriculas = FXCollections.observableArrayList();

        Runnable loadAvailableVehicles = () -> {
            try {
                matriculas.clear();
                // Forzar recarga sin caché
                List<Vehiculo> vehiculos = vehicleManager.findAllVehiculos();

                for (Vehiculo vehiculo : vehiculos) {
                    try {
                        List<Ruta> rutasAsignadas = ervManager.getRutaId(
                                new GenericType<List<Ruta>>() {
                        },
                                String.valueOf(vehiculo.getId())
                        );

                        boolean vehiculoAsignadoARutaActual = false;

                        if (rutasAsignadas != null) {
                            for (Ruta rutaAsignada : rutasAsignadas) {
                                // Usar .equals() para comparar
                                if (rutaAsignada.getLocalizador().equals(ruta.getLocalizador())) {
                                    vehiculoAsignadoARutaActual = true;
                                    break;
                                }
                            }
                        }

                        if (!vehiculoAsignadoARutaActual) {
                            matriculas.add(vehiculo.getMatricula());
                            logger.info("Vehículo disponible: " + vehiculo.getMatricula());
                        }
                    } catch (ClientErrorException ex) {
                        logger.log(Level.WARNING, "Error al verificar asignación del vehículo "
                                + vehiculo.getMatricula(), ex);
                    }
                }
            } catch (SelectException ex) {
                logger.log(Level.SEVERE, "Error al cargar los vehículos", ex);
                showAlert("Error", "No se pudieron cargar los vehículos");
            }
        };

        try {
            loadAvailableVehicles.run();
            vehicleListView.setItems(matriculas);

            vehicleListView.setCellFactory(lv -> new JFXListCell<String>() {
                @Override
                protected void updateItem(String matricula, boolean empty) {
                    super.updateItem(matricula, empty);

                    if (empty || matricula == null) {
                        setText(null);
                        setStyle("");
                    } else {
                        setText(matricula);
                        if (selectedMatriculas.contains(matricula)) {
                            setStyle("-fx-background-color: lightblue;");
                        } else {
                            setStyle("");
                        }
                    }

                    setOnMouseClicked(event -> {
                        if (!isEmpty() && matricula != null) {
                            if (selectedMatriculas.contains(matricula)) {
                                selectedMatriculas.remove(matricula);
                            } else {
                                selectedMatriculas.add(matricula);
                            }
                            updateItem(matricula, false);
                        }
                    });
                }
            });

            JFXButton confirmButton = new JFXButton("Confirmar");
            confirmButton.setOnAction(e -> {
                if (!selectedMatriculas.isEmpty()) {
                    try {
                        for (String matricula : selectedMatriculas) {
                            List<Vehiculo> vehiculo = vehicleManager.findAllVehiculosByPlate(matricula);
                            EnvioRutaVehiculo envioRutaVehiculo = new EnvioRutaVehiculo();
                            envioRutaVehiculo.setRutaLocalizador(ruta.getLocalizador());
                            envioRutaVehiculo.setVehiculoID(vehiculo.get(0).getId());
                            envioRutaVehiculo.setFechaAsignacion(new Date());
                            ervManager.create_XML(envioRutaVehiculo);
                            logger.info("Vehículo asignado: " + matricula);
                        }

                        ruta.setNumVehiculos(ruta.getNumVehiculos() + selectedMatriculas.size());
                        rutaManager.edit_XML(ruta, String.valueOf(ruta.getLocalizador()));

                        selectedMatriculas.clear();
                        vehicleStage.close();
                        loadRutaData();

                    } catch (Exception ex) {
                        logger.log(Level.SEVERE, "Error al añadir vehículos a la ruta", ex);
                        showAlert("Error", "No se pudieron añadir los vehículos a la ruta");
                    }
                }
            });

            VBox layout = new VBox(10);
            layout.getStyleClass().add("jfx-popup-container");
            layout.setPadding(new Insets(10));
            layout.getChildren().addAll(vehicleListView, confirmButton);

            Scene scene = new Scene(layout);
            vehicleStage.setScene(scene);
            vehicleStage.setWidth(300);
            vehicleStage.setHeight(300);
            vehicleStage.show();

        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Error al inicializar el diálogo", ex);
            showAlert("Error", "No se pudo abrir el diálogo de selección");
        }
    }

}
