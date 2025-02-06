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
import javafx.scene.control.TextField;
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
public class PaqueteControllerValidationTest extends ApplicationTest {

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
     * Test updating a package's sender name through table editing.
     */
    @Test
    public void test_updateSender() {
        WaitForAsyncUtils.waitForFxEvents(); // Ensure UI initialization
        // Verify table has data
        assertFalse("No packages available", paqueteTableView.getItems().isEmpty());
        // Select target row (using same index calculation as working version)
        int rowIndex = 0;
        Paquete targetPackage = paqueteTableView.getItems().get(rowIndex);
        String originalSender = targetPackage.getSender();
        //Locate sender column cell using column ID
        Node senderCell = lookup("#senderColumn").nth(rowIndex + 1).query();
        //Double-click to activate editing
        doubleClickOn(senderCell);
        //Verify edit mode activation
        Node textField = lookup(".text-field").query();
        assertNotNull("Cell not in edit mode", textField);
        //Enter invalid numeric characters
        write("Sender123");
        press(KeyCode.ENTER);
        //Verify model rollback
        assertEquals("Sender should revert to original value", originalSender, targetPackage.getSender());
        //Verify error alert
        WaitForAsyncUtils.waitForFxEvents();
        DialogPane alert = lookup(".alert").query();
        assertTrue("Missing validation message", alert.getContentText().contains("letters only"));
        //Cleanup alert
        clickOn(alert.lookupButton(ButtonType.OK));
    }

    @Test
    public void test_updateReceiver() {
        WaitForAsyncUtils.waitForFxEvents(); // Ensure UI initialization
        // Verify table has data
        assertFalse("No packages available", paqueteTableView.getItems().isEmpty());
        // Select target row (using same index calculation as working version)
        int rowIndex = 0;
        Paquete targetPackage = paqueteTableView.getItems().get(rowIndex);
        String originalReceiver = targetPackage.getReceiver();
        // Locate Receiver column cell using column ID
        Node receiverCell = lookup("#receiverColumn").nth(rowIndex + 1).query();
        //Double-click to activate editing
        doubleClickOn(receiverCell);
        //Verify edit mode activation
        Node textField = lookup(".text-field").query();
        assertNotNull("Cell not in edit mode", textField);
        // Enter invalid numeric characters
        write("Receiver Receiver Receiver Receiver");
        press(KeyCode.ENTER);
        //  Verify model rollback
        assertEquals("Receiver should revert to original value", originalReceiver, targetPackage.getReceiver());
        // Verify error alert
        WaitForAsyncUtils.waitForFxEvents();
        DialogPane alert = lookup(".alert").query();
        assertTrue("Missing validation message", alert.getContentText().contains("30"));
        // Cleanup alert
        clickOn(alert.lookupButton(ButtonType.OK));
    }

    @Test
    public void test_updateWeight() {
        WaitForAsyncUtils.waitForFxEvents(); // Ensure UI initialization
        // Verify table has data
        assertFalse("No packages available", paqueteTableView.getItems().isEmpty());
        // Select target row (using same index calculation as working version)
        int rowIndex = 0;
        Paquete targetPackage = paqueteTableView.getItems().get(rowIndex);
        Double originalWeight = targetPackage.getWeight();
        //Locate Receiver column cell using column ID
        Node weightCell = lookup("#weightColumn").nth(rowIndex + 1).query();
        //Double-click to activate editing
        doubleClickOn(weightCell);
        //Verify edit mode activation
        Node textField = lookup(".text-field").query();
        assertNotNull("Cell not in edit mode", textField);
        //Enter invalid numeric characters
        write("101");
        press(KeyCode.ENTER);
        //Verify model rollback
        assertEquals("Receiver should revert to original value", originalWeight, targetPackage.getWeight(), 0.0001);
        // Verify error alert
        WaitForAsyncUtils.waitForFxEvents();
        DialogPane alert = lookup(".alert").query();
        assertTrue("Missing validation message", alert.getContentText().contains(" 100"));
        // Cleanup alert
        clickOn(alert.lookupButton(ButtonType.OK));
    }

    @Test
    public void test_updateDate() {

        WaitForAsyncUtils.waitForFxEvents(); // Ensure UI initialization

        // Verify table has data
        assertFalse("No packages available", paqueteTableView.getItems().isEmpty());

        // Select first row
        int rowIndex = 0;
        Paquete targetPackage = paqueteTableView.getItems().get(rowIndex);
        Date originalDate = targetPackage.getCreationDate();

        //Locate date column cell using column ID
        Node dateCell = lookup("#dateColumn").nth(rowIndex + 1) // Account for header row
                        .query();

        //Double-click to activate editing
        doubleClickOn(dateCell);

        //Verify edit mode activation
        Node textField = lookup(".text-field").query();
        assertNotNull("Date cell not in edit mode", textField);

        // Clear existing text using keyboard navigation
        // Select all text (Ctrl+A) and delete
        press(KeyCode.CONTROL).press(KeyCode.A).release(KeyCode.A).release(KeyCode.CONTROL);
        press(KeyCode.BACK_SPACE);

        //Enter invalid future date
        write("15/12/2025");
        press(KeyCode.ENTER);

        // 6. Verify model rollback
        assertEquals("Creation date should revert to original value",
                        originalDate,
                        targetPackage.getCreationDate()
        );

        // Verify error alert
        WaitForAsyncUtils.waitForFxEvents();
        DialogPane alert = lookup(".alert").query();
        assertTrue("Missing validation message",
                        alert.getContentText().contains("exceed 15 days"));

        // Cleanup alert
        clickOn(alert.lookupButton(ButtonType.OK));
    }

}
