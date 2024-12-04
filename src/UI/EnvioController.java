package UI;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.input.MouseEvent;

import java.time.LocalDate;
import java.util.Optional;
import javafx.event.EventHandler;

public class EnvioController {

    @FXML
    private MenuItem filtrarPorFecha;
    @FXML
    private MenuItem filtrarPorEstado;
    @FXML
    private MenuItem filtrarPorNumPaquetes;

    @FXML
    private Label filterLabelFecha;
    @FXML
    private Label filterLabelEstado;
    @FXML
    private Label filterLabelPaquetes;

    @FXML
    private Label removeFilterFecha;
    @FXML
    private Label removeFilterEstado;
    @FXML
    private Label removeFilterPaquetes;

    @FXML
    private DatePicker fechaInicioPicker;
    @FXML
    private DatePicker fechaFinPicker;

    @FXML
    private TextField searchField;

    // Este método se encarga de mostrar el filtro de fecha
     @FXML
    private void handleFiltrarPorFecha() {
        // Mostrar el filtro
        filterLabelFecha.setVisible(true);

        // Crear el diálogo para seleccionar las fechas
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Filtrar por Fechas");
        dialog.setHeaderText("Selecciona el rango de fechas para filtrar.");

        VBox vbox = new VBox();
        fechaInicioPicker = new DatePicker();
        fechaFinPicker = new DatePicker();

        vbox.getChildren().addAll(new Label("Fecha inicio:"), fechaInicioPicker, new Label("Fecha fin:"), fechaFinPicker);

        dialog.getDialogPane().setContent(vbox);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        // Mostrar el diálogo y esperar una respuesta
        Optional<ButtonType> result = dialog.showAndWait();

        // Comprobamos si el usuario presionó OK
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Obtenemos las fechas solo si el usuario presionó OK
            LocalDate fechaInicio = fechaInicioPicker.getValue();
            LocalDate fechaFin = fechaFinPicker.getValue();

            // Aquí puedes implementar el filtrado según el rango de fechas
            if (fechaInicio != null && fechaFin != null) {
                System.out.println("Filtrar por fechas entre: " + fechaInicio + " y " + fechaFin);
                // Aquí podrías actualizar la tabla con los datos filtrados
            }
        }
    }

    // Este método se encarga de mostrar el filtro de estado
    @FXML
    private void handleFiltrarPorEstado() {
        // Mostrar el filtro
        filterLabelEstado.setVisible(true);

        // Mostrar un diálogo para seleccionar el estado
        ChoiceDialog<String> dialog = new ChoiceDialog<>("Estado 1", "Estado 1", "Estado 2", "Estado 3");
        dialog.setTitle("Filtrar por Estado");
        dialog.setHeaderText("Selecciona un estado");

        dialog.showAndWait().ifPresent(selectedState -> {
            System.out.println("Filtrar por estado: " + selectedState);
            // Aquí puedes actualizar la tabla con los datos filtrados
        });
    }

    // Este método se encarga de mostrar el filtro de número de paquetes
    @FXML
    private void handleFiltrarPorNumPaquetes() {
        // Mostrar el filtro
        filterLabelPaquetes.setVisible(true);

        // Crear un diálogo para seleccionar el número de paquetes
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Filtrar por Número de Paquetes");
        dialog.setHeaderText("Introduce el número de paquetes");

        dialog.showAndWait().ifPresent(input -> {
            try {
                int numPaquetes = Integer.parseInt(input);
                System.out.println("Filtrar por número de paquetes: " + numPaquetes);
                // Aquí puedes actualizar la tabla con los datos filtrados
            } catch (NumberFormatException e) {
                System.out.println("Número de paquetes inválido");
            }
        });
    }

    // Eliminar filtro de fecha
    @FXML
    private void removeFechaFilter() {
        filterLabelFecha.setVisible(false);
        // Aquí podrías también limpiar o resetear los datos en la tabla si es necesario
    }

    // Eliminar filtro de estado
    @FXML
    private void removeEstadoFilter() {
        filterLabelEstado.setVisible(false);
        // Aquí podrías resetear el estado de la tabla si es necesario
    }

    // Eliminar filtro de paquetes
    @FXML
    private void removePaqueteFilter() {
        filterLabelPaquetes.setVisible(false);
        // Aquí podrías resetear el número de paquetes en la tabla si es necesario
    }

    @FXML
    private void initialize() {
        // Asociar los eventos de clic a los métodos correspondientes sin usar lambdas

        // Evento de clic para filtrar por fecha
        filtrarPorFecha.setOnAction(new EventHandler<javafx.event.ActionEvent>() {
            @Override
            public void handle(javafx.event.ActionEvent event) {
                handleFiltrarPorFecha();
            }
        });

        // Evento de clic para filtrar por estado
        filtrarPorEstado.setOnAction(new EventHandler<javafx.event.ActionEvent>() {
            @Override
            public void handle(javafx.event.ActionEvent event) {
                handleFiltrarPorEstado();
            }
        });

        // Evento de clic para filtrar por número de paquetes
        filtrarPorNumPaquetes.setOnAction(new EventHandler<javafx.event.ActionEvent>() {
            @Override
            public void handle(javafx.event.ActionEvent event) {
                handleFiltrarPorNumPaquetes();
            }
        });

        // Evento de clic para eliminar filtro de fecha
        removeFilterFecha.setOnMouseClicked(new javafx.event.EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                removeFechaFilter();
            }
        });

        // Evento de clic para eliminar filtro de estado
        removeFilterEstado.setOnMouseClicked(new javafx.event.EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                removeEstadoFilter();
            }
        });

        // Evento de clic para eliminar filtro de número de paquetes
        removeFilterPaquetes.setOnMouseClicked(new javafx.event.EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                removePaqueteFilter();
            }
        });
    }
    
      @FXML
    private void handleLogOutClick(MouseEvent event) {
        // Aquí puedes implementar lo que quieres que pase cuando se haga clic en el logOut
        System.out.println("Clic en el LogOut!");
        
        // Ejemplo: cerrar la ventana o hacer logout
        // Stage stage = (Stage) logOut.getScene().getWindow();
        // stage.close();
    }
    
}
