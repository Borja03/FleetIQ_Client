package exception;

/**
 * Excepción que indica que el formato de una contraseña es inválido.
 * 
 * <p>Esta clase extiende {@link Exception} y se utiliza para señalar
 * situaciones en las que una contraseña proporcionada no cumple con los
 * criterios de formato válidos establecidos por la aplicación, como longitud
 * mínima o inclusión de caracteres especiales.</p>
 * 
 * @author Omar
 */
public class InvalidPasswordFormatException extends Exception {
    
    /**
     * Crea una nueva instancia de InvalidPasswordFormatException con un mensaje específico.
     *
     * @param message el mensaje que describe el motivo de la excepción
     */
    public InvalidPasswordFormatException(String message) {
        super(message);
    }
}
