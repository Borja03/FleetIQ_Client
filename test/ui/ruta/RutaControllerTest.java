/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.ruta;

import application.RutaMain;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import java.time.LocalDate;
import java.util.concurrent.TimeUnit;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import models.Ruta;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import javafx.stage.Stage;
import static org.junit.Assert.*;
import static org.testfx.api.FxAssert.verifyThat;
import org.testfx.framework.junit.ApplicationTest;
import static org.testfx.matcher.base.NodeMatchers.isDisabled;
import static org.testfx.matcher.base.NodeMatchers.isEnabled;
import static org.testfx.matcher.base.NodeMatchers.isVisible;
import static org.testfx.matcher.control.TextInputControlMatchers.hasText;
import org.testfx.util.WaitForAsyncUtils;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RutaControllerTest extends ApplicationTest {

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

        rutaTable = lookup("#rutaTable").queryTableView();
        addShipmentBtn = (JFXButton) lookup("#addShipmentBtn").queryButton();
        removeShipmentBtn = (JFXButton) lookup("#removeShipmentBtn").queryButton();
        printReportBtn = (JFXButton) lookup("#printReportBtn").queryButton();
        searchButton = (JFXButton) lookup("#searchButton").queryButton();
        searchButton1 = (JFXButton) lookup("#searchButton1").queryButton();
        applyFilterButton = (JFXButton) lookup("#applyFilterButton").queryButton();
        searchTextField = lookup("#searchTextField").query();
        filterValueField = lookup("#filterValueField").query();
        fromDatePicker = lookup("#fromDatePicker").query();
        toDatePicker = lookup("#toDatePicker").query();

        assertNotNull("rutaTable no encontrada", rutaTable);
        assertNotNull("addShipmentBtn no encontrado", addShipmentBtn);
        assertNotNull("removeShipmentBtn no encontrado", removeShipmentBtn);
        assertNotNull("searchButton no encontrado", searchButton);
        assertNotNull("applyFilterButton no encontrado", applyFilterButton);
    }

    @Test
    public void testA_initialState() {
        verifyThat("#rutaTable", isVisible());
        verifyThat("#addShipmentBtn", isEnabled());
        verifyThat("#removeShipmentBtn", isDisabled());
        verifyThat("#printReportBtn", isEnabled());
        verifyThat("#searchButton", isEnabled());
        verifyThat("#applyFilterButton", isEnabled());
        verifyThat("#searchTextField", hasText(""));
        verifyThat("#filterValueField", hasText(""));
        assertNull(fromDatePicker.getValue());
        assertNull(toDatePicker.getValue());
    }

    @Test
    public void testB_addRuta() {
        int initialCount = rutaTable.getItems().size();
        clickOn(addShipmentBtn);
        assertEquals(initialCount + 1, rutaTable.getItems().size());
    }

    @Test
    public void testJ_removeRuta() {
        int initialCount = rutaTable.getItems().size();
        Node row = lookup(".table-row-cell").nth(0).query();
        clickOn(row);
        verifyThat("#removeShipmentBtn", isEnabled());
        clickOn(removeShipmentBtn);
        verifyThat(".dialog-pane", Node::isVisible);
        clickOn("Aceptar");
        assertEquals(initialCount - 1, rutaTable.getItems().size());
    }

