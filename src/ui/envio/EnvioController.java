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

    @FXML
    public void initialize(Parent root) throws IOException, SelectException {
        envioService = EnvioFactory.getEnvioInstance();
        envioList = FXCollections.observableArrayList();

        table.setEditable(true);
        IdColumn.setEditable(false);
        estadoColumn.setEditable(false);
        rutaColumn.setEditable(false);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        fechaEnvioColumn.setCellFactory(column -> new TableCell<Envio, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item);
                }
            }
        });

        fechaEntregaColumn.setCellFactory(column -> new TableCell<Envio, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item);
                }
            }
        });

        loadInitialData();
    }

    private void loadInitialData() throws IOException, SelectException {
        List<Envio> envios = envioService.selectAll();
        envioList.setAll(envios);
        table.setItems(envioList);

        estadoFilterComboBox.setItems(FXCollections.observableArrayList(envioService.filterEstado(String estado)));
    }

    @FXML
    private void applyDateFilter(ActionEvent event) {
        try {
            Date from = fromDatePicker.getValue() != null ? java.sql.Date.valueOf(fromDatePicker.getValue()) : null;
            Date to = toDatePicker.getValue() != null ? java.sql.Date.valueOf(toDatePicker.getValue()) : null;

            if (from == null && to == null) {
                throw new IllegalArgumentException("Como mínimo debes llenar uno de los dos campos de fecha para poder filtrar.");
            }

            List<Envio> filteredData = envioService.filterByDates(from, to);
            envioList.setAll(filteredData);
        } catch (Exception e) {
            new UtilsMethods().showAlert("Error al filtrar por fechas", e.getMessage());
        }
    }

    @FXML
    private void applyEstadoFilter(ActionEvent event) {
        try {
            String estado = estadoFilterComboBox.getValue();
            if (estado == null || estado.isEmpty()) {
                throw new IllegalArgumentException("El campo no tiene ningún valor, selecciona uno de los valores.");
            }

            List<Envio> filteredData = envioService.filterEstado(estado);
            envioList.setAll(filteredData);
        } catch (Exception e) {
            new UtilsMethods().showAlert("Error al filtrar por estado", e.getMessage());
        }
    }

    @FXML
    private void applyNumPaquetesFilter(ActionEvent event) {
        try {
            String numPaquetes = numeroPaquetesTextField.getText();
            if (numPaquetes == null || numPaquetes.isEmpty()) {
                throw new IllegalArgumentException("El campo está vacío, inserta un número de paquetes para filtrar.");
            }
            int num = Integer.parseInt(numPaquetes);
            if (num <= 0) {
                throw new IllegalArgumentException("El número de paquetes debe ser mayor a 0 para poder hacer el filtrado.");
            }

            List<Envio> filteredData = envioService.filterNumPaquetes(num);
            envioList.setAll(filteredData);
        } catch (Exception e) {
            new UtilsMethods().showAlert("Error al filtrar por número de paquetes", e.getMessage());
        }
    }

    @FXML
    private void addShipment(ActionEvent event) {
        try {
            Envio nuevoEnvio = new Envio();
            envioList.add(nuevoEnvio);
            envioService.addEnvio();
        } catch (Exception e) {
            new UtilsMethods().showAlert("Error al añadir envío", e.getMessage());
        }
    }

    @FXML
    private void removeShipment(ActionEvent event) {
        try {
            Envio selectedEnvio = table.getSelectionModel().getSelectedItem();
            if (selectedEnvio == null) {
                throw new IllegalArgumentException("Debes seleccionar una línea antes de poder borrar.");
            }

            envioService.deleteEnvio(selectedEnvio.getId());
            envioList.remove(selectedEnvio);
        } catch (Exception e) {
            new UtilsMethods().showAlert("Error al eliminar envío", e.getMessage());
        }
    }

    @FXML
    private void printReport(ActionEvent event) {
        new UtilsMethods().showAlert("Reporte", "Funcionalidad de impresión aún no implementada.");
    }

    public void setStage(Stage loginStage) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
