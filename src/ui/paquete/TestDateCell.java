/*
 * This class represents a custom table cell for editing dates in a TableView.
 * It uses a DatePicker control for date input and updates the displayed value 
 * accordingly. The cell type is specialized for handling LocalDate values.
 */
package ui.paquete;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;
import models.Paquete;
import models.User; // A model representing users (not directly used in this class)

/**
 * A custom TableCell implementation that allows editing LocalDate values in a
 * JavaFX TableView. When the cell is in edit mode, it displays a DatePicker
 * control. Otherwise, it displays the date as a formatted string.
 */
public class TestDateCell extends TableCell<Paquete,LocalDate> {

    // A DatePicker control for selecting dates
    private DatePicker datePicker;
    // A formatter for converting LocalDate values to strings
    private DateTimeFormatter dateFormatter;
    private String dateFormat;

    // Constructor: Initializes the date formatter with the desired format
    public TestDateCell() {
        dateFormat = ResourceBundle.getBundle("config/config")
                        .getString("date.format");
        this.dateFormatter = DateTimeFormatter.ofPattern(dateFormat);
    }

    /**
     * Starts the editing process for the cell. When the cell is edited, it
     * replaces the displayed text with a DatePicker.
     */
    @Override
    public void startEdit() {
        if (!isEmpty()) { // Only allow editing if the cell is not empty
            super.startEdit(); // Call the base class method to enable editing
            createDatePicker(); // Create and configure the DatePicker control
            setText(null); // Remove any text from the cell
            setGraphic(datePicker); // Display the DatePicker in the cell
        }
    }

    /**
     * Cancels the editing process. Reverts the cell to its original state by
     * showing the formatted date.
     */
    @Override
    public void cancelEdit() {
        super.cancelEdit(); // Call the base class method to cancel editing
        setText(getDate()); // Set the text to the formatted date
        setGraphic(null); // Remove the DatePicker from the cell
    }

    /**
     * Updates the content of the cell based on the given item (date) and its
     * state.
     *
     * @param item The LocalDate value for the cell
     * @param empty Whether the cell is empty
     */
    @Override
    public void updateItem(LocalDate item, boolean empty) {
        super.updateItem(item, empty); // Call the base method to update the item

        if (empty) { // If the cell is empty, clear the content
            setText(null);
            setGraphic(null);
        } else {
            if (isEditing()) { // If the cell is in edit mode
                if (datePicker != null) {
                    datePicker.setValue(getItem()); // Update the DatePicker's value
                }
                setText(null); // Remove text when editing
                setGraphic(datePicker); // Show the DatePicker
            } else { // When not editing
                setText(getDate()); // Show the formatted date
                setGraphic(null); // No graphic content is needed
            }
        }
    }

    /**
     * Creates and configures the DatePicker control used for editing.
     */
    private void createDatePicker() {
        datePicker = new DatePicker(getItem()); // Initialize with the current value
        // Adjust the width of the DatePicker to fit the cell
        datePicker.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);

        // Add a listener to commit the edit when the DatePicker loses focus
        datePicker.focusedProperty().addListener(
                        (ObservableValue<? extends Boolean> arg0, Boolean oldFocus, Boolean newFocus) -> {
                            if (!newFocus) { // If the DatePicker loses focus
                                commitEdit(datePicker.getValue()); // Save the selected value
                            }
                        });

        // Commit the edit when a date is selected in the DatePicker
        datePicker.setOnAction(event -> {
            commitEdit(datePicker.getValue()); // Save the selected value
        });
    }

    /**
     * Converts the cell's value (LocalDate) into a formatted string.
     *
     * @return The formatted date string, or an empty string if the value is
     * null
     */
    private String getDate() {
        return getItem() == null ? "" : getItem().format(dateFormatter);
    }
}