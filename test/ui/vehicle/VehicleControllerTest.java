package ui.vehicle;

import application.VehiculoMain;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import exception.SelectException;
import factories.VehicleFactory;
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
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.control.DateCell;
import javafx.scene.control.TableColumn;
import javafx.scene.input.MouseButton;
import static org.junit.Assert.assertNotEquals;

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
    public void testB_readAll() {
        // Comprobar que hay al menos 1 vehiculo
        if (vehicleTableView.getItems().isEmpty()) {
            clickOn("#addShipmentBtn");
            WaitForAsyncUtils.waitForFxEvents();
        }

        // Obtener todos los objetos de la tabla
        ObservableList<Vehiculo> items = vehicleTableView.getItems();

        // Verificar que la tabla no está vacía
        assertFalse("Table should not be empty", items.isEmpty());

        // Verificar que todos los objetos de la tabla son de tipo Vehiculo
        for (Object item : items) {
            assertTrue("Table item should be of type Vehiculo",
                    item instanceof Vehiculo);
        }

    }

    @Test
    public void testC_addVehicle() {
        // Get initial row count
        int initialRowCount = vehicleTableView.getItems().size();

        // Add new vehicle
        clickOn("#addShipmentBtn");
        WaitForAsyncUtils.waitForFxEvents();

        // Verify row count increased
        int newRowCount = vehicleTableView.getItems().size();
        assertEquals("Row count should increase by 1", initialRowCount + 1, newRowCount);

        // Get the newly added vehicle
        Vehiculo newVehicle = vehicleTableView.getItems().get(newRowCount - 1);

        // Verify default values
        assertNotNull("New vehicle should not be null", newVehicle);
        assertNotNull("ID should not be null", newVehicle.getId());
        assertFalse("Default activo should be false", newVehicle.isActivo());
        assertNotNull("Registration date should not be null", newVehicle.getRegistrationDate());
        assertEquals("Matricula should be empty string", "", newVehicle.getMatricula());

        // Verify null fields
        assertNull("Modelo should be null", newVehicle.getModelo());
        assertNull("Capacidad should be null", newVehicle.getCapacidadCarga());
        assertNull("ITV date should be null", newVehicle.getItvDate());
    }

    @Test
    public void testD_updateVehicle() {
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

        // Ir al vehiculo añadido
        Node cell = lookup(".table-row-cell").nth(lastIndex)
                .lookup(".table-cell").nth(1).query();
        // Hacer doble clic para activar el modo de edición.
        doubleClickOn(cell);
        WaitForAsyncUtils.waitForFxEvents();

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
    public void testE_updateModelo() {
        if (vehicleTableView.getItems().isEmpty()) {
            clickOn("#addShipmentBtn");
            WaitForAsyncUtils.waitForFxEvents();
        }

        int lastIndex = vehicleTableView.getItems().size() - 1;
        Node cell = lookup(".table-row-cell").nth(lastIndex)
                .lookup(".table-cell").nth(2).query(); // Columna modelo

        doubleClickOn(cell);
        WaitForAsyncUtils.waitForFxEvents();

        write("Tesla Model 3");
        press(KeyCode.ENTER);
        release(KeyCode.ENTER);
        WaitForAsyncUtils.waitForFxEvents();

        Vehiculo updated = vehicleTableView.getItems().get(lastIndex);
        assertEquals("El modelo no se actualizó correctamente", "Tesla Model 3", updated.getModelo());
    }

    @Test
    public void testF_updateCapacidad() {
        if (vehicleTableView.getItems().isEmpty()) {
            clickOn("#addShipmentBtn");
            WaitForAsyncUtils.waitForFxEvents();
        }

        int lastIndex = vehicleTableView.getItems().size() - 1;
        Node cell = lookup(".table-row-cell").nth(lastIndex)
                .lookup(".table-cell").nth(3).query(); // Columna capacidad

        doubleClickOn(cell);
        WaitForAsyncUtils.waitForFxEvents();

        write("500");
        press(KeyCode.ENTER);
        release(KeyCode.ENTER);
        WaitForAsyncUtils.waitForFxEvents();

        Vehiculo updated = vehicleTableView.getItems().get(lastIndex);
        assertEquals("La capacidad no se actualizó correctamente", Integer.valueOf(500), updated.getCapacidadCarga());
    }

    @Test
    public void testG_updateActivo() {
        if (vehicleTableView.getItems().isEmpty()) {
            clickOn("#addShipmentBtn");
            WaitForAsyncUtils.waitForFxEvents();
            assertFalse("No se agregó un vehículo", vehicleTableView.getItems().isEmpty());
        }

        int lastIndex = vehicleTableView.getItems().size() - 1;
        Vehiculo vehiculo = vehicleTableView.getItems().get(lastIndex);
        boolean initialState = vehiculo.isActivo();

        // Obtener la celda de la columna "Activo"
        Node cell = lookup(".table-row-cell").nth(lastIndex)
                .lookup(".table-cell").nth(6).query();

        doubleClickOn(cell);
        WaitForAsyncUtils.waitForFxEvents();
        sleep(500); // Esperar a que se active el modo edición
    
        clickOn(cell);
        WaitForAsyncUtils.waitForFxEvents();
        sleep(500); // Esperar a que se procese el cambio

        // Confirmar la edición
        press(KeyCode.ENTER);
        release(KeyCode.ENTER);
        WaitForAsyncUtils.waitForFxEvents();

        // Forzar actualización
        interact(() -> vehicleTableView.refresh());

        // Verificar el cambio
        boolean updatedState = vehiculo.isActivo();
        assertNotEquals("El estado activo no se actualizó correctamente", initialState, updatedState);
    }

    @Test
    public void testH_updateITVDate() {
        // Se asume que el índice de la última fila es el que deseas actualizar
        int lastIndex = vehicleTableView.getItems().size() - 1;

        // Buscar la celda de ITV Date en la última fila
        Node dateCell = lookup(".table-row-cell").nth(lastIndex)
                .lookup(".table-cell").nth(5).query();  // 5 corresponde a la columna ITV Date

        // Hacer doble clic en la celda para activarla
        doubleClickOn(dateCell);

        // Esperar a que el campo de texto esté listo para escribir
        sleep(500);

        // Escribir la nueva fecha en formato d/M/yyyy
        String newDate = "2/4/2025";  // Formato d/M/yyyy como lo ingresaría el usuario
        write(newDate);

        // Presionar ENTER para confirmar el cambio
        push(KeyCode.ENTER);

        // Esperar la actualización por si el servidor va lento
        sleep(500);

        // Obtener el objeto Vehiculo actualizado
        Vehiculo updated = vehicleTableView.getItems().get(lastIndex);
        LocalDate updatedDate = updated.getItvDate().toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();

        // Formatear la fecha actualizada como MM-dd-yyyy
        String formattedDate = updatedDate.format(DateTimeFormatter.ofPattern("MM-dd-yyyy"));

        // Comprobar que la fecha de ITV se ha actualizado correctamente a 04-02-2025
        assertEquals("02-04-2025", formattedDate);
    }

    @Test
    public void testI_deleteVehicle() {
        // Verificar que se puede hacer la prueba con algún vehiculo
        if (vehicleTableView.getItems().isEmpty()) {
            clickOn("#addShipmentBtn");
            WaitForAsyncUtils.waitForFxEvents();
        }

        // Obtener el vehiculo a borrar
        int initialCount = vehicleTableView.getItems().size();
        Vehiculo vehicleToDelete = vehicleTableView.getItems().get(0);
        Integer idToDelete = vehicleToDelete.getId();

        // Seleccionar el primero
        Node firstRow = lookup(".table-row-cell").query();
        clickOn(firstRow);

        // Borrar el vehiculo
        clickOn(removeShipmentBtn);
        WaitForAsyncUtils.waitForFxEvents();

        // Confirmar el borrado
        DialogPane dialogPane = lookup(".dialog-pane").query();
        assertNotNull("Confirmation dialog not shown", dialogPane);
        Button okButton = (Button) dialogPane.lookupButton(ButtonType.OK);
        clickOn(okButton);
        WaitForAsyncUtils.waitForFxEvents();

        // Verificar que hay un vehiculo menos en la tabla
        assertEquals("Vehicle count should decrease by 1",
                initialCount - 1, vehicleTableView.getItems().size());

        // Verificar que el vehiculo seleccionado en concreto se ha eliminado
        boolean vehicleStillExists = vehicleTableView.getItems().stream()
                .anyMatch(v -> v.getId().equals(idToDelete));
        assertFalse("Deleted vehicle should not exist in table", vehicleStillExists);
    }

