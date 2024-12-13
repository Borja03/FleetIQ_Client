package exception;

/**
 * Excepción que indica que se ha alcanzado el número máximo de usuarios en
 * hilo.
 *
 * <p>
 * Esta clase extiende {@link Exception} y se utiliza para señalar situaciones
 * en las que no se puede añadir más usuarios debido a un límite
 * establecido.</p>
 *
 * @author Alder
 */
public class MaxThreadUserException extends Exception {

    /**
     * Crea una nueva instancia de MaxThreadUserException con un mensaje
     * específico.
     *
     * @param message el mensaje que describe el motivo de la excepción
     */
    public MaxThreadUserException(String message) {
        super(message);
    }

    public MaxThreadUserException() {
       super();
    }

}
