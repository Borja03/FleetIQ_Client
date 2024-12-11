package ui.menu;


import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;

public class MenuController {

    @FXML
    private MenuBar menuBar;

    @FXML
    private Menu menuGoTo;

    @FXML
    private MenuItem menuItemPaquete;

    @FXML
    private MenuItem menuItemEnvio;

    @FXML
    private MenuItem menuItemRuta;

    @FXML
    private MenuItem menuItemVehiculo;

    @FXML
    private MenuItem menuItemCambiarPasswd;

    @FXML
    private Menu menuView;

    @FXML
    private MenuItem menuItemDarkMode;

    @FXML
    private MenuItem menuItemLightMode;

    @FXML
    private Menu menuHelp;

    @FXML
    private MenuItem menuItemAboutPaquete;

    @FXML
    private MenuItem menuItemAboutRuta;

    @FXML
    private MenuItem menuItemAboutEnvio;

    @FXML
    private MenuItem menuItemAboutVehiculo;

    @FXML
    private MenuButton menuLogout;

    @FXML
    private MenuItem profilItem;

    @FXML
    private MenuItem logoutItem;

    public void initialize() {
        
        
    }

    @FXML
     public void handlePaqueteMenuItemAction(Event event) {
     
    }

    @FXML
    private void handleEnvioMenuItemAction(Event event) {
        System.out.println("Envio menu item clicked");
    }

    @FXML
    private void handleRutaMenuItemAction(Event event) {
        System.out.println("Ruta menu item clicked");
    }

    @FXML
    private void handleVehiculoMenuItemAction(Event event) {
        System.out.println("Vehiculo menu item clicked");
    }

    @FXML
    private void handleChangePasswordMenuItemAction(Event event) {
        System.out.println("Change password menu item clicked");
    }
}
