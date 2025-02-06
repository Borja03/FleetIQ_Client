package testController;

import application.Main;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.matcher.control.TableViewMatchers;

import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.base.NodeMatchers.isEnabled;
import static org.testfx.matcher.base.NodeMatchers.isDisabled;

@RunWith(org.junit.runners.JUnit4.class)
public class EnvioControllerIT extends ApplicationTest {

    private TableView table;
    private Button btnAddEnvio;
    private Button btnRemoveEnvio;

    @Override
    public void start(Stage stage) throws Exception {
        Main main = new Main();
        main.start(stage);
        stage.show();

        // Esperar a que la UI se cargue
        sleep(1000);

        // Buscar y asignar los elementos de la interfaz
        table = lookup("#table").query();
        btnAddEnvio = lookup("#btnAddEnvio").query();
        btnRemoveEnvio = lookup("#btnRemoveEnvio").query();

        // Depuración: Verificar si los nodos fueron encontrados
        System.out.println("Table: " + table);
        System.out.println("Button Add: " + btnAddEnvio);
        System.out.println("Button Remove: " + btnRemoveEnvio);
    }

    @Test
    public void testA_initialState() {
        verifyThat(btnAddEnvio, isEnabled());
        verifyThat(btnRemoveEnvio, isDisabled());
        verifyThat(table, TableViewMatchers.hasNumRows(0));
    }

    @Test
    public void testB_addEnvio() {
        int initialRowCount = table.getItems().size();
        clickOn(btnAddEnvio);
        verifyThat(table, TableViewMatchers.hasNumRows(initialRowCount + 1));
    }

    @Test
    public void testC_removeEnvio() {
        int initialRowCount = table.getItems().size();
        if (initialRowCount == 0) {
            clickOn(btnAddEnvio);
            initialRowCount = 1;
        }

        // Seleccionar la primera fila
        clickOn(table).clickOn(table.lookup(".table-row-cell"));

        clickOn(btnRemoveEnvio);
        verifyThat(table, TableViewMatchers.hasNumRows(initialRowCount - 1));
    }
}
