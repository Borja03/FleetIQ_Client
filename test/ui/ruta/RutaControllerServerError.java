package ui.ruta;

import application.EnvioMain;
import application.RutaMain;
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
import models.Ruta;
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
public class RutaControllerServerError extends ApplicationTest {

    private TableView<Ruta> table;
    private JFXButton removeShipmentBtn;
    private JFXButton addShipmentBtn;
    private JFXDatePicker fromDatePicker;
    private JFXDatePicker toDatePicker;
    private JFXButton applyDateFilterButton,searchButton;
    private JFXTextField searchTextField;
    int initialCount;

    @Override
    public void start(Stage stage) throws Exception {
        new RutaMain().start(stage);

        // Obtener referencias a los componentes de la UI
        table = lookup("#rutaTable").query();
        removeShipmentBtn = lookup("#removeShipmentBtn").query();
        searchButton =  lookup("#searchButton").query();
        addShipmentBtn = lookup("#addShipmentBtn").query();
        fromDatePicker = lookup("#fromDatePicker").query();
        toDatePicker = lookup("#toDatePicker").query();
        searchTextField = lookup("#searchTextField").query();
        applyDateFilterButton = (JFXButton) lookup("#applyFilterButton").queryButton();
        initialCount = table.getItems().size();
    }

//    @Test
//    public void testH_tableSelection_showsErrorAlert() {
//        // Simula la acción que, al no haber servidor, genera el error y muestra el Alert.
//        Node row = lookup(".table-row-cell").nth(0).query();
//        clickOn(row);
//
//        // Si la aparición del Alert es asíncrona, es posible que convenga esperar a que se renderice.
//        // Por ejemplo: WaitForAsyncUtils.waitForFxEvents();
//        // Verifica que se muestra el Alert (la raíz del Alert es un DialogPane con estilo "dialog-pane")
//        verifyThat(".dialog-pane", (DialogPane dialogPane)
//                -> dialogPane.getContentText().contains("Ocurrió un error inesperado."));
//    }

    //Seleccionar el test a ejecutar con right click y seleccionando run focused test method
    @Test
    public void testA_addRuta_InvalidPath() throws InterruptedException {
        Thread.sleep(5000);
        // Contar el número de envíos antes de añadir uno nuevo
        int initialCount = table.getItems().size();
        // Hacer clic en el botón "Añadir"
        clickOn(addShipmentBtn);
        WaitForAsyncUtils.waitForFxEvents();
        // Verificar que se muestra la alerta de error del servidor
        verifyThat("No se pudo añadir la nueva ruta.", isVisible());

        // Encontrar y hacer clic en el botón "OK" de la alerta
        Button alertOkButton = lookup(".dialog-pane .button").queryButton();
        clickOn(alertOkButton);
        WaitForAsyncUtils.waitForFxEvents();

    }

    @Test
    public void testB_deleteRuta_InvalidPath() throws InterruptedException {
        Thread.sleep(5000);
        // Contar el número de envíos antes de eliminar
        assertFalse("No hay rutas disponibles para eliminar", initialCount == 0);

        // Seleccionar la primera fila en la tabla
        TableRow<Ruta> row = lookup(".table-row-cell").nth(0).query();
        clickOn(row);

        // Hacer clic en el botón "Eliminar"
        clickOn("#removeShipmentBtn");
        WaitForAsyncUtils.waitForFxEvents();
        Button alertOkButton = lookup(".dialog-pane .button").queryButton();
        clickOn(alertOkButton);
        WaitForAsyncUtils.waitForFxEvents();

        // Confirm the deletion dialog
        DialogPane confirmationDialog = lookup(".dialog-pane").query();

        verifyThat("Error inesperado al eliminar rutas.", isVisible());
        // Encontrar y hacer clic en el botón "OK" de la alerta
        alertOkButton = lookup(".dialog-pane .button").queryButton();
        clickOn(alertOkButton);
        WaitForAsyncUtils.waitForFxEvents();

        // Confirmar el cuadro de diálogo de eliminación
    }

    @Test
    public void testC_editOrigen() throws InterruptedException {
        Thread.sleep(5000);
        // Wait for data to load
        WaitForAsyncUtils.waitForFxEvents();

        // Verify table has data
        assertFalse("Table empty - no routes found", table.getItems().isEmpty());
        TableRow<Ruta> row = lookup(".table-row-cell").nth(0).query(); // Cambiado a 0 para la primera fila
        TableColumn<Ruta, ?> origenColumn = table.getColumns().get(1);
        interact(() -> {
            doubleClickOn(row.getChildrenUnmodifiable().get(1));
        });

        // Enter new value
        write("NewOrigen");
        press(KeyCode.ENTER);
        WaitForAsyncUtils.waitForFxEvents();

        verifyThat("No se pudo actualizar el origen.", isVisible());
    }

