package ui.paquete;

import models.Paquete;
import models.PaqueteSize;
import models.User;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
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
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.util.StringConverter;

public class PaqueteController {

    private static final Logger LOGGER = Logger.getLogger(PaqueteController.class.getName());

    @FXML
    private JFXDatePicker fromDatePicker;

    @FXML
    private JFXDatePicker toDatePicker;

    @FXML
    private JFXComboBox<PaqueteSize> sizeFilterComboBox;

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
    private TableColumn<Paquete, PaqueteSize> sizeColumn;
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
    private JFXButton searchButton;

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
        fillTableWithExampleData();

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

        // Disable 'to' date picker until 'from' date is selected
        toDatePicker.setDisable(true);

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
            } else {
                // Disable 'to' date picker if 'from' is not selected
                toDatePicker.setDisable(true);
                toDatePicker.setValue(null);
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
        // Create a list with all enum values plus a null option
        ObservableList<PaqueteSize> sizeOptions = FXCollections.observableArrayList();
        sizeOptions.add(null); // Add null as the first option (represents "No filter")
        sizeOptions.addAll(PaqueteSize.values());
        
        sizeFilterComboBox.setItems(sizeOptions);
        
        // Custom converter to display "No filter" for null
        sizeFilterComboBox.setConverter(new StringConverter<PaqueteSize>() {
            @Override
            public String toString(PaqueteSize size) {
                return size == null ? "No Filter" : size.name();
            }

            @Override
            public PaqueteSize fromString(String string) {
                return string.equals("No Filter") ? null : PaqueteSize.valueOf(string);
            }
        });
    }

    private void setUpTableColumns() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        senderColumn.setCellValueFactory(new PropertyValueFactory<>("sender"));
        receiverColumn.setCellValueFactory(new PropertyValueFactory<>("receiver"));
        weightColumn.setCellValueFactory(new PropertyValueFactory<>("weight"));
        sizeColumn.setCellValueFactory(new PropertyValueFactory<>("size"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("creationDate"));
        fragileColumn.setCellValueFactory(new PropertyValueFactory<>("fragile"));

        // Date column formatting
        dateColumn.setCellFactory(column -> new TableCell<Paquete, LocalDate>() {
            @Override
            protected void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setText(empty || date == null ? null : dateFormatter.format(date));
            }
        });
    }

    private void fillTableWithExampleData() {
        ObservableList<Paquete> data = FXCollections.observableArrayList(
            new Paquete(1, "Alice", "Bob", 2.5, PaqueteSize.SMALL, LocalDate.now(), true),
            new Paquete(2, "Charlie", "Dave", 5.0, PaqueteSize.MEDIUM, LocalDate.now().minusDays(1), false),
            new Paquete(3, "Eve", "Frank", 10.0, PaqueteSize.LARGE, LocalDate.now().minusDays(2), true),
            new Paquete(4, "Grace", "Heidi", 7.5, PaqueteSize.MEDIUM, LocalDate.now().minusDays(3), false),
            new Paquete(5, "Ivan", "Jack", 15.0, PaqueteSize.EXTRA_LARGE, LocalDate.now().minusDays(4), true)
        );

        paqueteTableView.setItems(data);
    }

    @FXML
    private void onSearch() {
        String query = searchTextField.getText().trim().toLowerCase();
        
        if (query.isEmpty()) {
            // If no search query, reset to original data
            fillTableWithExampleData();
            return;
        }
        
        ObservableList<Paquete> originalData = paqueteTableView.getItems();
        ObservableList<Paquete> filteredData = FXCollections.observableArrayList();
        
        for (Paquete paquete : originalData) {
            // Check if query matches sender or receiver (case-insensitive)
            if (paquete.getSender().toLowerCase().contains(query) || 
                paquete.getReceiver().toLowerCase().contains(query)) {
                filteredData.add(paquete);
            }
            
            // If query is a valid number, search by weight
            try {
                double weightQuery = Double.parseDouble(query);
                if (paquete.getWeight() == weightQuery) {
                    filteredData.add(paquete);
                }
            } catch (NumberFormatException e) {
                // Not a valid number, ignore
            }
        }
        
        paqueteTableView.setItems(filteredData);
    }

    @FXML
    private void onApplyFilter() {
        ObservableList<Paquete> originalData = paqueteTableView.getItems();
        ObservableList<Paquete> filteredData = FXCollections.observableArrayList();
        
        LocalDate fromDate = fromDatePicker.getValue();
        LocalDate toDate = toDatePicker.getValue();
        PaqueteSize selectedSize = sizeFilterComboBox.getValue();
        
        for (Paquete paquete : originalData) {
            boolean dateMatch = true;
            boolean sizeMatch = true;
            
            // Date filtering
            if (fromDate != null && toDate != null) {
                dateMatch = !paquete.getCreationDate().isBefore(fromDate) && 
                            !paquete.getCreationDate().isAfter(toDate);
            }
            
            // Size filtering
            if (selectedSize != null) {
                sizeMatch = paquete.getSize() == selectedSize;
            }
            
            // Add to filtered list if both conditions are met
            if (dateMatch && sizeMatch) {
                filteredData.add(paquete);
            }
        }
        
        paqueteTableView.setItems(filteredData);
    }

    @FXML
    private void onAddShipment() {
        // TODO: Implement add shipment logic
        System.out.println("Add Shipment clicked");
    }

    @FXML
    private void onRemoveShipment() {
        Paquete selectedPaquete = paqueteTableView.getSelectionModel().getSelectedItem();
        if (selectedPaquete != null) {
            paqueteTableView.getItems().remove(selectedPaquete);
        }
    }

    @FXML
    private void onPrintReport() {
        // TODO: Implement print report logic
        System.out.println("Print Report clicked");
    }
}