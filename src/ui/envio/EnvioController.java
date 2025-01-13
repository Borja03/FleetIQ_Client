package ui.envio;

import com.jfoenix.controls.*;
import exception.SelectException;
import factories.EnvioFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import models.Envio;
import logicInterface.EnvioManager;
import utils.UtilsMethods;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.Parent;
import javafx.stage.Stage;

public class EnvioController {

    @FXML
    private JFXDatePicker fromDatePicker;

    @FXML
    private JFXDatePicker toDatePicker;

    @FXML
    private JFXComboBox<String> estadoFilterComboBox;

    @FXML
    private JFXTextField numeroPaquetesTextField;

    @FXML
    private TableView<Envio> table;

    @FXML
    private TableColumn<Envio, Integer> IdColumn;

    @FXML
    private TableColumn<Envio, String> fechaEnvioColumn;

    @FXML
    private TableColumn<Envio, String> fechaEntregaColumn;

    @FXML
    private TableColumn<Envio, String> estadoColumn;

    @FXML
    private TableColumn<Envio, Integer> numPaquetesColumn;

    @FXML
    private TableColumn<Envio, String> creadorEnvioColumn;

    @FXML
    private TableColumn<Envio, String> rutaColumn;

    @FXML
    private TableColumn<Envio, String> vehiculoColumn;

    @FXML
    private JFXButton applyDateFilterButton, applyEstadoFilterButton, applyNumPaquetesFilterButton;

    @FXML
    private JFXButton addShipmentBtn, removeShipmentBtn, printReportBtn;

    private EnvioManager envioService;
    private ObservableList<Envio> envioList;

    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    public void initialize(Parent root) throws IOException, SelectException {
        envioService = EnvioFactory.getEnvioInstance();
        envioList = FXCollections.observableArrayList();

        table.setEditable(true);
        IdColumn.setEditable(false);
        estadoColumn.setEditable(false);
        rutaColumn.setEditable(false);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        // Definir cómo se mostrarán las columnas de fecha
        fechaEnvioColumn.setCellFactory(column -> createDateCell());
        fechaEntregaColumn.setCellFactory(column -> createDateCell());

        loadInitialData();
    }

    private TableCell<Envio, String> createDateCell() {
        return new TableCell<Envio, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item);
                }
            }
        };
    }

    private void loadInitialData() {
        try {
            List<Envio> envios = envioService.findAll_XML(List.class);
            if (envios != null) {
                envioList.setAll(envios);
                table.setItems(envioList);
            }
        } catch (Exception ex) {
            Logger.getLogger(EnvioController.class.getName()).log(Level.SEVERE, "Error cargando los datos iniciales.", ex);
            new UtilsMethods().showAlert("Error", "Error al cargar los datos iniciales.");
        }

        table.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                String estadoSeleccionado = newValue.getEstado().toString();
                try {
                    List<Envio> enviosFiltrados = envioService.filterEstado_XML(List.class, estadoSeleccionado);
                    envioList.setAll(enviosFiltrados);
                } catch (Exception ex) {
                    Logger.getLogger(EnvioController.class.getName()).log(Level.SEVERE, "Error al filtrar por estado", ex);
                }
            }
        });
    }

    @FXML
    private void applyDateFilter(ActionEvent event) {
        try {
            Date from = fromDatePicker.getValue() != null ? java.sql.Date.valueOf(fromDatePicker.getValue()) : null;
            Date to = toDatePicker.getValue() != null ? java.sql.Date.valueOf(toDatePicker.getValue()) : null;

            if (from == null && to == null) {
                throw new IllegalArgumentException("Debe llenar al menos uno de los campos de fecha.");
            }

            List<Envio> filteredData = envioService.filterByDates_XML(List.class, from.toString(), to.toString());
            envioList.setAll(filteredData);
        } catch (IllegalArgumentException e) {
            new UtilsMethods().showAlert("Error de filtro", e.getMessage());
        } catch (Exception e) {
            new UtilsMethods().showAlert("Error al filtrar por fechas", e.getMessage());
        }
    }

    @FXML
    private void applyEstadoFilter(ActionEvent event) {
        try {
            String estado = estadoFilterComboBox.getValue();
            if (estado == null || estado.isEmpty()) {
                throw new IllegalArgumentException("El campo de estado está vacío.");
            }

            List<Envio> filteredData = envioService.filterEstado_XML(List.class, estado);
            envioList.setAll(filteredData);
        } catch (IllegalArgumentException e) {
            new UtilsMethods().showAlert("Error de filtro", e.getMessage());
        } catch (Exception e) {
            new UtilsMethods().showAlert("Error al filtrar por estado", e.getMessage());
        }
    }

    @FXML
    private void applyNumPaquetesFilter(ActionEvent event) {
        try {
            String numPaquetes = numeroPaquetesTextField.getText();
            if (numPaquetes == null || numPaquetes.isEmpty()) {
                throw new IllegalArgumentException("El campo está vacío, inserta un número de paquetes.");
            }

            int num = Integer.parseInt(numPaquetes);
            if (num <= 0) {
                throw new IllegalArgumentException("El número de paquetes debe ser mayor a 0.");
            }

            List<Envio> filteredData = envioService.filterNumPaquetes_XML(List.class, num);
            envioList.setAll(filteredData);
        } catch (IllegalArgumentException e) {
            new UtilsMethods().showAlert("Error de filtro", e.getMessage());
        } catch (Exception e) {
            new UtilsMethods().showAlert("Error al filtrar por número de paquetes", e.getMessage());
        }
    }

    @FXML
    private void addEnvio(ActionEvent event) {
        try {
            Envio nuevoEnvio = new Envio();
            envioList.add(nuevoEnvio);
            envioService.create_XML(nuevoEnvio);
        } catch (Exception e) {
            new UtilsMethods().showAlert("Error al añadir envío", e.getMessage());
        }
    }

    @FXML
    private void removeEnvio(ActionEvent event) {
        try {
            Envio selectedEnvio = table.getSelectionModel().getSelectedItem();
            if (selectedEnvio == null) {
                throw new IllegalArgumentException("Debes seleccionar un envío para eliminar.");
            }

            envioService.remove(selectedEnvio.getId());
            envioList.remove(selectedEnvio);
        } catch (IllegalArgumentException e) {
            new UtilsMethods().showAlert("Error", e.getMessage());
        } catch (Exception e) {
            new UtilsMethods().showAlert("Error al eliminar envío", e.getMessage());
        }
    }

    @FXML
    private void printReport(ActionEvent event) {
        new UtilsMethods().showAlert("Reporte", "Funcionalidad de impresión aún no implementada.");
    }
}
