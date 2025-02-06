package ui.ruta;

import application.RutaMain;
import javafx.scene.Node;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import models.Ruta;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.testfx.framework.junit.ApplicationTest;
import com.jfoenix.controls.*;

import static org.junit.Assert.*;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.base.NodeMatchers.isVisible;
import static org.testfx.matcher.control.TextInputControlMatchers.hasText;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RutaControllerServerError extends ApplicationTest {

    private TableView<Ruta> rutaTable;
    private JFXButton addShipmentBtn;
    private JFXButton removeShipmentBtn;
    private JFXButton printReportBtn;
    private JFXButton searchButton;
    private JFXButton searchButton1;
    private JFXButton applyFilterButton;
    private JFXTextField searchTextField;
    private JFXTextField filterValueField;
    private JFXDatePicker fromDatePicker;
    private JFXDatePicker toDatePicker;
    private JFXComboBox<String> filterTypeComboBox;
    private JFXComboBox<String> operatorComboBox;

    @Override
    public void start(Stage stage) throws Exception {
        new RutaMain().start(stage);

        // Initialize all UI components
        rutaTable = lookup("#rutaTable").queryTableView();
        addShipmentBtn = lookup("#addShipmentBtn").query();
        removeShipmentBtn = lookup("#removeShipmentBtn").query();
        printReportBtn = lookup("#printReportBtn").query();
        searchButton = lookup("#searchButton").query();
        searchButton1 = lookup("#searchButton1").query();
        applyFilterButton = lookup("#applyFilterButton").query();
        searchTextField = lookup("#searchTextField").query();
        filterValueField = lookup("#filterValueField").query();
        fromDatePicker = lookup("#fromDatePicker").query();
        toDatePicker = lookup("#toDatePicker").query();
        filterTypeComboBox = lookup("#filterTypeComboBox").query();
        operatorComboBox = lookup("#operatorComboBox").query();

        // Verify non-null after initialization
        assertNotNull("rutaTable not found", rutaTable);
        assertNotNull("addShipmentBtn not found", addShipmentBtn);
        assertNotNull("removeShipmentBtn not found", removeShipmentBtn);
    }

    @Test
    public void testA_waitForServerStop() throws InterruptedException {
        verifyThat(rutaTable, isVisible()); // Asegura que la UI está cargada
        Thread.sleep(10000); // Espera 10 segundos para detener el servidor
    }

    @Test
    public void testA_addRutaServerError() {
        int initialCount = rutaTable.getItems().size();
        clickOn(addShipmentBtn);

        // Wait for server response
        //sleep(10000); // Only if necessary for real server timeout
        // Verify error dialog
        verifyThat("Error", Node::isVisible);
        verifyThat("No se pudo añadir la nueva ruta.", Node::isVisible);
        clickOn("Aceptar");

        assertEquals("Route count should remain unchanged", initialCount, rutaTable.getItems().size());
    }

    @Test
    public void testB_editOrigenServerError() {
        // Get first route's origin
        Ruta original = rutaTable.getItems().get(0);
        String expectedOrigen = original.getOrigen();

        // Attempt to edit
        Node row = lookup(".table-row-cell").nth(0).query();
        doubleClickOn(row);
        Node origenCell = lookup(".table-cell").nth(1).query(); // Origin column
        doubleClickOn(origenCell);
        write("NewOrigen");
        press(KeyCode.ENTER);

        // Verify error
        verifyThat("Error del servidor", Node::isVisible);
        verifyThat("No se pudo actualizar el origen.", Node::isVisible);
        clickOn("Aceptar");

        assertEquals("Origin should remain unchanged", expectedOrigen, rutaTable.getItems().get(0).getOrigen());
    }

    @Test
    public void testC_removeRutaServerError() {
        int initialCount = rutaTable.getItems().size();

        // Select and attempt delete
        Node row = lookup(".table-row-cell").nth(0).query();
        clickOn(row);
        clickOn(removeShipmentBtn);

        // Confirm deletion
        verifyThat("¿Está seguro de que desea eliminar las rutas seleccionadas?", Node::isVisible);
        clickOn("Aceptar");

        // Verify error
        verifyThat("Error", Node::isVisible);
        verifyThat("Error inesperado al eliminar rutas.", Node::isVisible);
        clickOn("Aceptar");

        assertEquals("Route count should remain unchanged", initialCount, rutaTable.getItems().size());
    }
}
