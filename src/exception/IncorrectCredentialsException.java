package exception;


/**
 * Excepción que indica que las credenciales proporcionadas son incorrectas.
 *
 * <p>
 * Esta clase extiende {@link Exception} y se utiliza para señalar situaciones
 * en las que un usuario intenta acceder al sistema con credenciales que no son
 * válidas.</p>
 *
 * @author Alder
 */
public class IncorrectCredentialsException extends Exception {

    /**
     * Crea una nueva instancia de IncorrectCredentialsException con un mensaje
     * específico.
     *
     * @param message el mensaje que describe el motivo de la excepción
     */
    public IncorrectCredentialsException(String message) {
        super(message);
    }

    public IncorrectCredentialsException() {
         super();
    }

}
