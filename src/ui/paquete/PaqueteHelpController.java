package ui.paquete;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author Omar
 */
public class PaqueteHelpController {

    @FXML
    private WebView webView;

    /**
     * Initializes and show the help window.
     *
     * @param root The FXML document hierarchy root.
     */
    public void initAndShowStage(Parent root) {
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.getIcons().add(new Image("/image/fleet_icon.png")); // Set the window icon
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.setTitle("FleetIQ User Guide");
        stage.setResizable(false);
//        stage.setMinWidth(1024);
//        stage.setMinHeight(720);
        stage.setOnShowing(this::handleWindowShowing);
        stage.show();
    }

    /**
     * Initializes window state. It implements behavior for WINDOW_SHOWING type
     * event.
     *
     * @param event The window event
     */
    private void handleWindowShowing(WindowEvent event) {
        WebEngine webEngine = webView.getEngine();
        //Load help page.
        webEngine.load(getClass()
                        .getResource("/ui/help/PaqueteHelp.html").toExternalForm());
    }
}
