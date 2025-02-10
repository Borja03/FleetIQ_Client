package application;

import exception.SelectException;
import factories.PaqueteFactory;
import factories.RutaManagerFactory;
import factories.SignableFactory;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import models.User;
import ui.login.LogInController;
import static ui.login.LogInController.userSession;
import ui.vehicle.VehicleController;

public class VehiculoMain extends Application {

    public static void main(String[] args) {
        launch(args); // Start the JavaFX application
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            // Simulate a user for testing
//            User user = new User();
//            user.setEmail("multitartanga@gmail.com");
//            user.setPassword("12345@aA");
//
//            // Attempt to sign in the user
//            userSession = (User) SignableFactory.getSignable().signIn(user, User.class);
//
//            // Print the signed-in user information

            // Load the FXML file for the Paquete view
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/vehicle/vehicle.fxml"));
            Parent root = loader.load();

            // Get the controller and initialize the stage
            VehicleController controller = loader.getController();
            controller.setStage(primaryStage);
            controller.initStage(root);

        } catch (IOException ex) {
            // Log exceptions related to FXML loading
            Logger.getLogger(VehiculoMain.class.getName()).log(Level.SEVERE, "Error loading FXML file", ex);
        } catch (Exception ex) {
            // Log other unexpected exceptions
            Logger.getLogger(VehiculoMain.class.getName()).log(Level.SEVERE, "Unexpected error", ex);
        }
    }
}
