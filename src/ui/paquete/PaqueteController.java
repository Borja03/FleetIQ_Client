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
import exception.UpdateException;
import factories.PaqueteFactory;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;

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
import utils.UtilsMethods;

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
        stage.setTitle("Package");
        stage.setResizable(false);
        stage.centerOnScreen();
        stage.getIcons().add(new Image("/image/fleet_icon.png"));

        // Load configurations
        //loadConfigurations();
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

    private void setUpDatePickers() {
        fromDatePicker.setShowWeekNumbers(false);
        toDatePicker.setShowWeekNumbers(false);
        fromDatePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);

                if (date != null && (date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY)) {
                    setDisable(true); // Disable weekends
                    setStyle("-fx-background-color: #FFCCCC;"); // Style weekends
                }
            }
        });

        toDatePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);

                if (date != null && (date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY)) {
                    setDisable(true); // Disable weekends
                    setStyle("-fx-background-color: #FFCCCC;"); // Style weekends
                }
            }
        });
    }

    private void initHandlerActions() {
        searchBtn.setOnAction(this::handleSearchAction);
        filterDatesBtn.setOnAction(this::handleFilterByDatesAction);
        printReportBtn.setOnAction(this::handlePrintReportAction);
        removeShipmentBtn.setOnAction(this::handleRemoveShipmentAction);
        addShipmentBtn.setOnAction(this::handleAddShipmentAction);
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

//        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
//        senderColumn.setCellValueFactory(new PropertyValueFactory<>("sender"));
//        receiverColumn.setCellValueFactory(new PropertyValueFactory<>("receiver"));
//        weightColumn.setCellValueFactory(new PropertyValueFactory<>("weight"));
//        sizeColumn.setCellValueFactory(new PropertyValueFactory<>("size"));
//        dateColumn.setCellValueFactory(new PropertyValueFactory<>("creationDate"));
//        fragileColumn.setCellValueFactory(new PropertyValueFactory<>("fragile"));
        senderColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        senderColumn.setOnEditCommit(event -> {
            Paquete paquete = event.getRowValue();
            paquete.setSender(event.getNewValue());
            try {
                // Add any additional handling (like saving to database)
                PaqueteFactory.getPackageInstance().updatePackage(paquete);
            } catch (UpdateException ex) {
                Logger.getLogger(PaqueteController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        //
        receiverColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        receiverColumn.setOnEditCommit(event -> {
            Paquete paquete = event.getRowValue();
            paquete.setReceiver(event.getNewValue());
            try {
                // Add any additional handling (like saving to database)
                PaqueteFactory.getPackageInstance().updatePackage(paquete);
            } catch (UpdateException ex) {
                Logger.getLogger(PaqueteController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
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
        weightColumn.setOnEditCommit(event -> {
            Paquete paquete = event.getRowValue();
            paquete.setWeight(event.getNewValue());
            try {
                // Add any additional handling (like saving to database)
                PaqueteFactory.getPackageInstance().updatePackage(paquete);
            } catch (UpdateException ex) {
                Logger.getLogger(PaqueteController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        // Set the custom PaqueteCBoxEditingCell
        // Assign the PaqueteCBoxEditingCell without an explicit Callback
        sizeColumn.setCellFactory(column -> new PaqueteCBoxEditingCell());
        sizeColumn.setOnEditCommit(event -> {
            Paquete paquete = event.getRowValue();
            paquete.setSize(event.getNewValue());
            try {
                // Add any additional handling (like saving to database)
                PaqueteFactory.getPackageInstance().updatePackage(paquete);
            } catch (UpdateException ex) {
                Logger.getLogger(PaqueteController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        sizeColumn.setOnEditCommit(event -> {
            Paquete paquete = event.getRowValue();
            PackageSize newSize = event.getNewValue();
            paquete.setSize(newSize);

            try {
                // Save changes to the database or perform other updates
                PaqueteFactory.getPackageInstance().updatePackage(paquete);
            } catch (UpdateException ex) {
                Logger.getLogger(PaqueteController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        // Setup the date column
        dateColumn.setCellFactory(column -> new PaqueteDateEditingCell());
        dateColumn.setOnEditCommit(event -> {
            Paquete paquete = event.getRowValue();
            paquete.setCreationDate(event.getNewValue());
            try {
                // Add any additional handling (like saving to database)
                PaqueteFactory.getPackageInstance().updatePackage(paquete);
            } catch (UpdateException ex) {
                Logger.getLogger(PaqueteController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        // Setup the fragile column
        fragileColumn.setCellFactory(column -> new PaqueteFragileEditingCell());
        fragileColumn.setOnEditCommit(event -> {
            Paquete paquete = event.getRowValue();

            paquete.setFragile(event.getNewValue());
            try {
                PaqueteFactory.getPackageInstance().updatePackage(paquete);
            } catch (UpdateException ex) {
                Logger.getLogger(PaqueteController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

    }

    // fill test data from server 
    private void fillTableFromDataBase() {
        try {
            // Fetch data and populate the TableView
            data.clear();
            data.addAll(PaqueteFactory.getPackageInstance().findAllPackages());
            paqueteTableView.setItems(data);
        } catch (Exception e) {
            // Handle exceptions gracefully
            UtilsMethods.showAlert("Error Fetching Failed", "Could not fetch data from the server. Please try again later.");
        }
    }

    private void handleSearchAction(ActionEvent event) {
        LOGGER.info("searching");
        String query = searchTextField.getText().trim().toLowerCase();
        List<Paquete> filteredPaqueteList = null;
        if (query.isEmpty()) {
            fillTableFromDataBase();
            return;
        }
        try {
            filteredPaqueteList = PaqueteFactory.getPackageInstance().findAllPackagesByName(query);
        } catch (SelectException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
        paqueteTableView.setItems(FXCollections.observableArrayList(filteredPaqueteList));
    }

    private void handleFilterByDatesAction(ActionEvent event) {
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
            } else if (fromDate == null) {
                // Only toDate is selected
                System.out.println("Filtering packages before: " + mtoDate);
                filterPackagesBeforeDate(mtoDate);
            } else if (toDate == null) {
                // Only fromDate is selected
                System.out.println("Filtering packages after: " + mfromDate);
                filterPackagesAfterDate(mfromDate);
            } else {
                // Both dates are selected
                System.out.println("Filtering packages between: " + mfromDate + " and " + mtoDate);
                filterPackagesBetweenDates(mfromDate, mtoDate);
            }
        } catch (IllegalArgumentException e) {
            UtilsMethods.showAlert("Error de filtro", e.getMessage());
        } catch (Exception e) {
            UtilsMethods.showAlert("Error al filtrar por fechas", e.getMessage());
        }
    }

    /**
     * Filter packages after the specified date
     */
    private void filterPackagesAfterDate(String fromDate) {
        System.out.println("Executing filterPackagesAfterDate with date: " + fromDate);
        List<Paquete> paqueteList = null;
        try {
            paqueteList = PaqueteFactory.getPackageInstance().findPackagesAfterDate(fromDate);
            if (paqueteList != null && !paqueteList.isEmpty()) {
                paqueteTableView.setItems(FXCollections.observableArrayList(paqueteList));
            } else {
                paqueteTableView.setItems(FXCollections.observableArrayList());
                UtilsMethods.showAlert("Information", "No packages found after " + fromDate);
            }
        } catch (SelectException ex) {
            Logger.getLogger(PaqueteController.class.getName()).log(Level.SEVERE, null, ex);
            UtilsMethods.showAlert("Error", "Failed to filter packages: " + ex.getMessage());
        }
    }

    /**
     * Filter packages before the specified date
     */
    private void filterPackagesBeforeDate(String toDate) {
        System.out.println("Executing filterPackagesBeforeDate with date: " + toDate);
        List<Paquete> paqueteList = null;
        try {
            paqueteList = PaqueteFactory.getPackageInstance().findPackagesBeforeDate(toDate);
            if (paqueteList != null && !paqueteList.isEmpty()) {
                paqueteTableView.setItems(FXCollections.observableArrayList(paqueteList));
            } else {
                paqueteTableView.setItems(FXCollections.observableArrayList());
                UtilsMethods.showAlert("Information", "No packages found before " + toDate);
            }
        } catch (SelectException ex) {
            Logger.getLogger(PaqueteController.class.getName()).log(Level.SEVERE, null, ex);
            UtilsMethods.showAlert("Error", "Failed to filter packages: " + ex.getMessage());
        }
    }

    /**
     * Filter packages between two dates
     */
    private void filterPackagesBetweenDates(String fromDate, String toDate) {
        System.out.println("Executing filterPackagesBetweenDates with dates: " + fromDate + " to " + toDate);
        List<Paquete> paqueteList = null;
        try {
            paqueteList = PaqueteFactory.getPackageInstance().findPackagesBetweenDates(toDate, fromDate);
            if (paqueteList != null && !paqueteList.isEmpty()) {
                paqueteTableView.setItems(FXCollections.observableArrayList(paqueteList));
            } else {
                paqueteTableView.setItems(FXCollections.observableArrayList());
                UtilsMethods.showAlert("Information", "No packages found between " + fromDate + " and " + toDate);
            }
        } catch (SelectException ex) {
            Logger.getLogger(PaqueteController.class.getName()).log(Level.SEVERE, null, ex);
            UtilsMethods.showAlert("Error", "Failed to filter packages: " + ex.getMessage());
        }
    }

    private void handleAddShipmentAction(ActionEvent event) {
        try {
            // Create new package with ID from last item in data
            Paquete defaultPaquete = new Paquete("Sender", "Receiver", 1.0, PackageSize.MEDIUM, new Date(), false);
            // Add to database first
            Paquete savedPackage = PaqueteFactory.getPackageInstance().addPackage(defaultPaquete);
            if (savedPackage != null) {
                // If database insert successful, add to table data
                data.clear();
                data.addAll(PaqueteFactory.getPackageInstance().findAllPackages());
            }
        } catch (CreateException ex) {
            Logger.getLogger(PaqueteController.class.getName()).log(Level.SEVERE, null, ex);
            UtilsMethods.showAlert("Error", "Failed to create package: " + ex.getMessage());
        } catch (SelectException ex) {
            Logger.getLogger(PaqueteController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void handleRemoveShipmentAction(ActionEvent event) {
        // Get all selected items
        ObservableList<Paquete> selectedPaquetes = paqueteTableView.getSelectionModel().getSelectedItems();
        if (selectedPaquetes != null && !selectedPaquetes.isEmpty()) {
            try {
                // Delete each selected package from database
                for (Paquete paquete : selectedPaquetes) {
                    PaqueteFactory.getPackageInstance().deletePackages(paquete.getId());
                    LOGGER.info("Paquete with id " + paquete.getId() + " deleted");
                }
                // Remove all selected items from the table
                paqueteTableView.getItems().removeAll(selectedPaquetes);
            } catch (DeleteException ex) {
                Logger.getLogger(PaqueteController.class.getName()).log(Level.SEVERE, null, ex);
                UtilsMethods.showAlert("Error", "Failed to delete packages: " + ex.getMessage());
            }
        }
    }

    //
    private void handlePrintReportAction(ActionEvent event) {
        // TODO: Implement print report logic
        System.out.println("Print Report clicked");
    }
}
