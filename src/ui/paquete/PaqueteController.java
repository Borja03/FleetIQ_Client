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
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import javafx.application.Platform;
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

/**
 * PaqueteController handles the functionality for the Paquete window. It
 * manages the UI components and user interactions related to package
 * management, such as adding, removing, searching, filtering, and printing
 * reports.
 *
 * The controller includes the following:
 * <ul>
 * <li>Logger for tracking events and errors.</li>
 * <li>UI components such as date pickers, combo boxes, text fields, buttons,
 * and table columns.</li>
 * <li>Observable list for storing package data to be displayed in the
 * table.</li>
 * <li>Constant for the maximum allowed length of sender and receiver
 * names.</li>
 * </ul>
 *
 * <p>
 * UI components are annotated with {@link FXML} to indicate their association
 * with elements defined in the FXML file for the window.</p>
 */
public class PaqueteController {

    /**
     * Logger for tracking events and errors
     */
    private static final Logger LOGGER = Logger.getLogger(PaqueteController.class.getName());

    /**
     * Root container of the scene
     */
    @FXML
    private BorderPane root;

    /**
     * Date picker for selecting the 'from' date
     */
    @FXML
    private JFXDatePicker fromDatePicker;

    /**
     * Date picker for selecting the 'to' date
     */
    @FXML
    private JFXDatePicker toDatePicker;

    /**
     * ComboBox for selecting the package size filter
     */
    @FXML
    private JFXComboBox<PackageSize> sizeFilterComboBox;

    /**
     * Text field for searching packages
     */
    @FXML
    private JFXTextField searchTextField;

    /**
     * TableView for displaying the package data
     */
    @FXML
    private TableView<Paquete> paqueteTableView;

    /**
     * Table column for package ID
     */
    @FXML
    private TableColumn<Paquete, Integer> idColumn;

    /**
     * Table column for sender information
     */
    @FXML
    private TableColumn<Paquete, String> senderColumn;

    /**
     * Table column for receiver information
     */
    @FXML
    private TableColumn<Paquete, String> receiverColumn;

    /**
     * Table column for weight information
     */
    @FXML
    private TableColumn<Paquete, Double> weightColumn;

    /**
     * Table column for package size information
     */
    @FXML
    private TableColumn<Paquete, PackageSize> sizeColumn;

    /**
     * Table column for date information
     */
    @FXML
    private TableColumn<Paquete, Date> dateColumn;

    /**
     * Table column for fragile status information
     */
    @FXML
    private TableColumn<Paquete, Boolean> fragileColumn;

    /**
     * Button to add a new shipment
     */
    @FXML
    private JFXButton addShipmentBtn;

    /**
     * Button to remove an existing shipment
     */
    @FXML
    private JFXButton removeShipmentBtn;

    /**
     * Button to print a report
     */
    @FXML
    private JFXButton printReportBtn;

    /**
     * Button to initiate search
     */
    @FXML
    private JFXButton searchBtn;

    /**
     * Button to filter by dates
     */
    @FXML
    private JFXButton filterDatesBtn;

    /**
     * Stage for the Paquete window
     */
    private Stage stage;

    /**
     * Observable list for storing package data
     */
    private ObservableList<Paquete> data;

    /**
     * Maximum length for sender and receiver names
     */
    private static final int MAX_LENGTH = 30;

    /**
     * Gets the current stage (window) associated with this controller.
     *
     * @return The current stage
     */
    public Stage getStage() {
        return stage;
    }

