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
import static utils.UtilsMethods.logger;

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
                    return Integer.valueOf(string);
                } catch (NumberFormatException e) {
                    LOGGER.warning("Invalid weight: " + string);
                    return null;
                }
            }
        }
        ));

        capacityColumn.setOnEditCommit(event -> event.getRowValue().setCapacidadCarga(event.getNewValue()));
        capacityColumn.setOnEditCommit(event -> {
            Vehiculo vehiculo = event.getRowValue();
            vehiculo.setCapacidadCarga(event.getNewValue());
            try {
                // Add any additional handling (like saving to database)
                VehicleFactory.getVehicleInstance().updateVehiculo(vehiculo);
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

    private void handleAddShipmentAction() {

        try {
            // Create new package with ID from last item in data
            Vehiculo defaultVehiculo = new Vehiculo();
            defaultVehiculo.setMatricula("");
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

        /*

        try {
            List<Vehiculo> infovehicleList = VehicleFactory.getVehicleInstance().findAllVehiculos();
            Vehiculo defaultVehiculo = null;
            defaultVehiculo = new Vehiculo();
            defaultVehiculo.setId(infovehicleList.get(infovehicleList.size() - 1).getId() + 1);
            defaultVehiculo.setMatricula("");
            defaultVehiculo.setModelo("Default Model");
            defaultVehiculo.setCapacidadCarga(1);
            defaultVehiculo.setRegistrationDate(new Date());
            defaultVehiculo.setItvDate(new Date());
            defaultVehiculo.setActivo(true);

            infovehicleList.add(defaultVehiculo);
            Vehiculo v2 = VehicleFactory.getVehicleInstance().createVehicle(defaultVehiculo);
            System.out.println(v2.toString());
            vehicleTableView.setItems(FXCollections.observableArrayList(infovehicleList));

        } catch (Exception ex) {
            Logger.getLogger(VehicleController.class.getName()).log(Level.SEVERE, null, ex);
            // show alert
            System.out.println("Somthinr is woripgn");
        }
         */
    }

//    private void addShipment() {
//        try {
//            // Crear una nueva ruta vacía
//            Vehiculo defaultVehiculo = new Vehiculo();
//            defaultVehiculo.setCapacidadCarga(0);
//            defaultVehiculo.setMatricula("");
//            defaultVehiculo.setModelo("");
//            defaultVehiculo.setItvDate(new Date());
//            defaultVehiculo.setActivo(false);
//            defaultVehiculo.setRegistrationDate(new Date());
//
//            // Enviar la nueva ruta al servidor
//            Vehiculo createVehicle = VehicleFactory.getVehicleInstance().createVehicle(defaultVehiculo);
//            // rutaManager.edit_XML(nuevaRuta, "0"); // Usar "0" o el ID correspondiente para el servidor
//
//            // Refrescar la tabla recargando los datos desde el servidor
//            //loadRutaData();
//            logger.info("Nueva ruta vacía añadida y tabla actualizada correctamente.");
//        } catch (WebApplicationException e) {
//            logger.log(Level.SEVERE, "Error al añadir una nueva ruta", e);
//            // showAlert("Error", "No se pudo añadir la nueva ruta.");
//        } catch (Exception e) {
//            logger.log(Level.SEVERE, "Error inesperado", e);
//            //  showAlert("Error", "Error inesperado al añadir la nueva ruta.");
//        }
//    }
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
}
