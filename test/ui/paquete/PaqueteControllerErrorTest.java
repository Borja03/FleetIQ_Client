package ui.paquete;

import application.PaqueteMain;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import models.Paquete;
import org.junit.Test;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;
import service.PackageRESTClient;

import static org.junit.Assert.*;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.base.NodeMatchers.isVisible;

/**
 * TestFX-based CRUD tests simulating server failure (wrong API path).
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PaqueteControllerErrorTest extends ApplicationTest {

    private TableView<Paquete> paqueteTableView;
    private JFXButton addShipmentBtn;
    private JFXButton removeShipmentBtn;
    private JFXButton searchBtn;
    private JFXTextField searchTextField;

    private static final String VALID_API_PATH = "http://localhost:8080/FleetIQ_Server/webresources";
    private static final String INVALID_API_PATH = "http://localhost:8080/api/non_existent_path";

    @Override
    public void start(Stage stage) throws Exception {
        new PaqueteMain().start(stage);

        paqueteTableView = lookup("#paqueteTableView").queryTableView();
        addShipmentBtn = (JFXButton) lookup("#addShipmentBtn").queryButton();
        removeShipmentBtn = (JFXButton) lookup("#removeShipmentBtn").queryButton();
        searchBtn = (JFXButton) lookup("#searchBtn").queryButton();
        searchTextField = lookup("#searchTextField").query();

        // Ensure TableView is properly initialized
        assertNotNull("TableView is not initialized!", paqueteTableView);
        assertNotNull("TableView items list is null!", paqueteTableView.getItems());
    }

    /**
     * Test fetching packages when the server path is incorrect.
     */
    @Test
    public void testA_listPackagesServerFailure() {
        int initialSize = paqueteTableView.getItems().size();

        // Simulate server failure
        PackageRESTClient.setBASE_URI(INVALID_API_PATH);

        clickOn("#searchBtn");
        WaitForAsyncUtils.waitForFxEvents();
        sleep(1000); // Ensure UI updates

        // Table should remain unchanged
        assertEquals("Table should not update when server fails", initialSize, paqueteTableView.getItems().size());

        verifyAndDismissAlert();

        // Restore correct API path
        PackageRESTClient.setBASE_URI(VALID_API_PATH);
    }

    /**
     * Test adding a package when the server path is incorrect.
     */
    @Test
    public void testB_addPackageServerFailure() {
        PackageRESTClient.setBASE_URI(INVALID_API_PATH);

        clickOn("#addShipmentBtn");
        WaitForAsyncUtils.waitForFxEvents();
        sleep(1000); // Ensure UI updates

        verifyAndDismissAlert();

        // Restore correct API path
        PackageRESTClient.setBASE_URI(VALID_API_PATH);
    }

    /**
     * Test updating a package when the server path is incorrect.
     */
    @Test
    public void testC_updatePackageServerFailure() {
        ObservableList<Paquete> items = paqueteTableView.getItems();
        if (items.isEmpty()) {
            System.out.println("Skipping update test, no data available.");
            return;
        }

        clickOn(".table-row-cell"); // Select first row
        clickOn("#editButton"); // Assuming there is an edit button
        write("Updated Sender");
        type(KeyCode.ENTER);

        PackageRESTClient.setBASE_URI(INVALID_API_PATH);
        clickOn("#saveButton");
        WaitForAsyncUtils.waitForFxEvents();
        sleep(1000); // Ensure UI updates

        verifyAndDismissAlert();

        // Restore correct API path
        PackageRESTClient.setBASE_URI(VALID_API_PATH);
    }

    /**
     * Test deleting a package when the server path is incorrect.
     */
    @Test
    public void testD_deletePackageServerFailure() {
        ObservableList<Paquete> items = paqueteTableView.getItems();
        if (items.isEmpty()) {
            System.out.println("Skipping delete test, no data available.");
            return;
        }

        clickOn(".table-row-cell"); // Select first row
        clickOn("#removeShipmentBtn");

        DialogPane dialogPane = lookup(".dialog-pane").query();
        assertNotNull("Confirmation dialog not shown", dialogPane);

        Button okButton = (Button) dialogPane.lookupButton(ButtonType.OK);
        clickOn(okButton);

        PackageRESTClient.setBASE_URI(INVALID_API_PATH);
        WaitForAsyncUtils.waitForFxEvents();
        sleep(1000); // Ensure UI updates

        verifyAndDismissAlert();

        // Restore correct API path
        PackageRESTClient.setBASE_URI(VALID_API_PATH);
    }

    /**
     * Verifies if an alert is visible and dismisses it.
     */
    private void verifyAndDismissAlert() {
        if (lookup(".alert").tryQuery().isPresent()) {
            verifyThat(".alert", isVisible());

            try {
                DialogPane alertPane = lookup(".alert").query();
                Button okButton = (Button) alertPane.lookupButton(ButtonType.OK);
                clickOn(okButton);
                WaitForAsyncUtils.waitForFxEvents();
            } catch (Exception e) {
                fail("Alert was expected but could not be dismissed!");
            }
        } else {
            fail("Expected alert did not appear!");
        }
    }
}
