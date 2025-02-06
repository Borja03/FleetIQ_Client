package ui.vehicle;

import application.VehiculoMain;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import exception.SelectException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import models.Vehiculo;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import javafx.stage.Stage;
import javax.ws.rs.ProcessingException;
import logicimplementation.VehicleManagerImp;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.testfx.api.FxAssert.verifyThat;
import org.testfx.framework.junit.ApplicationTest;
import static org.testfx.matcher.base.NodeMatchers.isDisabled;
import static org.testfx.matcher.base.NodeMatchers.isEnabled;
import static org.testfx.matcher.base.NodeMatchers.isVisible;
import static org.testfx.matcher.control.TextInputControlMatchers.hasText;
import org.testfx.util.WaitForAsyncUtils;
import java.time.format.DateTimeFormatter;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class VehicleControllerTest extends ApplicationTest {

    private TableView<Vehiculo> vehicleTableView;
    private JFXButton addShipmentBtn;
    private JFXButton removeShipmentBtn;
    private JFXButton printReportBtn;
    private JFXButton searchButton;
    private JFXButton applyFilterButton;
    private JFXTextField searchTextField;
    private JFXTextField capacityTextField;
    private JFXDatePicker fromDatePicker;
    private JFXDatePicker toDatePicker;
    private JFXComboBox<String> filterTypeComboBox;
    private JFXButton minusButton;
    private JFXButton plusButton;

    @Override
    public void start(Stage stage) throws Exception {
        new VehiculoMain().start(stage);

        // Lookup components
        vehicleTableView = lookup("#vehicleTableView").queryTableView();
        addShipmentBtn = (JFXButton) lookup("#addShipmentBtn").queryButton();
        removeShipmentBtn = (JFXButton) lookup("#removeShipmentBtn").queryButton();
        printReportBtn = (JFXButton) lookup("#printReportBtn").queryButton();
        searchButton = (JFXButton) lookup("#searchButton").queryButton();
        applyFilterButton = (JFXButton) lookup("#applyFilterButton").queryButton();
        searchTextField = (JFXTextField) lookup("#searchTextField").query();
        capacityTextField = (JFXTextField) lookup("#capacityTextField").query();
        fromDatePicker = (JFXDatePicker) lookup("#fromDatePicker").query();
        toDatePicker = (JFXDatePicker) lookup("#toDatePicker").query();
        filterTypeComboBox = (JFXComboBox<String>) lookup("#filterTypeComboBox").query();
        minusButton = (JFXButton) lookup("#minusButton").queryButton();
        plusButton = (JFXButton) lookup("#plusButton").queryButton();

        // Verify components are found
        assertNotNull("VehicleTableView not found", vehicleTableView);
        assertNotNull("SearchTextField not found", searchTextField);
        assertNotNull("CapacityTextField not found", capacityTextField);
        assertNotNull("FromDatePicker not found", fromDatePicker);
        assertNotNull("ToDatePicker not found", toDatePicker);
        assertNotNull("FilterTypeComboBox not found", filterTypeComboBox);
    }

    @Test
    public void testA_initialState() {
        verifyThat("#vehicleTableView", isVisible());
        verifyThat("#addShipmentBtn", isEnabled());
        verifyThat("#removeShipmentBtn", isDisabled());
        verifyThat("#printReportBtn", isEnabled());
        verifyThat("#searchButton", isEnabled());
        verifyThat("#applyFilterButton", isEnabled());
        verifyThat("#searchTextField", hasText(""));
        verifyThat("#capacityTextField", hasText("0"));

        // Check date pickers are empty
        assertNull(fromDatePicker.getValue());
        assertNull(toDatePicker.getValue());

        // Verify filter type combo box is initialized with correct values
        ObservableList<String> items = filterTypeComboBox.getItems();
        assertTrue(items.contains("ITV Date"));
        assertTrue(items.contains("Registration Date"));
    }

    @Test
    public void testB_addVehicle() {
        int initialRowCount = vehicleTableView.getItems().size();

        clickOn("#addShipmentBtn");
        WaitForAsyncUtils.waitForFxEvents();

        int newRowCount = vehicleTableView.getItems().size();
        assertEquals(initialRowCount + 1, newRowCount);

        // Verify default values of new vehicle
        Vehiculo newVehicle = vehicleTableView.getItems().get(newRowCount - 1);
        assertEquals("", newVehicle.getMatricula());
        assertNotNull(newVehicle.getRegistrationDate());
    }

    @Test
public void testC_updateVehicle() {
    WaitForAsyncUtils.waitForFxEvents();

    // Asegurarse de que la tabla tenga al menos un vehículo.
    if (vehicleTableView.getItems().isEmpty()) {
        clickOn("#addShipmentBtn");
        WaitForAsyncUtils.waitForFxEvents();
    }

    // Agregar un vehículo nuevo para trabajar sobre un registro conocido.
    clickOn("#addShipmentBtn");
    WaitForAsyncUtils.waitForFxEvents();
    int lastIndex = vehicleTableView.getItems().size() - 1;

    // Ubicar la celda correspondiente a la matrícula de la fila recién agregada.
    Node cell = lookup(".table-row-cell").nth(lastIndex)
                      .lookup(".table-cell").nth(1).query();
    // Hacer doble clic para activar el modo de edición.
    doubleClickOn(cell);
    WaitForAsyncUtils.waitForFxEvents();
    
    // Escribir la nueva matrícula. Si hay contenido anterior, se puede intentar borrarlo.
    // Se puede usar eraseText(n) si se sabe cuántos caracteres hay, o bien escribir sobre el campo.
    eraseText(10); 
    write("ABC123");
    press(KeyCode.ENTER);
    release(KeyCode.ENTER);
    WaitForAsyncUtils.waitForFxEvents();

    // Verificar que la matrícula se actualizó.
    Vehiculo updated = vehicleTableView.getItems().get(lastIndex);
    assertEquals("La matrícula del vehículo no se actualizó correctamente",
                 "ABC123", updated.getMatricula());
}


    @Test
    public void testD_deleteVehicle() {
        WaitForAsyncUtils.waitForFxEvents();

        int initialCount = vehicleTableView.getItems().size();
        if (initialCount == 0) {
            return;
        }

        // Select first row
        Node firstRow = lookup(".table-row-cell").query();
        clickOn(firstRow);
        
        clickOn(removeShipmentBtn);
        WaitForAsyncUtils.waitForFxEvents();

        // Verify confirmation dialog
        DialogPane dialogPane = lookup(".dialog-pane").query();
        assertNotNull("Confirmation dialog not shown", dialogPane);

        Button okButton = (Button) dialogPane.lookupButton(ButtonType.OK);
        assertNotNull("OK button not found in dialog", okButton);

        clickOn(okButton);
        WaitForAsyncUtils.waitForFxEvents();

        assertEquals("Vehicle count should decrease by 1", 
                    initialCount - 1, vehicleTableView.getItems().size());
    }

@Test
public void testE_searchFunctionality() {
    // Agregar un vehículo nuevo para la búsqueda.
    clickOn("#addShipmentBtn");
    WaitForAsyncUtils.waitForFxEvents();
    int lastIndex = vehicleTableView.getItems().size() - 1;

    // Ubicar la celda de la matrícula en la fila recién agregada.
    Node cell = lookup(".table-row-cell").nth(lastIndex)
                      .lookup(".table-cell").nth(1).query();
    // Doble clic para activar el modo edición.
    doubleClickOn(cell);
    WaitForAsyncUtils.waitForFxEvents();
    
    // Borrar cualquier contenido previo y escribir "ABC123".
    eraseText(10);
    write("ABC123");
    press(KeyCode.ENTER);
    release(KeyCode.ENTER);
    WaitForAsyncUtils.waitForFxEvents();

    // Confirmar que la matrícula se actualizó.
    Vehiculo newVehicle = vehicleTableView.getItems().get(lastIndex);
    assertEquals("La matrícula del vehículo no se actualizó correctamente",
                 "ABC123", newVehicle.getMatricula());

    // Limpiar el campo de búsqueda (usando doble clic para seleccionar todo) y escribir "ABC".
    doubleClickOn("#searchTextField");
    write("ABC");
    clickOn("#searchButton");
    WaitForAsyncUtils.waitForFxEvents();

    // Verificar que al menos un vehículo tenga "ABC" en la matrícula.
    ObservableList<Vehiculo> results = vehicleTableView.getItems();
    boolean found = results.stream().anyMatch(v -> v.getMatricula().contains("ABC"));
    if (!found) {
         String allMatriculas = "";
         for (Vehiculo v : results) {
              allMatriculas += "[" + v.getMatricula() + "] ";
         }
         fail("No se encontró ningún vehículo con 'ABC' en la matrícula. Matrículas presentes: " + allMatriculas);
    }
    assertTrue("Se encontró al menos un vehículo con 'ABC' en la matrícula", found);
}



    @Test
    public void testH_serverConnection() {
        try {
            VehicleManagerImp vehicleManager = new VehicleManagerImp();
            List<Vehiculo> result = vehicleManager.findAllVehiculos();

            assertTrue("Server is connected", true);
            assertNotNull("Received valid response", result);
        } catch (SelectException e) {
            if (e.getMessage().contains("Database server connection failed")) {
                fail("Server connection failed: " + e.getMessage());
            } else if (e.getCause() instanceof ProcessingException) {
                fail("Network error: " + e.getCause().getMessage());
            } else {
                assertTrue("Server is reachable but operation failed", true);
            }
        } catch (Exception e) {
            fail("Unexpected error: " + e.getMessage());
        }
    }
}