    /**
     * Sets the stage (window) for this controller.
     *
     * @param stage The stage to set
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Initializes the Paquete window and sets up the necessary components. This
     * method configures the main stage for the Paquete window, including
     * setting the scene, title, icon, and various UI components. It also
     * initializes filters, table columns, and button actions. The method loads
     * data into the table from the database and applies the selected theme to
     * the scene.
     *
     * @param root The root node of the scene graph.
     */
    public void initStage(Parent root) {
        LOGGER.info("Initializing Paquete window.");
        // Set the scene for the stage
        stage.setScene(new Scene(root));
        stage.setTitle("Package"); // Set the title of the window
        stage.setResizable(false); // Disable window resizing
        stage.centerOnScreen(); // Center the window on the screen
        stage.getIcons().add(new Image("/image/fleet_icon.png")); // Set the window icon

        // Load configurations (if any)
        //loadConfigurations();
        // Set up filters (date pickers and size filter combo box)
        setUpDatePickers();
        setUpSizeFilterComboBox();

        // Set up the context menu for the table
        setUpContextMenu();

        // Set up the table columns
        paqueteTableView.setEditable(true); // Allow editing of the table
        idColumn.setEditable(false); // Prevent editing of the ID column
        dateColumn.setEditable(true); // Allow editing of the Date column

        // Enable multiple row selection
        paqueteTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        // Initially disable the "Remove Shipment" button
        removeShipmentBtn.setDisable(true);

        // Set up the table columns (sender, receiver, etc.)
        setUpTableColumns();

        // Initialize the data list and load data from the database
        data = FXCollections.observableArrayList();
        fillTableFromDataBase(); // Populate the table with data from the database

        // Initialize button actions for various features
        initHandlerActions();

        // Add listener for search text field to perform search as text changes
        searchTextField.textProperty().addListener((observable, oldValue, newValue)
                        -> performSearch(newValue.trim().toLowerCase())
        );

        // Apply the selected theme to the scene
        ThemeManager.getInstance().applyTheme(stage.getScene());

        // Show the stage (window)
        stage.show();
        LOGGER.info("Showing Paquete window.");

    }

    /**
     * Sets up and configures the "From Date" and "To Date" pickers with
     * appropriate date range restrictions. The method initializes the date
     * pickers, ensures the "To Date" cannot be before the "From Date", and vice
     * versa. It also applies a DateCellFactory to both date pickers to limit
     * selectable dates based on the configured date range.
     *
     * The "From Date" and "To Date" pickers are updated dynamically based on
     * user input to ensure the date range is valid.
     */
    private void setUpDatePickers() {
        fromDatePicker.setShowWeekNumbers(false);
        toDatePicker.setShowWeekNumbers(false);

        // Load and parse date range from config
        LocalDate startDate = parseConfigDate("start.date");
        LocalDate endDate = parseConfigDate("end.date");

        // Set DateCellFactory for fromDatePicker
        configureDatePicker(fromDatePicker, startDate, endDate, null, toDatePicker);

        // Set DateCellFactory for toDatePicker (must be after "From Date")
        configureDatePicker(toDatePicker, startDate, endDate, fromDatePicker, null);

        // Ensure "To Date" cannot be before "From Date"
        fromDatePicker.valueProperty().addListener((obs, oldDate, newDate) -> {
            if (newDate != null) {
                // Adjust toDatePicker to ensure it's after the new "From Date"
                if (toDatePicker.getValue() == null || toDatePicker.getValue().isBefore(newDate)) {
                    toDatePicker.setValue(newDate);
                }
                // Reconfigure toDatePicker based on the updated "From Date"
                configureDatePicker(toDatePicker, startDate, endDate, fromDatePicker, null);
            }
        });

        // Ensure "From Date" cannot be after "To Date"
        toDatePicker.valueProperty().addListener((obs, oldDate, newDate) -> {
            if (newDate != null && fromDatePicker.getValue() != null) {
                if (newDate.isBefore(fromDatePicker.getValue())) {
                    fromDatePicker.setValue(newDate);
                }
            }
        });
    }

