package ui.ruta;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import java.time.LocalDate;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import models.Ruta;

public class RutaController {

    @FXML
    private JFXDatePicker fromDatePicker;

    @FXML
    private JFXDatePicker toDatePicker;

    @FXML
    private JFXButton applyFilterButton;

    @FXML
    private JFXComboBox<String> sizeFilterComboBox;

    @FXML
    private JFXComboBox<String> sizeFilterComboBox1;

    @FXML
    private JFXTextField filterValueField;

    @FXML
    private JFXTextField searchTextField;

    @FXML
    private JFXButton searchButton;

    @FXML
    private JFXButton searchButton1;

    @FXML
    private TableColumn<Ruta, Integer> localizadorColumn;

    @FXML
    private TableColumn<Ruta, String> origenColumn;

    @FXML
    private TableColumn<Ruta, String> destinoColumn;

    @FXML
    private TableColumn<Ruta, Float> distanciaColumn;

    @FXML
    private TableColumn<Ruta, Integer> tiempoColumn;

    @FXML
    private TableColumn<Ruta, String> typeColumn;

    @FXML
    private TableColumn<Ruta, LocalDate> fechaColumn;

    @FXML
    private TableColumn<Ruta, Integer> numVehiculosColumn;

    @FXML
    private JFXButton addShipmentBtn;

    @FXML
    private JFXButton removeShipmentBtn;

    @FXML
    private JFXButton printReportBtn;

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    private Stage stage;

    @FXML
    public void initialize(Parent root) {
        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.setTitle("Ruta");
        stage.setResizable(false);
        stage.centerOnScreen();
        stage.getIcons().add(new Image("/image/fleet_icon.png"));
        removeShipmentBtn.setDisable(true);
        

        // Configurar opciones para los filtros de tamaño
        sizeFilterComboBox.setItems(FXCollections.observableArrayList("Filter by Time", "Filter by Distance"));
        sizeFilterComboBox1.setItems(FXCollections.observableArrayList(">", "<", "="));

        // Configurar acciones de los filtros
        sizeFilterComboBox.setOnAction(event -> updateUnitLabel());

        // Configurar acción del botón "Apply"
        applyFilterButton.setOnAction(event -> applyDateFilter());

        // Configurar acción del botón "Search"
        searchButton.setOnAction(event -> searchByName());
        searchButton1.setOnAction(event -> searchBySize());

        // Configurar acción de los botones de paquetes
        addShipmentBtn.setOnAction(event -> addShipment());
        removeShipmentBtn.setOnAction(event -> removeShipment());
        printReportBtn.setOnAction(event -> printReport());

        stage.show();
    }

    private void updateUnitLabel() {
        String selectedFilter = sizeFilterComboBox.getValue();
        if ("Filter by Time".equals(selectedFilter)) {
            sizeFilterComboBox1.setPromptText("Horas");
        } else if ("Filter by Distance".equals(selectedFilter)) {
            sizeFilterComboBox1.setPromptText("Km");
        } else {
            sizeFilterComboBox1.setPromptText("Unit");
        }
    }

    private void applyDateFilter() {
        // Lógica para aplicar el filtro de fecha
        System.out.println("Aplicando filtro de fecha: Desde " + fromDatePicker.getValue() + " hasta " + toDatePicker.getValue());
    }

    private void searchByName() {
        // Lógica para buscar por nombre
        String searchText = searchTextField.getText();
        System.out.println("Buscando envíos por nombre: " + searchText);
    }

    private void searchBySize() {
        // Lógica para buscar por tamaño
        String filterValue = filterValueField.getText();
        String operator = sizeFilterComboBox1.getValue();
        System.out.println("Buscando por tamaño con " + operator + " " + filterValue);
    }

    private void addShipment() {
        // Lógica para agregar envío
        System.out.println("Añadiendo nuevo envío.");
    }

    private void removeShipment() {
        // Lógica para eliminar envío
        System.out.println("Eliminando envío seleccionado.");
    }

    private void printReport() {
        // Lógica para generar reporte
        System.out.println("Generando reporte.");
    }
}
