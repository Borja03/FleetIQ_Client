package ui.paquete;

import cellFactories.PaqueteDateEditingCell;
import cellFactories.PaqueteCBoxEditingCell;
import cellFactories.PaqueteFragileEditingCell;
import models.PackageSize;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import exception.CreateException;
import exception.DeleteException;
import exception.SelectException;
import factories.PaqueteFactory;
import java.text.SimpleDateFormat;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.cell.PropertyValueFactory;
import java.util.Date;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.DateCell;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import models.Paquete;

public class PaqueteController {

    private static final Logger LOGGER = Logger.getLogger(PaqueteController.class.getName());

    @FXML
    private BorderPane root;
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
    private TableColumn<Paquete, Date> dateColumn;
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
    private JFXButton filterDatesBtn;

    private Stage stage;

    private DateTimeFormatter dateFormatter;

    private LocalDate startDate;

    private LocalDate endDate;

    private ObservableList<Paquete> data;

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void initStage(Parent root) {
        LOGGER.info("Initializing Paquete window.");
        stage.setScene(new Scene(root));
        stage.setTitle("Paquete");
        stage.setResizable(false);
        stage.centerOnScreen();
        stage.getIcons().add(new Image("/image/fleet_icon.png"));
        
        // Load configurations
        loadConfigurations();

        // Set up filters
        setUpDatePickers();
        setUpSizeFilterComboBox();

        // Set up columns
        paqueteTableView.setEditable(true);
        idColumn.setEditable(false);
        dateColumn.setEditable(true);
        // allow multiple row select
        paqueteTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        removeShipmentBtn.setDisable(true);

        setUpTableColumns();

        // Fill table data
        data = FXCollections.observableArrayList();

        // load data from data base
        fillTableFromDataBase();
        // Setup button actions

        initHandlerActions();

        stage.show();
    }

    private void loadConfigurations() {
        ResourceBundle rb = ResourceBundle.getBundle("config/config");
        String dateFormat = rb.getString("date.format");
        dateFormatter = DateTimeFormatter.ofPattern(dateFormat);
        // Read start and end dates
        String startDateStr = rb.getString("start.date");
        String endDateStr = rb.getString("end.date");

        if (startDateStr != null && endDateStr != null) {
            startDate = LocalDate.parse(startDateStr, dateFormatter);
            endDate = LocalDate.parse(endDateStr, dateFormatter);
        } else {
            // Default to today if not specified
            startDate = LocalDate.now();
            endDate = LocalDate.now().plusYears(1);
        }
    }

    private void initHandlerActions() {
        searchBtn.setOnAction(this::handleSearchAction);
        filterDatesBtn.setOnAction(this::handleFilterByDatesAction);
        printReportBtn.setOnAction(this::handlePrintReportAction);
        removeShipmentBtn.setOnAction(this::handleRemoveShipmentAction);
        addShipmentBtn.setOnAction(this::handleAddShipmentAction);
    }

