package ui.vehicle;

import models.Vehiculo;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
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
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.DateCell;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import javafx.util.converter.IntegerStringConverter;
import models.EnvioRutaVehiculo;
import models.Vehiculo;

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
    private TableColumn<Vehiculo, Double> capacityColumn;

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

        // Add event handlers for other buttons
        searchButton.setOnAction(event -> onSearchButtonClicked());
        removeShipmentBtn.setOnAction(event -> onRemoveShipmentClicked());
        configureRemoveShipmentButton();

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

    }

    /**
     * Initializes the event handlers for incrementing and decrementing the
     * capacity value.
     */
    @FXML
    private void initialize() {
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

        // Add event handler for search button
        searchButton.setOnAction(event -> onSearchButtonClicked());

        LOGGER.info("Capacity controls initialized.");
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

    @FXML
    private void onRemoveShipmentClicked() {
        Vehiculo selectedVehicle = vehicleTableView.getSelectionModel().getSelectedItem();

        if (selectedVehicle == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setHeaderText("No Vehicle Selected");
            alert.setContentText("Please select a vehicle to remove.");
            alert.showAndWait();
            return;
        }

        // Confirm deletion
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Deletion");
        confirmAlert.setHeaderText("Delete Vehicle");
        confirmAlert.setContentText("Are you sure you want to delete the selected vehicle?");

        confirmAlert.showAndWait().ifPresent(response -> {
            if (response == javafx.scene.control.ButtonType.OK) {
                vehicleTableView.getItems().remove(selectedVehicle);

                // Optionally delete from database
                try {
                    VehicleFactory.getVehicleInstance().deleteVehiculo(selectedVehicle.getId());
                    LOGGER.info("Vehicle removed successfully: " + selectedVehicle.getMatricula());
                } catch (Exception e) {
                    LOGGER.severe("Error deleting vehicle: " + e.getMessage());
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setTitle("Error");
                    errorAlert.setHeaderText("Deletion Failed");
                    errorAlert.setContentText("An error occurred while deleting the vehicle.");
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

    /*
    @FXML
    private void onApplyFilter() {
        // Obtener la selección actual de la ComboBox
        String selectedFilterType = filterTypeComboBox.getValue();

        // Obtener las fechas de los DatePicker
        LocalDate startDate = fromDatePicker.getValue();
        LocalDate endDate = toDatePicker.getValue();

        // Comprobar si se seleccionaron valores válidos
        if (selectedFilterType == null || startDate == null || endDate == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Invalid Input");
            alert.setHeaderText("Invalid Filter Configuration");
            alert.setContentText("Please ensure both filter type and date range are selected.");
            alert.showAndWait();
            return;
        }

        // Convertir LocalDate a Date
        Date startDateConverted = convertToDate(startDate);
        Date endDateConverted = convertToDate(endDate);

        try {
            // Llamar a la función correcta según el tipo de filtro seleccionado
            if ("ITV Date".equals(selectedFilterType)) {
                // Llamar a la función para filtrar por fecha ITV
                List<Vehiculo> filteredVehicles = VehicleFactory.getVehicleInstance()
                        .findVehiculosByItvDateRange(startDateConverted, endDateConverted);
                vehicleTableView.setItems(FXCollections.observableArrayList(filteredVehicles));
            } else if ("Registration Date".equals(selectedFilterType)) {
                // Llamar a la función para filtrar por fecha de registro
                List<Vehiculo> filteredVehicles = VehicleFactory.getVehicleInstance()
                        .findVehiculosByRegistrationDateRange(startDateConverted, endDateConverted);
                vehicleTableView.setItems(FXCollections.observableArrayList(filteredVehicles));
            }
        } catch (Exception e) {
            LOGGER.severe("Error applying filter: " + e.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Filter Application Failed");
            alert.setContentText("An error occurred while applying the filter. Please try again later.");
            alert.showAndWait();
        }
    }
     */
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

    /*
    @FXML
    private void onAddVehicle() {
        try {
            // Gather input from UI fields
            String matricula = matriculaTextField.getText();
            String modelo = modelTextField.getText();
            double capacidadCarga = Double.parseDouble(capacityTextField.getText());
            LocalDate registrationDate = fromDatePicker.getValue();
            LocalDate itvDate = toDatePicker.getValue();
            boolean activo = activeCheckBox.isSelected();

            // Create a Vehiculo object
            Vehiculo newVehicle = new Vehiculo(matricula, modelo, capacidadCarga, Date.from(registrationDate.atStartOfDay(ZoneId.systemDefault()).toInstant()), Date.from(itvDate.atStartOfDay(ZoneId.systemDefault()).toInstant()), activo);

            // Save it to the database
            VehicleFactory.getVehicleInstance().createVehicle(newVehicle);

            // Refresh the table view
            fillTableFromDataBase();

            showAlert(Alert.AlertType.INFORMATION, "Success", "Vehicle added successfully.");
        } catch (Exception e) {
            LOGGER.severe("Error adding vehicle: " + e.getMessage());
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to add vehicle. Please check the input values.");
        }
    }
     */

 /*
    @FXML
private void onUpdateVehicle() {
    Vehiculo selectedVehicle = vehicleTableView.getSelectionModel().getSelectedItem();
    if (selectedVehicle == null) {
        showAlert(Alert.AlertType.WARNING, "Warning", "Please select a vehicle to update.");
        return;
    }
    try {
        // Update vehicle fields
        selectedVehicle.setMatricula(matriculaTextField.getText());
        selectedVehicle.setModelo(modelTextField.getText());
        selectedVehicle.setCapacidadCarga(Double.parseDouble(capacityTextField.getText()));
        selectedVehicle.setRegistrationDate(Date.from(fromDatePicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        selectedVehicle.setItvDate(Date.from(toDatePicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        selectedVehicle.setActivo(activeCheckBox.isSelected());

        // Save the updated vehicle to the database
        VehicleFactory.getVehicleInstance().updateVehicle(selectedVehicle);

        // Refresh the table view
        fillTableFromDataBase();

        showAlert(Alert.AlertType.INFORMATION, "Success", "Vehicle updated successfully.");
    } catch (Exception e) {
        LOGGER.severe("Error updating vehicle: " + e.getMessage());
        showAlert(Alert.AlertType.ERROR, "Error", "Failed to update vehicle. Please check the input values.");
    }
}
     */
 /*
    @FXML
private void onDeleteVehicle() {
    Vehiculo selectedVehicle = vehicleTableView.getSelectionModel().getSelectedItem();
    if (selectedVehicle == null) {
        showAlert(Alert.AlertType.WARNING, "Warning", "Please select a vehicle to delete.");
        return;
    }
    try {
        // Delete the selected vehicle
        VehicleFactory.getVehicleInstance().deleteVehicle(selectedVehicle.getId());

        // Refresh the table view
        fillTableFromDataBase();

        showAlert(Alert.AlertType.INFORMATION, "Success", "Vehicle deleted successfully.");
    } catch (Exception e) {
        LOGGER.severe("Error deleting vehicle: " + e.getMessage());
        showAlert(Alert.AlertType.ERROR, "Error", "Failed to delete vehicle.");
    }
}
     */
}
