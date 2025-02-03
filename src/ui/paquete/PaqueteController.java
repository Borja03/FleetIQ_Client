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
import exception.InvalidNameFormatException;
import exception.UpdateException;
import factories.PaqueteFactory;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import models.Paquete;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;
import utils.ThemeManager;
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
    // Maximum length for sender and receiver
    private static final int MAX_LENGTH = 30;

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
        setUpContextMenu();
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

        searchTextField.textProperty().addListener((observable, oldValue, newValue)
                        -> performSearch(newValue.trim().toLowerCase())
        );
        ThemeManager.getInstance().applyTheme(stage.getScene());

        stage.show();
    }

    // end of initStage 
    private void setUpDatePickers() {
        fromDatePicker.setShowWeekNumbers(false);
        toDatePicker.setShowWeekNumbers(false);

        // Load and parse date range from config
        LocalDate startDate = parseConfigDate("start.date");
        LocalDate endDate = parseConfigDate("end.date");

        // Set DateCellFactory for fromDatePicker
        configureDatePicker(fromDatePicker, startDate, endDate, null);

        // Set DateCellFactory for toDatePicker (must be after "From Date")
        configureDatePicker(toDatePicker, startDate, endDate, fromDatePicker);

        // Ensure "To Date" cannot be before "From Date"
        fromDatePicker.valueProperty().addListener((obs, oldDate, newDate) -> {
            if (newDate != null) {
                // Adjust toDatePicker to ensure it's after the new "From Date"
                if (toDatePicker.getValue() == null || toDatePicker.getValue().isBefore(newDate)) {
                    toDatePicker.setValue(newDate);
                }
                configureDatePicker(toDatePicker, startDate, endDate, fromDatePicker);
            }
        });
    }

    //private void set
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

    private void setUpContextMenu() {
        ContextMenu contextMenu = new ContextMenu();
        // First menu item
        MenuItem addItem = new MenuItem("Add New Package");
        addItem.setOnAction(this::handleAddShipmentAction);
        // Second menu item
        MenuItem deleteItem = new MenuItem("Delete Package");
        deleteItem.setOnAction(this::handleRemoveShipmentAction);
        MenuItem printItem = new MenuItem("Print Report");
        printItem.setOnAction(this::handlePrintReportAction);
        // Add menu items to the context menu
        contextMenu.getItems().addAll(addItem, deleteItem, printItem);
        paqueteTableView.setContextMenu(contextMenu);
    }

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

        //
        validateAndUpdateSenderColumn();
        //
        validateAndUpdateReceiverColumn();
        //
        validateAndUpdateWeightColumn();
        // Set the custom PaqueteCBoxEditingCell
        validateAndUpdateSizeColumn();
        // Setup the date column
        validateAndUpdateDateColumn();
        // Setup the fragile column
        validateAndUpdateFragileColumn();

    }

    private void validateAndUpdateSenderColumn() {
        senderColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        senderColumn.setOnEditCommit(event -> {
            Paquete paquete = event.getRowValue();
            String newSenderValue = event.getNewValue();
            String originalValue = paquete.getSender(); // Store original value
            LOGGER.info("Sender original value" + originalValue);
            try {
                // Validate the new sender value
                if (newSenderValue.length() > MAX_LENGTH || !newSenderValue.matches("^[a-zA-Z\\s]+$")) {
                    throw new InvalidNameFormatException("Sender name must not exceed " + MAX_LENGTH + " letters and must contain letters only.");
                }

                // First try to update in database
                Paquete updatedPaquete = paquete.clone(); // Create a temporary copy
                updatedPaquete.setSender(newSenderValue);
                LOGGER.info("Sender new value" + newSenderValue);

                PaqueteFactory.getPackageInstance().updatePackage(updatedPaquete);
                // If database update successful, then update the object
                paquete.setSender(newSenderValue);
                LOGGER.info("Sender updated successfully for package with ID: " + paquete.getId());
            } catch (InvalidNameFormatException ex) {
                // Handle validation failure
                UtilsMethods.showAlert("Error", ex.getMessage(), "ERROR");
                LOGGER.warning("Invalid Sender input: " + newSenderValue);
                paquete.setSender(originalValue);
                senderColumn.getTableView().refresh();

            } catch (UpdateException ex) {
                // Handle database update failure
                LOGGER.log(Level.SEVERE, "Failed to update package with ID: " + paquete.getId(), ex);
                UtilsMethods.showAlert("Error", "Failed to update sender." + ex.getMessage() + " Please try again.", "ERROR");
                paquete.setSender(originalValue);
                senderColumn.getTableView().refresh();
            } catch (CloneNotSupportedException ex) {
                Logger.getLogger(PaqueteController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    private void validateAndUpdateReceiverColumn() {
        receiverColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        receiverColumn.setOnEditCommit(event -> {
            Paquete paquete = event.getRowValue();
            String newReceiverValue = event.getNewValue();
            String originalValue = paquete.getReceiver();

            try {
                // Validate the new receiver value
                if (newReceiverValue.length() > MAX_LENGTH || !newReceiverValue.matches("^[a-zA-Z\\s]+$")) {
                    throw new InvalidNameFormatException("Receiver must not exceed " + MAX_LENGTH + " letters and must contain letters only.");
                }

                // Create a clone for testing
                Paquete tempPaquete = (Paquete) paquete.clone();
                tempPaquete.setReceiver(newReceiverValue);

                // Try database update with the clone
                PaqueteFactory.getPackageInstance().updatePackage(tempPaquete);

                // If successful, update the original
                paquete.setReceiver(newReceiverValue);
                LOGGER.info("Receiver updated successfully for package with ID: " + paquete.getId());

            } catch (CloneNotSupportedException | InvalidNameFormatException | UpdateException ex) {
                // Handle all exceptions
                String errorMessage = ex instanceof InvalidNameFormatException
                                ? ex.getMessage() : "Failed to update receiver. Please try again.";

                UtilsMethods.showAlert("Error", errorMessage, "ERROR");
                LOGGER.warning("Update failed: " + ex.getMessage());

                // Restore original value
                paquete.setReceiver(originalValue);
                receiverColumn.getTableView().refresh();
            }
        });
    }

    private void validateAndUpdateWeightColumn() {
        weightColumn.setCellFactory(TextFieldTableCell.forTableColumn(
                        new StringConverter<Double>() {
            @Override
            public String toString(Double object) {
                return object != null ? object.toString() : "";
            }

            @Override
            public Double fromString(String string) {
                if (!string.matches("^\\d*\\.?\\d+$")) {
                    UtilsMethods.showAlert("Error", "Weight must be a valid number", "ERROR");
                    LOGGER.warning("Invalid weight format: " + string);
                    return null;
                }
                try {
                    return Double.valueOf(string);
                } catch (NumberFormatException e) {
                    UtilsMethods.showAlert("Error", "Weight must be a valid number", "ERROR");
                    LOGGER.warning("Invalid weight: " + string);
                    return null;
                }
            }
        }));

        weightColumn.setOnEditCommit(event -> {
            Paquete paquete = event.getRowValue();
            Double newWeightValue = event.getNewValue();
            Double originalValue = paquete.getWeight();

            // If newWeightValue is null (from invalid input), just refresh the view with original value
            if (newWeightValue == null) {
                paquete.setWeight(originalValue);
                weightColumn.getTableView().refresh();
                return;
            }

            try {
                // Validate the new weight value
                if (newWeightValue < 0) {
                    throw new IllegalArgumentException("Weight must be a positive number and less than 100 kg");
                } else if (newWeightValue > 100) {
                    throw new IllegalArgumentException("Weight must not be more than 100 kg");
                }

                // Create a clone for testing
                Paquete tempPaquete = (Paquete) paquete.clone();
                tempPaquete.setWeight(newWeightValue);

                // Try database update with the clone
                PaqueteFactory.getPackageInstance().updatePackage(tempPaquete);

                // If successful, update the original
                paquete.setWeight(newWeightValue);
                LOGGER.info("Weight updated successfully for package with ID: " + paquete.getId());

            } catch (CloneNotSupportedException | IllegalArgumentException | UpdateException ex) {
                String errorMessage = ex instanceof IllegalArgumentException
                                ? ex.getMessage() : "Failed to update weight. Please try again.";

                UtilsMethods.showAlert("Error", errorMessage, "ERROR");
                LOGGER.warning("Update failed: " + ex.getMessage());

                // Restore original value
                paquete.setWeight(originalValue);
                weightColumn.getTableView().refresh();
            }
        });
    }

    private void validateAndUpdateSizeColumn() {
        sizeColumn.setCellFactory(column -> new PaqueteCBoxEditingCell());
        sizeColumn.setOnEditCommit(event -> {
            Paquete paquete = event.getRowValue();
            PackageSize originalSize = paquete.getSize();  // Store old size before the update
            PackageSize newSize = event.getNewValue();

            try {
                // Create a clone for testing
                Paquete tempPaquete = (Paquete) paquete.clone();
                tempPaquete.setSize(newSize);

                // Try database update with the clone
                PaqueteFactory.getPackageInstance().updatePackage(tempPaquete);

                // If successful, update the original
                paquete.setSize(newSize);
                LOGGER.info("Size updated successfully for package with ID: " + paquete.getId());

            } catch (CloneNotSupportedException | UpdateException ex) {
                String errorMessage = "Failed to update package size. Please try again.";
                UtilsMethods.showAlert("Error", errorMessage, "ERROR");
                LOGGER.warning("Update failed: " + ex.getMessage());

                // Restore original value
                paquete.setSize(originalSize);
                sizeColumn.getTableView().refresh();
            }
        });
    }

    private void validateAndUpdateDateColumn() {
        dateColumn.setCellFactory(column -> new PaqueteDateEditingCell());
        dateColumn.setOnEditCommit(event -> {
            Paquete paquete = event.getRowValue();
            Date originalDate = paquete.getCreationDate();  // Store old date before the update
            Date newDate = event.getNewValue();

            try {
                // Create a clone for testing
                Paquete tempPaquete = (Paquete) paquete.clone();
                tempPaquete.setCreationDate(newDate);

                // Try database update with the clone
                PaqueteFactory.getPackageInstance().updatePackage(tempPaquete);

                // If successful, update the original
                paquete.setCreationDate(newDate);
                LOGGER.info("Date updated successfully for package with ID: " + paquete.getId());

            } catch (CloneNotSupportedException | UpdateException ex) {
                String errorMessage = "Failed to update creation date. Please try again.";
                UtilsMethods.showAlert("Error", errorMessage, "ERROR");
                LOGGER.warning("Update failed: " + ex.getMessage());

                // Restore original value
                paquete.setCreationDate(originalDate);
                dateColumn.getTableView().refresh();
            }
        });
    }

    private void validateAndUpdateFragileColumn() {
        fragileColumn.setCellFactory(column -> new PaqueteFragileEditingCell());
        fragileColumn.setOnEditCommit(event -> {
            Paquete paquete = event.getRowValue();
            boolean originalFragile = paquete.isFragile();  // Store old value of fragile before update
            boolean newFragile = event.getNewValue();

            try {
                // Create a clone for testing
                Paquete tempPaquete = (Paquete) paquete.clone();
                tempPaquete.setFragile(newFragile);

                // Try database update with the clone
                PaqueteFactory.getPackageInstance().updatePackage(tempPaquete);

                // If successful, update the original
                paquete.setFragile(newFragile);
                LOGGER.info("Fragile status updated successfully for package with ID: " + paquete.getId());

            } catch (CloneNotSupportedException | UpdateException ex) {
                String errorMessage = "Failed to update fragile status. Please try again.";
                UtilsMethods.showAlert("Error", errorMessage, "ERROR");
                LOGGER.warning("Update failed: " + ex.getMessage());

                // Restore original value
                paquete.setFragile(originalFragile);
                fragileColumn.getTableView().refresh();
            }
        });
    }

    /**
     * Parses a date from the config.properties file.
     */
    private LocalDate parseConfigDate(String key) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            return LocalDate.parse(ResourceBundle.getBundle("config/config").getString(key), formatter);
        } catch (DateTimeParseException e) {
            throw new RuntimeException("Invalid date format in config.properties for key: " + key + ". Expected format: dd-MM-yyyy.", e);
        }
    }

    /**
     * Configures a DatePicker with a DayCellFactory to disable weekends and
     * out-of-range dates.
     *
     * @param datePicker The DatePicker to configure.
     * @param startDate The minimum allowed date.
     * @param endDate The maximum allowed date.
     * @param minDatePicker (Optional) A DatePicker that sets the minimum
     * selectable date.
     */
    private void configureDatePicker(DatePicker datePicker, LocalDate startDate, LocalDate endDate, DatePicker minDatePicker) {
        datePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);

                if (date == null || empty) {
                    setDisable(true);
                    return;
                }

                // Disable weekends
                if (date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY) {
                    setDisable(true);
                    setStyle("-fx-background-color: #FFCCCC;");
                }

                // Disable out-of-range dates
                if (date.isBefore(startDate) || date.isAfter(endDate)) {
                    setDisable(true);
                    setStyle("-fx-background-color: #D3D3D3;");
                }

                // Ensure "To Date" is not before "From Date"
                if (minDatePicker != null && date.isBefore(minDatePicker.getValue())) {
                    setDisable(true);
                    setStyle("-fx-background-color: #D3D3D3;");
                }
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
            UtilsMethods.showAlert("Error Fetching Failed", "Could not fetch data from the server. Please try again later.", "ERROR");
        }
    }

    private void handleFilterByDatesAction(ActionEvent event) {
        try {
            // Get date format from config
            String dateFormatPattern = ResourceBundle.getBundle("config/config").getString("date.format");
            SimpleDateFormat dateFormat = new SimpleDateFormat(dateFormatPattern);

            // Get selected dates
            LocalDate fromDate = fromDatePicker.getValue();
            LocalDate toDate = toDatePicker.getValue();

            // Convert and format dates
            String formattedFromDate = formatDate(fromDate, dateFormat);
            String formattedToDate = formatDate(toDate, dateFormat);

            // Handle filtering logic
            processDateFiltering(fromDate, toDate, formattedFromDate, formattedToDate);

            // Clear other filters
            sizeFilterComboBox.setValue(null);
            searchTextField.clear();
        } catch (IllegalArgumentException e) {
            UtilsMethods.showAlert("Error de filtro", e.getMessage(), "ERROR");
        } catch (Exception e) {
            UtilsMethods.showAlert("Error al filtrar por fechas", e.getMessage(), "ERROR");
        }
    }

    /**
     * Converts a LocalDate to a formatted string.
     */
    private String formatDate(LocalDate date, SimpleDateFormat dateFormat) {
        if (date == null) {
            return null;
        }
        Date convertedDate = Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
        return dateFormat.format(convertedDate);
    }

    /**
     * Processes filtering logic based on selected dates.
     */
    private void processDateFiltering(LocalDate fromDate, LocalDate toDate, String formattedFromDate, String formattedToDate) {
        if (fromDate == null && toDate == null) {
            UtilsMethods.showAlert("Error de filter", "Select a date", "ERROR");
        } else if (fromDate == null) {
            System.out.println("Filtering packages before: " + formattedToDate);
            filterPackagesBeforeDate(formattedToDate);
        } else if (toDate == null) {
            System.out.println("Filtering packages after: " + formattedFromDate);
            filterPackagesAfterDate(formattedFromDate);
        } else {
            System.out.println("Filtering packages between: " + formattedFromDate + " and " + formattedToDate);
            filterPackagesBetweenDates(formattedFromDate, formattedToDate);
        }
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
                LOGGER.log(Level.SEVERE, null, ex);
            }
        }

        // Clear other filters
        fromDatePicker.setValue(null);
        toDatePicker.setValue(null);
        searchTextField.clear();
    }

    private void performSearch(String query) {

        LOGGER.info("Performing live search...");

        if (query.isEmpty()) {
            LOGGER.info("Query is empty, filling table from database...");
            fillTableFromDataBase();
        } else {
            List<Paquete> filteredPaqueteList = null;
            try {
                filteredPaqueteList = PaqueteFactory.getPackageInstance().findAllPackagesByName(query);
            } catch (SelectException ex) {
                LOGGER.log(Level.SEVERE, "Error during search", ex);
            }

            // Update table view
            if (filteredPaqueteList != null && !filteredPaqueteList.isEmpty()) {
                paqueteTableView.setItems(FXCollections.observableArrayList(filteredPaqueteList));
            } else {
                // Handle empty search results
                LOGGER.info("No results found for query: " + query);
                paqueteTableView.setItems(FXCollections.observableArrayList());
            }
        }
        // Reset filters

        sizeFilterComboBox.setValue(null);
        fromDatePicker.setValue(null);
        toDatePicker.setValue(null);
    }

    //
    private void handleSearchAction(ActionEvent event) {
        // Clear other filters
        sizeFilterComboBox.setValue(null);
        fromDatePicker.setValue(null);
        toDatePicker.setValue(null);
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
                UtilsMethods.showAlert("Information", "No packages found after " + fromDate, "INFORMATION");
            }
        } catch (SelectException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            UtilsMethods.showAlert("Error", "Failed to filter packages: " + ex.getMessage(), "ERROR");
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
                UtilsMethods.showAlert("Information", "No packages found before " + toDate, "INFORMATION");
            }
        } catch (SelectException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            UtilsMethods.showAlert("Error", "Failed to filter packages: " + ex.getMessage(), "ERROR");
        }
    }

    /**
     * Filter packages between two dates
     */
    private void filterPackagesBetweenDates(String fromDate, String toDate) {
        System.out.println("Executing filterPackagesBetweenDates with dates: " + fromDate + " to " + toDate);
        try {
            List<Paquete> paqueteList = PaqueteFactory.getPackageInstance().findPackagesBetweenDates(toDate, fromDate);
            if (paqueteList != null && !paqueteList.isEmpty()) {
                paqueteTableView.setItems(FXCollections.observableArrayList(paqueteList));
            } else {
                paqueteTableView.setItems(FXCollections.observableArrayList());
                UtilsMethods.showAlert("Information", "No packages found between " + fromDate + " and " + toDate, "INFORMATION");
            }
        } catch (SelectException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            UtilsMethods.showAlert("Error", "Failed to filter packages: " + ex.getMessage(), "ERROR");
        }
    }

    /**
     * Handles the action of adding a new shipment/package to the system.
     * Creates a default package with predefined values and adds it to the
     * database. After successful addition, refreshes the table view to display
     * the new entry.
     *
     * @param event The ActionEvent triggered by the add shipment button
     */
    private void handleAddShipmentAction(ActionEvent event) {
        try {
            // Initialize with empty values instead of placeholders
            Paquete defaultPaquete = new Paquete(
                            "", // Empty sender
                            "", // Empty receiver
                            0.0,
                            PackageSize.MEDIUM,
                            new Date(),
                            false
            );

            Paquete savedPackage = PaqueteFactory.getPackageInstance().addPackage(defaultPaquete);
            if (savedPackage != null) {
                fillTableFromDataBase();
                UtilsMethods.showAlert("Success", "Package created successfully.", "INFORMATION");
            }
        } catch (Exception ex) {
            UtilsMethods.showAlert("Error", "Failed to create package: " + ex.getMessage(), "ERROR");
        }
    }

    private void handleRemoveShipmentAction(ActionEvent event) {
        // Get all selected items
        ObservableList<Paquete> selectedPaquetes = paqueteTableView.getSelectionModel().getSelectedItems();
        if (selectedPaquetes != null && !selectedPaquetes.isEmpty()) {
            // Show confirmation alert
            String confirmMessage = "Are you sure you want to delete " + selectedPaquetes.size()
                            + " selected package(s)? This action cannot be undone.";
            Alert alert = UtilsMethods.showAlert("Confirm Deletion", confirmMessage, "CONFIRMATION");

            if (alert.getResult() == ButtonType.OK) {
                try {
                    // Delete each selected package from database
                    for (Paquete paquete : selectedPaquetes) {
                        PaqueteFactory.getPackageInstance().deletePackages(paquete.getId());
                        LOGGER.info("Paquete with id " + paquete.getId() + " deleted");
                    }
                    // Remove all selected items from the table
                    paqueteTableView.getItems().removeAll(selectedPaquetes);

                    // Show success alert
                    UtilsMethods.showAlert("Success", selectedPaquetes.size() + " package(s) successfully deleted.", "INFORMATION");

                } catch (DeleteException ex) {
                    LOGGER.log(Level.SEVERE, null, ex);
                    UtilsMethods.showAlert("Error", "Failed to delete packages: " + ex.getMessage(), "ERROR");
                }
            }
        }
    }

    //
    private void handlePrintReportAction(ActionEvent event) {
        // TODO: Implement print report logic
        System.out.println("Print Report clicked");
        try {
            LOGGER.info("Beginning printing action...");
            JasperReport report = JasperCompileManager.compileReport(getClass().getResourceAsStream("/ui/report/PackageReport.jrxml"));
            //Data for the report: a collection of UserBean passed as a JRDataSource 
            //implementation 
            JRBeanCollectionDataSource dataItems = new JRBeanCollectionDataSource((Collection<Paquete>) this.paqueteTableView.getItems());
            //Map of parameter to be passed to the report
            Map<String, Object> parameters = new HashMap<>();
            //Fill report with data
            JasperPrint jasperPrint = JasperFillManager.fillReport(report, parameters, dataItems);
            //Create and show the report window. The second parameter false value makes 
            //report window not to close app.
            JasperViewer jasperViewer = new JasperViewer(jasperPrint, false);
            jasperViewer.setVisible(true);
            // jasperViewer.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        } catch (JRException ex) {
            //If there is an error show message and
            //log it.
            utils.UtilsMethods.showAlert("Error al imprimir: ", ex.getMessage(), "ERROR");
            LOGGER.log(Level.SEVERE, "UI GestionUsuariosController: Error printing report: {0}", ex.getMessage());
        }
    }
}
