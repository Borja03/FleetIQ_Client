package ui.paquete;

import application.PaqueteMain;
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
import exception.DeleteException;
import factories.PaqueteFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeoutException;
import java.util.function.Predicate;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableRow;
import javafx.scene.input.KeyCode;
import logicInterface.PaqueteManager;
import logicimplementation.PackageManagerImp;
import models.PackageSize;
import models.Paquete;
import org.junit.After;
import org.junit.Before;
import org.testfx.api.FxAssert;
import static org.testfx.api.FxAssert.verifyThat;
import org.testfx.api.FxToolkit;
import static org.testfx.matcher.base.NodeMatchers.isVisible;
import org.testfx.matcher.base.WindowMatchers;
import service.PackageRESTClient;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PaqueteControllerServerError extends ApplicationTest {

    private TableView<Paquete> paqueteTableView;
    private JFXButton removeShipmentBtn;
    private JFXButton addShipmentBtn;
    private static final String VALID_API_PATH = "http://localhost:8080/FleetIQ_Server/webresources";
    private static final String INVALID_API_PATH = "http://localhost:9090/api/wrong_path";

    @Override
    public void start(Stage stage) throws Exception {
        // Initialize your JavaFX UI here (e.g., load FXML, inject components)
        new PaqueteMain().start(stage);

        // Get references to UI components
        paqueteTableView = lookup("#paqueteTableView").query();
        removeShipmentBtn = lookup("#removeShipmentBtn").query();
        addShipmentBtn = lookup("#addShipmentBtn").query();

        // Inject INVALID API path to simulate server failure
        PaqueteManager validManager = new PackageManagerImp(new PackageRESTClient(VALID_API_PATH));
        PaqueteFactory.setPackageInstance(validManager);
    }

    @Before
    public void setup() {
        // Reset the REST client configuration before each test
        PaqueteFactory.setPackageInstance(null);
        // use invalid API path to force server error
        PaqueteManager invalidManager = new PackageManagerImp(new PackageRESTClient(INVALID_API_PATH));
        PaqueteFactory.setPackageInstance(invalidManager);
    }

    @After
    public void cleanup() {
        // Reset PaqueteManager to a valid state
        PaqueteManager validManager = new PackageManagerImp(new PackageRESTClient(VALID_API_PATH));
        PaqueteFactory.setPackageInstance(validManager);
    }

    @Test
    public void testA_deletePackage_InvalidPath() {
        // use invalid API path to force server error
//        PaqueteManager invalidManager = new PackageManagerImp(new PackageRESTClient(INVALID_API_PATH));
//        PaqueteFactory.setPackageInstance(invalidManager);
        // Initial package count
        int initialCount = paqueteTableView.getItems().size();
        assertFalse("No packages available to delete", initialCount == 0);

        // Select the first row in the table
        TableRow<Paquete> row = lookup(".table-row-cell").nth(0).query();
        clickOn(row);

        // Click the "Delete" button
        clickOn("#removeShipmentBtn");
        WaitForAsyncUtils.waitForFxEvents();

        // Confirm the deletion dialog
        DialogPane confirmationDialog = lookup(".dialog-pane").query();
        Button okButton = (Button) confirmationDialog.lookupButton(ButtonType.OK);
        clickOn(okButton);
        WaitForAsyncUtils.waitForFxEvents();

        // Verify the error alert window is showing
        FxAssert.verifyThat(window("Server Error"), WindowMatchers.isShowing());

        // Ensure the package count remains the same
        assertEquals("Package count should NOT change", initialCount,
                        paqueteTableView.getItems().size());
    }

    @Test
    public void testA_addPackage_InvalidPath() {
        // use invalid API path to force server error
//        PaqueteManager invalidManager = new PackageManagerImp(new PackageRESTClient(INVALID_API_PATH));
//        PaqueteFactory.setPackageInstance(invalidManager);

        // Initial package count
        int initialCount = paqueteTableView.getItems().size();

        // Click the "Add" button
        clickOn(addShipmentBtn);
        WaitForAsyncUtils.waitForFxEvents();

        // Verify error alert
        // Verify the error alert window is showing
        FxAssert.verifyThat(window("Server Error"), WindowMatchers.isShowing());

        // Verify the error alert window is showing
        FxAssert.verifyThat(window("Server Error"), WindowMatchers.isShowing());

        // Find and click the OK button in the alert
        Button alertOkButton = lookup(".dialog-pane .button").queryButton();
        clickOn(alertOkButton);
        WaitForAsyncUtils.waitForFxEvents();

        // Ensure the table count did NOT change
        assertEquals("Package count should NOT change", initialCount, paqueteTableView.getItems().size()
        );
    }

    @Test
    public void testD_updateSenderColumn_InvalidPath() {
        // use invalid API path to force server error
        // aqueteManager invalidManager = new PackageManagerImp(new PackageRESTClient(INVALID_API_PATH));
        //PaqueteFactory.setPackageInstance(invalidManager);

        // Get initial data from first row
        Paquete targetPaquete = paqueteTableView.getItems().get(0);
        String originalSender = targetPaquete.getSender();
        int initialCount = paqueteTableView.getItems().size();

        // Double-click sender cell to start editing
        TableRow<Paquete> row = lookup(".table-row-cell").nth(0).query();
        doubleClickOn(row.getChildrenUnmodifiable().get(1)); // Assuming sender is first column

        // Enter valid format but trigger server error
        write("ValidButFails");  // Valid format but server unreachable
        press(KeyCode.ENTER);
        WaitForAsyncUtils.waitForFxEvents();

        // Verify server error alert
        // Verify the error alert window is showing
        FxAssert.verifyThat(window("Server Error"), WindowMatchers.isShowing());

        // Find and click the OK button in the alert
        Button alertOkButton = lookup(".dialog-pane .button").queryButton();
        clickOn(alertOkButton);
        WaitForAsyncUtils.waitForFxEvents();

        // Verify value reverted
        assertEquals("Sender value should revert to original", originalSender, targetPaquete.getSender());

        // Verify table remains consistent
        assertEquals("Row count should not change", initialCount, paqueteTableView.getItems().size());

        // Verify table refresh
        assertNotNull("Table data should persist", paqueteTableView.getItems());
    }

}