    /**
     * Initializes the action handlers for the buttons. This method sets up the
     * event handlers for the following buttons:
     * <ul>
     * <li>searchBtn - triggers the handleSearchAction method</li>
     * <li>filterDatesBtn - triggers the handleFilterByDatesAction method</li>
     * <li>printReportBtn - triggers the handlePrintReportAction method</li>
     * <li>removeShipmentBtn - triggers the handleRemoveShipmentAction
     * method</li>
     * <li>addShipmentBtn - triggers the handleAddShipmentAction method</li>
     * </ul>
     */
    private void initHandlerActions() {
        searchBtn.setOnAction(this::handleSearchAction);
        filterDatesBtn.setOnAction(this::handleFilterByDatesAction);
        printReportBtn.setOnAction(this::handlePrintReportAction);
        removeShipmentBtn.setOnAction(this::handleRemoveShipmentAction);
        addShipmentBtn.setOnAction(this::handleAddShipmentAction);
    }

    /**
     * Sets up the size filter combo box for selecting package sizes. This
     * method initializes the combo box with a list of available package sizes
     * defined in the `PackageSize` enum. The combo box is populated with these
     * options, and its default value is set to null. It also includes an event
     * handler to respond to selection changes. When the user selects a size,
     * the `handleSizeFilterChange` method is called to handle the filter change
     * based on the selected package size.
     */
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

    /**
     * Sets up the context menu for the Paquete table. This method creates a
     * context menu with three options: "Add New Package", "Delete Package", and
     * "Print Report". Each menu item is linked to a corresponding action
     * handler method. The context menu is then assigned to the Paquete table
     * view, allowing users to interact with the table using right-click
     * functionality.
     *
     * The following menu items are added to the context menu:
     * <ul>
     * <li>"Add New Package" - triggers the handleAddShipmentAction method</li>
     * <li>"Delete Package" - triggers the handleRemoveShipmentActionmethod</li>
     * <li>"Print Report" - triggers the handlePrintReportAction method</li>
     * </ul>
     */
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

    /**
     * Sets up the columns for the Paquete table and adds event handlers. This
     * method configures the table columns, validates, and updates the sender,
     * receiver, weight, size, date, and fragile columns. It also adds a click
     * event handler to the root container of the scene to manage the selection
     * of the table rows. When a click occurs outside the table, the selection
     * is cleared, and the remove button is disabled. Otherwise, the remove
     * button is enabled.
     *
     * The following columns are set up:
     * <ul>
     * <li>Sender Column</li>
     * <li>Receiver Column</li>
     * <li>Weight Column</li>
     * <li>Size Column</li>
     * <li>Date Column</li>
     * <li>Fragile Column</li>
     * </ul>
     */
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

