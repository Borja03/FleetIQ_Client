package application;

import exception.SelectException;
import factories.PaqueteFactory;
import factories.RutaManagerFactory;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javax.ws.rs.core.GenericType;
import models.Paquete;
import models.Ruta;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Cargar el archivo FXML
        Parent root = FXMLLoader.load(getClass().getResource("/ui/envio/envio.fxml"));

        // Crear la escena
        Scene scene = new Scene(root);

        // Establecer el tamaño fijo
        primaryStage.setScene(scene);
        primaryStage.setTitle("Splash");
        primaryStage.getIcons().add(new Image("/image/fleet_icon.png"));

        primaryStage.setResizable(false);  // Aquí se desactiva la opción de redimensionar

        // Mostrar la ventana
        primaryStage.show();
    }
}
