/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cellFactories;


import com.jfoenix.controls.JFXComboBox;
import javafx.collections.FXCollections;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableCell;
import javafx.scene.input.KeyCode;
import models.PackageSize;
import models.Paquete;

/**
 *
 * @author Omar
 */

public class PaqueteCBoxEditingCell extends TableCell<Paquete, PackageSize> {


    private final JFXComboBox<PackageSize> comboBox;

    public PaqueteCBoxEditingCell() {
        // Initialize ComboBox with static options
        comboBox = new JFXComboBox<>(FXCollections.observableArrayList(PackageSize.values()));

        // Configure ComboBox behavior
        comboBox.setOnAction(e -> commitEdit(comboBox.getValue()));
        comboBox.setOnKeyReleased(e -> {
            if (e.getCode() == KeyCode.ESCAPE) {
                cancelEdit(); // Cancel editing on ESC
            }
        });

      
    }

    @Override
    public void startEdit() {
        super.startEdit();
        if (!isEmpty()) {
            comboBox.setValue(getItem()); // Set the current value
            setText(null); // Remove text
            setGraphic(comboBox); // Show ComboBox
            comboBox.requestFocus(); // Focus on ComboBox
        }
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();
        setText(getItem() == null ? "" : getItem().toString()); // Restore text
        setGraphic(null); // Remove ComboBox
    }

    @Override
    public void updateItem(PackageSize item, boolean empty) {
        super.updateItem(item, empty);

        if (empty || item == null) {
            setText(null);
            setGraphic(null);
        } else {
            if (isEditing()) {
                comboBox.setValue(item); // Update ComboBox value
                setText(null);
                setGraphic(comboBox);
            } else {
                setText(item.toString()); // Display text when not editing
                setGraphic(null);
            }
        }
    }
}