    @Test
    public void testSearchByLocalizador_connectionRefusedAlert() throws InterruptedException {
        // Espera a que la UI se estabilice
        Thread.sleep(5000);

        // Ingresar un valor válido en el campo de búsqueda
        interact(() -> {
            searchTextField.setText("1234");
        });

        
        // Simular el clic en el botón que dispara la búsqueda.
        // Se asume que dicho botón tiene fx:id "searchButton" y está correctamente referenciado.
        clickOn(searchButton);
        WaitForAsyncUtils.waitForFxEvents();
        

        // Verificar que se muestre la alerta con el mensaje esperado.
        // Dado que en el bloque 'catch (Exception e)' se llama a:
        // showAlert("Ruta no encontrada", e.getMessage());
        // se espera que el contenido de la alerta muestre el mensaje de la excepción.
        verifyThat("java.net.ConnectException: Connection refused: connect", isVisible());

        // Opcional: cerrar la alerta para dejar el estado de la UI limpio.
        Button alertOkButton = lookup(".dialog-pane .button").queryButton();
        clickOn(alertOkButton);
        WaitForAsyncUtils.waitForFxEvents();
    }
//
//    @Test
//    public void testFilterByDates_invalidPath() throws InterruptedException {
//        // Espera a que la interfaz se estabilice
//        Thread.sleep(5000);
//
//        // Se asume que 'rutaTable' es el TableView que muestra las rutas
//        int initialCount = table.getItems().size();
//
//        // Establece los valores en los DatePickers
//        interact(() -> {
//            fromDatePicker.setValue(LocalDate.of(2024, 1, 1));
//            toDatePicker.setValue(LocalDate.of(2024, 12, 31));
//        });
//
//        // Simula el clic en el botón que aplica el filtro de fechas
//        clickOn(applyDateFilterButton);
//        WaitForAsyncUtils.waitForFxEvents();
//
//        // Verifica que se muestre la alerta con el mensaje de error (en este caso, el contenido mostrado es "Error al filtrar por fechas")
//        verifyThat("Error al filtrar por fechas", isVisible());
//
//        // Localiza y cierra la alerta (se asume que el botón de OK está identificado dentro del '.dialog-pane')
//        Button alertOkButton = lookup(".dialog-pane .button").queryButton();
//        clickOn(alertOkButton);
//        WaitForAsyncUtils.waitForFxEvents();
//
//        // Verifica que, tras el error, el contenido de la tabla no se haya modificado
//        assertEquals("La cantidad de rutas no debe cambiar tras un error en el filtrado",
//                initialCount, table.getItems().size());
//
//        // Verifica que la tabla siga mostrando datos
//        assertNotNull("Los datos de la tabla deben persistir", table.getItems());
//    }
//    @Test
//    public void testD_updateSenderColumn_InvalidPath() throws InterruptedException {
//        Thread.sleep(5000);
//        Ruta targetRuta = table.getItems().get(0);
//        int antiguoValor = targetRuta.getNumPaquetes();
//        int initialCount = table.getItems().size();
//        assertFalse("Table empty - no routes found", table.getItems().isEmpty());
//        TableRow<Envio> row = lookup(".table-row-cell").nth(5)
//                .query();
//        Node numPaquetesColumn = lookup(".table-cell").nth(4).query();
//        doubleClickOn(numPaquetesColumn);
//        write("5");
//        press(KeyCode.ENTER);
//        WaitForAsyncUtils.waitForFxEvents();
//
//        verifyThat("Already connected", isVisible());
//        // Encontrar y hacer clic en el botón "OK" de la alerta
//        Button alertOkButton = lookup(".dialog-pane .button").queryButton();
//        clickOn(alertOkButton);
//        WaitForAsyncUtils.waitForFxEvents();
//
//        // Verificar que el valor del remitente vuelve al original
//        assertEquals("El remitente debe volver a su valor original", antiguoValor, targetEnvio.getNumPaquetes().toString());
//
//        // Verificar que la cantidad de filas no ha cambiado
//        assertEquals("El número de envíos no debe cambiar", initialCount, table.getItems().size());
//
//        // Verificar que la tabla sigue mostrando datos
//        assertNotNull("Los datos de la tabla deben persistir", table.getItems());
//    }
}
