package exception;

/**
 * Excepción que indica que el formato de una dirección de correo electrónico es inválido.
 * 
 * <p>Esta clase extiende {@link Exception} y se utiliza para señalar
 * situaciones en las que una dirección de correo electrónico proporcionada
 * no cumple con los criterios de formato válidos establecidos por la aplicación.</p>
 * 
 * @author Adrian
 */
public class InvalidEmailFormatException extends Exception {
    
    /**
     * Crea una nueva instancia de InvalidEmailFormatException con un mensaje específico.
     *
     * @param message el mensaje que describe el motivo de la excepción
     */
    public InvalidEmailFormatException(String message) {
        super(message);
    }
}
