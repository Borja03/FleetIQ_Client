package UI;

import javafx.fxml.FXML;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;

public class MenuController {

    // Referencias a las imágenes
    @FXML private ImageView imgView1;
    @FXML private ImageView imgView2;
    @FXML private ImageView imgView3;
    @FXML private ImageView imgView4;
    @FXML private ImageView imgView5;
    @FXML private ImageView imgView6;

    @FXML
    public void initialize() {
        // Cargar imágenes en cada opción desde la carpeta 'menuImage'
        imgView1.setImage(new Image(getClass().getResourceAsStream("/Images/user.png")));
        imgView2.setImage(new Image(getClass().getResourceAsStream("/Images/package.png")));
        imgView3.setImage(new Image(getClass().getResourceAsStream("/Images/send.png")));
        imgView4.setImage(new Image(getClass().getResourceAsStream("/Images/location.png")));
        imgView5.setImage(new Image(getClass().getResourceAsStream("/Images/vehicle.png")));
        imgView6.setImage(new Image(getClass().getResourceAsStream("/Images/logout.png")));
    }

    // Método para manejar el evento de cuando el ratón entra en la región (contenedor)
    @FXML
    private void handleMouseEntered(MouseEvent event) {
        Region region = (Region) event.getSource(); // Obtener el contenedor que activó el evento

        // Comprobar la imagen correspondiente y aplicar el efecto
        if (region.getParent() != null) {
            StackPane stackPane = (StackPane) region.getParent();
            ImageView imageView = (ImageView) stackPane.getChildren().get(0); // Obtener la ImageView dentro del StackPane

            applyHoverEffect(imageView);
        }
    }

    // Método para manejar el evento de cuando el ratón sale de la región (contenedor)
    @FXML
    private void handleMouseExited(MouseEvent event) {
        Region region = (Region) event.getSource(); // Obtener el contenedor que activó el evento

        // Comprobar la imagen correspondiente y quitar el efecto
        if (region.getParent() != null) {
            StackPane stackPane = (StackPane) region.getParent();
            ImageView imageView = (ImageView) stackPane.getChildren().get(0); // Obtener la ImageView dentro del StackPane

            removeHoverEffect(imageView);
        }
    }

    // Método para aplicar el efecto de hover
    private void applyHoverEffect(ImageView imageView) {
        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setBrightness(-0.2);  // Aumenta el brillo
        imageView.setEffect(colorAdjust);
    }

    // Método para quitar el efecto de hover
    private void removeHoverEffect(ImageView imageView) {
        imageView.setEffect(null);
    }
}
