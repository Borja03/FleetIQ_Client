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
public class EnvioControllerFilterServerError extends ApplicationTest {

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
    public void testB_filterByDateRange_invalidPath() throws InterruptedException {
        Thread.sleep(5000);
        int initialCount = table.getItems().size();
        interact(() -> {
            fromDatePicker.setValue(LocalDate.of(2024, 1, 1));
            toDatePicker.setValue(LocalDate.of(2024, 12, 31));
        });
        clickOn(applyDateFilterButton);
        WaitForAsyncUtils.waitForFxEvents();

        FxAssert.verifyThat(window("Error al filtrar por fechas"), WindowMatchers.isShowing());

        assertEquals("El número de envíos no debe cambiar", initialCount, table.getItems().size());

        // Verificar que la tabla sigue mostrando datos
        assertNotNull("Los datos de la tabla deben persistir", table.getItems());
    }
}
