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
import java.time.LocalDate;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import models.PackageSize;
import models.Paquete;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import javafx.stage.Stage;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
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
public class PaqueteControllerTest extends ApplicationTest {

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

    // crud Test 
    /**
     * Test adding a new package.
     */
    @Test
    public void testB_addPackage() {
        int initialRowCount = paqueteTableView.getItems().size();
        clickOn(addShipmentBtn);
        assertEquals("Package not added", initialRowCount + 1, paqueteTableView.getItems().size());
    }

    /**
     * Test removing a package.
     */
    @Test
    public void testC_removePackage() {
        int initialRowCount = paqueteTableView.getItems().size();
        Node row = lookup(".table-row-cell").nth(5).query();
        clickOn(row);
        verifyThat("#removeShipmentBtn", isEnabled());
        clickOn(removeShipmentBtn);
        assertEquals("Package not removed", initialRowCount - 1, paqueteTableView.getItems().size());
    }

    /**
     * Test validation of sender and receiver fields.
     */
    @Test
    public void testD_updatePackage() {
 
    }

    /**
     * Test filtering packages by size.
     */
    @Test
    public void testD_filterBySize() {
        // Verify that the ComboBox is visible and enabled
        verifyThat(sizeFilterComboBox, isVisible());
        verifyThat(sizeFilterComboBox, isEnabled());

        // Open the ComboBox dropdown
        clickOn(sizeFilterComboBox); // Use the default clickOn method
        sleep(1000); // Wait for the dropdown to fully render

        // Debug: Print the available options
        System.out.println("Dropdown options: " + sizeFilterComboBox.getItems());

        // Debug: Verify that the dropdown is open
        Node dropdown = lookup("#sizeFilterComboBox").query();
        assertNotNull("Dropdown not found", dropdown);
        verifyThat(dropdown, isVisible());

        // Select the "MEDIUM" option by text
        Node mediumOption = lookup("MEDIUM").query();
        assertNotNull("MEDIUM option not found", mediumOption);
        clickOn(mediumOption);

        // Verify that the TableView is filtered correctly
        verifyThat(paqueteTableView, (TableView<Paquete> table)
                        -> table.getItems().stream().allMatch(p -> p.getSize() == PackageSize.MEDIUM)
        );
    }

//    @Test
//public void testD_filterBySize() {
//    // Verify that the ComboBox is visible and enabled
//    verifyThat(sizeFilterComboBox, isVisible());
//    verifyThat(sizeFilterComboBox, isEnabled());
//
//    // Open the ComboBox dropdown
//    int retryCount = 3;
//    boolean selected = false;
//
//    for (int i = 0; i < retryCount; i++) {
//        try {
//            clickOn(sizeFilterComboBox); // Open dropdown
//            waitForFxEvents();
//
//            // Ensure dropdown is rendered and visible
//            Node dropdown = lookup("#sizeFilterComboBox").query();
//            assertNotNull("Dropdown not found", dropdown);
//            verifyThat(dropdown, isVisible());
//
//            // Select the "MEDIUM" option by text
//            Node mediumOption = lookup("MEDIUM").query();
//            if (mediumOption != null && mediumOption.isVisible()) {
//                clickOn(mediumOption);
//                selected = true;
//                break;
//            }
//        } catch (Exception e) {
//            System.err.println("Attempt " + (i + 1) + " failed: " + e.getMessage());
//        }
//    }
//
//    assertTrue("Failed to select 'MEDIUM' option after retries", selected);
//
//    // Verify that the TableView is filtered correctly
//    verifyThat(paqueteTableView, (TableView<Paquete> table)
//            -> table.getItems().stream().allMatch(p -> p.getSize() == PackageSize.MEDIUM)
//    );
//}
// Utility method to wait for FX events to complete
//    private void waitForFxEvents() {
//        WaitForAsyncUtils.waitForFxEvents();
//    }
    /**
     * Test searching packages by name.
     */
    @Test
    public void testE_searchByName() {
        clickOn(searchTextField);
        write("John Doe");
        clickOn(searchBtn);

        // Verify that the table contains at least one row with the sender "John Doe"
        verifyThat(paqueteTableView, (TableView<Paquete> table)
                        -> table.getItems()
                                        .stream()
                                        .anyMatch(p -> p.getSender().contains("John Doe"))
        );
    }

    /**
     * Test filtering packages by date range.
     */
//    @Test
//    public void testF_filterByDateRange() {
//        clickOn(fromDatePicker);
//        write("15/01/2025");
//        clickOn(toDatePicker);
//        write("31/01/2025");
//        clickOn(filterDatesBtn);
//
//        // Verify that the table contains rows within the specified date range
//        verifyThat(paqueteTableView, (TableView<Paquete> table)
//                        -> table.getItems().stream().allMatch(p
//                                        -> p.getCreationDate().after(java.sql.Date.valueOf("2025-01-15"))
//                        && p.getCreationDate().before(java.sql.Date.valueOf("2025-01-31"))
//                        )
//        );
//    }
    @Test
    public void testF_filterByDateRange() {
        // Set date values programmatically to avoid format issues
        interact(() -> {
            fromDatePicker.setValue(LocalDate.of(2025, 1, 15));
            toDatePicker.setValue(LocalDate.of(2025, 1, 31));
        });

        // Click the filter button
        clickOn(filterDatesBtn);
        sleep(1000); // Wait for filtering logic to apply

        // Verify the table contains rows within the specified date range (inclusive)
        verifyThat(paqueteTableView, (TableView<Paquete> table)
                        -> table.getItems().stream().allMatch(p
                                        -> p.getCreationDate().compareTo(java.sql.Date.valueOf("2025-01-15")) >= 0
                        && p.getCreationDate().compareTo(java.sql.Date.valueOf("2025-01-31")) <= 0
                        )
        );
    }

    /**
     * Test printing report.
     */
    @Test
    public void testG_printReport() {
        clickOn(printReportBtn);
        // Verify that the print report action is triggered (this might require mocking or additional setup)
    }

    /**
     * Test table row selection.
     */
    @Test
    public void testH_tableSelection() {
        Node row = lookup(".table-row-cell").nth(0).query();
        clickOn(row);
        verifyThat("#removeShipmentBtn", isEnabled());
    }

    /**
     * Test validation of sender and receiver fields.
     */
    @Test
    public void testI_validation() {
        // Ensure the table has data
        assertNotNull(paqueteTableView.getItems());
        assertFalse(paqueteTableView.getItems().isEmpty());

        // Select the first row
        Node row = lookup(".table-row-cell").nth(0).query();
        clickOn(row);

        // Find the sender cell in the first row
        Node senderCell = lookup(".table-row-cell .table-cell")
                        .nth(1) // Assuming senderColumn is the second column (index 1)
                        .query();
        assertNotNull("Sender cell not found", senderCell);

        // Double-click the sender cell to enter edit mode
        doubleClickOn(senderCell);

        // Enter invalid data and press Enter
        write("InvalidSender123");
        press(KeyCode.ENTER);

        // Verify that the validation error message is shown
        verifyThat("Sender name must not exceed 30 letters and must contain letters only.", isVisible());
        clickOn(isDefaultButton());
    }

    private void clickOn(PackageSize orElseThrow) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
