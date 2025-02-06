package ui.vehicle;

import cellFactories.VehicleActiveEditingCell;
import cellFactories.VehicleDateEditingCell;
import models.Vehiculo;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import exception.CreateException;
import exception.SelectException;
import exception.UpdateException;
import factories.EnvioRutaVehiculoFactory;
import factories.VehicleFactory;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.DateCell;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import javafx.util.converter.IntegerStringConverter;
import javax.ws.rs.WebApplicationException;
import models.EnvioRutaVehiculo;
import models.Vehiculo;
import utils.ThemeManager;
import utils.UtilsMethods;
import static utils.UtilsMethods.logger;
import javafx.scene.control.TablePosition;
import javafx.scene.image.Image;
import javax.ws.rs.core.GenericType;
import logicInterface.EnvioRutaVehiculoManager;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;

/**
 * Controller for managing the Vehiculo UI. Handles initialization,
 * configuration, and event logic.
 *
 * @author Adrián
 */
public class VehicleController {

    private static final Logger LOGGER = Logger.getLogger(VehicleController.class.getName());

    @FXML
    private JFXDatePicker fromDatePicker;

    @FXML
    private JFXDatePicker toDatePicker;

    @FXML
    private JFXComboBox<String> filterTypeComboBox;

    @FXML
    private JFXTextField searchTextField;

    @FXML
    private TableView<Vehiculo> vehicleTableView;

    @FXML
    private TableColumn<Vehiculo, Integer> idColumn;

    @FXML
    private TableColumn<Vehiculo, String> matriculaColumn;

    @FXML
    private TableColumn<Vehiculo, String> modelColumn;

    @FXML
    private TableColumn<Vehiculo, Integer> capacityColumn;

    @FXML
    private TableColumn<Vehiculo, Date> registrationDateColumn;

    @FXML
    private TableColumn<Vehiculo, Date> itvDateColumn;

    @FXML
    private TableColumn<Vehiculo, Boolean> activeColumn;

    @FXML
    private JFXButton addShipmentBtn;

    @FXML
    private JFXButton removeShipmentBtn;

    @FXML
    private JFXButton printReportBtn;

    @FXML
    private JFXButton searchButton;

    @FXML
    private JFXButton applyFilterButton;

    @FXML
    private JFXButton minusButton;

    @FXML
    private JFXButton plusButton;

    @FXML
    private JFXTextField capacityTextField;

    @FXML
    private TableColumn<EnvioRutaVehiculo, Integer> rutaColumn;

    @FXML
    private TableColumn<EnvioRutaVehiculo, Date> fecha_asignacionColumn;

    private Stage stage;
    private DateTimeFormatter dateFormatter;
    private LocalDate startDate;
    private LocalDate endDate;
    private ObservableList<Vehiculo> data;

    /**
     * Gets the stage for this controller.
     *
     * @return the stage instance.
     */
    public Stage getStage() {
        return stage;
    }

    /**
     * Sets the stage for this controller.
     *
     * @param stage the stage to set.
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Initializes the stage with the given root layout.
     *
     * @param root the root layout for the scene.
     */
    public void initStage(Parent root) {
        LOGGER.info("Initializing Vehicle window.");

        // Configure the scene and stage properties
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Vehicle Management");
        stage.getIcons().add(new Image("/image/fleet_icon.png"));
        stage.setResizable(false);
        stage.centerOnScreen();

        // Populate combo box
        filterTypeComboBox.getItems().setAll("ITV Date", "Registration Date");
        setUpTableColumns();
        fillTableFromDataBase();

        // Initialize event handlers for the buttons
        plusButton.setOnAction(event -> modifyCapacity(1));
        minusButton.setOnAction(event -> modifyCapacity(-1));

        // Set default value for capacityTextField if empty
        if (capacityTextField.getText().isEmpty()) {
            capacityTextField.setText("0");

            TextFormatter<Integer> formatter = new TextFormatter<>(new IntegerStringConverter(), 0, change
                    -> change.getControlNewText().matches("\\d*") ? change : null); // Allow only digits
            capacityTextField.setTextFormatter(formatter);
        }
        data = FXCollections.observableArrayList();
        // Add event handlers for other buttons
        searchButton.setOnAction(event -> onSearchButtonClicked());
        removeShipmentBtn.setOnAction(event -> onRemoveShipmentClicked());
        printReportBtn.setOnAction(this::handlePrintReportAction);
        configureRemoveShipmentButton();

        addShipmentBtn.setOnAction(event -> handleAddShipmentAction());
        applyFilterButton.setOnAction(this::handleFilterByDatesAction);

        vehicleTableView.setEditable(true);
        matriculaColumn.setEditable(true);
        capacityColumn.setEditable(true);
        itvDateColumn.setEditable(true);
        activeColumn.setEditable(true);

        LOGGER.info("Vehicle window and capacity controls initialized.");
        ThemeManager.getInstance().applyTheme(stage.getScene());
        stage.show();
    }

