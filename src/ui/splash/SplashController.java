package ui.splash;

import ui.paquete.*;
import model.Paquete;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.HttpURLConnection;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import javafx.util.StringConverter;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

/**
 *
 * @author Omar
 */
public class SplashController  {

    @FXML
    private ImageView logoImageView;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private Label loadingLabel;

    private Stage splashStage;

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

            return false;
        }
    }

    private void loadMainApplication() {
        // Replace this with the code to load your main application
        System.out.println("Loading main application...");
         FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/paquete/paquete.fxml"));
            Parent root=null;
        try {
            root = loader.load();
        } catch (IOException ex) {
            Logger.getLogger(SplashController.class.getName()).log(Level.SEVERE, null, ex);
        }
              PaqueteController controller = loader.getController();
                Stage newStage = new Stage();
                controller.setStage(newStage);
                controller.initStage(root);
               // stage.close();
    }
    
    
    
}