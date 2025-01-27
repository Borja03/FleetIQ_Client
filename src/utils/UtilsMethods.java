package utils;

import exception.InvalidEmailFormatException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import models.User;
import ui.paquete.PaqueteController;

/**
 * Clase que contiene métodos utilitarios para la validación de datos y la
 * gestión de alertas.
 *
 * <p>
 * Esta clase proporciona métodos para validar direcciones de correo electrónico
 * y mostrar alertas en la interfaz gráfica.</p>
 *
 * @author Alder
 */
public class UtilsMethods {

    /**
     * Logger para registrar advertencias y mensajes.
     */
    public static final Logger logger = Logger.getLogger(UtilsMethods.class.getName());

    /**
     * Valida el formato de una dirección de correo electrónico.
     *
     * <p>
     * Si el formato es inválido, se registra una advertencia y se muestra una
     * alerta al usuario.</p>
     *
     * @param email la dirección de correo electrónico a validar
     * @throws InvalidEmailFormatException si el formato del correo electrónico
     * es inválido
     */
    public void validateEmail(String email) throws InvalidEmailFormatException {
        String emailRegex = "^[\\w-\\.]+@[\\w-]+\\.[a-zA-Z]{2,4}$";
        if (!email.matches(emailRegex)) {
            logger.warning("Formato de email inválido: " + email);
            throw new InvalidEmailFormatException("\"El texto tiene que estar en formato email 'example@example.extension'\" " + email);
        }
    }

    /**
     * Muestra una alerta con un mensaje específico.
     *
     * @param title el título de la alerta
     * @param message el mensaje a mostrar en la alerta
     */
    public static void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static Optional<ButtonType> showConfirmationDialog(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        return alert.showAndWait();
    }

//    private String dateToString(LocalDate fromDate) {
//        String mDateFormat = ResourceBundle.getBundle("config/config").getString("date.format");
//        SimpleDateFormat dateFormat = new SimpleDateFormat(mDateFormat);
//
//        Date fromDateAsDate = (fromDate != null)
//                        ? Date.from(fromDate.atStartOfDay(ZoneId.systemDefault()).toInstant())
//                        : null;
//
//        String mfromDate = (fromDateAsDate != null) ? dateFormat.format(fromDateAsDate) : null;
//        return mfromDate;
//    }

}
