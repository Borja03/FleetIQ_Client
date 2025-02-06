package ui.envio;

import application.EnvioMain;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import org.junit.Test;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;
import static org.junit.Assert.*;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import factories.EnvioFactory;
import java.time.LocalDate;
import javafx.scene.Node;
import javafx.scene.control.TableRow;
import javafx.scene.input.KeyCode;
import logicInterface.EnvioManager;
import models.Envio;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.testfx.api.FxAssert;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.base.NodeMatchers.isDisabled;
import static org.testfx.matcher.base.NodeMatchers.isEnabled;
import static org.testfx.matcher.base.NodeMatchers.isVisible;
import org.testfx.matcher.base.WindowMatchers;
import service.EnvioRESTClient;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class EnvioControllerUpdateServerError extends ApplicationTest {

    private TableView<Envio> table;
    private JFXButton removeShipmentBtn;
    private JFXButton addShipmentBtn;
    private JFXDatePicker fromDatePicker;
    private JFXDatePicker toDatePicker;
    private JFXButton applyDateFilterButton;
    private static final String INVALID_API_PATH = "http://localhost:9090/api/wrong_path";
    int initialCount;

    @Override
    public void start(Stage stage) throws Exception {
        new EnvioMain().start(stage);

        // Obtener referencias a los componentes de la UI
        table = lookup("#table").query();
        removeShipmentBtn = lookup("#removeShipmentBtn").query();
        addShipmentBtn = lookup("#addShipmentBtn").query();
        fromDatePicker = lookup("#fromDatePicker").query();
        toDatePicker = lookup("#toDatePicker").query();
        applyDateFilterButton = (JFXButton) lookup("#applyDateFilterButton").queryButton();
        initialCount = table.getItems().size();

    }

    @Test
    public void testA_initialState() throws InterruptedException {
        verifyThat("#table", isVisible());
        verifyThat("#addShipmentBtn", isEnabled());
        verifyThat("#removeShipmentBtn", isDisabled());
        verifyThat("#applyDateFilterButton", isEnabled());
        assertNull(fromDatePicker.getValue());
        assertNull(toDatePicker.getValue());
    }

    @Test
    public void testB_updateSenderColumn_InvalidPath() throws InterruptedException {
        Thread.sleep(5000);
        Envio targetEnvio = table.getItems().get(0);
        int antiguoValor = targetEnvio.getNumPaquetes();
        int initialCount = table.getItems().size();
        assertFalse("Table empty - no routes found", table.getItems().isEmpty());
        TableRow<Envio> row = lookup(".table-row-cell").nth(5)
                .query();
        Node numPaquetesColumn = lookup(".table-cell").nth(4).query();
        doubleClickOn(numPaquetesColumn);
        write("5");
        press(KeyCode.ENTER);
        WaitForAsyncUtils.waitForFxEvents();

        FxAssert.verifyThat(window("Error al actualizar estado"), WindowMatchers.isShowing());

        // Encontrar y hacer clic en el botón "OK" de la alerta
        Button alertOkButton = lookup(".dialog-pane .button").queryButton();
        clickOn(alertOkButton);
        WaitForAsyncUtils.waitForFxEvents();

        // Verificar que el valor del remitente vuelve al original
        assertEquals("El remitente debe volver a su valor original", antiguoValor, targetEnvio.getNumPaquetes().toString());

        // Verificar que la cantidad de filas no ha cambiado
        assertEquals("El número de envíos no debe cambiar", initialCount, table.getItems().size());

        // Verificar que la tabla sigue mostrando datos
        assertNotNull("Los datos de la tabla deben persistir", table.getItems());
    }
}
