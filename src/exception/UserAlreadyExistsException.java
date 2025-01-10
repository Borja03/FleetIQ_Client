package exception;

/**
 * Excepción que indica que el usuario ya existe en el sistema.
 * 
 * <p>Esta clase extiende {@link Exception} y se utiliza para señalar
 * situaciones en las que se intenta registrar un usuario que ya está
 * registrado en el sistema.</p>
 * 
 * @author Alder
 */
public class UserAlreadyExistsException extends Exception {
    
    /**
     * Crea una nueva instancia de UserAlreadyExistsException con un mensaje específico.
     *
     * @param message el mensaje que describe el motivo de la excepción
     */
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
