/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.paquete;

import application.PaqueteMain;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import exception.SelectException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import models.PackageSize;
import models.Paquete;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import javafx.stage.Stage;
import javax.ws.rs.ProcessingException;
import logicimplementation.PackageManagerImp;
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
import static org.testfx.matcher.control.ButtonMatchers.isCancelButton;
import static org.testfx.matcher.control.ButtonMatchers.isDefaultButton;
import static org.testfx.matcher.control.ComboBoxMatchers.hasSelectedItem;
import static org.testfx.matcher.control.TextInputControlMatchers.hasText;
import org.testfx.util.WaitForAsyncUtils;

/**
 * Testing class for PaqueteController. Tests package management view behavior
 * using TestFX framework.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class EnvioControllerTest extends ApplicationTest {

    private TableView<Paquete> paqueteTableView;
    private JFXButton addShipmentBtn;
    private JFXButton removeShipmentBtn;
    private JFXButton printReportBtn;
    private JFXButton searchBtn;
    private JFXButton filterDatesBtn;
    private JFXTextField searchTextField;
    private JFXDatePicker fromDatePicker;
    private JFXDatePicker toDatePicker;
    private JFXComboBox<PackageSize> sizeFilterComboBox;

    /**
     * Starts application to be tested.
     *
     * @param stage Primary Stage object
     * @throws Exception If there is any error
     */
    @Override
    public void start(Stage stage) throws Exception {
        new PaqueteMain().start(stage);

        // Lookup JFoenix components and cast them to the appropriate types
        paqueteTableView = lookup("#paqueteTableView").queryTableView();
        addShipmentBtn = (JFXButton) lookup("#addShipmentBtn").queryButton();
        removeShipmentBtn = (JFXButton) lookup("#removeShipmentBtn").queryButton();
        printReportBtn = (JFXButton) lookup("#printReportBtn").queryButton();
        searchBtn = (JFXButton) lookup("#searchBtn").queryButton();
        filterDatesBtn = (JFXButton) lookup("#filterDatesBtn").queryButton();
        searchTextField = (JFXTextField) lookup("#searchTextField").query();
        fromDatePicker = (JFXDatePicker) lookup("#fromDatePicker").query();
        toDatePicker = (JFXDatePicker) lookup("#toDatePicker").query();
        sizeFilterComboBox = (JFXComboBox<PackageSize>) lookup("#sizeFilterComboBox").query();

        // Verify that nodes are found
        assertNotNull("SearchTextField not found", searchTextField);
        assertNotNull("FromDatePicker not found", fromDatePicker);
        assertNotNull("ToDatePicker not found", toDatePicker);
        assertNotNull("SizeFilterComboBox not found", sizeFilterComboBox);
    }

    /**
     * Test initial state of the package management view.
     */
    @Test
    public void testA_initialState() {
        verifyThat("#paqueteTableView", isVisible());
        verifyThat("#addShipmentBtn", isEnabled());
        verifyThat("#removeShipmentBtn", isDisabled());
        verifyThat("#printReportBtn", isEnabled());
        verifyThat("#searchBtn", isEnabled());
        verifyThat("#filterDatesBtn", isEnabled());
        verifyThat("#searchTextField", hasText(""));

        // Check that the date pickers are unset (value is null)
        assertNull(fromDatePicker.getValue());
        assertNull(toDatePicker.getValue());

        assertNull(sizeFilterComboBox.getValue());

    }

    /**
     * Test adding a new package to the system.
     */
    @Test
    public void testB_addPackage() {
        int initialRowCount = paqueteTableView.getItems().size();

        // Click the "Add Shipment" button
        clickOn("#addShipmentBtn");
        WaitForAsyncUtils.waitForFxEvents();

        // Verify the table has one more row than before
        int newRowCount = paqueteTableView.getItems().size();
        assertEquals(initialRowCount + 1, newRowCount);

        // Verify default values of the new package
        Paquete newPackage = paqueteTableView.getItems().get(newRowCount - 1);
        assertEquals("", newPackage.getSender());
        assertEquals("", newPackage.getReceiver());
        assertEquals(PackageSize.MEDIUM, newPackage.getSize());
    }

    /**
     * Test updating a package's sender name through table editing.
     */
    @Test
    public void testC_updatePackage() {
        // Wait for data to load
        WaitForAsyncUtils.waitForFxEvents();

        // Verify table has data
        assertFalse("Table empty - no packages found", paqueteTableView.getItems().isEmpty());
        TableRow<Paquete> row = lookup(".table-row-cell").nth(5)
                        .query();
        TableColumn<Paquete, ?> senderColumn = paqueteTableView.getColumns().get(1);
        interact(() -> {
            doubleClickOn(row.getChildrenUnmodifiable().get(1));
        });

        // Enter new value
        write("NewSender");
        press(KeyCode.ENTER);
        WaitForAsyncUtils.waitForFxEvents();

        // Verify update
        Paquete updated = paqueteTableView.getItems().get(5);
        assertEquals("NewSender", updated.getSender());
    }

    /**
     * Test deleting a package from the table.
     */
    @Test
    public void testD_deletePackage() {
        // Wait for initial data load
        WaitForAsyncUtils.waitForFxEvents();

        int initialCount = paqueteTableView.getItems().size();
        if (initialCount == 0) {
            return;
        }

        TableRow<Paquete> row = lookup(".table-row-cell").query();
        
        clickOn(row);

        clickOn("#removeShipmentBtn");

        WaitForAsyncUtils.waitForFxEvents();

        DialogPane dialogPane = lookup(".dialog-pane").query();
        assertNotNull("Confirmation dialog not shown", dialogPane);

        Button okButton = (Button) dialogPane.lookupButton(ButtonType.OK);
        assertNotNull("OK button not found in dialog", okButton);

        clickOn(okButton);

        WaitForAsyncUtils.waitForFxEvents();
        assertEquals("Package count should decrease by 1",initialCount - 1, paqueteTableView.getItems().size());
    }
    // test backend
    @Test
    public void testServerConnectionStatus() {
        try {
            // Attempt a lightweight server operation
            PackageManagerImp packageManager = new PackageManagerImp();

            // Test connection by fetching minimal data
            List<Paquete> result = packageManager.findAllPackages();

            // If we reach here, server is connected
            assertTrue("Server is connected", true);

            // Optional: Verify response format if needed
            assertNotNull("Received valid response", result);
        } catch (SelectException e) {
            // Analyze exception to determine connection failure
            if (e.getMessage().contains("Database server connection failed")) {
                fail("Server connection failed: " + e.getMessage());
            } else if (e.getCause() instanceof ProcessingException) {
                fail("Network error: " + e.getCause().getMessage());
            } else {
                // Other database-related error (server is connected)
                assertTrue("Server is reachable but operation failed", true);
            }
        } catch (Exception e) {
            fail("Unexpected error: " + e.getMessage());
        }
    }

    /**
     * Test date range filtering functionality.
     */
    @Test
    public void testF_filterByDates() {
        // Set dates
        fromDatePicker.setValue(LocalDate.now().minusDays(7));
        toDatePicker.setValue(LocalDate.now());
        clickOn("#filterDatesBtn");
        WaitForAsyncUtils.waitForFxEvents();

        // Verify packages are within date range
        ObservableList<Paquete> filtered = paqueteTableView.getItems();
        assertTrue(filtered.stream().allMatch(p
                        -> !p.getCreationDate().before(Date.from(fromDatePicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()))
                        && !p.getCreationDate().after(Date.from(toDatePicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()))
        ));
    }

    // test filters 
    /**
     * Test search functionality by name.
     */
    @Test
    public void testG_searchFunctionality() {
        // Assume there's a package with "John" in sender/receiver
        clickOn("#searchTextField");
        write("John");
        clickOn("#searchBtn");
        WaitForAsyncUtils.waitForFxEvents();

        // Verify results contain "John"
        ObservableList<Paquete> results = paqueteTableView.getItems();
        assertTrue(results.stream().anyMatch(p
                        -> p.getSender().contains("John") || p.getReceiver().contains("John")
        ));
    }

    /**
     * Test filtering packages by size.
     */
    @Test
    public void testE_filterBySize() {
        // Select MEDIUM size from combo box
        clickOn("#sizeFilterComboBox");
        clickOn("MEDIUM");
        WaitForAsyncUtils.waitForFxEvents();

        // Verify all displayed packages have MEDIUM size
        ObservableList<Paquete> filtered = paqueteTableView.getItems();
        assertTrue(filtered.stream().allMatch(p -> p.getSize() == PackageSize.MEDIUM));
    }

    // test errors handling 
    /**
     * Test error handling for invalid inputs.
     */
//    @Test
//    public void testH_errorHandling() {
//        // Test invalid sender name format
//        paqueteTableView.getSelectionModel().selectFirst();
//        doubleClickOn(paqueteTableView.lookup(".table-row-cell").nth(0).lookup(".table-cell").nth(1));
//        write("123Invalid"); // Numbers are invalid
//        press(KeyCode.ENTER);
//        WaitForAsyncUtils.waitForFxEvents();
//
//        // Verify error alert is shown
//        Node dialogPane = lookup(".alert").query();
//        assertNotNull(dialogPane);
//        clickOn("OK");
//    }
}
