package ui.paquete;

import models.PackageSize;
import models.User;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import exception.SelectException;
import factories.PackageFactory;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.DateCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import javax.ws.rs.core.GenericType;
import models.Paquete;
import service.PackageRESTClient;

public class PackageController {

    private static final Logger LOGGER = Logger.getLogger(PackageController.class.getName());

    @FXML
    private JFXDatePicker fromDatePicker;

    @FXML
    private JFXDatePicker toDatePicker;

    @FXML
    private JFXComboBox<PackageSize> sizeFilterComboBox;

    @FXML
    private JFXTextField searchTextField;

    @FXML
    private TableView<Paquete> paqueteTableView;

    @FXML
    private TableColumn<Paquete, Integer> idColumn;
    @FXML
    private TableColumn<Paquete, String> senderColumn;
    @FXML
    private TableColumn<Paquete, String> receiverColumn;
    @FXML
    private TableColumn<Paquete, Double> weightColumn;
    @FXML
    private TableColumn<Paquete, PackageSize> sizeColumn;
    @FXML
    private TableColumn<Paquete, LocalDate> dateColumn;
    @FXML
    private TableColumn<Paquete, Boolean> fragileColumn;

    @FXML
    private JFXButton addShipmentBtn;

    @FXML
    private JFXButton removeShipmentBtn;

    @FXML
    private JFXButton printReportBtn;
    @FXML
    private JFXButton searchBtn;

    @FXML
    private JFXButton applyFilterButton;

