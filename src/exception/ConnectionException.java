package exception;

/**
 * Excepción que indica un problema de conexión en el sistema.
 *
 * <p>
 * Esta clase extiende {@link Exception} y se utiliza para señalar condiciones
 * excepcionales relacionadas con la conexión, proporcionando un mensaje
 * descriptivo sobre el error.</p>
 *
 * @author Alder
 */
public class ConnectionException extends Exception {

    /**
     * Crea una nueva instancia de ConnectionException con un mensaje
     * específico.
     *
     * @param message el mensaje que describe el motivo de la excepción
     */
    public ConnectionException(String message) {
        super(message);
    }

    public ConnectionException() {
         super();
    }

}
