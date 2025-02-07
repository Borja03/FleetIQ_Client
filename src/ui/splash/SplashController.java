package ui.splash;

import ui.paquete.*;

import java.io.IOException;
import java.net.HttpURLConnection;
import javafx.fxml.FXML;

import java.net.URL;

import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import models.User;
import ui.login.LogInController;

/**
 * Controller class for the application's splash screen. This class manages the
 * initial loading screen displayed when the application starts, handles server
 * connectivity checks, and transitions to the main login screen.
 *
 * @author Omar
 */
public class SplashController {

    /**
     * The ImageView containing the application logo displayed in the splash
     * screen
     */
    @FXML
    private ImageView logoImageView;

    /**
     * Progress bar showing the loading status
     */
    @FXML
    private ProgressBar progressBar;

    /**
     * Label displaying loading status messages to the user
     */
    @FXML
    private Label loadingLabel;

    /**
     * The primary stage for the splash screen
     */
    private Stage splashStage;

    /**
     * Initializes the splash screen controller. This method is automatically
     * called after the FXML file has been loaded. It performs the following
     * operations:
     * <ul>
     * <li>Retrieves the splash screen stage</li>
     * <li>Checks server connectivity</li>
     * <li>Updates the UI with loading status</li>
     * <li>Transitions to the main application after loading</li>
     * </ul>
     */
    public void initialize() {
        Platform.runLater(() -> {
            // Get the Stage from a UI component
            splashStage = (Stage) progressBar.getScene().getWindow();

            new Thread(() -> {
                if (isServerConnected()) {
                    Platform.runLater(() -> {
                        loadingLabel.setText("Loading main application...");
                    });

                    try {
                        Thread.sleep(3000); // Simulate loading time
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    Platform.runLater(() -> {
                        if (splashStage != null) {
                            splashStage.close();
                        } else {
                            System.out.println("splashStage is null, cannot close.");
                        }
                        loadMainApplication();
                    });
                } else {
                    Platform.runLater(() -> {
                        loadingLabel.setText("Error: Could not connect to server");
                    });
                }
            }).start();
        });
    }

    public void setSplashStage(Stage splashStage) {
        this.splashStage = splashStage;
    }

    /**
     * Checks if the server is accessible by attempting to connect to
     * Google.com. This method serves as a basic connectivity test to ensure the
     * application can access network resources.
     *
     * @return true if the server is accessible (HTTP 200 response received), or
     * if the connection attempt fails (for testing purposes), false otherwise
     */
    private boolean isServerConnected() {
        try {
            URL url = new URL("http://google.com");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(5000); // 5-second timeout
            int responseCode = connection.getResponseCode();
            System.out.println("Able to connect to google ");

            return (responseCode == HttpURLConnection.HTTP_OK);
        } catch (IOException e) {
            System.out.println("Not Able to connect to google ");

            return true;
        }
    }

    /**
     * Loads the main application login screen. This method is called after the
     * splash screen has completed its initialization and server connectivity
     * check. It performs the following operations:
     * <ul>
     * <li>Loads the login screen FXML</li>
     * <li>Initializes the login controller</li>
     * <li>Creates a new stage for the login screen</li>
     * <li>Closes the splash screen</li>
     * </ul>
     */
    private void loadMainApplication() {
        System.out.println("Loading main application...");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/login/LogIn.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException ex) {
            Logger.getLogger(SplashController.class.getName()).log(Level.SEVERE, null, ex);
        }
        LogInController controller = loader.getController();
        Stage newStage = new Stage();
        controller.setStage(newStage);
        controller.initialize(root);
        splashStage.close();
    }

}
