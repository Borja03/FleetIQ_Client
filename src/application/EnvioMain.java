package application;

import exception.SelectException;
import factories.PaqueteFactory;
import factories.RutaManagerFactory;
import factories.SignableFactory;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import models.User;
import ui.envio.EnvioController;
import ui.login.LogInController;
import static ui.login.LogInController.userSession;
import ui.paquete.PaqueteController;

public class EnvioMain extends Application {

    public static void main(String[] args) {
        launch(args); // Start the JavaFX application
    }

    @Override
    public void start(Stage primaryStage) {
        try {

            // Load the FXML file for the Paquete view
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/envio/envio.fxml"));
            Parent root = loader.load();

            // Get the controller and initialize the stage
            EnvioController controller = loader.getController();
            controller.setStage(primaryStage);
            controller.initStage(root);

        } catch (IOException ex) {
            // Log exceptions related to FXML loading
            Logger.getLogger(PaqueteMain.class.getName()).log(Level.SEVERE, "Error loading FXML file", ex);
        } catch (Exception ex) {
            // Log other unexpected exceptions
            Logger.getLogger(PaqueteMain.class.getName()).log(Level.SEVERE, "Unexpected error", ex);
        }
    }
}
