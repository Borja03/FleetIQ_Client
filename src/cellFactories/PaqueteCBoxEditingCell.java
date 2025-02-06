package cellFactories;

import com.jfoenix.controls.JFXComboBox;
import javafx.collections.FXCollections;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableCell;
import javafx.scene.input.KeyCode;
import models.PackageSize;
import models.Paquete;

/**
 * Custom TableCell implementation for editing PackageSize values in a TableView using a JFXComboBox.
 * This class allows users to select a package size from a list of predefined options, and it 
 * updates the cell with the selected value when the user finishes editing. The ComboBox is shown during 
 */
public class PaqueteCBoxEditingCell extends TableCell<Paquete, PackageSize> {

    private final JFXComboBox<PackageSize> comboBox;

    /**
     * Constructor that initializes the ComboBox with the available package sizes.
     * The ComboBox options are populated from the `PackageSize` enum.
     * It also configures the behavior for committing edits and handling key presses.
     */
    public PaqueteCBoxEditingCell() {
        // Initialize ComboBox with static options from PackageSize enum
        comboBox = new JFXComboBox<>(FXCollections.observableArrayList(PackageSize.values()));

        // Configure ComboBox behavior for committing edits
        comboBox.setOnAction(e -> commitEdit(comboBox.getValue()));

        // Handle the ESC key to cancel editing
        comboBox.setOnKeyReleased(e -> {
            if (e.getCode() == KeyCode.ESCAPE) {
                cancelEdit(); // Cancel editing on ESC
            }
        });
    }

    /**
     * Starts the edit mode by displaying a ComboBox with the current value of the item.
     * The ComboBox is populated with available options and the current item is set as the selected value.
     */
    @Override
    public void startEdit() {
        super.startEdit();
        if (!isEmpty()) {
            comboBox.setValue(getItem()); // Set the current value of the item
            setText(null); // Remove the text display
            setGraphic(comboBox); // Show the ComboBox for editing
            comboBox.requestFocus(); // Automatically focus on the ComboBox
        }
    }

    /**
     * Cancels the edit operation and restores the previous value.
     * The text representation of the item is shown again.
     */
    @Override
    public void cancelEdit() {
        super.cancelEdit();
        setText(getItem() == null ? "" : getItem().toString()); // Restore the text
        setGraphic(null); // Remove the ComboBox
    }

    /**
     * Updates the display of the cell based on the current state of the item.
     * If the cell is in edit mode, a ComboBox is displayed. Otherwise, the string representation
     * of the item is shown.
     *
     * @param item  The current PackageSize item to be displayed in the cell.
     * @param empty True if the cell is empty, false otherwise.
     */
    @Override
    public void updateItem(PackageSize item, boolean empty) {
        super.updateItem(item, empty);

        if (empty || item == null) {
            setText(null);
            setGraphic(null); // Remove the ComboBox and text if the item is empty or null
        } else {
            if (isEditing()) {
                comboBox.setValue(item); // Update ComboBox value when in editing mode
                setText(null); // Remove the text display
                setGraphic(comboBox); // Display the ComboBox for editing
            } else {
                setText(item.toString()); // Display the string representation of the item when not editing
                setGraphic(null); // Remove the ComboBox when not editing
            }
        }
    }
}
