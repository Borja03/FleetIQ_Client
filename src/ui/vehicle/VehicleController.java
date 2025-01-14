package ui.vehicle;

import models.Vehiculo;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import factories.VehicleFactory;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;
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
import javafx.stage.Stage;
import javafx.util.StringConverter;
import javafx.util.converter.IntegerStringConverter;

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
    private TableColumn<Vehiculo, LocalDate> registrationDateColumn;

    @FXML
    private TableColumn<Vehiculo, LocalDate> itvDateColumn;

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
    private TableColumn<?, ?> ruta;

    @FXML
    //private TableColumn<Ruta, String> fecha_asignacion;

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
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Vehicle Management");
        stage.setResizable(false);
        stage.centerOnScreen();

//        loadConfigurations();
//        setUpDatePickers();

        filterTypeComboBox.getItems().setAll("ITV Date", "Registration Date");
        setUpTableColumns();
        fillTableFromDataBase();

        stage.show();
    }

    /**
     * Loads configurations from a properties file.
//     */
//    private void loadConfigurations() {
//        Properties config = new Properties();
//        try (InputStream input = getClass().getResourceAsStream("/config/config.properties")) {
//            if (input == null) {
//                LOGGER.warning("Unable to find config.properties");
//                return;
//            }
//
//            config.load(input);
//
//            String dateFormat = config.getProperty("date.format", "dd/MM/yyyy");
//            dateFormatter = DateTimeFormatter.ofPattern(dateFormat);
//
//            startDate = LocalDate.parse(config.getProperty("start.date", LocalDate.now().toString()), dateFormatter);
//            endDate = LocalDate.parse(config.getProperty("end.date", LocalDate.now().toString()), dateFormatter);
//        } catch (IOException ex) {
//            LOGGER.severe("Error loading configurations: " + ex.getMessage());
//        }
//    }

    /**
     * Sets up date pickers with formatting and range limits.
     */
//    private void setUpDatePickers() {
//        StringConverter<LocalDate> converter = new StringConverter<LocalDate>() {
//            @Override
//            public String toString(LocalDate date) {
//                return date != null ? dateFormatter.format(date) : "";
//            }
//
//            @Override
//            public LocalDate fromString(String string) {
//                return string != null && !string.isEmpty() ? LocalDate.parse(string, dateFormatter) : null;
//            }
//        };
//
//        fromDatePicker.setConverter(converter);
//        toDatePicker.setConverter(converter);
//
//        fromDatePicker.setDayCellFactory(picker -> new DateCell() {
//            @Override
//            public void updateItem(LocalDate date, boolean empty) {
//                super.updateItem(date, empty);
//                setDisable(empty || date.isBefore(startDate) || date.isAfter(endDate));
//            }
//        });
//
//        toDatePicker.setDayCellFactory(picker -> new DateCell() {
//            @Override
//            public void updateItem(LocalDate date, boolean empty) {
//                super.updateItem(date, empty);
//                setDisable(empty || date.isBefore(startDate) || date.isAfter(endDate));
//            }
//        });
//    }

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
     * Configures the table columns.
     */
    private void setUpTableColumns() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        matriculaColumn.setCellValueFactory(new PropertyValueFactory<>("matricula"));
        modelColumn.setCellValueFactory(new PropertyValueFactory<>("modelo"));
        capacityColumn.setCellValueFactory(new PropertyValueFactory<>("capacidadCarga"));
        registrationDateColumn.setCellValueFactory(new PropertyValueFactory<>("registrationDate"));
        itvDateColumn.setCellValueFactory(new PropertyValueFactory<>("itvDate"));
        activeColumn.setCellValueFactory(new PropertyValueFactory<>("activo"));
//
//        itvDateColumn.setCellFactory(column -> new TableCell<Vehiculo, Date>() {
//            @Override
//            protected void updateItem(Date date, boolean empty) {
//                super.updateItem(date, empty);
//                setText(empty || date == null ? null : dateFormatter.format(date));
//            }
//        });
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
     * Populates the table with example data.
     */
    @FXML
    private void onApplyFilter() {
        // Implement filter logic
    }

    @FXML
    private void onSearch() {
        String query = searchTextField.getText();
        // Implement search logic
    }

    @FXML
    private void onAddVehicle() {
        // Open dialog to add a vehicle
    }

    @FXML
    private void onRemoveVehicle() {
        Vehiculo selectedVehicle = vehicleTableView.getSelectionModel().getSelectedItem();
        if (selectedVehicle != null) {
            vehicleTableView.getItems().remove(selectedVehicle);
        }
    }
}