    /**
     * Validates and updates the sender column of a package table. This method
     * sets up an event listener on the sender column to trigger validation and
     * updating when the user edits the sender field in the table. The sender
     * value is validated to ensure it doesn't exceed the maximum allowed length
     * and contains only letters and spaces. If the validation is successful, an
     * attempt is made to update the sender value in the database. In case of an
     * error, the original value is restored, and an alert is shown to the user.
     *
     * 
     * 
     */
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
                UtilsMethods.showAlert("Validation Error", ex.getMessage(), "ERROR");
                LOGGER.warning("Invalid Sender input: " + newSenderValue);
                paquete.setSender(originalValue);
                senderColumn.getTableView().refresh();
            } catch (UpdateException ex) {
                // Handle database update failure
                LOGGER.log(Level.SEVERE, "Failed to update package with ID: " + paquete.getId(), ex);
                UtilsMethods.showAlert("Server Error", "Failed to update sender.Please try again later.", "ERROR");
                paquete.setSender(originalValue);
                senderColumn.getTableView().refresh();
            } catch (Exception ex) {
                LOGGER.log(Level.SEVERE, null, ex);
                UtilsMethods.showAlert("Error", "An unexpected error occurred. Please try again.", "ERROR");
            }
        });
    }

    /**
     * Validates and updates the receiver column of a package table. This method
     * sets up an event listener on the receiver column to trigger validation
     * and updating when the user edits the receiver field in the table. The
     * receiver value is validated to ensure it doesn't exceed the maximum
     * allowed length and contains only letters and spaces. If the validation is
     * successful, an attempt is made to update the receiver value in the
     * database. In case of an error, the original value is restored, and an
     * alert is shown to the user.
     *
     * 
     * 
     */
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

            } catch (InvalidNameFormatException ex) {
                // Handle validation failure
                UtilsMethods.showAlert("Validation Error", ex.getMessage(), "ERROR");
                LOGGER.warning("Invalid receiver input: " + newReceiverValue);
                paquete.setReceiver(originalValue);
                receiverColumn.getTableView().refresh();
            } catch (UpdateException ex) {
                // Handle all exceptions
                UtilsMethods.showAlert("Server Error", "Failed to update receiver. Please try again later.", "ERROR");
                LOGGER.warning("Update failed: " + ex.getMessage());
                // Restore original value
                paquete.setReceiver(originalValue);
                receiverColumn.getTableView().refresh();
            } catch (Exception ex) {
                LOGGER.log(Level.SEVERE, null, ex.getMessage());
                UtilsMethods.showAlert("Error", "An unexpected error occurred. Please try again.", "ERROR");

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
                    UtilsMethods.showAlert("Validation Error", "Weight must be a valid number", "ERROR");
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
            } catch (UpdateException ex) {
                UtilsMethods.showAlert("Server Error", "Failed to update weight. Please try again later.", "ERROR");
                LOGGER.warning("Update failed: " + ex.getMessage());
                // Restore original value
                paquete.setWeight(originalValue);
                weightColumn.getTableView().refresh();
            } catch (IllegalArgumentException ex) {
                // Handle validation failure
                UtilsMethods.showAlert("Validation Error", ex.getMessage(), "ERROR");
                LOGGER.warning("Invalid weight input: " + newWeightValue);
                paquete.setWeight(originalValue);
                weightColumn.getTableView().refresh();
            } catch (Exception ex) {
                LOGGER.log(Level.SEVERE, null, ex.getMessage());
                UtilsMethods.showAlert("Error", "An unexpected error occurred. Please try again.", "ERROR");

            }
        });
    }

    /**
     * Validates and updates the size of a package in the table view.
     *
     * This method sets the cell factory for the size column to enable editing,
     * and it handles the edit commit event to validate and update the size of a
     * specific package. It first stores the original size, then attempts to
     * update the size in the database using a cloned instance of the package to
     * avoid altering the original object until the update is confirmed. If the
     * update is successful, the original package is updated; otherwise, it
     * restores the original size value and displays an error message.
     *
     *
     * in the database
     */
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

            } catch (UpdateException ex) {
                String errorMessage = "Failed to update package size. Please try again later.";
                UtilsMethods.showAlert("Server Error", errorMessage, "ERROR");
                LOGGER.warning("Update failed: " + ex.getMessage());
                // Restore original value
                paquete.setSize(originalSize);
                sizeColumn.getTableView().refresh();
            } catch (Exception ex) {
                LOGGER.log(Level.SEVERE, null, ex.getMessage());
                UtilsMethods.showAlert("Error", "An unexpected error occurred. Please try again.", "ERROR");

            }
        });
    }

    /**
     * Validates and updates the creation date of a package in the table view.
     *
     * This method sets the cell factory for the date column to enable editing,
     * and it handles the edit commit event to validate and update the date for
     * a specific package. It first stores the original date, then attempts to
     * update the date in the database using a cloned instance of the package to
     * avoid altering the original object until the update is confirmed. If the
     * update is successful, the original package is updated; otherwise, it
     * restores the original date value and displays an error message.
     *
     *
     * in the database
     */
    private void validateAndUpdateDateColumn() {
        dateColumn.setCellFactory(column -> new PaqueteDateEditingCell());
        dateColumn.setOnEditCommit(event -> {
            Paquete paquete = event.getRowValue();
            Date originalDate = paquete.getCreationDate();  // Store old date before the update
            Date newDate = event.getNewValue();

            try {
                // Validate date range (15 days)
                long daysDifference = Math.abs((newDate.getTime() - originalDate.getTime())
                                / (24 * 60 * 60 * 1000));

                if (daysDifference > 15) {
                    throw new IllegalArgumentException("Date modification cannot exceed 15 days from the original date.");
                }

                // Create a clone for testing
                Paquete tempPaquete = (Paquete) paquete.clone();
                tempPaquete.setCreationDate(newDate);
                // Try database update with the clone
                PaqueteFactory.getPackageInstance().updatePackage(tempPaquete);
                // If successful, update the original
                paquete.setCreationDate(newDate);
                LOGGER.info("Date updated successfully for package with ID: " + paquete.getId());

            } catch (IllegalArgumentException ex) {
                String errorMessage = "Invalid date: " + ex.getMessage();
                UtilsMethods.showAlert("Validation Error", errorMessage, "ERROR");
                LOGGER.warning("Date validation failed: " + ex.getMessage());
                // Restore original value
                paquete.setCreationDate(originalDate);
                dateColumn.getTableView().refresh();
            } catch (UpdateException ex) {
                UtilsMethods.showAlert("Server Error", "Failed to update creation date. Please try again.", "ERROR");
                LOGGER.warning("Update failed: " + ex.getMessage());
                // Restore original value
                paquete.setCreationDate(originalDate);
                dateColumn.getTableView().refresh();

            } catch (Exception ex) {
                LOGGER.log(Level.SEVERE, null, ex.getMessage());
                UtilsMethods.showAlert("Error", "An unexpected error occurred. Please try again.", "ERROR");
                // Restore original value
                paquete.setCreationDate(originalDate);
                dateColumn.getTableView().refresh();
            }
        });
    }


    /**
     * Validates and updates the "Fragile" column in the TableView.
     *
     * This method sets up an editable cell factory for the `fragileColumn`,
     * allowing users to modify the fragile status of a package. When an edit is
     * committed, it attempts to update the fragile status in the database using
     * a cloned instance of the package. - If the update is successful, the
     * change is applied. - If the update fails, an error alert is shown, the
     * original fragile status is restored, and the TableView is refreshed.
     *
     */
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

            } catch ( UpdateException ex) {
                String errorMessage = "Failed to update fragile status. Please try again.";
                UtilsMethods.showAlert("Server Error", errorMessage, "ERROR");
                LOGGER.warning("Update failed: " + ex.getMessage());
                // Restore original value
                paquete.setFragile(originalFragile);
                fragileColumn.getTableView().refresh();
            } catch (Exception ex) {
                UtilsMethods.showAlert("Error", "An unexpected error occurred. Please try again.", "ERROR");
                LOGGER.log(Level.SEVERE, null, ex.getMessage());
            }
        });
    }

    /**
     * Parses a date from the application configuration file.
     *
     * This method retrieves a date value from the `config.properties` file
     * using the specified key and converts it into a `LocalDate` object using
     * the `dd-MM-yyyy` format. If the date format is invalid, a
     * `RuntimeException` is thrown with a descriptive error message.
     *
     * @param key The key used to retrieve the date value from the configuration
     * file.
     * @return The parsed `LocalDate` object.
     * @throws RuntimeException If the date format is invalid or cannot be
     * parsed.
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
    private void configureDatePicker(DatePicker datePicker, LocalDate startDate, LocalDate endDate, DatePicker minDatePicker, DatePicker maxDatePicker) {
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

                // Ensure "To Date" is not before "From Date" (if "From Date" is selected)
                if (minDatePicker != null && minDatePicker.getValue() != null && date.isBefore(minDatePicker.getValue())) {
                    setDisable(true);
                    setStyle("-fx-background-color: #D3D3D3;");
                }

                // Ensure "From Date" is not after "To Date" (if "To Date" is selected)
                if (maxDatePicker != null && maxDatePicker.getValue() != null && date.isAfter(maxDatePicker.getValue())) {
                    setDisable(true);
                    setStyle("-fx-background-color: #D3D3D3;");
                }
            }
        });
    }

    /**
     * Fetches all packages from the database and populates the TableView.
     *
     * This method retrieves the complete list of packages from the database and
     * updates the TableView. If an error occurs while fetching the data, an
     * error alert is displayed to the user.
     */
    private void fillTableFromDataBase() {
        try {
            // Fetch data and populate the TableView
            data.clear();
            data.addAll(PaqueteFactory.getPackageInstance().findAllPackages());
            paqueteTableView.setItems(data);
        } catch (SelectException e) {
            // Handle exceptions gracefully
            UtilsMethods.showAlert("Server Error", "Could not fetch data from the server. Please try again later.", "ERROR");
        } catch (Exception e) {
            // Handle exceptions gracefully
            UtilsMethods.showAlert("Error", "An unexpected error occurred. Please try again.", "ERROR");
        }
    }

    /**
     * Handles the action of filtering packages by the selected date range.
     * Retrieves the date format from the configuration, converts the selected
     * dates to strings using the configured date format, and applies the
     * filtering logic. It also clears other active filters, such as the size
     * filter and search text. If any exception occurs during the filtering
     * process, an alert is shown to the user.
     *
     * @param event The ActionEvent triggered by the user interacting with the
     * date filter controls.
     */
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
            UtilsMethods.showAlert("Validation Error", e.getMessage(), "ERROR");
        } catch (Exception e) {
            UtilsMethods.showAlert("ERROR", "Error:Failed to filter packages by dates", "ERROR");

        }
    }

    /**
     * Formats a given LocalDate into a string representation using the
     * specified SimpleDateFormat. The LocalDate is first converted to a Date
     * object before formatting. If the provided LocalDate is null, this method
     * returns null.
     *
     * @param date The LocalDate to be formatted. If null, the method returns
     * null.
     * @param dateFormat The SimpleDateFormat instance used to format the Date
     * object.
     * @return A string representation of the formatted date, or null if the
     * provided date is null.
     */
    private String formatDate(LocalDate date, SimpleDateFormat dateFormat) {
        if (date == null) {
            return null;
        }
        Date convertedDate = Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
        return dateFormat.format(convertedDate);
    }

    /**
     * Processes date filtering based on the provided date range.
     *
     * This method determines the appropriate filtering action based on the
     * presence of `fromDate` and `toDate`. - If both dates are null, an error
     * alert is shown. - If only `toDate` is provided, it filters packages
     * before that date. - If only `fromDate` is provided, it filters packages
     * after that date. - If both dates are provided, it filters packages
     * between the given date range.
     *
     * @param fromDate The starting date for filtering (nullable).
     * @param toDate The ending date for filtering (nullable).
     * @param formattedFromDate The formatted string representation of
     * `fromDate`.
     * @param formattedToDate The formatted string representation of `toDate`.
     */
    private void processDateFiltering(LocalDate fromDate, LocalDate toDate, String formattedFromDate, String formattedToDate) {
        if (fromDate == null && toDate == null) {
            UtilsMethods.showAlert("Error de filter", "Select a date", "ERROR");
        } else if (fromDate == null) {

            LOGGER.info("Filtering packages before: " + formattedToDate);
            filterPackagesBeforeDate(formattedToDate);
        } else if (toDate == null) {
            LOGGER.info("Filtering packages after: " + formattedFromDate);
            filterPackagesAfterDate(formattedFromDate);
        } else {
            LOGGER.info("Filtering packages between: " + formattedFromDate + " and " + formattedToDate);
            filterPackagesBetweenDates(formattedFromDate, formattedToDate);
        }
    }

    /**
     * Handles the change in the selected package size filter. When a new size
     * is selected, this method filters the list of packages based on the
     * selected size and updates the table view. If no size is selected, it
     * retrieves and displays the full list of packages from the database. It
     * also clears other active filters, such as the date range and search text.
     *
     * @param selectedSize The selected package size to filter the list of
     * packages by. If null, the full list of packages is displayed without
     * filtering by size.
     */
    private void handleSizeFilterChange(PackageSize selectedSize) {

        List<Paquete> filteredPaqueteList = null;
        if (selectedSize == null) {
            fillTableFromDataBase();
        } else {
            try {
                filteredPaqueteList = PaqueteFactory.getPackageInstance().findAllPackageBySize(selectedSize);
                paqueteTableView.setItems(FXCollections.observableArrayList(filteredPaqueteList));
            } catch (SelectException ex) {
                UtilsMethods.showAlert("Server Error", "Failed to filter packages by size:Please try again later ", "ERROR");
                LOGGER.log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                LOGGER.log(Level.SEVERE, null, ex);
                UtilsMethods.showAlert("Error", "An unexpected error occurred. Please try again.", "ERROR");
            }
        }

        // Clear other filters
        fromDatePicker.setValue(null);
        toDatePicker.setValue(null);
        searchTextField.clear();
    }

    /**
     * Performs a live search for packages based on the given query.
     *
     * This method retrieves packages from the database that match the provided
     * search query. If the query is empty, the full package list is reloaded.
     * The search results update the TableView, and any previously applied
     * filters (size, date range) are reset to avoid conflicts.
     *
     * @param query The search keyword used to filter packages by name.
     */
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
                UtilsMethods.showAlert("Server Error", "Error during search:Please try again later ", "ERROR");

            } catch (Exception ex) {
                LOGGER.log(Level.SEVERE, null, ex);
                UtilsMethods.showAlert("Error", "An unexpected error occurred. Please try again.", "ERROR");
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

    /**
     * Handles the search action for filtering packages by name.
     *
     * This method retrieves packages from the database that match the search
     * query entered in the searchTextField. If the query is empty, the full
     * package list is reloaded. Additionally, any previously applied filters
     * (size, date range) are cleared to ensure the search results are displayed
     * without conflicts.
     *
     * @param event The event triggered by clicking the "Search" button or
     * pressing Enter.
     */
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
            // Clear other filters
            sizeFilterComboBox.setValue(null);
            fromDatePicker.setValue(null);
            toDatePicker.setValue(null);
        } catch (SelectException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            UtilsMethods.showAlert("Server Error", "Failed to search package:Please try again later ", "ERROR");
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            UtilsMethods.showAlert("Error", "An unexpected error occurred. Please try again.", "ERROR");
        }
        paqueteTableView.setItems(FXCollections.observableArrayList(filteredPaqueteList));
    }

    /**
     * Filters and displays packages that were created after the specified date.
     *
     * This method retrieves packages from the database that have a date later
     * than the given `fromDate`. If matching packages are found, they are
     * displayed in the TableView; otherwise, an information alert is shown. If
     * an error occurs, an appropriate alert is displayed, and the error is
     * logged.
     *
     * @param fromDate The starting date; only packages after this date will be
     * shown.
     */
    private void filterPackagesAfterDate(String fromDate) {
        LOGGER.info("Executing filterPackagesAfterDate with date: " + fromDate);
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
            UtilsMethods.showAlert("Server Error", "Failed to search package:Please try again later ", "ERROR");
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            UtilsMethods.showAlert("Error", "An unexpected error occurred. Please try again.", "ERROR");
        }

    }

    /**
     * Filters and displays packages that were created before the specified
     * date.
     *
     * This method retrieves packages from the database that have a date earlier
     * than the given `toDate`. If matching packages are found, they are
     * displayed in the TableView; otherwise, an information alert is shown. If
     * an error occurs, an appropriate alert is displayed, and the error is
     * logged.
     *
     * @param toDate The cutoff date; only packages before this date will be
     * shown.
     */
    private void filterPackagesBeforeDate(String toDate) {
        LOGGER.info("Executing filterPackagesBeforeDate with date: " + toDate);
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
            UtilsMethods.showAlert("Server Error", "Failed to filter packages: ", "ERROR");
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            UtilsMethods.showAlert("Error", "An unexpected error occurred. Please try again.", "ERROR");
        }

    }

    /**
     * Filters and displays packages within a specified date range.
     *
     * This method retrieves packages between the given `fromDate` and `toDate`
     * from the database. If matching packages are found, they are displayed in
     * the TableView; otherwise, an information alert is shown. In case of an
     * error, an appropriate alert is displayed, and the error is logged.
     *
     * @param fromDate The start date of the filter range (inclusive).
     * @param toDate The end date of the filter range (inclusive).
     */
    private void filterPackagesBetweenDates(String fromDate, String toDate) {
        LOGGER.info("Executing filterPackagesBetweenDates with dates: " + fromDate + " to " + toDate);
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
            UtilsMethods.showAlert("Server Error", "Failed to filter packages by dates: ", "ERROR");
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            UtilsMethods.showAlert("Error", "An unexpected error occurred. Please try again.", "ERROR");
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
        } catch (CreateException ex) {
            UtilsMethods.showAlert("Server Error", "Failed to create package ", "ERROR");
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            UtilsMethods.showAlert("Error", "An unexpected error occurred. Please try again.", "ERROR");
        }

    }

    /**
     * Handles the action of removing selected shipments from the TableView and
     * database.
     *
     * This method retrieves the selected packages from the TableView, prompts
     * the user for confirmation, and attempts to delete each package from the
     * database. If all deletions are successful, the packages are removed from
     * the UI. In case of a failure, an error message is displayed, and the
     * TableView is refreshed to maintain consistency.
     *
     * @param event The event triggered by clicking the "Remove Shipment"
     * button.
     */
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
                    // Try to delete each selected package
                    for (Paquete paquete : selectedPaquetes) {
                        try {
                            PaqueteFactory.getPackageInstance().deletePackages(paquete.getId());
                            LOGGER.info("Package with id " + paquete.getId() + " deleted");
                        } catch (DeleteException ex) {
                            // If any deletion fails, throw exception immediately
                            throw ex;
                        }
                    }

                    // Only remove from TableView if ALL deletions were successful
                    paqueteTableView.getItems().removeAll(selectedPaquetes);
                    UtilsMethods.showAlert("Success", selectedPaquetes.size() + " package(s) successfully deleted.", "INFORMATION");
                    paqueteTableView.getSelectionModel().clearSelection();

                } catch (DeleteException ex) {
                    LOGGER.log(Level.SEVERE, "Error deleting packages", ex);
                    String errorMsg = "Failed to delete packages: Server connection error. Please try again later.";
                    UtilsMethods.showAlert("Server Error", errorMsg, "ERROR");

                    // Refresh table to ensure consistency
                    fillTableFromDataBase();
                } catch (Exception ex) {
                    LOGGER.log(Level.SEVERE, null, ex);
                    UtilsMethods.showAlert("Error", "An unexpected error occurred. Please try again.", "ERROR");
                }

            }
        }
    }

    /**
     * Handles the action of printing a report of the displayed shipments.
     *
     * This method compiles and fills a JasperReport using the package data from
     * the TableView. It then displays the report using JasperViewer. If an
     * error occurs, it is logged and an alert is shown.
     *
     * @param event The event triggered by clicking the "Print Report" button.
     */
    private void handlePrintReportAction(ActionEvent event) {
        LOGGER.info("Print Report clicked");
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
        } catch (Exception ex) {
            UtilsMethods.showAlert("Error", "An unexpected error occurred. Please try again.", "ERROR");
            LOGGER.log(Level.SEVERE, null, ex);
        }

    }
}
