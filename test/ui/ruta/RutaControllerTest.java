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
import javafx.scene.Node;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import models.Ruta;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import javafx.stage.Stage;
import models.PackageSize;
import static org.junit.Assert.*;
import static org.testfx.api.FxAssert.verifyThat;
import org.testfx.framework.junit.ApplicationTest;
import static org.testfx.matcher.base.NodeMatchers.isDisabled;
import static org.testfx.matcher.base.NodeMatchers.isEnabled;
import static org.testfx.matcher.base.NodeMatchers.isVisible;
import static org.testfx.matcher.control.TextInputControlMatchers.hasText;

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
        // Iniciar la aplicación y cargar el controlador Ruta
        // Asume que la clase principal de la aplicación inicia correctamente la UI de Ruta
        new RutaMain().start(stage);

        // Buscar componentes de la UI
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
        //filterTypeComboBox = (JFXComboBox<Object>)lookup("#sizeFilterComboBox").query();
     //   operatorComboBox = (JFXComboBox<Object>)lookup("#sizeFilterComboBox1").query();
        
                

        // Verificar que los componentes existen
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
        assertTrue(filterTypeComboBox.getSelectionModel().isEmpty());
        assertTrue(operatorComboBox.getSelectionModel().isEmpty());
    }

    @Test
    public void testB_addRuta() {
        int initialCount = rutaTable.getItems().size();
        clickOn(addShipmentBtn);
        assertEquals(initialCount + 1, rutaTable.getItems().size());
    }

//    @Test
//    public void testC_removeRuta() {
//        int initialCount = rutaTable.getItems().size();
//        Node row = lookup(".table-row-cell").nth(0).query();
//        clickOn(row);
//        verifyThat("#removeShipmentBtn", isEnabled());
//        clickOn(removeShipmentBtn);
//        assertEquals(initialCount - 1, rutaTable.getItems().size());
//    }

    @Test
    public void testD_filterByTime() {
        interact(() -> {
            filterTypeComboBox.getSelectionModel().select("Filter by Time");
            operatorComboBox.getSelectionModel().select(">");
            filterValueField.setText("10");
        });
        clickOn(searchButton1);
        verifyThat(rutaTable, (TableView<Ruta> t) ->
            t.getItems().stream().allMatch(r -> r.getTiempo() > 10)
        );
    }

    @Test
    public void testE_searchByLocalizador() {
        interact(() -> searchTextField.setText("1"));
        clickOn(searchButton);
        verifyThat(rutaTable, (TableView<Ruta> t) ->
            t.getItems().stream().anyMatch(r -> r.getLocalizador().toString().contains("1"))
        );
    }

    @Test
    public void testF_filterByDateRange() {
        interact(() -> {
            fromDatePicker.setValue(LocalDate.of(2024, 1, 1));
            toDatePicker.setValue(LocalDate.of(2024, 12, 31));
        });
        clickOn(applyFilterButton);
        verifyThat(rutaTable, (TableView<Ruta> t) ->
            t.getItems().stream().allMatch(r ->
                !r.getFechaCreacion().before(java.sql.Date.valueOf("2024-01-01")) &&
                !r.getFechaCreacion().after(java.sql.Date.valueOf("2024-12-31"))
            )
        );
    }

    @Test
    public void testG_printReport() {
        clickOn(printReportBtn);
        // Verificación básica de que el botón es funcional
    }

    @Test
    public void testH_tableSelection() {
        Node row = lookup(".table-row-cell").nth(0).query();
        clickOn(row);
        verifyThat("#removeShipmentBtn", isEnabled());
    }

    @Test
    public void testI_validation() {
        Node distanciaCell = lookup(".table-cell").nth(3).query(); // Ajustar índice según la columna
        doubleClickOn(distanciaCell);
        write("-10");
        press(KeyCode.ENTER);
        verifyThat("La distancia no puede ser negativa.", isVisible());
    }
}