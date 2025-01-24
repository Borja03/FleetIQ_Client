package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

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