    private void setUpDatePickers() {
        // Set default values
        fromDatePicker.setValue(LocalDate.now());
        toDatePicker.setValue(LocalDate.now().plusDays(7));

        // Prevent selecting dates before today for both pickers
        fromDatePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.isBefore(LocalDate.now()));
            }
        });

        // Prevent selecting dates before fromDatePicker's value
        toDatePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty
                                || date.isBefore(LocalDate.now())
                                || (fromDatePicker.getValue() != null && date.isBefore(fromDatePicker.getValue())));
            }
        });

        // Add listeners to ensure valid date ranges
        fromDatePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                // If toDate is before fromDate, update toDate
                if (toDatePicker.getValue() != null && toDatePicker.getValue().isBefore(newValue)) {
                    toDatePicker.setValue(newValue);
                }
            }
        });

        toDatePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                // If fromDate is after toDate, update fromDate
                if (fromDatePicker.getValue() != null && fromDatePicker.getValue().isAfter(newValue)) {
                    fromDatePicker.setValue(newValue);
                }
            }
        });
    }

    private void setUpSizeFilterComboBox() {
        // Create list of package sizes
        ObservableList<PackageSize> sizeOptions = FXCollections.observableArrayList();
        sizeOptions.addAll(PackageSize.values());
        // Set up the combo box
        sizeFilterComboBox.setItems(sizeOptions);
        sizeFilterComboBox.setValue(null);
        // Handle selection changes
        sizeFilterComboBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                PackageSize selectedSize = sizeFilterComboBox.getValue();
                handleSizeFilterChange(selectedSize);
            }
        });
    }

    // Method to handle the filter change
    private void handleSizeFilterChange(PackageSize selectedSize) {
        List<Paquete> filteredPaqueteList = null;
        if (selectedSize == null) {
            fillTableFromDataBase();
        } else {
            try {
                filteredPaqueteList = PaqueteFactory.getPackageInstance().findAllPackageBySize(selectedSize);
                paqueteTableView.setItems(FXCollections.observableArrayList(filteredPaqueteList));

            } catch (SelectException ex) {
                Logger.getLogger(PaqueteController.class
                                .getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
//

    private void setUpTableColumns() {
        // Add click event handler to the root container
        paqueteTableView.getScene().addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
            // Check if the click target is not the TableView or its children
            if (!paqueteTableView.getBoundsInParent().contains(event.getSceneX(), event.getSceneY())) {
                paqueteTableView.getSelectionModel().clearSelection();
                removeShipmentBtn.setDisable(true);
            } else {
                removeShipmentBtn.setDisable(false);
            }
        });

        //idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        //senderColumn.setCellValueFactory(new PropertyValueFactory<>("sender"));
        //receiverColumn.setCellValueFactory(new PropertyValueFactory<>("receiver"));
        //weightColumn.setCellValueFactory(new PropertyValueFactory<>("weight"));
        //sizeColumn.setCellValueFactory(new PropertyValueFactory<>("size"));
        // dateColumn.setCellValueFactory(new PropertyValueFactory<>("creationDate"));
        //fragileColumn.setCellValueFactory(new PropertyValueFactory<>("fragile"));
        //
        senderColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        senderColumn.setOnEditCommit(event -> event.getRowValue().setSender(event.getNewValue()));
        //
        receiverColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        receiverColumn.setOnEditCommit(event -> event.getRowValue().setReceiver(event.getNewValue()));

        //
        weightColumn.setCellFactory(TextFieldTableCell.forTableColumn(
                        new StringConverter<Double>() {
            @Override
            public String toString(Double object) {
                return object != null ? object.toString() : "";
            }

            @Override
            public Double fromString(String string) {
                try {
                    return Double.valueOf(string);
                } catch (NumberFormatException e) {
                    LOGGER.warning("Invalid weight: " + string);
                    return null;
                }
            }
        }));

        weightColumn.setOnEditCommit(event -> event.getRowValue().setWeight(event.getNewValue()));

        //
        // Set the custom PaqueteCBoxEditingCell
        // Assign the PaqueteCBoxEditingCell without an explicit Callback
        sizeColumn.setCellFactory(column -> new PaqueteCBoxEditingCell());
        // Setup the date column
        dateColumn.setCellFactory(column -> new PaqueteDateEditingCell());
        // Setup the fragile column
        fragileColumn.setCellFactory(column -> new PaqueteFragileEditingCell());
        // Optional: add commit handler if you need to respond to changes
        fragileColumn.setOnEditCommit(event -> {
            Paquete paquete = event.getRowValue();
            paquete.setFragile(event.getNewValue());
            // Add any additional handling (like saving to database)
        });

    }

    // fill test data from server 
    private void fillTableFromDataBase() {

        try {
            List<Paquete> paqueteList = PaqueteFactory.getPackageInstance().findAllPackages();
            // Fetch data and populate the TableView
            data.addAll(paqueteList);
            paqueteTableView.setItems(data);
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
            filteredPaqueteList = PaqueteFactory.getPackageInstance().findAllPackagesByName(query);

        } catch (SelectException ex) {
            Logger.getLogger(PaqueteController.class
                            .getName()).log(Level.SEVERE, null, ex);
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

    private void handleFilterByDatesAction(ActionEvent event) {
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

    //
    private void filterPackagesAfterDate(Date fromDate) {
        List<Paquete> paqueteList = null;
        try {
            paqueteList = PaqueteFactory.getPackageInstance().findAllPackagesByDates(fromDate, null);

        } catch (SelectException ex) {
            Logger.getLogger(PaqueteController.class
                            .getName()).log(Level.SEVERE, null, ex);
        }

        paqueteTableView.setItems(FXCollections.observableArrayList(paqueteList));
    }

    //
    private void filterPackagesBeforeDate(Date toDate) {
        List<Paquete> paqueteList = null;
        try {
            paqueteList = PaqueteFactory.getPackageInstance().findAllPackagesByDates(null, toDate);

        } catch (SelectException ex) {
            Logger.getLogger(PaqueteController.class
                            .getName()).log(Level.SEVERE, null, ex);
        }
        paqueteTableView.setItems(FXCollections.observableArrayList(paqueteList));
    }

    //
    private void filterPackagesBetweenDates(Date fromDate, Date toDate) {
        List<Paquete> paqueteList = null;
        try {
            paqueteList = PaqueteFactory.getPackageInstance().findAllPackagesByDates(fromDate, toDate);

        } catch (SelectException ex) {
            Logger.getLogger(PaqueteController.class
                            .getName()).log(Level.SEVERE, null, ex);
        }

        paqueteTableView.setItems(FXCollections.observableArrayList(paqueteList));
    }

    //
    private void handleAddShipmentAction(ActionEvent event) {

        try {
            Paquete defaultPaquete = new Paquete(data.get(data.size() - 1).getId() + 1, "Sender", "Receiver", 1.0, PackageSize.MEDIUM, new Date(), false);
            data.add(defaultPaquete);
            Paquete p2 = PaqueteFactory.getPackageInstance().addPackage(defaultPaquete);
            System.out.println(p2.toString());
        } catch (CreateException ex) {
            Logger.getLogger(PaqueteController.class.getName()).log(Level.SEVERE, null, ex);
            // show alert
            System.out.println("Somthinr is woripgn");
        }

    }

    //
    private void handleRemoveShipmentAction(ActionEvent event) {
        Paquete selectedPaquete = paqueteTableView.getSelectionModel().getSelectedItem();

        if (selectedPaquete != null) {
            paqueteTableView.getItems().remove(selectedPaquete);
            try {
                PaqueteFactory.getPackageInstance().deletePackages(selectedPaquete.getId());
                LOGGER.info("Paquete with id  deleted");
            } catch (DeleteException ex) {
                Logger.getLogger(PaqueteController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    //
    private void handlePrintReportAction(ActionEvent event) {
        // TODO: Implement print report logic
        System.out.println("Print Report clicked");
    }
}
