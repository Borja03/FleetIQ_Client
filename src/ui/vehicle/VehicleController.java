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
import factories.VehicleFactory;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
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
import utils.UtilsMethods;
import static utils.UtilsMethods.logger;
import javafx.scene.control.TablePosition;

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
        configureRemoveShipmentButton();
        addShipmentBtn.setOnAction(event -> handleAddShipmentAction());
        applyFilterButton.setOnAction(this::handleFilterByDatesAction);

        vehicleTableView.setEditable(true);
        matriculaColumn.setEditable(true);
        capacityColumn.setEditable(true);
        itvDateColumn.setEditable(true);
        activeColumn.setEditable(true);

        LOGGER.info("Vehicle window and capacity controls initialized.");

        stage.show();
    }

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

    // Método para inicializar la lógica de deshabilitación del botón
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
        vehicleTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        matriculaColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        matriculaColumn.setOnEditCommit(event -> {
            Vehiculo vehiculo = event.getRowValue();
            System.out.println(vehiculo.toString());
            vehiculo.setMatricula(event.getNewValue());
            try {
                // Add any additional handling (like saving to database)
                VehicleFactory.getVehicleInstance().updateVehiculo(vehiculo);
            } catch (UpdateException ex) {
                Logger.getLogger(VehicleController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        modelColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        modelColumn.setOnEditCommit(event -> {
            Vehiculo vehiculo = event.getRowValue();
            System.out.println(vehiculo.toString());
            vehiculo.setModelo(event.getNewValue());
            try {
                // Add any additional handling (like saving to database)
                VehicleFactory.getVehicleInstance().updateVehiculo(vehiculo);
            } catch (UpdateException ex) {
                Logger.getLogger(VehicleController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        capacityColumn.setCellFactory(TextFieldTableCell.forTableColumn(
                new StringConverter<Integer>() {
            @Override
            public String toString(Integer object) {
                return object != null ? object.toString() : "";
            }

            @Override
            public Integer fromString(String string) {
                try {
                    return Integer.parseInt(string);
                } catch (NumberFormatException e) {
                    LOGGER.warning("Invalid capacity: " + string);
                    throw new IllegalArgumentException("Only numeric values are allowed");
                }
            }
        }
        ));

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
                    throw new IllegalArgumentException("Only positive numeric values are allowed");
                }
            }
        }
        ));

        capacityColumn.setOnEditCommit(event -> {
            TableView<Vehiculo> tableView = event.getTableView();
            TablePosition<Vehiculo, Integer> pos = event.getTablePosition();
            int row = pos.getRow();

            try {
                Integer newValue = event.getNewValue();
                if (newValue == null || newValue < 0) {
                    throw new IllegalArgumentException("Value cannot be negative");
                }

                Vehiculo vehiculo = event.getRowValue();
                vehiculo.setCapacidadCarga(newValue);

                // Save to the database
                VehicleFactory.getVehicleInstance().updateVehiculo(vehiculo);
            } catch (IllegalArgumentException ex) {
                LOGGER.warning(ex.getMessage());
                // Show alert to user
                showAlert("Invalid Input", "Only positive numeric values are allowed.", AlertType.ERROR);

                // Restore the previous value and cancel editing
                tableView.getItems().get(row).setCapacidadCarga(event.getOldValue());
                tableView.edit(row, event.getTableColumn()); // Stay in edit mode
            } catch (UpdateException ex) {
                Logger.getLogger(VehicleController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

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
            vehiculo.setItvDate(event.getNewValue());

            try {
                // Add any additional handling (like saving to database)
                VehicleFactory.getVehicleInstance().updateVehiculo(vehiculo);
            } catch (UpdateException ex) {
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
        }
    }

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

            System.out.println("From Date: " + mfromDate);
            System.out.println("To Date: " + mtoDate);

            if (fromDate == null && toDate == null) {
                // No dates selected
                UtilsMethods.showAlert("Error de filtro", "Select a date");
                return;
            }

            // Determine which filter to apply based on ComboBox selection
            String selectedFilter = filterTypeComboBox.getValue();

            if ("ITV Date".equals(selectedFilter)) {
                if (fromDate == null) {
                    System.out.println("Filtering vehicles before ITV date: " + mtoDate);
                    filterVehiclesBeforeDateITV(mtoDate);
                } else if (toDate == null) {
                    System.out.println("Filtering vehicles after ITV date: " + mfromDate);
                    filterVehiclesAfterDateITV(mfromDate);
                } else {
                    System.out.println("Filtering vehicles between ITV dates: " + mfromDate + " and " + mtoDate);
                    filterVehiclesBetweenDatesITV(mfromDate, mtoDate);

                }
            } else if ("Registration Date".equals(selectedFilter)) {
                if (fromDate == null) {
                    System.out.println("Filtering vehicles before registration date: " + mtoDate);
                    filterVehiclesBeforeDateRegistration(mtoDate);
                } else if (toDate == null) {
                    System.out.println("Filtering vehicles after registration date: " + mfromDate);
                    filterVehiclesAfterDateRegistration(mfromDate);
                } else {
                    System.out.println("Filtering vehicles between registration dates: " + mfromDate + " and " + mtoDate);
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

    private void filterVehiclesAfterDateRegistration(String fromDate) {
        System.out.println("Executing filterVehiclesAfterDateRegistration with date: " + fromDate);

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
        }
    }

    private void showAlert(String title, String message, AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Filter packages before the specified date
     */
    private void filterVehiclesBeforeDateRegistration(String toDate) {
        System.out.println("Executing filterVehiclesBeforeDateRegistration with date: " + toDate);

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
        }
    }

    /**
     * Filter packages between two dates
     */
    private void filterVehiclesBetweenDatesRegistration(String fromDate, String toDate) {
        System.out.println("Executing filterVehiclesBetweenDatesRegistration with dates: " + fromDate + " to " + toDate);

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
        }
    }

    private void filterVehiclesAfterDateITV(String fromDate) {
        System.out.println("Executing filterVehiclesAfterDateITV with date: " + fromDate);

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
        }
    }

    /**
     * Filter packages before the specified date
     */
    private void filterVehiclesBeforeDateITV(String toDate) {
        System.out.println("Executing filterVehiclesBeforeDateITV with date: " + toDate);

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
        }
    }

    /**
     * Filter packages between two dates
     */
    private void filterVehiclesBetweenDatesITV(String fromDate, String toDate) {
        System.out.println("Executing filterVehiclesBetweenDatesITV with dates: " + fromDate + " to " + toDate);
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
        }
    }

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
        }

    }

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

        // Confirm deletion for multiple vehicles
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Deletion");
        confirmAlert.setHeaderText("Delete Vehicles");
        confirmAlert.setContentText("Are you sure you want to delete the selected vehicles?");

        confirmAlert.showAndWait().ifPresent(response -> {
            if (response == javafx.scene.control.ButtonType.OK) {
                // Create an array to hold the IDs of the selected vehicles
                int[] vehicleIdsToDelete = new int[selectedVehicles.size()];
                for (int i = 0; i < selectedVehicles.size(); i++) {
                    vehicleIdsToDelete[i] = selectedVehicles.get(i).getId();
                }

                // Deleting from the database
                try {
                    // Deleting each vehicle from the database
                    for (int vehicleId : vehicleIdsToDelete) {
                        VehicleFactory.getVehicleInstance().deleteVehiculo(vehicleId);
                    }
                    LOGGER.info("Vehicles removed successfully");

                    // Remove vehicles from TableView
                    vehicleTableView.getItems().removeAll(selectedVehicles);

                    // Inform the user of the successful removal
                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                    successAlert.setTitle("Success");
                    successAlert.setHeaderText("Vehicles Removed");
                    successAlert.setContentText("The selected vehicles have been removed successfully.");
                    successAlert.showAndWait();
                } catch (Exception e) {
                    LOGGER.severe("Error deleting vehicles: " + e.getMessage());
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setTitle("Error");
                    errorAlert.setHeaderText("Deletion Failed");
                    errorAlert.setContentText("An error occurred while deleting one or more vehicles.");
                    errorAlert.showAndWait();
                }
            }
        });
    }

    /**
     * Filters the vehicle list by license plate (matricula) when the search
     * button is clicked.
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

}
