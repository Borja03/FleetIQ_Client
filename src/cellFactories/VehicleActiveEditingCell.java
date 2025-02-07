/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cellFactories;

import com.jfoenix.controls.JFXToggleButton;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TableCell;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import models.Vehiculo;

/**
 *
 * @author Adrian
 */
public class VehicleActiveEditingCell extends TableCell<Vehiculo, Boolean> {

    private final JFXToggleButton toggleButton = new JFXToggleButton();

    public VehicleActiveEditingCell() {
        // Configure the ToggleButton
        toggleButton.setOnAction(event -> {
            boolean newValue = toggleButton.isSelected();
            commitEdit(newValue); // Commit the edit when the ToggleButton is toggled
        });
    }

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

    @Override
    public void cancelEdit() {
        super.cancelEdit();
        updateItem(getItem(), false);
    }

    @Override
    public void updateItem(Boolean item, boolean empty) {
        super.updateItem(item, empty);

        if (empty) {
            setText(null);
            setGraphic(null);
        } else {
            // Use a Text node for larger symbols
            Text icon = new Text();
            icon.setFont(Font.font(18)); // Set font size to 20
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
