package exception;

/**
 * Excepción que indica que el formato de una dirección de calle es inválido.
 * 
 * <p>Esta clase extiende {@link Exception} y se utiliza para señalar
 * situaciones en las que una dirección de calle proporcionada no cumple con
 * los criterios de formato válidos establecidos por la aplicación.</p>
 * 
 * @author Omar
 */
public class InvalidStreetFormatException extends Exception {
    
    /**
     * Crea una nueva instancia de InvalidStreetFormatException con un mensaje específico.
     *
     * @param message el mensaje que describe el motivo de la excepción
     */
    public InvalidStreetFormatException(String message) {
        super(message);
    }
}
