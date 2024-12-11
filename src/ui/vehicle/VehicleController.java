package ui.vehicle;

import model.Paquete;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.io.InputStream;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import javafx.util.StringConverter;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DateCell;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.PaqueteSize;

/**
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
    private JFXComboBox<PaqueteSize> filterTypeComboBox;

    @FXML
    private JFXTextField searchTextField;

    @FXML
    private TableView<Paquete> paqueteTableView;

    @FXML
    private TableColumn<Paquete, Integer> idColumn;
    @FXML
    private TableColumn<Paquete, String> matriculaColumn;
    @FXML
    private TableColumn<Paquete, String> modelColumn;
    @FXML
    private TableColumn<Paquete, Double> capacityColumn;
    @FXML
    private TableColumn<Paquete, PaqueteSize> registrationDateColumn;
    @FXML
    private TableColumn<Paquete, LocalDate> ITVdateColumn;
    @FXML
    private TableColumn<Paquete, Boolean> activeColumn;

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

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
    // Date formatter and limits
    private DateTimeFormatter dateFormatter;
    private LocalDate startDate;
    private LocalDate endDate;

    public void initStage(Parent root) {
        LOGGER.info("Initialising Sign Up window.");
        Scene scene = new Scene(root);
        // Set the stage properties
        stage.setScene(scene);
        stage.setTitle("Paquete");
        stage.setResizable(false);
        // stage.initModality(Modality.APPLICATION_MODAL);
        stage.centerOnScreen();
        //stage.getIcons().add(new Image("/images/userIcon.png"));
        LOGGER.info("Window opened.");
        // Load configuration using ResourceBundle
        // loadConfigurationSettings();

        // Load configurations
        loadConfigurations();
        // Set up date pickers
        setUpDatePickers();
        // Populate size filter combo box
        filterTypeComboBox.getItems().setAll(PaqueteSize.values());
        // Set up table columns
        setUpTableColumns();
        // Fill table with example data
        fillTableWithExampleData();

        stage.show();
    }

    /**
     * Loads configurations from the config.properties file.
     */
    private void loadConfigurations() {
        Properties config = new Properties();

        try (InputStream input = getClass().getResourceAsStream("/config/config.properties")) {
            if (input == null) {
                System.out.println("Sorry, unable to find config.properties");
                return;
            }

            config.load(input);

            // Read date format
            String dateFormat = config.getProperty("date.format", "dd/MM/yyyy");
            dateFormatter = DateTimeFormatter.ofPattern(dateFormat);

            // Read start and end dates
            String startDateStr = config.getProperty("start.date");
            String endDateStr = config.getProperty("end.date");

            if (startDateStr != null && endDateStr != null) {
                startDate = LocalDate.parse(startDateStr, dateFormatter);
                endDate = LocalDate.parse(endDateStr, dateFormatter);
            } else {
                // Default to today if not specified
                startDate = LocalDate.now();
                endDate = LocalDate.now();
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Sets up the date pickers with the appropriate format and limits.
     */
    private void setUpDatePickers() {
        // Set the date converter for both date pickers
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

        // Disable dates outside the specified range
        fromDatePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);

                // Disable dates before startDate and after endDate
                setDisable(empty || date.compareTo(startDate) < 0 || date.compareTo(endDate) > 0);
            }
        });

        toDatePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);

                // Disable dates before startDate and after endDate
                setDisable(empty || date.compareTo(startDate) < 0 || date.compareTo(endDate) > 0);
            }
        });
    }

    /**
     * Sets up the table columns with appropriate cell value factories.
     */
    private void setUpTableColumns() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        matriculaColumn.setCellValueFactory(new PropertyValueFactory<>("License Plate"));
        modelColumn.setCellValueFactory(new PropertyValueFactory<>("Model"));
        capacityColumn.setCellValueFactory(new PropertyValueFactory<>("Capacity"));
        registrationDateColumn.setCellValueFactory(new PropertyValueFactory<>("RegistrationDate"));
        ITVdateColumn.setCellValueFactory(new PropertyValueFactory<>("ITVDate"));
        activeColumn.setCellValueFactory(new PropertyValueFactory<>("active"));

        // Optionally, set cell factories for formatting
        ITVdateColumn.setCellFactory(column -> new TableCell<Paquete, LocalDate>() {
            private final DateTimeFormatter formatter = dateFormatter;

            @Override
            protected void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                if (empty || date == null) {
                    setText(null);
                } else {
                    setText(formatter.format(date));
                }
            }
        });
    }

    /**
     * Fills the table with example data.
     */
    private void fillTableWithExampleData() {
        ObservableList<Paquete> data = FXCollections.observableArrayList(
                        new Paquete(1, "Alice", "Bob", 2.5, PaqueteSize.SMALL, LocalDate.now(), true),
                        new Paquete(2, "Charlie", "Dave", 5.0, PaqueteSize.MEDIUM, LocalDate.now().minusDays(1), false),
                        new Paquete(3, "Eve", "Frank", 10.0, PaqueteSize.LARGE, LocalDate.now().minusDays(2), true),
                        new Paquete(4, "Eve", "Frank", 10.0, PaqueteSize.LARGE, LocalDate.now().minusDays(2), true),
                        new Paquete(5, "Charlie", "Dave", 5.0, PaqueteSize.MEDIUM, LocalDate.now().minusDays(1), false),
                        new Paquete(6, "Eve", "Frank", 10.0, PaqueteSize.LARGE, LocalDate.now().minusDays(2), true),
                        new Paquete(7, "Eve", "Frank", 10.0, PaqueteSize.LARGE, LocalDate.now().minusDays(2), true),
                        new Paquete(8, "Eve", "Frank", 10.0, PaqueteSize.LARGE, LocalDate.now().minusDays(2), true),
                        new Paquete(9, "Eve", "Frank", 10.0, PaqueteSize.LARGE, LocalDate.now().minusDays(2), true),
                        new Paquete(10, "Grace", "Heidi", 20.0, PaqueteSize.EXTRA_LARGE, LocalDate.now().minusDays(3), false)
        );

        paqueteTableView.setItems(data);
    }

    @FXML
    private void onApplyFilter() {
        // Implement filtering logic based on selected dates and size
    }

    @FXML
    private void onSearch() {
        String query = searchTextField.getText();
        // Implement search logic
    }

    @FXML
    private void onAddVehicle() {
        // Open a new window or dialog to add a shipment
    }

    @FXML
    private void onRemoveVehicle() {
        Paquete selectedPaquete = paqueteTableView.getSelectionModel().getSelectedItem();
        if (selectedPaquete != null) {
            paqueteTableView.getItems().remove(selectedPaquete);
        }
    }

}
