package ui.ruta;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import java.time.LocalDate;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import logicInterface.RutaManager;
import models.Ruta;

public class RutaController {

    private static final Logger logger = Logger.getLogger(RutaController.class.getName());

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
    
    private RutaManager rutaManager;
    
    private ObservableList<Ruta> rutaData;

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
        searchButton.setOnAction(event -> searchByLocalidazor());

        // Configurar acción de los botones de paquetes
        addShipmentBtn.setOnAction(event -> addShipment());
        removeShipmentBtn.setOnAction(event -> removeShipment());
        printReportBtn.setOnAction(event -> printReport());

        localizadorColumn.setCellValueFactory(new PropertyValueFactory<>("localizador"));
        origenColumn.setCellValueFactory(new PropertyValueFactory<>("origen"));
        destinoColumn.setCellValueFactory(new PropertyValueFactory<>("destino"));
        distanciaColumn.setCellValueFactory(new PropertyValueFactory<>("distancia"));
        tiempoColumn.setCellValueFactory(new PropertyValueFactory<>("tiempo"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        fechaColumn.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        numVehiculosColumn.setCellValueFactory(new PropertyValueFactory<>("numVehiculos"));
        
        cargarTabla();
        
   
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
    
     private void cargarTabla() {
    try {
        // Obtener los datos desde el gestor (RutaManager)
        rutaData = FXCollections.observableArrayList(rutaManager.selectAll());
        logger.info("Datos cargados exitosamente en la tabla.");
    } catch (Exception e) {
        logger.severe("Error al cargar los datos en la tabla: " + e.getMessage());
        e.printStackTrace();
    }
}

    private void applyDateFilter() {
        // Lógica para aplicar el filtro de fecha
        logger.info("Aplicando filtro de fecha: Desde " + fromDatePicker.getValue() + " hasta " + toDatePicker.getValue());
    }

    private void searchByLocalidazor() {
        // Lógica para buscar por nombre
        String searchText = searchTextField.getText();
        logger.info("Buscando rutas por : " + searchText);
    }

    private void addShipment() {
        // Lógica para agregar envío
        logger.info("Añadiendo nueva ruta.");
    }

    private void removeShipment() {
        // Lógica para eliminar envío
        logger.info("Eliminando ruta seleccionada.");
    }

    private void printReport() {
        // Lógica para generar reporte
        logger.info("Generando reporte.");
    }
}
