package cellFactories;

import com.jfoenix.controls.JFXToggleButton;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TableCell;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import models.Paquete;

/**
 * Custom TableCell implementation for editing Boolean values in a TableView using a JFXToggleButton.
 * This class is specifically designed for handling the "fragile" status of a Paquete (package),
 * displaying a toggle button for editing the value and a graphical representation (check mark or cross) 
 */
public class PaqueteFragileEditingCell extends TableCell<Paquete, Boolean> {

    private final JFXToggleButton toggleButton = new JFXToggleButton();

    /**
     * Constructor that initializes the toggle button and sets up the event handler for when the button is toggled.
     * The toggle button commits the new value when clicked.
     */
    public PaqueteFragileEditingCell() {
        // Configure the ToggleButton
        toggleButton.setOnAction(event -> {
            boolean newValue = toggleButton.isSelected();
            commitEdit(newValue); // Commit the edit when the ToggleButton is toggled
        });
    }

    /**
     * Starts the editing mode by displaying a JFXToggleButton that reflects the current value of the Boolean item.
     * The toggle button is selected based on whether the item is true or false.
     */
    @Override
    public void startEdit() {
        super.startEdit();
        if (!isEmpty()) {
            toggleButton.setSelected(getItem() != null && getItem());
            setGraphic(toggleButton);
            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            toggleButton.requestFocus();
        }
    }

    /**
     * Cancels the editing mode and restores the original value, updating the cell to reflect the current Boolean item.
     */
    @Override
    public void cancelEdit() {
        super.cancelEdit();
        updateItem(getItem(), false);
    }

    /**
     * Updates the cell with the current Boolean item. If the item is not empty, it displays either a check mark
     * or a cross as a graphical representation of the Boolean value. A green check mark represents true, while a 
     * red cross represents false.
     *
     * @param item  The current Boolean item to be displayed.
     * @param empty True if the cell is empty, false otherwise.
     */
    @Override
    public void updateItem(Boolean item, boolean empty) {
        super.updateItem(item, empty);

        if (empty) {
            setText(null);
            setGraphic(null);
        } else {
            // Use a Text node for larger symbols
            Text icon = new Text();
            icon.setFont(Font.font(18)); // Set font size to 18
            if (item != null && item) {
                icon.setText("\u2714"); // Unicode for ✔
                icon.setFill(Color.GREEN); // Green color for true
            } else {
                icon.setText("\u2718"); // Unicode for ✘
                icon.setFill(Color.RED); // Red color for false
            }
            setGraphic(icon);
            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        }
    }
}