//    @Test
//    public void testF_filterByDateRange() {
//        interact(() -> {
//            fromDatePicker.setValue(LocalDate.of(2024, 1, 1));
//            toDatePicker.setValue(LocalDate.of(2024, 12, 31));
//        });
//        clickOn(applyFilterButton);
//        verifyThat(rutaTable, (TableView<Ruta> t)
//                -> t.getItems().stream().allMatch(r
//                        -> !r.getFechaCreacion().before(java.sql.Date.valueOf("2024-01-01"))
//                && !r.getFechaCreacion().after(java.sql.Date.valueOf("2024-12-31"))
//                )
//        );
//    }

    @Test
    public void testH_tableSelection() {
        Node row = lookup(".table-row-cell").nth(0).query();
        clickOn(row);
        verifyThat("#removeShipmentBtn", isEnabled());
    }

    @Test
    public void testI_validation() {
        Node distanciaCell = lookup(".table-cell").nth(3).query();
        doubleClickOn(distanciaCell);
        write("-10");
        press(KeyCode.ENTER);
        verifyThat("La distancia no puede ser negativa.", isVisible());
        clickOn("Aceptar");
    }

    @Test
    public void testC_editOrigen() {
        // Wait for data to load
        WaitForAsyncUtils.waitForFxEvents();

        // Verify table has data
        assertFalse("Table empty - no routes found", rutaTable.getItems().isEmpty());
        TableRow<Ruta> row = lookup(".table-row-cell").nth(0).query(); // Cambiado a 0 para la primera fila
        TableColumn<Ruta, ?> origenColumn = rutaTable.getColumns().get(1);
        interact(() -> {
            doubleClickOn(row.getChildrenUnmodifiable().get(1));
        });

        // Enter new value
        write("NewOrigen");
        press(KeyCode.ENTER);
        WaitForAsyncUtils.waitForFxEvents();

        // Verify update
        Ruta updated = rutaTable.getItems().get(0); // Cambiado a 0 para la primera fila
        assertEquals("NewOrigen", updated.getOrigen());
    }

    // SERVER ERROR TESTS
//    @Test
//    public void testK_addRutaServerError() {
//        int initialCount = rutaTable.getItems().size();
//
//     
//        WaitForAsyncUtils.sleep(5, TimeUnit.SECONDS);
//
//        clickOn(addShipmentBtn);
//
//        verifyThat("Error", Node::isVisible);
//        verifyThat("No se pudo añadir la nueva ruta.", Node::isVisible);
//        clickOn("Aceptar");
//
//        assertEquals(initialCount, rutaTable.getItems().size());
//    }
//
//    @Test
//    public void testL_editOrigenServerError() {
//        Node row = lookup(".table-row-cell").nth(0).query();
//        Ruta original = rutaTable.getItems().get(0);
//        String expectedOrigen = original.getOrigen();
//
//        doubleClickOn(row);
//        Node origenCell = lookup(".table-cell").nth(1).query(); // Columna origen
//        doubleClickOn(origenCell);
//        write("NewOrigen");
//        press(KeyCode.ENTER);
//
//        verifyThat("Error del servidor", Node::isVisible);
//        verifyThat("No se pudo actualizar el origen.", Node::isVisible);
//        clickOn("Aceptar");
//
//        assertEquals(expectedOrigen, rutaTable.getItems().get(0).getOrigen());
//    }
//
//    @Test
//    public void testM_removeRutaServerError() {
//        int initialCount = rutaTable.getItems().size();
//        Node row = lookup(".table-row-cell").nth(0).query();
//
//        clickOn(row);
//        clickOn(removeShipmentBtn);
//
//        verifyThat("¿Está seguro de que desea eliminar las rutas seleccionadas?", Node::isVisible);
//        clickOn("Aceptar");
//
//        verifyThat("Error", Node::isVisible);
//        verifyThat("Error inesperado al eliminar rutas.", Node::isVisible);
//        clickOn("Aceptar");
//
//        assertEquals(initialCount, rutaTable.getItems().size());
//    }
//    
//    
    //TEST ADICIONALES
//    @Test ESTE NO
//    public void testG_printReport() {
//        clickOn(printReportBtn);
//        // Verificación básica de que el botón es funcional
//    }
//    
//      @Test ESTE NO
//    public void testD_filterByTime() {
//        interact(() -> {
//            filterTypeComboBox.getSelectionModel().select("Filter by Time");
//            operatorComboBox.getSelectionModel().select(">");
//            filterValueField.setText("10");
//        });
//        clickOn(searchButton1);
//        verifyThat(rutaTable, (TableView<Ruta> t)
//                -> t.getItems().stream().allMatch(r -> r.getTiempo() > 10)
//        );
//    }
//
//    @Test  ESTE NO
//    public void testE_searchByLocalizador() {
//        interact(() -> searchTextField.setText("1"));
//        clickOn(searchButton);
//        verifyThat(rutaTable, (TableView<Ruta> t)
//                -> t.getItems().stream().anyMatch(r -> r.getLocalizador().toString().contains("1"))
//        );
//    }
}