    /**
     * Fills the table with data from the database.
     */
    private void fillTableFromDataBase() {
        try {
            List<Vehiculo> vehicleList = VehicleFactory.getVehicleInstance().findAllVehiculos();

            // Fetch data and populate the TableView
            vehicleTableView.setItems(FXCollections.observableArrayList(vehicleList));

        } catch (Exception e) {
            // Handle exceptions gracefully
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Data Fetching Failed");
            alert.setContentText("Could not fetch data from the server. Please try again later.");
        }
    }

    /**
     * Configures the remove shipment button to be disabled when no row is
     * selected.
     */
    private void configureRemoveShipmentButton() {
        // Listener para deshabilitar el botón si no hay ninguna fila seleccionada
        vehicleTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            removeShipmentBtn.setDisable(newSelection == null);
        });

        // Deshabilitar el botón al inicio, ya que no hay selección por defecto
        removeShipmentBtn.setDisable(true);
    }

    /**
     * Configures the table columns.
     */
    private void setUpTableColumns() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        matriculaColumn.setCellValueFactory(new PropertyValueFactory<>("matricula"));
        modelColumn.setCellValueFactory(new PropertyValueFactory<>("modelo"));
        capacityColumn.setCellValueFactory(new PropertyValueFactory<>("capacidadCarga"));
        activeColumn.setCellValueFactory(new PropertyValueFactory<>("activo"));

        // Configuración para registrationDateColumn
        registrationDateColumn.setCellValueFactory(new PropertyValueFactory<>("registrationDate"));
        registrationDateColumn.setCellFactory(column -> new TableCell<Vehiculo, Date>() {
            private final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

            @Override
            protected void updateItem(Date item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(formatter.format(item));
                }
            }
        });

        // Configuración para itvDateColumn
        itvDateColumn.setCellValueFactory(new PropertyValueFactory<>("itvDate"));
        itvDateColumn.setCellFactory(column -> new TableCell<Vehiculo, Date>() {
            private final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

            @Override
            protected void updateItem(Date item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(formatter.format(item));
                }
            }
        });

        // Supongamos que 'tableView' es tu instancia de TableView
        vehicleTableView.getScene().addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
            // Verifica si el clic no ocurrió dentro de la tabla
            if (!vehicleTableView.getBoundsInParent().contains(event.getSceneX(), event.getSceneY())) {
                vehicleTableView.getSelectionModel().clearSelection(); // Deselecciona la fila seleccionada
            }
        });
        // Permitir selección múltiple en la tabla
        vehicleTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

// Habilitar edición en la tabla
        vehicleTableView.setEditable(true);

// Configurar la celda para que use un TextField al editar
        matriculaColumn.setCellFactory(TextFieldTableCell.forTableColumn());

