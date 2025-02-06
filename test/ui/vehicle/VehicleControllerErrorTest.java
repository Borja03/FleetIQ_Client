package ui.vehicle;

import application.VehiculoMain;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import exception.DeleteException;
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
import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import service.VehicleRESTClient;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class VehicleControllerErrorTest extends ApplicationTest {

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
  private VehicleManagerImp vehicleManager;

@Test
public void testDeleteVehiculo_ServerDown() {
    WaitForAsyncUtils.waitForFxEvents();

    int initialCount = vehicleTableView.getItems().size();
    if (initialCount == 0) {
        return;
    }

    // Simular servidor caído
    VehicleRESTClient client = new VehicleRESTClient("http://localhost:9999/api");
    vehicleManager = new VehicleManagerImp(client);

    // Seleccionar la primera fila
    Node firstRow = lookup(".table-row-cell").query();
    clickOn(firstRow);

    // Hacer clic en el botón de eliminar
    clickOn(removeShipmentBtn);
    WaitForAsyncUtils.waitForFxEvents();

    // Verificar si la alerta de confirmación aparece
    DialogPane dialogPane = lookup(".dialog-pane").query();
    assertNotNull("Confirmation dialog not shown", dialogPane);

    // Obtener y hacer clic en el botón OK de la alerta
    Button okButton = (Button) dialogPane.lookupButton(ButtonType.OK);
    assertNotNull("OK button not found in dialog", okButton);
    clickOn(okButton);
    WaitForAsyncUtils.waitForFxEvents();

    // Verificar si la alerta de error se muestra debido al servidor caído
    DialogPane errorDialog = lookup(".dialog-pane").query();
    assertNotNull("Error dialog not shown", errorDialog);
    
    // Obtener el texto de la alerta de error
    Label errorLabel = lookup(".dialog-pane .label").query();
    assertTrue("Error message not found", errorLabel.getText().contains("Deletion Failed"));

    // Cerrar la alerta de error
    Button errorOkButton = (Button) errorDialog.lookupButton(ButtonType.OK);
    assertNotNull("OK button not found in error dialog", errorOkButton);
    clickOn(errorOkButton);
    WaitForAsyncUtils.waitForFxEvents();

    // Verificar que el número de vehículos no cambió
    assertEquals("Vehicle count should remain the same when server is down", 
                initialCount, vehicleTableView.getItems().size());
}


    
    
}
