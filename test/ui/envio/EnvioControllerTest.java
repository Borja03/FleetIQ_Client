/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.envio;

import application.EnvioMain;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import java.time.LocalDate;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import models.Envio;
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
public class EnvioControllerTest extends ApplicationTest {

    private TableView<Envio> table;
    private JFXButton addShipmentBtn;
    private JFXButton removeShipmentBtn;
    private JFXButton printReportBtn;
    private JFXButton applyNumPaquetesFilterButton;
    private JFXButton applyDateFilterButton;
    private JFXTextField searchTextField;
    private JFXTextField numeroPaquetesTextField;
    private JFXDatePicker fromDatePicker;
    private JFXDatePicker toDatePicker;

    @Override
    public void start(Stage stage) throws Exception {
        new EnvioMain().start(stage);

        table = lookup("#table").queryTableView();
        addShipmentBtn = (JFXButton) lookup("#addShipmentBtn").queryButton();
        removeShipmentBtn = (JFXButton) lookup("#removeShipmentBtn").queryButton();
        printReportBtn = (JFXButton) lookup("#printReportBtn").queryButton();
        applyNumPaquetesFilterButton = (JFXButton) lookup("#applyNumPaquetesFilterButton").queryButton();
        applyDateFilterButton = (JFXButton) lookup("#applyDateFilterButton").queryButton();
        searchTextField = lookup("#searchTextField").query();
        numeroPaquetesTextField = lookup("#numeroPaquetesTextField").query();
        fromDatePicker = lookup("#fromDatePicker").query();
        toDatePicker = lookup("#toDatePicker").query();

        assertNotNull("table no encontrada", table);
        assertNotNull("addShipmentBtn no encontrado", addShipmentBtn);
        assertNotNull("removeShipmentBtn no encontrado", removeShipmentBtn);
        assertNotNull("applyNumPaquetesFilterButton no encontrado", applyNumPaquetesFilterButton);
        assertNotNull("applyDateFilterButton no encontrado", applyDateFilterButton);
    }

    @Test
    public void testA_initialState() {
        verifyThat("#table", isVisible());
        verifyThat("#addShipmentBtn", isEnabled());
        verifyThat("#removeShipmentBtn", isDisabled());
        verifyThat("#printReportBtn", isEnabled());
        verifyThat("#applyNumPaquetesFilterButton", isEnabled());
        verifyThat("#applyDateFilterButton", isEnabled());
        assertNull(fromDatePicker.getValue());
        assertNull(toDatePicker.getValue());
    }

    @Test
    public void testB_addEnvio() {
        int initialCount = table.getItems().size();
        clickOn(addShipmentBtn);
        assertEquals(initialCount + 1, table.getItems().size());
    }

    @Test
    public void testC_removeEnvio() {
        int initialCount = table.getItems().size();
        Node row = lookup(".table-row-cell").nth(0).query();
        clickOn(row);
        verifyThat("#removeShipmentBtn", isEnabled());
        clickOn(removeShipmentBtn);
        assertEquals(initialCount - 1, table.getItems().size());
    }

    @Test
    public void testF_filterByDateRange() {
        interact(() -> {
            fromDatePicker.setValue(LocalDate.of(2024, 1, 1));
            toDatePicker.setValue(LocalDate.of(2024, 12, 31));
        });
        clickOn(applyDateFilterButton);
        verifyThat(table, (TableView<Envio> t)
                -> t.getItems().stream().allMatch(r
                        -> !r.getFechaEnvio().before(java.sql.Date.valueOf("2024-01-01"))
                && !r.getFechaEnvio().after(java.sql.Date.valueOf("2024-12-31"))
                )
        );
    }

    @Test
    public void testH_tableSelection() {
        Node row = lookup(".table-row-cell").nth(0).query();
        clickOn(row);
        verifyThat("#removeShipmentBtn", isEnabled());
    }


    /**
     * Test date range filtering functionality.
     */

    @Test
    public void testI_validation() {
        Node numPaquetesColumn = lookup(".table-cell").nth(4).query();
        doubleClickOn(numPaquetesColumn);
        write("-5");
        press(KeyCode.ENTER);
        verifyThat("En numero de paquetes debe ser mayor o igual a cero", isVisible());
        clickOn("Aceptar");
    }

    @Test
    public void testJ_editOrigen() {
        // Wait for data to load
        WaitForAsyncUtils.waitForFxEvents();

        // Verify table has data
        assertFalse("Table empty - no routes found", table.getItems().isEmpty());
        TableRow<Envio> row = lookup(".table-row-cell").nth(5)
                .query();
        Node numPaquetesColumn = lookup(".table-cell").nth(4).query();
        doubleClickOn(numPaquetesColumn);
        write("5");
        press(KeyCode.ENTER);
        WaitForAsyncUtils.waitForFxEvents();

        // Verify update
        Envio updated = table.getItems().get(0);
        assertEquals("5", updated.getNumPaquetes().toString());
    }

}
