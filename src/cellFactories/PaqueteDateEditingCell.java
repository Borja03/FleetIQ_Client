package cellFactories;

import com.jfoenix.controls.JFXDatePicker;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.scene.control.DateCell;
import javafx.scene.control.TableCell;
import javafx.scene.input.KeyCode;
import models.Paquete;

/**
 * Custom TableCell implementation for editing Date values in a TableView using JFXDatePicker.
 * This class disables weekends (Saturdays and Sundays) in the date picker and allows the user to 
 */
public class PaqueteDateEditingCell extends TableCell<Paquete, Date> {

    private JFXDatePicker datePicker = new JFXDatePicker();
    private String mDateFormat;

    /**
     * Constructor that initializes the date format from a resource bundle
     * and configures the JFXDatePicker for date selection.
     */
    public PaqueteDateEditingCell() {
        // Load date format from resource bundle
        mDateFormat = ResourceBundle.getBundle("config/config").getString("date.format");

        // Initialize and configure the DatePicker
        configureDatePicker();
    }

    /**
     * Configures the JFXDatePicker with event handling and styles.
     * The weekends (Saturdays and Sundays) are disabled in the date picker.
     * The selected date is committed when the user selects a date.
     * The ESC key cancels the editing.
     */
    private void configureDatePicker() {

        datePicker = new JFXDatePicker();
        datePicker.setShowWeekNumbers(false);
        
        // Disable weekends (Saturdays and Sundays)
        datePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);

                // Check if the day is Saturday or Sunday
                if (date != null && (date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY)) {
                    setDisable(true); // Disable the date
                    setStyle("-fx-background-color: #f08080;"); 
                }
            }
        });
        
        // Handle date selection
        datePicker.setOnAction(e -> {
            if (datePicker.getValue() != null) {
                Date date = Date.from(datePicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
                commitEdit(date); // Commit the edited date
            }
        });

        // Handle ESC key to cancel editing
        datePicker.setOnKeyReleased(e -> {
            if (e.getCode() == KeyCode.ESCAPE) {
                cancelEdit();
            }
        });

        // Optional: Set default styles
        datePicker.setPrefWidth(150); // Set preferred width
        datePicker.setStyle("-fx-font-size: 14px;"); // Adjust font size
    }

    /**
     * Starts the editing of the DateCell by displaying a JFXDatePicker
     * initialized with the current value of the Date item.
     */
    @Override
    public void startEdit() {
        super.startEdit();
        if (!isEmpty()) {
            // Convert the current Date value to LocalDate and set it in the DatePicker
            Date value = getItem();
            if (value != null) {
                LocalDate localDate = value.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                datePicker.setValue(localDate);
            } else {
                datePicker.setValue(null); // Clear the DatePicker if no value exists
            }

            setText(null);
            setGraphic(datePicker); // Display the DatePicker for editing
            datePicker.requestFocus(); // Automatically focus the DatePicker
        }
    }

    /**
     * Cancels the edit operation and restores the cell's displayed value.
     */
    @Override
    public void cancelEdit() {
        super.cancelEdit();
        setText(getDateAsString());
        setGraphic(null); // Hide the DatePicker when editing is canceled
    }

    /**
     * Updates the cell with the current Date item. If the cell is in editing mode,
     * it displays the JFXDatePicker with the current Date value. Otherwise, it displays
     * the formatted date as a string.
     *
     * @param item  The current Date item to be displayed.
     * @param empty True if the cell is empty, false otherwise.
     */
    @Override
    public void updateItem(Date item, boolean empty) {
        super.updateItem(item, empty);

        if (empty || item == null) {
            setText(null);
            setGraphic(null);
        } else {
            if (isEditing()) {
                // Update the DatePicker with the current Date value during editing
                if (item != null) {
                    LocalDate localDate = item.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    datePicker.setValue(localDate);
                }
                setText(null);
                setGraphic(datePicker);
            } else {
                // Display the formatted date when not editing
                setText(getDateAsString());
                setGraphic(null);
            }
        }
    }

    /**
     * Converts the current Date item into a formatted string based on the specified date format.
     *
     * @return The formatted date string or an empty string if no value exists.
     */
    private String getDateAsString() {
        if (getItem() == null) {
            return "";
        }
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(mDateFormat);
            return dateFormat.format(getItem());
        } catch (Exception e) {
            return ""; // Fallback to an empty string if formatting fails
        }
    }

    /**
     * Dynamically updates the date format used for displaying values.
     *
     * @param newFormat The new date format string.
     */
    public void setDateFormat(String newFormat) {
        this.mDateFormat = newFormat;
    }
}
