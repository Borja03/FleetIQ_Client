package ui.vehicle;

import model.Paquete;
import model.PaqueteSize;
import model.Vehicle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DateCell;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.StringConverter;

/**
 * Controller for managing the Vehicle UI.
 * Handles initialization, configuration, and event logic.
 * 
 * @author Omar
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
    private TableView<Vehicle> vehicleTableView;

    @FXML
    private TableColumn<Vehicle, Integer> idColumn;
    
    @FXML
    private TableColumn<Vehicle, String> matriculaColumn;
    
    @FXML
    private TableColumn<Vehicle, String> modelColumn;
    
    @FXML
    private TableColumn<Vehicle, Double> capacityColumn;
    
    @FXML
    private TableColumn<Vehicle, LocalDate> registrationDateColumn;
    
    @FXML
    private TableColumn<Vehicle, LocalDate> itvDateColumn;
    
    @FXML
    private TableColumn<Vehicle, Boolean> activeColumn;

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

        loadConfigurations();
        setUpDatePickers();

        filterTypeComboBox.getItems().setAll("ITV Date", "Registration Date");
        setUpTableColumns();
        fillTableWithExampleData();

        stage.show();
    }

    /**
     * Loads configurations from a properties file.
     */
    private void loadConfigurations() {
        Properties config = new Properties();
        try (InputStream input = getClass().getResourceAsStream("/config/config.properties")) {
            if (input == null) {
                LOGGER.warning("Unable to find config.properties");
                return;
            }

            config.load(input);

            String dateFormat = config.getProperty("date.format", "dd/MM/yyyy");
            dateFormatter = DateTimeFormatter.ofPattern(dateFormat);

            startDate = LocalDate.parse(config.getProperty("start.date", LocalDate.now().toString()), dateFormatter);
            endDate = LocalDate.parse(config.getProperty("end.date", LocalDate.now().toString()), dateFormatter);
        } catch (IOException ex) {
            LOGGER.severe("Error loading configurations: " + ex.getMessage());
        }
    }

    /**
     * Sets up date pickers with formatting and range limits.
     */
    private void setUpDatePickers() {
        StringConverter<LocalDate> converter = new StringConverter<LocalDate>() {
            @Override
            public String toString(LocalDate date) {
                return date != null ? dateFormatter.format(date) : "";
            }

            @Override
            public LocalDate fromString(String string) {
                return string != null && !string.isEmpty() ? LocalDate.parse(string, dateFormatter) : null;
            }
        };

        fromDatePicker.setConverter(converter);
        toDatePicker.setConverter(converter);

        fromDatePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.isBefore(startDate) || date.isAfter(endDate));
            }
        });

        toDatePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.isBefore(startDate) || date.isAfter(endDate));
            }
        });
    }

    /**
     * Configures the table columns.
     */
    private void setUpTableColumns() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        matriculaColumn.setCellValueFactory(new PropertyValueFactory<>("licensePlate"));
        modelColumn.setCellValueFactory(new PropertyValueFactory<>("model"));
        capacityColumn.setCellValueFactory(new PropertyValueFactory<>("capacity"));
        registrationDateColumn.setCellValueFactory(new PropertyValueFactory<>("registrationDate"));
        itvDateColumn.setCellValueFactory(new PropertyValueFactory<>("itvDate"));
        activeColumn.setCellValueFactory(new PropertyValueFactory<>("active"));

        itvDateColumn.setCellFactory(column -> new TableCell<Vehicle, LocalDate>() {
            @Override
            protected void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setText(empty || date == null ? null : dateFormatter.format(date));
            }
        });
    }

    /**
     * Populates the table with example data.
     */
    private void fillTableWithExampleData() {
        ObservableList<Vehicle> data = FXCollections.observableArrayList(
                new Vehicle(1, "ABC123", "Toyota Corolla", 5, LocalDate.now().minusYears(2), LocalDate.now().minusMonths(6), true),
                new Vehicle(2, "XYZ789", "Honda Civic", 5, LocalDate.now().minusYears(3), LocalDate.now().minusMonths(3), false),
                new Vehicle(3, "DEF456", "Ford Focus", 4, LocalDate.now().minusYears(1), LocalDate.now().minusMonths(2), true)
        );

        vehicleTableView.setItems(data);
    }

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
        Vehicle selectedVehicle = vehicleTableView.getSelectionModel().getSelectedItem();
        if (selectedVehicle != null) {
            vehicleTableView.getItems().remove(selectedVehicle);
        }
    }
}
