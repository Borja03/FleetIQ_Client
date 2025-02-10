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
import javax.ws.rs.core.GenericType;
import logicimplementation.PackageManagerImp;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
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
import service.PackageRESTClient;

/**
 * Testing class for PaqueteController. Tests package management view behavior
 * using TestFX framework.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PaqueteControllerTestOK extends ApplicationTest {

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

    @Test
    public void testA_readPackages() {
        WaitForAsyncUtils.waitForFxEvents(); // Ensure UI updates

        // Ensure table is visible and not empty
        verifyThat("#paqueteTableView", isVisible());
        assertFalse("TableView should contain data", paqueteTableView.getItems().isEmpty());

        // Select the first package
        Paquete firstPackage = paqueteTableView.getItems().get(0);
        assertNotNull("First package should not be null", firstPackage);

    }

    /**
     * Test adding a new package to the system.
     */
    @Test
    public void testB_addPackage() {
        // Get initial state
        int initialRowCount = paqueteTableView.getItems().size();
        Paquete lastPackage = paqueteTableView.getItems().get(initialRowCount - 1);
        Paquete p = new Paquete();
        // Click the "Add Shipment" button
        clickOn("#addShipmentBtn");
        WaitForAsyncUtils.waitForFxEvents();
        // Check if dialog appears
        DialogPane dialogPane = lookup(".dialog-pane").query();
        assertNotNull("Dialog not shown after adding a package", dialogPane);
        // Click "OK" to confirm the addition
        Button okButton = (Button) dialogPane.lookupButton(ButtonType.OK);
        assertNotNull("OK button not found in dialog", okButton);
        clickOn(okButton);
        WaitForAsyncUtils.waitForFxEvents();
        // Verify row count increased
        int newRowCount = paqueteTableView.getItems().size();
        assertEquals("Package count should increase by 1", initialRowCount + 1, newRowCount);
        // Get last row package
        Paquete newPackage = paqueteTableView.getItems().get(newRowCount - 1);
        // Verify the new package is not null
        assertNotNull("New package should not be null", newPackage);

        assertNotEquals("New package a different ID ", lastPackage.getId(), newPackage.getId());
        
               // Verify default values of the new package
        assertTrue("New package should new value",
                paqueteTableView.getItems().contains(newPackage));
//        // Verify default values of the new package
//        assertTrue("New package should new value",
//                paqueteTableView.getItems().stream().anyMatch(pkg -> pkg.getId().equals(newPackage.getId())));
    }

    /**
     * Test updating a package's sender name through table editing.
     */
    @Test
    public void testC_updatePackage() {
        WaitForAsyncUtils.waitForFxEvents(); // Ensure UI updates

        paqueteTableView = lookup("#paqueteTableView").query();

        // Ensure the table is not empty
        assertFalse("TableView is empty, no row to update", paqueteTableView.getItems().isEmpty());

        int rowIndex = Math.min(7, paqueteTableView.getItems().size() - 1); // Avoid out-of-bounds
        Node row = lookup(".table-row-cell").nth(rowIndex).query();

        clickOn(row);
        Integer tableRow = paqueteTableView.getSelectionModel().getSelectedIndex();

        // Fetch the selected package
        Paquete packageSelected = paqueteTableView.getItems().get(tableRow);

        String originalSender = packageSelected.getSender();
        String originalReceiver = packageSelected.getReceiver();
        double originalWeight = packageSelected.getWeight();
        PackageSize originalSize = packageSelected.getSize();
        Date originalDate = packageSelected.getCreationDate();
        boolean originalFragile = packageSelected.isFragile();
        //Locate date column cell using column ID
        Node dateCell = lookup("#dateColumn").nth(rowIndex + 1) // Account for header row
                .query();

        // Ensure updated values are different from the original ones
        String newSender = originalSender.equals("Updated Sender") ? "New Sender" : "Updated Sender";
        String newReceiver = originalReceiver.equals("Updated Receiver") ? "New Receiver" : "Updated Receiver";
        double newWeight = (originalWeight == 17.0) ? 18.5 : 17.0;
        PackageSize newSize = (originalSize == PackageSize.EXTRA_LARGE) ? PackageSize.MEDIUM : PackageSize.EXTRA_LARGE;
        boolean newFragile = !originalFragile;

        // Update Sender
        Node tableColumnSender = lookup("#senderColumn").nth(tableRow + 1).query();
        clickOn(tableColumnSender);
        write(newSender);
        type(KeyCode.ENTER);

        // Update Receiver
        Node tableColumnReceiver = lookup("#receiverColumn").nth(tableRow + 1).query();
        clickOn(tableColumnReceiver);
        write(newReceiver);
        type(KeyCode.ENTER);

        // Update Weight
        Node tableColumnWeight = lookup("#weightColumn").nth(tableRow + 1).query();
        clickOn(tableColumnWeight);
        write(String.valueOf(newWeight));
        type(KeyCode.ENTER);

        // Update Size
        Node tableColumnSize = lookup("#sizeColumn").nth(tableRow + 1).query();
        clickOn(tableColumnSize);
        if (packageSelected.getSize().equals(PackageSize.EXTRA_LARGE)) {
            push(KeyCode.UP);
        } else {
            push(KeyCode.DOWN);
        }

        doubleClickOn(dateCell);
        press(KeyCode.CONTROL).press(KeyCode.A).release(KeyCode.A).release(KeyCode.CONTROL);
        press(KeyCode.BACK_SPACE);
        //Enter invalid future date
        write("07/02/2025");
        press(KeyCode.ENTER);
        // Update Fragile status
        Node tableColumnFragile = lookup("#fragileColumn").nth(tableRow + 1).query();
        clickOn(tableColumnFragile);
        type(KeyCode.SPACE);

        // Get updated package
        Paquete updatedPackage = paqueteTableView.getItems().get(tableRow);

        // Assertions to check if values changed
        assertNotEquals("Sender should be updated", originalSender, updatedPackage.getSender());
        assertNotEquals("Receiver should be updated", originalReceiver, updatedPackage.getReceiver());
        assertNotEquals("Weight should be updated", originalWeight, updatedPackage.getWeight());
        assertNotEquals("Size should be updated", originalSize, updatedPackage.getSize());
        assertNotEquals("Creation date should be original value", originalDate, packageSelected.getCreationDate());
        assertNotEquals("Fragile status should be updated", originalFragile, updatedPackage.isFragile());
    }

    /**
     * Test deleting a package from the table.
     */
    @Test
    public void testD_deletePackage() {
        // Ensure UI updates before starting
        WaitForAsyncUtils.waitForFxEvents();
        // Get initial row count and verify there is at least one package
        int initialCount = paqueteTableView.getItems().size();
        assertFalse("No package available to delete", initialCount == 0);
        // Select the first package in the table
        Paquete selectedPackage = paqueteTableView.getItems().get(0);
        TableRow<Paquete> row = lookup(".table-row-cell").nth(0).query();
        clickOn(row);
        // Click the remove button
        clickOn("#removeShipmentBtn");
        // Wait for the confirmation dialog
        WaitForAsyncUtils.waitForFxEvents();
        // Verify confirmation dialog appears
        DialogPane dialogPane = lookup(".dialog-pane").query();
        assertNotNull("Confirmation dialog not shown", dialogPane);

        // Click "OK" to confirm deletion
        Button okButton = (Button) dialogPane.lookupButton(ButtonType.OK);
        assertNotNull("OK button not found in dialog", okButton);
        clickOn(okButton);

        // Wait for UI updates
        WaitForAsyncUtils.waitForFxEvents();

        // Refresh table to ensure it reflects changes
        interact(() -> paqueteTableView.refresh());

        // Verify row decreased by 1
        int newCount = paqueteTableView.getItems().size();
        assertEquals("Package count should decrease by 1", initialCount - 1, newCount);

        //deleted package in should not existe 
        ObservableList<Paquete> results = paqueteTableView.getItems();
        assertFalse("Deleted package is not be in the table", results.stream().anyMatch(p -> p.getId().equals(selectedPackage.getId())));
    }

}
