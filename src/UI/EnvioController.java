package UI;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EnvioController {

    // Logger para la clase
    private static final Logger logger = Logger.getLogger(EnvioController.class.getName());

    @FXML
    private TableView<Linea> table; // Referencia a la TableView
    @FXML
    private TableColumn<Linea, String> idColumn;
    @FXML
    private TableColumn<Linea, String> fechaEnvioColumn;
    @FXML
    private TableColumn<Linea, String> fechaEntregaColumn;
    @FXML
    private TableColumn<Linea, String> estadoColumn;
    @FXML
    private TableColumn<Linea, String> rutaColumn;
    @FXML
    private TableColumn<Linea, String> creadorColumn;
    @FXML
    private TableColumn<Linea, String> vehiculoColumn;
    @FXML
    private TableColumn<Linea, Integer> numPaquetesColumn;

    // Lista observable para la TableView
    private ObservableList<Linea> lineas = FXCollections.observableArrayList();

    // Clase Linea para representar cada fila en la tabla
    public static class Linea {
        private String id;
        private String fechaEnvio;
        private String fechaEntrega;
        private String estado;
        private String ruta;
        private String creador;
        private String vehiculo;
        private int numPaquetes;

        public Linea(String id, String fechaEnvio, String fechaEntrega, String estado, String ruta,
                     String creador, String vehiculo, int numPaquetes) {
            this.id = id;
            this.fechaEnvio = fechaEnvio;
            this.fechaEntrega = fechaEntrega;
            this.estado = estado;
            this.ruta = ruta;
            this.creador = creador;
            this.vehiculo = vehiculo;
            this.numPaquetes = numPaquetes;
        }

        public String getId() { return id; }
        public String getFechaEnvio() { return fechaEnvio; }
        public String getFechaEntrega() { return fechaEntrega; }
        public String getEstado() { return estado; }
        public String getRuta() { return ruta; }
        public String getCreador() { return creador; }
        public String getVehiculo() { return vehiculo; }
        public int getNumPaquetes() { return numPaquetes; }
    }

    @FXML
    public void initialize() {
        // Configuración de las columnas de la tabla
        idColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getId()));
        fechaEnvioColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getFechaEnvio()));
        fechaEntregaColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getFechaEntrega()));
        estadoColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getEstado()));
        rutaColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getRuta()));
        creadorColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getCreador()));
        vehiculoColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getVehiculo()));
        numPaquetesColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getNumPaquetes()).asObject());

        // Asignar la lista a la TableView
        table.setItems(lineas);
    }

    @FXML
    private void showAddLineDialog() {
        // Crear el diálogo personalizado para añadir una línea
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Añadir Línea");
        dialog.setHeaderText("Introduce los detalles para la nueva línea.");

        // Botones OK y Cancelar
        ButtonType okButtonType = new ButtonType("Añadir", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

        // Campos de texto para ID, Fecha Envío, Fecha Entrega y Estado
        TextField idField = new TextField();
        idField.setPromptText("ID");

        TextField fechaEnvioField = new TextField();
        fechaEnvioField.setPromptText("Fecha Envío");

        TextField fechaEntregaField = new TextField();
        fechaEntregaField.setPromptText("Fecha Entrega");

        TextField estadoField = new TextField();
        estadoField.setPromptText("Estado");

        // ComboBox para Ruta (selección única)
        ComboBox<String> rutaComboBox = new ComboBox<>();
        rutaComboBox.getItems().addAll("Ruta A", "Ruta B", "Ruta C");
        rutaComboBox.setPromptText("Ruta");

        // ComboBox para Creador (selección única)
        ComboBox<String> creadorComboBox = new ComboBox<>();
        creadorComboBox.getItems().addAll("Creador 1", "Creador 2", "Creador 3");
        creadorComboBox.setPromptText("Creador");

        // ComboBox para Vehículo (selección única)
        ComboBox<String> vehiculoComboBox = new ComboBox<>();
        vehiculoComboBox.getItems().addAll("Vehículo 1", "Vehículo 2", "Vehículo 3");
        vehiculoComboBox.setPromptText("Vehículo");

        // ComboBox para seleccionar el número de paquetes (inicialmente 0)
        ComboBox<String> numPaquetesComboBox = new ComboBox<>();
        numPaquetesComboBox.getItems().add("0"); // Iniciar con 0
        numPaquetesComboBox.setPromptText("Num Paquetes");

        // Lista de paquetes disponibles
        ListView<String> paquetesListView = new ListView<>();
        paquetesListView.getItems().addAll("Paquete 1", "Paquete 2", "Paquete 3", "Paquete 4", "Paquete 5");
        paquetesListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        // Mostrar la lista de paquetes al hacer clic en el ComboBox
        numPaquetesComboBox.setOnAction(event -> {
            if (!paquetesListView.isVisible()) {
                // Mostrar la lista de paquetes solo cuando se hace clic
                paquetesListView.setVisible(true);
            }
        });

        // Actualizar el número de paquetes seleccionados al hacer una selección
        paquetesListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            // Al seleccionar/deseleccionar, actualizar el número de paquetes
            int numPaquetes = paquetesListView.getSelectionModel().getSelectedItems().size();
            numPaquetesComboBox.getItems().clear();
            numPaquetesComboBox.getItems().add(String.valueOf(numPaquetes)); // Actualizar con el número de paquetes seleccionados
        });

        // VBox para los campos de texto y ComboBoxes (distribuidos en un HBox)
        VBox leftVBox = new VBox(10, idField, fechaEnvioField, fechaEntregaField, estadoField);
        VBox rightVBox = new VBox(10, rutaComboBox, creadorComboBox, vehiculoComboBox, numPaquetesComboBox, paquetesListView);

        // HBox para organizar los VBox
        HBox dialogContent = new HBox(20, leftVBox, rightVBox);

        dialog.getDialogPane().setContent(dialogContent);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == okButtonType) {
                // Verificar si los campos están completos
                if (idField.getText().isEmpty() || fechaEnvioField.getText().isEmpty() || fechaEntregaField.getText().isEmpty() ||
                        estadoField.getText().isEmpty() || rutaComboBox.getValue() == null || creadorComboBox.getValue() == null ||
                        vehiculoComboBox.getValue() == null) {

                    // Log para error
                    logger.log(Level.WARNING, "Campos incompletos. No se puede añadir la línea.");
                    return null;
                }

                // Calcular el número total de paquetes seleccionados
                int numPaquetes = paquetesListView.getSelectionModel().getSelectedItems().size();

                // Crear el objeto de la nueva línea
                Linea nuevaLinea = new Linea(
                        idField.getText(),
                        fechaEnvioField.getText(),
                        fechaEntregaField.getText(),
                        estadoField.getText(),
                        rutaComboBox.getValue(),
                        creadorComboBox.getValue(),
                        vehiculoComboBox.getValue(),
                        numPaquetes
                );

                // Log para depuración
                logger.log(Level.INFO, "Línea añadida: {0}", nuevaLinea);

                // Añadir la nueva línea a la tabla
                lineas.add(nuevaLinea);

                return "Línea añadida";
            }
            return null;
        });

        // Mostrar el diálogo y mostrar el resultado si se presiona el botón OK
        dialog.showAndWait().ifPresent(result -> {
            logger.log(Level.INFO, "Resultado del diálogo: {0}", result);
        });
    }
}
