package ui.menu;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;
import javafx.stage.Window;
import models.User;
import ui.login.LogInController;
import ui.paquete.PaqueteController;
import ui.profile.MainController;
import ui.ruta.RutaController;
import ui.vehicle.VehicleController;

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
    private Stage menuStage;
    public static User connectedUser;

    private static final Logger LOGGER = Logger.getLogger("package menu");

    public void initStage() {
    

    }

    @FXML
    public void handlePaqueteMenuItemAction(Event event) {
       try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/paquete/paquete.fxml"));
            Parent root = loader.load();
            PaqueteController controller = loader.getController();
            Stage loginStage = new Stage();
            controller.setStage(loginStage);
            controller.initStage(root,connectedUser);
            LOGGER.info("Paquete window opened");
            // Close the current stage (the one with the menu)
            Stage currentStage = (Stage) menuBar.getScene().getWindow();
            currentStage.close();
        } catch (IOException ex) {
            LOGGER.severe("Error loading paquete window: " + ex);
        }
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
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/vehicle/vehicle.fxml"));
            Parent root = loader.load();
            VehicleController controller = loader.getController();
            Stage loginStage = new Stage();
            controller.setStage(loginStage);
            controller.initStage(root);
            LOGGER.info("Paquete window opened");
            // Close the current stage (the one with the menu)
            Stage currentStage = (Stage) menuBar.getScene().getWindow();
            currentStage.close();
        } catch (IOException ex) {
            LOGGER.severe("Error loading paquete window: " + ex);
        }
        System.out.println("Vehiculo menu item clicked");
    }

    @FXML
    private void handleChangePasswordMenuItemAction(Event event) {
        System.out.println("Change password menu item clicked");
    }

    @FXML
    private void handleDarkModeMenuItemAction(Event event) {
        System.out.println("Change password menu item clicked");
    }

    @FXML
    private void handleLightModeMenuItemAction(Event event) {
        System.out.println("Change password menu item clicked");
    }

    @FXML
    private void handleAboutPaqueteMenuItemAction(Event event) {
        System.out.println("Change password menu item clicked");
    }

    @FXML
    private void handleAboutRutaMenuItemAction(Event event) {
        System.out.println("Change password menu item clicked");
    }

    @FXML
    private void handleAboutEnvioMenuItemAction(Event event) {
        System.out.println("Change password menu item clicked");
    }

    @FXML
    private void handleAboutVehiculoMenuItemAction(Event event) {
        System.out.println("Change password menu item clicked");
    }

    @FXML
    private void handleProfileMenuItemAction(Event event) {
        connectedUser = new User();
         connectedUser.setEmail("email@email.com");
        connectedUser.setName("AdminTest");
        connectedUser.setPassword("12345");
        connectedUser.setCity("city");
        connectedUser.setStreet("street");
        connectedUser.setZip(1234);
            try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/profile/Main.fxml"));
            Parent root = loader.load();

            MainController controller = loader.getController();
            Stage loginStage = new Stage();
            controller.setStage(loginStage);
            controller.initStage(root,connectedUser);
            LOGGER.info("Finish session and open login window");
            // Close the current stage (the one with the menu)
            Stage currentStage = (Stage) menuBar.getScene().getWindow();
            currentStage.close();

        } catch (IOException ex) {
            LOGGER.severe("Error loading login window: " + ex);
        }
    }


    @FXML
    private void handleLogoutMenuItemAction(Event event) {
        
        // logic to finish session here 
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/login/LogIn.fxml"));
            Parent root = loader.load();

            LogInController controller = loader.getController();
            Stage loginStage = new Stage();
            controller.setStage(loginStage);
            controller.initialize(root);
            LOGGER.info("Finish session and open login window");
            // Close the current stage (the one with the menu)
            Stage currentStage = (Stage) menuBar.getScene().getWindow();
            currentStage.close();
        } catch (IOException ex) {
            LOGGER.severe("Error loading login window: " + ex);
        }
    }
}
