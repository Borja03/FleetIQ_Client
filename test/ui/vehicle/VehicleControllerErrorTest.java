package ui.vehicle;

import application.EnvioMain;
import application.RutaMain;
import application.VehiculoMain;
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
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import factories.EnvioFactory;
import factories.PaqueteFactory;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.input.KeyCode;
import logicInterface.EnvioManager;
import logicInterface.PaqueteManager;
import models.Envio;
import models.Vehiculo;
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
import service.EnvioRESTClient;

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
    int initialCount ;

    @Override
    public void start(Stage stage) throws Exception {
        new VehiculoMain().start(stage);

        // Obtener referencias a los componentes de la UI
        vehicleTableView = lookup("#vehicleTableView").query();
        removeShipmentBtn = lookup("#removeShipmentBtn").query();
        searchButton = lookup("#searchButton").query();
        addShipmentBtn = lookup("#addShipmentBtn").query();
        fromDatePicker = lookup("#fromDatePicker").query();
        toDatePicker = lookup("#toDatePicker").query();
        searchTextField = lookup("#searchTextField").query();
        applyFilterButton = (JFXButton) lookup("#applyFilterButton").queryButton();
        initialCount=vehicleTableView.getItems().size();
    }

    @Test
    public void testA_addEnvio_InvalidPath() throws InterruptedException {
        Thread.sleep(5000);
        // Contar el número de envíos antes de añadir uno nuevo
        // Hacer clic en el botón "Añadir"
        clickOn(addShipmentBtn);
        WaitForAsyncUtils.waitForFxEvents();
        // Verificar que se muestra la alerta de error del servidor
        verifyThat("An error occurred while filtering vehicles. Please try again later.", isVisible());

        // Encontrar y hacer clic en el botón "OK" de la alerta
        Button alertOkButton = lookup(".dialog-pane .button").queryButton();
        clickOn(alertOkButton);
        WaitForAsyncUtils.waitForFxEvents();

    }

    @Test
public void testB_deleteVehicle_InvalidPath() throws InterruptedException {
    Thread.sleep(5000);
    
    // Contar el número de envíos antes de eliminar
    assertFalse("No hay vehículos disponibles para eliminar", initialCount == 0);

    // Seleccionar la primera fila en la tabla
    TableRow<Vehiculo> row = lookup(".table-row-cell").nth(0).query();
    clickOn(row);

    // Hacer clic en el botón "Eliminar"
    clickOn("#removeShipmentBtn");
    WaitForAsyncUtils.waitForFxEvents();

    // Encontrar y hacer clic en el segundo botón del cuadro de diálogo
    Button alertOkButton = lookup(".dialog-pane .button").nth(1).queryButton();
    clickOn(alertOkButton);
    WaitForAsyncUtils.waitForFxEvents();

    // Confirmar el cuadro de diálogo de eliminación
    DialogPane confirmationDialog = lookup(".dialog-pane").query();
    
    verifyThat("An unexpected error occurred while deleting vehicles and their associated data.", isVisible());
    
   
}


    @Test
    public void testC_editMatricula() throws InterruptedException {
        Thread.sleep(5000);
        // Wait for data to load
        WaitForAsyncUtils.waitForFxEvents();

        // Verify table has data
        assertFalse("Table empty - no vehicles found", vehicleTableView.getItems().isEmpty());
        TableRow<Vehiculo> row = lookup(".table-row-cell").nth(0).query(); // Cambiado a 0 para la primera fila
        TableColumn<Vehiculo, ?> origenColumn = vehicleTableView.getColumns().get(1);
        interact(() -> {
            doubleClickOn(row.getChildrenUnmodifiable().get(1));
        });

        // Enter new value
        write("PAU123");
        press(KeyCode.ENTER);
        WaitForAsyncUtils.waitForFxEvents();

        verifyThat("Inténtalo de nuevo más tarde.", isVisible());
    }

    @Test
    public void testSearchByLocalizador_connectionRefusedAlert() throws InterruptedException {
        // Espera a que la UI se estabilice
        Thread.sleep(5000);

        // Ingresar un valor válido en el campo de búsqueda
        interact(() -> {
            searchTextField.setText("PAU");
        });

        // Simular el clic en el botón que dispara la búsqueda.
        // Se asume que dicho botón tiene fx:id "searchButton" y está correctamente referenciado.
        clickOn(searchButton);
        WaitForAsyncUtils.waitForFxEvents();

        // Verificar que se muestre la alerta con el mensaje esperado.
        // Dado que en el bloque 'catch (Exception e)' se llama a:
        // showAlert("Ruta no encontrada", e.getMessage());
        // se espera que el contenido de la alerta muestre el mensaje de la excepción.
        verifyThat("An error occurred while filtering vehicles. Please try again later.", isVisible());

        // Opcional: cerrar la alerta para dejar el estado de la UI limpio.
        Button alertOkButton = lookup(".dialog-pane .button").queryButton();
        clickOn(alertOkButton);
        WaitForAsyncUtils.waitForFxEvents();
    }

}
