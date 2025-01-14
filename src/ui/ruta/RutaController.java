package ui.ruta;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import exception.SelectException;
import factories.RutaManagerFactory;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.GenericType;
import logicInterface.RutaManager;
import models.Ruta;
import service.RutaRESTClient;

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
    private TableColumn<Ruta, Date> fechaColumn;

    @FXML
    private TableColumn<Ruta, Integer> numeroVehiculosColumn;

    @FXML
    private JFXButton addShipmentBtn;

    @FXML
    private JFXButton removeShipmentBtn;

    @FXML
    private JFXButton printReportBtn;

    private RutaManager rutaManager;

    private ObservableList<Ruta> rutaData;

    private Stage stage;

    @FXML
    private TableView<Ruta> rutaTable;

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    private void loadRutaData() throws SelectException {
        try {
            // Llamar al servicio REST para obtener las rutas en formato XML
            List<Ruta> rutas = RutaManagerFactory.getRutaManager().findAll_XML(new GenericType<List<Ruta>>() {
            });

            // Convertir la lista de rutas en un ObservableList para el TableView
            rutaData = FXCollections.observableArrayList(rutas);

            // Enlazar las columnas con las propiedades correspondientes de la clase Ruta
            localizadorColumn.setCellValueFactory(new PropertyValueFactory<>("localizador"));
            origenColumn.setCellValueFactory(new PropertyValueFactory<>("origen"));
            destinoColumn.setCellValueFactory(new PropertyValueFactory<>("destino"));
            distanciaColumn.setCellValueFactory(new PropertyValueFactory<>("distancia"));
            tiempoColumn.setCellValueFactory(new PropertyValueFactory<>("tiempo"));
            fechaColumn.setCellValueFactory(new PropertyValueFactory<>("FechaCreacion"));
            numeroVehiculosColumn.setCellValueFactory(new PropertyValueFactory<>("numVehiculos"));

            // Establecer los elementos en el TableView para mostrar las rutas
            rutaTable.setItems(rutaData);
        } catch (WebApplicationException e) {
            logger.log(Level.SEVERE, "Error loading ruta data", e);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unexpected error", e);
        }
    }

    @FXML
    public void initialize(Parent root) {
        // Asegúrate de que rutaManager esté inicializado
        rutaManager = RutaManagerFactory.getRutaManager();

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
        searchButton1.setOnAction(event -> applyFilterButtonAction());

        // Configurar acción del botón "Search"
        searchButton.setOnAction(event -> searchByLocalizador());

        addShipmentBtn.setOnAction(event -> addShipment());
        removeShipmentBtn.setOnAction(event -> removeShipment());
        printReportBtn.setOnAction(event -> printReport());

        try {
            // Llamar al método para cargar las rutas
            loadRutaData();
        } catch (SelectException ex) {
            Logger.getLogger(RutaController.class.getName()).log(Level.SEVERE, null, ex);
        }

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

    private void applyFilterButtonAction() {
        // Get selected values from combo boxes and the text field
        String filterType = sizeFilterComboBox.getValue();
        String comparisonOperator = sizeFilterComboBox1.getValue();
        String filterValue = filterValueField.getText().trim();

        if (filterValue.isEmpty()) {
            try {
                loadRutaData();  // Llamar al método que carga todas las rutas
                logger.info("Recargando todas las rutas.");
            } catch (SelectException e) {
                logger.log(Level.SEVERE, "Error al recargar las rutas", e);
            }
            return;  // Terminar el método para evitar buscar por localizador
        }

        // Apply filter based on selected type (Time or Distance)
        try {
            if ("Filter by Time".equals(filterType)) {
                applyTimeFilter(comparisonOperator, filterValue);
            } else if ("Filter by Distance".equals(filterType)) {
                applyDistanceFilter(comparisonOperator, filterValue);
            }
        } catch (WebApplicationException e) {
            logger.log(Level.SEVERE, "Error applying filter", e);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unexpected error while applying filter", e);
        }
    }

    private void applyTimeFilter(String comparisonOperator, String filterValue) {
        switch (comparisonOperator) {
            case ">":
                rutaData.clear();
                rutaData.addAll(rutaManager.filterTiempoMayor_XML(new GenericType<List<Ruta>>() {
                }, filterValue));
                break;
            case "<":
                rutaData.clear();
                rutaData.addAll(rutaManager.filterTiempoMenor_XML(new GenericType<List<Ruta>>() {
                }, filterValue));
                break;
            case "=":
                rutaData.clear();
                rutaData.addAll(rutaManager.filterTiempoIgual_XML(new GenericType<List<Ruta>>() {
                }, filterValue));
                break;
            default:
                logger.warning("Invalid comparison operator for Time filter.");
                break;
        }
        rutaTable.setItems(rutaData);
    }

    private void applyDistanceFilter(String comparisonOperator, String filterValue) {
        switch (comparisonOperator) {
            case ">":
                rutaData.clear();
                rutaData.addAll(rutaManager.filterDistanciaMayor_XML(new GenericType<List<Ruta>>() {
                }, filterValue));
                break;
            case "<":
                rutaData.clear();
                rutaData.addAll(rutaManager.filterDistanciaMenor_XML(new GenericType<List<Ruta>>() {
                }, filterValue));
                break;
            case "=":
                rutaData.clear();
                rutaData.addAll(rutaManager.filterDistanciaIgual_XML(new GenericType<List<Ruta>>() {
                }, filterValue));
                break;
            default:
                logger.warning("Invalid comparison operator for Distance filter.");
                break;
        }
        rutaTable.setItems(rutaData);
    }

    private void searchByLocalizador() {
        String searchText = searchTextField.getText().trim(); // Obtener el texto del campo de búsqueda
        if (searchText.isEmpty()) {
            // Si el campo está vacío, recargar todos los datos
            try {
                loadRutaData();  // Llamar al método que carga todas las rutas
                logger.info("Recargando todas las rutas.");
            } catch (SelectException e) {
                logger.log(Level.SEVERE, "Error al recargar las rutas", e);
            }
            return;  // Terminar el método para evitar buscar por localizador
        }

        try {
            // Intentar convertir el texto de búsqueda en un Integer
            Integer localizador = Integer.parseInt(searchText);

            // Llamar al método del manager para obtener la ruta filtrada por localizador
            Ruta ruta = rutaManager.findByLocalizadorInteger_XML(Ruta.class, localizador);

            if (ruta != null) {
                // Actualizar el ObservableList con la ruta encontrada (solo una)
                rutaData.clear();  // Limpiar los resultados previos
                rutaData.add(ruta);  // Añadir la ruta encontrada

                rutaTable.setItems(rutaData);  // Mostrar la ruta en el TableView
                logger.info("Ruta filtrada cargada correctamente.");
            } else {
                logger.info("No se encontró ninguna ruta con el localizador proporcionado.");
            }
        } catch (NumberFormatException e) {
            // Manejar el caso cuando el texto no se puede convertir en un Integer
            logger.warning("El texto de búsqueda no es un número válido.");
        } catch (WebApplicationException e) {
            logger.log(Level.SEVERE, "Error al buscar por localizador", e);
        }
    }

    private void addShipment() {
        // Lógica para agregar una nueva ruta
    }

    private void removeShipment() {
        // Lógica para eliminar una ruta
    }

    private void printReport() {
        // Lógica para imprimir un informe
    }
}