    private Stage stage;
    private DateTimeFormatter dateFormatter;
    private LocalDate startDate;
    private LocalDate endDate;

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void initStage(Parent root, User connectedUser) {
        LOGGER.info("Initialising Paquete window.");
        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.setTitle("Paquete");
        stage.setResizable(false);
        stage.centerOnScreen();
        stage.getIcons().add(new Image("/image/fleet_icon.png"));
        removeShipmentBtn.setDisable(true);
        // Load configurations
        loadConfigurations();

        // Set up date pickers
        setUpDatePickers();

        // Set up size filter combo box
        setUpSizeFilterComboBox();

        // Set up table columns
        setUpTableColumns();

        // Fill table with example data
        //fillTableWithExampleData();
        // Fill table with database data
        fillTableFromDataBase();

        searchBtn.setOnAction(this::handleSearchAction);
        applyFilterButton.setOnAction(this::handleDateFilterAction);
        
//        printReportBtn.setOnAction(this::handlePrintAction);
//        removeShipmentBtn.setOnAction(this::handleRemoveShipmentAction);
//        addShipmentBtn.setOnAction(this::handleAddShipmentAction);

        stage.show();
    }

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
                endDate = LocalDate.now().plusYears(1);
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void setUpDatePickers() {
        // Date converter
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

        // Listener for 'from' date picker
        fromDatePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                // Enable 'to' date picker
                toDatePicker.setDisable(false);

                // Set day cell factory for 'to' date picker
                toDatePicker.setDayCellFactory(picker -> new DateCell() {
                    @Override
                    public void updateItem(LocalDate date, boolean empty) {
                        super.updateItem(date, empty);
                        // Disable dates before the selected 'from' date and after endDate
                        setDisable(empty || date.compareTo(newValue) < 0 || date.compareTo(endDate) > 0);
                    }
                });
            }
        });

        // Set initial date picker constraints
        fromDatePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                // Disable dates before startDate and after endDate
                setDisable(empty || date.compareTo(startDate) < 0 || date.compareTo(endDate) > 0);
            }
        });
    }

    private void setUpSizeFilterComboBox() {
        // Create an observable list starting with "No Filter" option
        ObservableList<PackageSize> sizeOptions = FXCollections.observableArrayList();
        //sizeOptions.add();  // Represents "No Filter"
        sizeOptions.addAll(PackageSize.values());

        // Set items to the ComboBox
        sizeFilterComboBox.setItems(sizeOptions);

        // Set default value to "No Filter"
        sizeFilterComboBox.setValue(null);

        // Custom converter to handle null value and enum display
        sizeFilterComboBox.setConverter(new StringConverter<PackageSize>() {
            @Override
            public String toString(PackageSize size) {
                return size == null ? "No Filter" : size.toString();
            }

            @Override
            public PackageSize fromString(String string) {
                if (string == null || string.equals("No Filter")) {
                    return null;
                }
                try {
                    return PackageSize.valueOf(string);
                } catch (IllegalArgumentException e) {
                    return null;
                }
            }
        });

        // Add change listener to handle selection changes
        sizeFilterComboBox.setOnAction(event -> {
            PackageSize selectedSize = sizeFilterComboBox.getValue();
            handleSizeFilterChange(selectedSize);
        });
    }

    // Method to handle the filter change
    private void handleSizeFilterChange(PackageSize selectedSize) {
        List<Paquete> filteredPaqueteList = null;

        if (selectedSize == null) {
            fillTableFromDataBase();
        } else {
            try {
                filteredPaqueteList = PackageFactory.getPackageInstance().findAllPackageBySize(selectedSize);
                paqueteTableView.setItems(FXCollections.observableArrayList(filteredPaqueteList));
            } catch (SelectException ex) {
                Logger.getLogger(PackageController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void setUpTableColumns() {
//        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
//        senderColumn.setCellValueFactory(new PropertyValueFactory<>("sender"));
//        receiverColumn.setCellValueFactory(new PropertyValueFactory<>("receiver"));
//        weightColumn.setCellValueFactory(new PropertyValueFactory<>("weight"));
//        sizeColumn.setCellValueFactory(new PropertyValueFactory<>("size"));
//        dateColumn.setCellValueFactory(new PropertyValueFactory<>("creationDate"));
//        fragileColumn.setCellValueFactory(new PropertyValueFactory<>("fragile"));
    }

    // fill test data from server 
    private void fillTableFromDataBase() {
        try {
            List<Paquete> paqueteList = PackageFactory.getPackageInstance().findAllPackages();
            // Fetch data and populate the TableView
            paqueteTableView.setItems(FXCollections.observableArrayList(paqueteList));
        } catch (Exception e) {
            // Handle exceptions gracefully
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Data Fetching Failed");
            alert.setContentText("Could not fetch data from the server. Please try again later.");
        }
    }

    private void handleSearchAction(ActionEvent event) {
        System.out.println("Search clicked");

        String query = searchTextField.getText().trim().toLowerCase();
        List<Paquete> filteredPaqueteList = null;
        if (query.isEmpty()) {
            fillTableFromDataBase();
            return;
        }
        try {
            filteredPaqueteList = PackageFactory.getPackageInstance().findAllPackagesByName(query);
            System.out.println(filteredPaqueteList.toString());
        } catch (SelectException ex) {
            Logger.getLogger(PackageController.class.getName()).log(Level.SEVERE, null, ex);
        }

        ObservableList<Paquete> originalData = paqueteTableView.getItems();
        ObservableList<Paquete> filteredData = FXCollections.observableArrayList();

        for (Paquete paquete : originalData) {
            // Check if query matches sender or receiver (case-insensitive)
            if (paquete.getSender().toLowerCase().contains(query)
                            || paquete.getReceiver().toLowerCase().contains(query)) {
                filteredData.add(paquete);
            }

        }

        paqueteTableView.setItems(FXCollections.observableArrayList(filteredPaqueteList));
    }

    private void handleDateFilterAction(ActionEvent event) {
        Date fromDate = null;
        if (fromDatePicker.getValue() != null) {
            fromDate = Date.from(fromDatePicker.getValue()
                            .atStartOfDay(ZoneId.systemDefault()).toInstant());
        }
        Date toDate = null;
        if (toDatePicker.getValue() != null) {
            toDate = Date.from(toDatePicker.getValue()
                            .atStartOfDay(ZoneId.systemDefault()).toInstant());
        }
        if (fromDate == null && toDate == null) {
            fillTableFromDataBase();
            return;
        }
        if (fromDate == null && toDate != null) {
            filterPackagesBeforeDate(toDate);
            return;
        }
        if (fromDate != null && toDate == null) {
            filterPackagesAfterDate(fromDate);
            return;
        }
        if (fromDate != null && toDate != null) {
            if (fromDate.after(toDate)) {
                // showDateRangeError();
                return;
            }
            filterPackagesBetweenDates(fromDate, toDate);
        }
    }

    private void filterPackagesAfterDate(Date fromDate) {
        List<Paquete> paqueteList = null;
        try {
            paqueteList = PackageFactory.getPackageInstance().findAllPackagesByDates(fromDate, null);
        } catch (SelectException ex) {
            Logger.getLogger(PackageController.class.getName()).log(Level.SEVERE, null, ex);
        }

        paqueteTableView.setItems(FXCollections.observableArrayList(paqueteList));
    }

    private void filterPackagesBeforeDate(Date toDate) {
        List<Paquete> paqueteList = null;
        try {
            paqueteList = PackageFactory.getPackageInstance().findAllPackagesByDates(null, toDate);
        } catch (SelectException ex) {
            Logger.getLogger(PackageController.class.getName()).log(Level.SEVERE, null, ex);
        }
        paqueteTableView.setItems(FXCollections.observableArrayList(paqueteList));
    }

    private void filterPackagesBetweenDates(Date fromDate, Date toDate) {
        List<Paquete> paqueteList = null;
        try {
            paqueteList = PackageFactory.getPackageInstance().findAllPackagesByDates(fromDate, toDate);
        } catch (SelectException ex) {
            Logger.getLogger(PackageController.class.getName()).log(Level.SEVERE, null, ex);
        }

        paqueteTableView.setItems(FXCollections.observableArrayList(paqueteList));
    }

    private void onAddShipment() {
        // TODO: Implement add shipment logic
        System.out.println("Add Shipment clicked");
    }

    private void onRemoveShipment() {
        Paquete selectedPaquete = paqueteTableView.getSelectionModel().getSelectedItem();
        if (selectedPaquete != null) {
            paqueteTableView.getItems().remove(selectedPaquete);
        }
    }

    private void onPrintReport() {
        // TODO: Implement print report logic
        System.out.println("Print Report clicked");
    }
}