// Manejar la edición de la celda
        matriculaColumn.setOnEditCommit(event -> {
            Vehiculo vehiculo = event.getRowValue();

            String nuevaMatricula = event.getNewValue();
            String matriculaOriginal = vehiculo.getMatricula(); // Guardar valor original
            Logger.getLogger(VehicleController.class.getName()).info("Matrícula original: " + matriculaOriginal);

            try {
                // Validar que la matrícula no esté vacía
                if (nuevaMatricula == null || nuevaMatricula.trim().isEmpty()) {
                    throw new IllegalArgumentException("La matrícula no puede estar vacía.");
                }

                // Intentar actualizar en la base de datos primero
                Vehiculo vehiculoActualizado = vehiculo.clone(); // Crear una copia temporal
                vehiculoActualizado.setMatricula(nuevaMatricula);
                Logger.getLogger(VehicleController.class.getName()).info("Nueva matrícula: " + nuevaMatricula);

                VehicleFactory.getVehicleInstance().updateVehiculo(vehiculoActualizado);

                // Si la actualización es exitosa, actualizar el objeto en la tabla
                vehiculo.setMatricula(nuevaMatricula);
                Logger.getLogger(VehicleController.class.getName()).info("Matrícula actualizada correctamente para el vehículo con ID: " + vehiculo.getId());
            } catch (IllegalArgumentException ex) {
                // Manejo de validación fallida
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Entrada no válida");
                alert.setHeaderText(ex.getMessage());
                alert.setContentText("Introduce una matrícula válida.");
                alert.showAndWait();
                Logger.getLogger(VehicleController.class.getName()).warning("Matrícula inválida: " + nuevaMatricula);
                vehiculo.setMatricula(matriculaOriginal);
                vehicleTableView.refresh();
            } catch (UpdateException ex) {
                // Manejo de fallo en la actualización de la base de datos
                Logger.getLogger(VehicleController.class.getName()).log(Level.SEVERE, "Error al actualizar la matrícula del vehículo con ID: " + vehiculo.getId(), ex);
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error de actualización");
                alert.setHeaderText("No se pudo actualizar la matrícula");
                alert.setContentText("Inténtalo de nuevo más tarde.");
                alert.showAndWait();
                vehiculo.setMatricula(matriculaOriginal);
                vehicleTableView.refresh();
            } catch (CloneNotSupportedException ex) {
                Logger.getLogger(VehicleController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        // Permitir edición en la columna
        modelColumn.setCellFactory(TextFieldTableCell.forTableColumn());

// Manejar la edición de la celda
        modelColumn.setOnEditCommit(event -> {
            Vehiculo vehiculo = event.getRowValue();

            String nuevoModelo = event.getNewValue();
            String modeloOriginal = vehiculo.getModelo(); // Guardar valor original
            Logger.getLogger(VehicleController.class.getName()).info("Modelo original: " + modeloOriginal);

            try {
                // Validar que el modelo no esté vacío
                if (nuevoModelo == null || nuevoModelo.trim().isEmpty()) {
                    throw new IllegalArgumentException("El modelo no puede estar vacío.");
                }

                // Intentar actualizar en la base de datos primero
                Vehiculo vehiculoActualizado = vehiculo.clone(); // Crear una copia temporal
                vehiculoActualizado.setModelo(nuevoModelo);
                Logger.getLogger(VehicleController.class.getName()).info("Nuevo modelo: " + nuevoModelo);

                VehicleFactory.getVehicleInstance().updateVehiculo(vehiculoActualizado);

                // Si la actualización es exitosa, actualizar el objeto en la tabla
                vehiculo.setModelo(nuevoModelo);
                Logger.getLogger(VehicleController.class.getName()).info("Modelo actualizado correctamente para el vehículo con ID: " + vehiculo.getId());
            } catch (IllegalArgumentException ex) {
                // Manejo de validación fallida
                UtilsMethods.showAlert("Error al actualizar el modelo", "Inténtalo más tarde", "ERROR");
                Logger.getLogger(VehicleController.class.getName()).warning("Modelo inválido: " + nuevoModelo);
                vehiculo.setModelo(modeloOriginal);
                vehicleTableView.refresh();
            } catch (UpdateException ex) {
                // Manejo de fallo en la actualización de la base de datos
                Logger.getLogger(VehicleController.class.getName()).log(Level.SEVERE, "Error al actualizar el modelo del vehículo con ID: " + vehiculo.getId(), ex);
                UtilsMethods.showAlert("Error al actualizar el modelo", "Inténtalo más tarde", "ERROR");
                vehiculo.setModelo(modeloOriginal);
                vehicleTableView.refresh();
            } catch (CloneNotSupportedException ex) {
                Logger.getLogger(VehicleController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

// Habilitar la edición en la tabla
        vehicleTableView.setEditable(true);

        capacityColumn.setCellFactory(TextFieldTableCell.forTableColumn(
                new StringConverter<Integer>() {
            @Override
            public String toString(Integer object) {
                return object != null ? object.toString() : "";
            }

            @Override
            public Integer fromString(String string) {
                try {
                    Integer value = Integer.parseInt(string);
                    // Check if the number is negative
                    if (value < 0) {
                        throw new IllegalArgumentException("Value cannot be negative");
                    }
                    return value;
                } catch (IllegalArgumentException e) {
                    LOGGER.warning("Invalid capacity: " + string);
                    UtilsMethods.showAlert("Capacidad no valida", "El valor no puede ser negativo", "ERROR");
                    throw new IllegalArgumentException("Only positive numeric values are allowed");
                }
            }
        }
        ));
        //Hablita la edición de la capacidad
        capacityColumn.setOnEditCommit(event -> {
            TableView<Vehiculo> tableView = event.getTableView();
            TablePosition<Vehiculo, Integer> pos = event.getTablePosition();
            int row = pos.getRow();

            Vehiculo vehiculo = event.getRowValue();
            Integer capacidadOriginal = vehiculo.getCapacidadCarga(); // Guardar valor original
            Logger.getLogger(VehicleController.class.getName()).info("Capacidad original: " + capacidadOriginal);

            try {
                Integer newValue = event.getNewValue();
                if (newValue == null || newValue < 0 || newValue > 999) {
                    throw new IllegalArgumentException("La capacidad debe estar entre 0 y 999.");
                }

                Vehiculo vehiculoActualizado = vehiculo.clone(); // Crear una copia temporal
                vehiculoActualizado.setCapacidadCarga(newValue);
                Logger.getLogger(VehicleController.class.getName()).info("Nueva capacidad: " + newValue);

                VehicleFactory.getVehicleInstance().updateVehiculo(vehiculoActualizado);

                vehiculo.setCapacidadCarga(newValue);
                Logger.getLogger(VehicleController.class.getName()).info("Capacidad actualizada correctamente para el vehículo con ID: " + vehiculo.getId());
            } catch (IllegalArgumentException ex) {
                UtilsMethods.showAlert("Capacidad no valida", "Prueba con una capacidad diferente", "ERROR");
                Logger.getLogger(VehicleController.class.getName()).warning("Capacidad inválida: " + event.getNewValue());
                vehiculo.setCapacidadCarga(capacidadOriginal);
                tableView.refresh();
            } catch (UpdateException ex) {
                Logger.getLogger(VehicleController.class.getName()).log(Level.SEVERE, "Error al actualizar la capacidad del vehículo con ID: " + vehiculo.getId(), ex);
                UtilsMethods.showAlert("Error al actualizar la capacidad", "Inténtalo más tarde", "ERROR");
                vehiculo.setCapacidadCarga(capacidadOriginal);
                tableView.refresh();
            } catch (CloneNotSupportedException ex) {
                Logger.getLogger(VehicleController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        //Habilita la edición del campo activo
        activeColumn.setCellFactory(column -> new VehicleActiveEditingCell());
        activeColumn.setOnEditCommit(event -> {
            Vehiculo vehiculo = event.getRowValue();

            vehiculo.setActivo(event.getNewValue());
            try {
                VehicleFactory.getVehicleInstance().updateVehiculo(vehiculo);
            } catch (UpdateException ex) {
                Logger.getLogger(VehicleController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        itvDateColumn.setCellFactory(column -> new VehicleDateEditingCell());
        itvDateColumn.setOnEditCommit(event -> {
            Vehiculo vehiculo = event.getRowValue();
            Date originalValue = vehiculo.getItvDate(); // Guardar valor original
            Logger.getLogger(VehicleController.class.getName()).info("Fecha ITV original: " + originalValue);

            try {
                Date newValue = event.getNewValue();
                if (newValue == null) {
                    throw new IllegalArgumentException("La fecha no puede estar vacía.");
                }

                Date currentDate = new Date();
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(currentDate);
                calendar.add(Calendar.YEAR, 10);
                Date maxDate = calendar.getTime();

                if (newValue.after(maxDate)) {
                    throw new IllegalArgumentException("La fecha ITV no puede ser mayor a 10 años en el futuro.");
                }

                Vehiculo vehiculoActualizado = vehiculo.clone(); // Crear una copia temporal
                vehiculoActualizado.setItvDate(newValue);
                Logger.getLogger(VehicleController.class.getName()).info("Nueva fecha ITV: " + newValue);

                VehicleFactory.getVehicleInstance().updateVehiculo(vehiculoActualizado);

                vehiculo.setItvDate(newValue); // Aplicar la actualización en la UI
                Logger.getLogger(VehicleController.class.getName()).info("Fecha ITV actualizada correctamente para el vehículo con ID: " + vehiculo.getId());
            } catch (IllegalArgumentException ex) {
                Logger.getLogger(VehicleController.class.getName()).warning("Entrada inválida para la fecha ITV: " + ex.getMessage());
                UtilsMethods.showAlert("Entrada no valida para la fecha ITV", "Prueba con una fecha diferente", "ERROR");

                vehiculo.setItvDate(originalValue); // Restaurar valor original
                itvDateColumn.getTableView().refresh();
            } catch (UpdateException ex) {
                Logger.getLogger(VehicleController.class.getName()).log(Level.SEVERE, "Error al actualizar la fecha ITV para el vehículo con ID: " + vehiculo.getId(), ex);

                UtilsMethods.showAlert("No se pudo actualizar la fecha ITV", "Inténtalo de nuevo más tarde", "ERROR");

                vehiculo.setItvDate(originalValue); // Restaurar valor original
                itvDateColumn.getTableView().refresh();
            } catch (CloneNotSupportedException ex) {
                Logger.getLogger(VehicleController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    /**
     * Modifies the capacity value in the TextField by a given amount.
     *
     * @param amount the amount to modify the capacity by (positive or
     * negative).
     */
    private void modifyCapacity(int amount) {
        try {
            int currentCapacity = Integer.parseInt(capacityTextField.getText().trim());
            int newCapacity = currentCapacity + amount;

            if (newCapacity >= 0 && newCapacity <= 999) {
                capacityTextField.setText(String.valueOf(newCapacity));
                filterByCapacity();
            } else {
                LOGGER.warning("Attempted to set capacity value outside the range (0-999).");
            }
        } catch (NumberFormatException e) {
            LOGGER.warning("Invalid capacity value: " + capacityTextField.getText());
            capacityTextField.setText("0");
        } catch (Exception e) {
            LOGGER.severe("Error updating vehicle capacity: " + e.getMessage());
            UtilsMethods.showAlert("No se pudo actualizar la fecha la capacidad", "Inténtalo de nuevo más tarde", "ERROR");
        }
    }

    /**
     * Handles the action of filtering vehicles based on date selection.
     *
     * This method clears the search text field, retrieves the date format from
     * the configuration file, and converts the selected dates from the date
     * pickers to the required format. Depending on the selected filter type, it
     * applies the appropriate filtering logic for "ITV Date" or "Registration
     * Date".
     *
     * If no dates are selected, an alert is displayed. If the selected filter
     * type is invalid, an error message is shown. Exceptions are caught and
     * handled with alerts displaying relevant messages.
     *
     * @param event the action event triggered by the filter button or other UI
     * component
     */
    private void handleFilterByDatesAction(ActionEvent event) {
        // Clear other filters
        searchTextField.clear();

        String mDateFormat = ResourceBundle.getBundle("config/config").getString("date.format");
        SimpleDateFormat dateFormat = new SimpleDateFormat(mDateFormat);

        try {
            LocalDate fromDate = fromDatePicker.getValue();
            LocalDate toDate = toDatePicker.getValue();

            // Convert to Date only if LocalDate is not null
            Date fromDateAsDate = (fromDate != null)
                    ? Date.from(fromDate.atStartOfDay(ZoneId.systemDefault()).toInstant())
                    : null;
            Date toDateAsDate = (toDate != null)
                    ? Date.from(toDate.atStartOfDay(ZoneId.systemDefault()).toInstant())
                    : null;

            // Format dates only if they are not null
            String mfromDate = (fromDateAsDate != null) ? dateFormat.format(fromDateAsDate) : null;
            String mtoDate = (toDateAsDate != null) ? dateFormat.format(toDateAsDate) : null;

            if (fromDate == null && toDate == null) {
                // No dates selected
                UtilsMethods.showAlert("Error de filtro", "Select a date");
                return;
            }

            // Determine which filter to apply based on ComboBox selection
            String selectedFilter = filterTypeComboBox.getValue();

            if ("ITV Date".equals(selectedFilter)) {
                if (fromDate == null) {

                    filterVehiclesBeforeDateITV(mtoDate);
                } else if (toDate == null) {

                    filterVehiclesAfterDateITV(mfromDate);
                } else {

                    filterVehiclesBetweenDatesITV(mfromDate, mtoDate);

                }
            } else if ("Registration Date".equals(selectedFilter)) {
                if (fromDate == null) {

                    filterVehiclesBeforeDateRegistration(mtoDate);
                } else if (toDate == null) {

                    filterVehiclesAfterDateRegistration(mfromDate);
                } else {

                    filterVehiclesBetweenDatesRegistration(mfromDate, mtoDate);
                }
            } else {
                UtilsMethods.showAlert("Error de filtro", "Please select a valid filter type.");
            }

        } catch (IllegalArgumentException e) {
            UtilsMethods.showAlert("Error de filtro", e.getMessage());
        } catch (Exception e) {
            UtilsMethods.showAlert("Error al filtrar por fechas", e.getMessage());
        }
    }

    /**
     * Filters the list of vehicles registered after a specified date.
     *
     * This method retrieves vehicles from the database that were registered
     * after the given date and updates the TableView with the results. If no
     * vehicles are found, an informational alert is displayed. In case of an
     * exception, an error message is logged and an alert is shown to the user.
     *
     * @param fromDate the starting date for filtering vehicles (exclusive)
     */
    private void filterVehiclesAfterDateRegistration(String fromDate) {

        try {
            List<Vehiculo> vehicleList = VehicleFactory.getVehicleInstance().findVehiclesAfterDateRegistration(fromDate);
            if (vehicleList != null && !vehicleList.isEmpty()) {
                vehicleTableView.setItems(FXCollections.observableArrayList(vehicleList));
            } else {
                vehicleTableView.setItems(FXCollections.observableArrayList());
                UtilsMethods.showAlert("Information", "No vehicles found after " + fromDate);
            }
        } catch (SelectException ex) {
            Logger.getLogger(VehicleController.class.getName()).log(Level.SEVERE, null, ex);
            UtilsMethods.showAlert("Error", "Failed to filter vehicles: " + ex.getMessage());
        } catch (Exception e) {
            LOGGER.severe("Error filtering vehicles by capacity: " + e.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Filter Failed");
            alert.setContentText("An error occurred while filtering vehicles. Please try again later.");
            alert.showAndWait();
        }
    }

   /**
 * Filters and displays vehicles that were registered before the specified date.
 * If vehicles are found, they are displayed in the table view. If no vehicles
 * match the criteria, an information alert is shown. In case of an error, an 
 * error alert is displayed.
 *
 * @param toDate the date to filter vehicles by registration date. Vehicles 
 *               registered before this date will be displayed.
 */
    private void filterVehiclesBeforeDateRegistration(String toDate) {

        try {
            List<Vehiculo> vehicleList = VehicleFactory.getVehicleInstance().findVehiclesBeforeDateRegistration(toDate);
            if (vehicleList != null && !vehicleList.isEmpty()) {
                vehicleTableView.setItems(FXCollections.observableArrayList(vehicleList));
            } else {
                vehicleTableView.setItems(FXCollections.observableArrayList());
                UtilsMethods.showAlert("Information", "No vehicles found before " + toDate);
            }
        } catch (SelectException ex) {
            Logger.getLogger(VehicleController.class.getName()).log(Level.SEVERE, null, ex);
            UtilsMethods.showAlert("Error", "Failed to filter vehicles: " + ex.getMessage());
        } catch (Exception e) {
            LOGGER.severe("Error filtering vehicles by capacity: " + e.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Filter Failed");
            alert.setContentText("An error occurred while filtering vehicles. Please try again later.");
            alert.showAndWait();
        }
    }

/**
 * Filters vehicles that were registered between the specified dates.
 *
 * This method retrieves a list of vehicles from the database that were registered 
 * within the given date range and updates the TableView accordingly.
 *
 * @param fromDate The starting date of the registration period (inclusive).
 * @param toDate   The ending date of the registration period (inclusive).
 *
 * @throws SelectException If there is an error retrieving the filtered vehicle data.
 * @throws Exception If an unexpected error occurs during filtering.
 */
    private void filterVehiclesBetweenDatesRegistration(String fromDate, String toDate) {

        try {
            List<Vehiculo> vehicleList = VehicleFactory.getVehicleInstance().findVehiclesBetweenDatesRegistration(toDate, fromDate);
            if (vehicleList != null && !vehicleList.isEmpty()) {
                vehicleTableView.setItems(FXCollections.observableArrayList(vehicleList));
            } else {
                vehicleTableView.setItems(FXCollections.observableArrayList());
                UtilsMethods.showAlert("Information", "No vehicles found between " + fromDate + " and " + toDate);
            }
        } catch (SelectException ex) {
            Logger.getLogger(VehicleController.class.getName()).log(Level.SEVERE, null, ex);
            UtilsMethods.showAlert("Error", "Failed to filter vehicles: " + ex.getMessage());
        } catch (Exception e) {
            LOGGER.severe("Error filtering vehicles by capacity: " + e.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Filter Failed");
            alert.setContentText("An error occurred while filtering vehicles. Please try again later.");
            alert.showAndWait();
        }
    }
/**
 * Filters vehicles that have passed their ITV (technical inspection) after the specified date.
 *
 * This method retrieves a list of vehicles from the database that have undergone 
 * their ITV after the given date and updates the TableView accordingly.
 *
 * @param fromDate The date in string format used as the filter criterion.
 *
 * @throws SelectException If there is an error retrieving the filtered vehicle data.
 * @throws Exception If an unexpected error occurs during filtering.
 */
    private void filterVehiclesAfterDateITV(String fromDate) {

        try {
            List<Vehiculo> vehicleList = VehicleFactory.getVehicleInstance().findVehiclesAfterDateITV(fromDate);
            if (vehicleList != null && !vehicleList.isEmpty()) {
                vehicleTableView.setItems(FXCollections.observableArrayList(vehicleList));
            } else {
                vehicleTableView.setItems(FXCollections.observableArrayList());
                UtilsMethods.showAlert("Information", "No vehicles found after " + fromDate);
            }
        } catch (SelectException ex) {
            Logger.getLogger(VehicleController.class.getName()).log(Level.SEVERE, null, ex);
            UtilsMethods.showAlert("Error", "Failed to filter vehicles: " + ex.getMessage());
        } catch (Exception e) {
            LOGGER.severe("Error filtering vehicles by capacity: " + e.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Filter Failed");
            alert.setContentText("An error occurred while filtering vehicles. Please try again later.");
            alert.showAndWait();
        }
    }

  /**
 * Filters and displays vehicles that have an ITV (Inspección Técnica de Vehículos) date 
 * before the specified date. If vehicles are found, they are displayed in the table view. 
 * If no vehicles match the criteria, an information alert is shown. In case of an error, 
 * an error alert is displayed.
 *
 * @param toDate the date to filter vehicles by ITV date. Vehicles with an ITV date 
 *               before this date will be displayed.
 */
    private void filterVehiclesBeforeDateITV(String toDate) {

        try {
            List<Vehiculo> vehicleList = VehicleFactory.getVehicleInstance().findVehiclesBeforeDateITV(toDate);
            if (vehicleList != null && !vehicleList.isEmpty()) {
                vehicleTableView.setItems(FXCollections.observableArrayList(vehicleList));
            } else {
                vehicleTableView.setItems(FXCollections.observableArrayList());
                UtilsMethods.showAlert("Information", "No vehicles found before " + toDate);
            }
        } catch (SelectException ex) {
            Logger.getLogger(VehicleController.class.getName()).log(Level.SEVERE, null, ex);
            UtilsMethods.showAlert("Error", "Failed to filter vehicles: " + ex.getMessage());
        } catch (Exception e) {
            LOGGER.severe("Error filtering vehicles by capacity: " + e.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Filter Failed");
            alert.setContentText("An error occurred while filtering vehicles. Please try again later.");
            alert.showAndWait();
        }
    }

    /**
 * Filters and displays vehicles that have an ITV (Inspección Técnica de Vehículos) date 
 * between the specified date range. If vehicles are found, they are displayed in the table view. 
 * If no vehicles match the criteria, an information alert is shown. In case of an error, 
 * an error alert is displayed.
 *
 * @param fromDate the starting date of the range. Vehicles with an ITV date greater than or equal 
 *                 to this date will be included in the filter.
 * @param toDate the ending date of the range. Vehicles with an ITV date less than or equal to 
 *               this date will be included in the filter.
 */
    private void filterVehiclesBetweenDatesITV(String fromDate, String toDate) {

        try {
            List<Vehiculo> vehicleList = VehicleFactory.getVehicleInstance().findVehiclesBetweenDatesITV(toDate, fromDate);

            if (vehicleList != null && !vehicleList.isEmpty()) {
                vehicleTableView.setItems(FXCollections.observableArrayList(vehicleList));
            } else {
                vehicleTableView.setItems(FXCollections.observableArrayList());
                UtilsMethods.showAlert("Information", "No vehicles found between " + fromDate + " and " + toDate);
            }
        } catch (SelectException ex) {
            Logger.getLogger(VehicleController.class.getName()).log(Level.SEVERE, null, ex);
            UtilsMethods.showAlert("Error", "Failed to filter vehicles: " + ex.getMessage());
        } catch (Exception e) {
            LOGGER.severe("Error filtering vehicles by capacity: " + e.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Filter Failed");
            alert.setContentText("An error occurred while filtering vehicles. Please try again later.");
            alert.showAndWait();
        }
    }

    /**
     * Handles the action of adding a new shipment (vehicle) to the system.
     *
     * This method creates a new vehicle with default values and attempts to
     * insert it into the database. If the insertion is successful, the vehicle
     * list is refreshed and displayed in the TableView. Errors during the
     * creation or retrieval process are logged, and appropriate alerts are
     * displayed to inform the user.
     */
    private void handleAddShipmentAction() {

        try {
            // Create new package with ID from last item in data
            Vehiculo defaultVehiculo = new Vehiculo();
            defaultVehiculo.setMatricula("");
            defaultVehiculo.setRegistrationDate(new Date());
            // Add to database first
            Vehiculo savedPackage = VehicleFactory.getVehicleInstance().createVehicle(defaultVehiculo);
            if (savedPackage != null) {
                // If database insert successful, add to table data
                data.clear();
                data.addAll(VehicleFactory.getVehicleInstance().findAllVehiculos());
                vehicleTableView.setItems(data);
            }
        } catch (CreateException ex) {
            Logger.getLogger(VehicleFactory.class.getName()).log(Level.SEVERE, null, ex);
            //   UtilsMethods.showAlert("Error", "Failed to create package: " + ex.getMessage());
        } catch (SelectException ex) {
            Logger.getLogger(VehicleFactory.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception e) {
            LOGGER.severe("Error filtering vehicles by capacity: " + e.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Filter Failed");
            alert.setContentText("An error occurred while filtering vehicles. Please try again later.");
            alert.showAndWait();
        }

    }

    /**
     * Handles the removal of selected vehicles from the system.
     *
     * This method checks if any vehicles are selected in the TableView and
     * prompts the user for confirmation before proceeding with deletion. If
     * confirmed, it updates related records in `EnvioRutaVehiculo`, setting
     * their `vehiculo_id` to null before deleting the vehicle from the
     * database. If successful, the TableView is updated accordingly.
     *
     * Error handling ensures that any issues during the deletion process,
     * including database constraints and web service errors, are logged and
     * displayed to the user through alerts.
     */
    @FXML
    private void onRemoveShipmentClicked() {
        ObservableList<Vehiculo> selectedVehicles = vehicleTableView.getSelectionModel().getSelectedItems();

        if (selectedVehicles.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setHeaderText("No Vehicles Selected");
            alert.setContentText("Please select at least one vehicle to remove.");
            alert.showAndWait();
            return;
        }

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Deletion");
        confirmAlert.setHeaderText("Delete Vehicles");
        confirmAlert.setContentText("Are you sure you want to delete the selected vehicles? This will also remove any associated shipments and route assignments.");

        confirmAlert.showAndWait().ifPresent(response -> {
            if (response == javafx.scene.control.ButtonType.OK) {
                try {
                    EnvioRutaVehiculoManager ervManager = EnvioRutaVehiculoFactory.getEnvioRutaVehiculoInstance();

                    for (Vehiculo vehicle : selectedVehicles) {
                        try {
                            // 1. Obtener los registros de EnvioRutaVehiculo asociados con este vehículo
                            List<EnvioRutaVehiculo> assignments = ervManager.getId(
                                    new GenericType<List<EnvioRutaVehiculo>>() {
                            },
                                    String.valueOf(vehicle.getId())
                            );

                            if (assignments != null && !assignments.isEmpty()) {
                                // 2. Actualizar cada registro poniendo vehiculo_id a null
                                for (EnvioRutaVehiculo assignment : assignments) {
                                    try {
                                        // Establecer el vehiculo_id a null
                                        assignment.setVehiculo(null);
                                        // Usar el método edit_XML para actualizar el registro
                                        ervManager.edit_XML(assignment, String.valueOf(assignment.getId()));
                                        LOGGER.info("Successfully updated route assignment: " + assignment.getId());
                                    } catch (WebApplicationException ex) {
                                        LOGGER.severe("Error updating route assignment: " + assignment.getId()
                                                + " Error: " + ex.getMessage());
                                        throw ex;
                                    }
                                }
                            }

                            // 3. Una vez que todos los registros relacionados están actualizados, eliminar el vehículo
                            VehicleFactory.getVehicleInstance().deleteVehiculo(vehicle.getId());
                            LOGGER.info("Successfully removed vehicle: " + vehicle.getId());

                        } catch (WebApplicationException ex) {
                            LOGGER.severe("Error processing vehicle: " + vehicle.getId()
                                    + " Error: " + ex.getMessage());
                            throw ex;
                        }
                    }

                    // Remover vehículos del TableView
                    vehicleTableView.getItems().removeAll(selectedVehicles);
                    vehicleTableView.getSelectionModel().clearSelection();
                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                    successAlert.setTitle("Success");
                    successAlert.setHeaderText("Vehicles Removed");
                    successAlert.setContentText("The selected vehicles and their associated data have been removed successfully.");
                    successAlert.showAndWait();

                } catch (WebApplicationException ex) {
                    LOGGER.severe("Error in deletion process: " + ex.getMessage());
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setTitle("Error");
                    errorAlert.setHeaderText("Deletion Failed");
                    errorAlert.setContentText("An error occurred while deleting vehicles and their associated data: "
                            + ex.getResponse().getStatusInfo().getReasonPhrase());
                    errorAlert.showAndWait();
                } catch (Exception e) {
                    LOGGER.severe("Unexpected error during deletion: " + e.getMessage());
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setTitle("Error");
                    errorAlert.setHeaderText("Deletion Failed");
                    errorAlert.setContentText("An unexpected error occurred while deleting vehicles and their associated data.");
                    errorAlert.showAndWait();
                }
            }
        });
    }

    /**
     * Handles the search functionality for vehicles based on the entered
     * license plate.
     *
     * If the search field is empty, the method reloads all vehicles from the
     * database. Otherwise, it retrieves vehicles matching the provided license
     * plate (`matricula`) and updates the TableView accordingly.
     *
     * Error handling ensures that any issues during the filtering process are
     * logged and displayed to the user through an alert.
     */
    @FXML
    private void onSearchButtonClicked() {
        String matricula = searchTextField.getText().trim();

        if (matricula.isEmpty()) {
            fillTableFromDataBase();
            return;
        }

        try {
            List<Vehiculo> filteredVehicles = VehicleFactory.getVehicleInstance().findAllVehiculosByPlate(matricula);
            vehicleTableView.setItems(FXCollections.observableArrayList(filteredVehicles));
        } catch (Exception e) {
            LOGGER.severe("Error filtering vehicles by matricula: " + e.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Filter Failed");
            alert.setContentText("An error occurred while filtering vehicles. Please try again later.");
            alert.showAndWait();
        }
    }

    /**
     * Filters the list of vehicles based on the specified capacity.
     *
     * The method retrieves the capacity value entered in the
     * `capacityTextField`, converts it to an integer, and queries the database
     * for vehicles matching the specified capacity. The results are displayed
     * in the TableView.
     *
     * If the input is not a valid number, a warning alert is shown to the user.
     * Any unexpected errors during the filtering process are logged and
     * displayed through an error alert.
     */
    private void filterByCapacity() {
        try {
            int capacity = Integer.parseInt(capacityTextField.getText().trim());
            List<Vehiculo> filteredVehicles = VehicleFactory.getVehicleInstance().findAllVehiculosByCapacity(capacity);
            vehicleTableView.setItems(FXCollections.observableArrayList(filteredVehicles));
        } catch (NumberFormatException e) {
            LOGGER.warning("Invalid capacity input for filtering: " + capacityTextField.getText());
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Invalid Input");
            alert.setHeaderText("Invalid Capacity");
            alert.setContentText("Please enter a valid number for capacity.");
            alert.showAndWait();
        } catch (Exception e) {
            LOGGER.severe("Error filtering vehicles by capacity: " + e.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Filter Failed");
            alert.setContentText("An error occurred while filtering vehicles. Please try again later.");
            alert.showAndWait();
        }
    }

    /**
     * Convierte un LocalDate a un objeto Date.
     *
     * @param localDate la fecha LocalDate a convertir
     * @return la fecha convertida en formato Date
     */
    private Date convertToDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    /**
     * Populates the table with example data.
     */
    @FXML
    private void onSearch() {
        String query = searchTextField.getText();
        // Implement search logic
    }

    /**
     * Handles the action of printing a vehicle report using JasperReports.
     *
     * This method compiles a JasperReport template, retrieves vehicle data from
     * the TableView, and fills the report with the necessary data before
     * displaying it in a JasperViewer window.
     *
     * @param event The action event triggered by clicking the print report
     * button.
     *
     * @throws JRException If an error occurs while compiling or filling the
     * report.
     * @throws Exception If an unexpected error occurs during report generation.
     */
    private void handlePrintReportAction(ActionEvent event) {
        // TODO: Implement print report logic

        try {
            LOGGER.info("Beginning printing action...");
            JasperReport report = JasperCompileManager.compileReport(getClass().getResourceAsStream("/ui/report/vehicleReport.jrxml"));
            //Data for the report: a collection of UserBean passed as a JRDataSource 
            //implementation 
            JRBeanCollectionDataSource dataItems = new JRBeanCollectionDataSource((Collection<Vehiculo>) this.vehicleTableView.getItems());
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
            LOGGER.log(Level.SEVERE, "UI GestionUsuariosController: Error printing report: {0}", ex.getMessage());
        } catch (Exception e) {
            LOGGER.severe("Error filtering vehicles by capacity: " + e.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Filter Failed");
            alert.setContentText("An error occurred while filtering vehicles. Please try again later.");
            alert.showAndWait();
        }
    }

}
