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
import ui.envio.EnvioController;
import ui.login.LogInController;
import ui.paquete.PaqueteController;
import ui.paquete.PaqueteHelpController;
import ui.profile.MainController;
import ui.ruta.RutaController;
import ui.vehicle.VehicleController;
import ui.vehicle.VehicleHelpController;
import utils.ThemeManager;

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
            controller.initStage(root);
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
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/envio/envio.fxml"));
            Parent root = loader.load();
            EnvioController controller = loader.getController();
            Stage loginStage = new Stage();
            controller.setStage(loginStage);
            controller.initStage(root);
            LOGGER.info("Envio window opened");
            // Close the current stage (the one with the menu)
            Stage currentStage = (Stage) menuBar.getScene().getWindow();
            currentStage.close();
        } catch (IOException ex) {
            LOGGER.severe("Error loading paquete window: " + ex);
        } catch (Exception ex) {
            Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void handleRutaMenuItemAction(Event event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/ruta/ruta.fxml"));
            Parent root = loader.load();
            RutaController controller = loader.getController();
            Stage loginStage = new Stage();
            controller.setStage(loginStage);
            controller.initialize(root);
            LOGGER.info("Ruta window opened");
            // Close the current stage (the one with the menu)
            Stage currentStage = (Stage) menuBar.getScene().getWindow();
            currentStage.close();
        } catch (IOException ex) {
            LOGGER.severe("Error loading paquete window: " + ex);
        }

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

    }

    @FXML
    private void handleDarkModeMenuItemAction(Event event) {
        ThemeManager.getInstance().setTheme(ThemeManager.Theme.DARK);
    }

    @FXML
    private void handleLightModeMenuItemAction(Event event) {
        ThemeManager.getInstance().setTheme(ThemeManager.Theme.LIGHT);
    }

    @FXML
    private void handleAboutPaqueteMenuItemAction(Event event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/paquete/paqueteHelp.fxml"));
            Parent root = loader.load();
            PaqueteHelpController controller = loader.getController();
            controller.initAndShowStage(root);
            LOGGER.info("Help window opened");
        } catch (IOException ex) {
            LOGGER.severe("Error loading HelpPaquete window: " + ex);
        }
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
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/vehicle/vehicleHelp.fxml"));
            Parent root = loader.load();
            VehicleHelpController controller = loader.getController();
            controller.initAndShowStage(root);
            LOGGER.info("Help window opened");
        } catch (IOException ex) {
            LOGGER.severe("Error loading HelpVehiculo window: " + ex);
        }
    }

    @FXML
    private void handleProfileMenuItemAction(Event event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/profile/Main.fxml"));
            Parent root = loader.load();

            MainController controller = loader.getController();
            Stage loginStage = new Stage();
            controller.setStage(loginStage);
            controller.initStage(root);
            LOGGER.info("Finish session and open login window");
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
