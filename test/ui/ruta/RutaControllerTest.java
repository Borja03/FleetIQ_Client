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
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Button;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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

    @Test
    public void testC_editDestino() {
        // Wait for data to load
        WaitForAsyncUtils.waitForFxEvents();

        // Verify table has data
        assertFalse("Table empty - no routes found", rutaTable.getItems().isEmpty());
        TableRow<Ruta> row = lookup(".table-row-cell").nth(0).query(); // Primera fila
        TableColumn<Ruta, ?> destinoColumn = rutaTable.getColumns().get(2); // Asegúrate de que el índice sea correcto para la columna "destino"

        interact(() -> {
            doubleClickOn(row.getChildrenUnmodifiable().get(2)); // Cambiado para editar la columna correcta
        });

        // Enter new value
        write("NewDestino");
        press(KeyCode.ENTER);
        WaitForAsyncUtils.waitForFxEvents();

        // Verify update
        Ruta updated = rutaTable.getItems().get(0); // Primera fila
        assertEquals("NewDestino", updated.getDestino()); // Ahora verifica el destino en lugar del origen
    }

    @Test
    public void testC_editDistancia() {
        // Wait for data to load
        WaitForAsyncUtils.waitForFxEvents();

        // Verify table has data
        assertFalse("Table empty - no routes found", rutaTable.getItems().isEmpty());
        TableRow<Ruta> row = lookup(".table-row-cell").nth(0).query(); // Primera fila
        TableColumn<Ruta, ?> distanciaColumn = rutaTable.getColumns().get(3); // Asegúrate de que el índice sea el correcto para la columna "distancia"

        interact(() -> {
            doubleClickOn(row.getChildrenUnmodifiable().get(3)); // Cambiado para editar la columna correcta
        });

        // Enter new value
        String newDistancia = "12.5"; // Un valor Float válido
        write(newDistancia);
        press(KeyCode.ENTER);
        WaitForAsyncUtils.waitForFxEvents();

        // Verify update
        Ruta updated = rutaTable.getItems().get(0); // Primera fila
        assertEquals(Float.parseFloat(newDistancia), updated.getDistancia(), 0.01); // Verifica con un margen de error pequeño
    }

    @Test
    public void testC_editTiempo() {
        // Wait for data to load
        WaitForAsyncUtils.waitForFxEvents();

        // Verify table has data
        assertFalse("Table empty - no routes found", rutaTable.getItems().isEmpty());
        TableRow<Ruta> row = lookup(".table-row-cell").nth(0).query(); // Primera fila
        TableColumn<Ruta, ?> tiempoColumn = rutaTable.getColumns().get(4); // Asegúrate de que el índice sea el correcto para la columna "tiempo"

        interact(() -> {
            doubleClickOn(row.getChildrenUnmodifiable().get(4)); // Editar la columna correcta
        });

        // Enter new value
        String newTiempo = "45"; // Un valor Integer válido
        write(newTiempo);
        press(KeyCode.ENTER);
        WaitForAsyncUtils.waitForFxEvents();

        // Verify update
        Ruta updated = rutaTable.getItems().get(0); // Primera fila
        assertEquals(Integer.valueOf(newTiempo), updated.getTiempo()); // Se usa Integer.valueOf() para evitar ambigüedad
    }

    @Test
    public void testC_editFecha() throws ParseException {
        // Wait for data to load
        WaitForAsyncUtils.waitForFxEvents();

        // Verify table has data
        assertFalse("Table empty - no routes found", rutaTable.getItems().isEmpty());
        TableRow<Ruta> row = lookup(".table-row-cell").nth(0).query(); // Primera fila
        TableColumn<Ruta, ?> fechaColumn = rutaTable.getColumns().get(5); // Asegúrate de que el índice sea el correcto para la columna "fecha"

        interact(() -> {
            doubleClickOn(row.getChildrenUnmodifiable().get(5)); // Editar la columna correcta
        });

        // Borrar contenido previo
        push(KeyCode.CONTROL, KeyCode.A); // Selecciona todo
        push(KeyCode.BACK_SPACE); // Borra el contenido
        WaitForAsyncUtils.waitForFxEvents();

        // Enter new value
        String newFecha = "4/01/2024"; // Fecha en formato día/mes/año
        write(newFecha);
        press(KeyCode.ENTER);
        WaitForAsyncUtils.waitForFxEvents();

        // Convertir la cadena de fecha a tipo Date
        SimpleDateFormat dateFormat = new SimpleDateFormat("d/MM/yyyy"); // Formato compatible con la entrada
        Date expectedDate = dateFormat.parse(newFecha);

        // Verify update
        Ruta updated = rutaTable.getItems().get(0); // Primera fila
        assertEquals(expectedDate, updated.getFechaCreacion()); // Verifica que la fecha se haya actualizado correctamente
    }

//    @Test
//    public void testD_addVehicle() {
//        WaitForAsyncUtils.waitForFxEvents();
//
//        assertFalse("Table empty - no routes found", rutaTable.getItems().isEmpty());
//        TableRow<Ruta> row = lookup(".table-row-cell").nth(0).query();
//
//        // Adjust the column index if necessary (e.g., 2 for the third column)
//        TableColumn<Ruta, ?> numVehiculosColumn = rutaTable.getColumns().get(6);
//
//        // Get the cell text scoped to the row and correct column index
//        String initialCountText = from(row).lookup(".table-cell").nth(6).queryText().getText();
//        int initialCountFromTable = Integer.parseInt(initialCountText);
//
//        Ruta selectedRuta = rutaTable.getItems().get(0);
//        int initialCount = selectedRuta.getNumVehiculos();
//        assertEquals(initialCount, initialCountFromTable);
//
//        interact(() -> rightClickOn(row));
//        clickOn("Añadir Vehículo");
//
//        WaitForAsyncUtils.waitForFxEvents();
//        JFXListView<String> vehicleListView = lookup(".jfx-list-view").query();
//        interact(() -> vehicleListView.getSelectionModel().select(0));
//
//        clickOn("Confirmar");
//        WaitForAsyncUtils.waitForFxEvents();
//
//        // Check updated count similarly, scoped to the row
//        String updatedCountText = from(row).lookup(".table-cell").nth(3).queryText().getText();
//        int updatedCountFromTable = Integer.parseInt(updatedCountText);
//        int updatedCount = selectedRuta.getNumVehiculos();
//
//        assertEquals(initialCount + 1, updatedCount);
//        assertEquals(updatedCount, updatedCountFromTable);
//    }

    @Test
    public void testSearchByLocalizador_success() throws InterruptedException {
        // Ingresar el valor "2" en el campo de búsqueda
        interact(() -> {
            searchTextField.setText("3");
        });

        // Simular el clic en el botón que dispara la búsqueda.
        // Se asume que dicho botón tiene fx:id "searchButton" y está correctamente referenciado.
        clickOn(searchButton);
        WaitForAsyncUtils.waitForFxEvents();

        // Verificar que la tabla se haya actualizado con la ruta correspondiente.
        // Se espera que la tabla contenga exactamente un elemento.
        ObservableList<Ruta> items = rutaTable.getItems();
        assertNotNull("La lista de rutas no debe ser null", items);
        assertEquals("Debe haber exactamente un elemento en la tabla", 1, items.size());

        // Verificar que el objeto Ruta presente tenga el valor 2 en el campo de localizador
        Ruta ruta = items.get(0);
        assertEquals("El localizador de la ruta debe ser 3", Integer.valueOf(3), ruta.getLocalizador());

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
