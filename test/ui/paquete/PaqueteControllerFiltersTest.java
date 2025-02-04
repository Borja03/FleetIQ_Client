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
public class PaqueteControllerFiltersTest extends ApplicationTest {

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
    @Test
    public void testH_errorHandling() {
        // Test invalid sender name format
        paqueteTableView.getSelectionModel().selectFirst();
        Node tableColumnSender = lookup("#senderColumn").nth(1).query();
        clickOn(tableColumnSender);
        write("123Invalid ");
        type(KeyCode.ENTER);

        WaitForAsyncUtils.waitForFxEvents();

        // Verify error alert is shown
        Node dialogPane = lookup(".alert").query();
        assertNotNull(dialogPane);
        clickOn("OK");
    }

}
