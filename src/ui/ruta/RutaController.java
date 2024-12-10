package ui.ruta;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class RutaController {

    @FXML
    private JFXComboBox<String> sizeFilterComboBox;

    @FXML
    private JFXComboBox<String> operatorComboBox;

    @FXML
    private JFXTextField filterValueField;

    @FXML
    private Label unitLabel;

    @FXML
    public void initialize() {
        // Configurar opciones para el filtro
        sizeFilterComboBox.setItems(FXCollections.observableArrayList("Filter by Time", "Filter by Distance"));

        // Configurar opciones para los operadores
        operatorComboBox.setItems(FXCollections.observableArrayList(">", "<", "="));

        // Configurar comportamiento dinámico para el filtro
        sizeFilterComboBox.setOnAction(event -> updateUnitLabel());

        // Establecer valor inicial del label
        updateUnitLabel();
    }

    private void updateUnitLabel() {
        String selectedFilter = sizeFilterComboBox.getValue();
        if ("Filter by Time".equals(selectedFilter)) {
            unitLabel.setText("Horas");
        } else if ("Filter by Distance".equals(selectedFilter)) {
            unitLabel.setText("Km");
        } else {
            unitLabel.setText("Unit");
        }
    }
}
