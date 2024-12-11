package exception;

/**
 * Excepción que indica que el formato de un código postal es inválido.
 * 
 * <p>Esta clase extiende {@link Exception} y se utiliza para señalar
 * situaciones en las que un código postal proporcionado no cumple con
 * los criterios de formato válidos establecidos por la aplicación.</p>
 * 
 * @author Adrian
 */
public class InvalidZipFormatException extends Exception {
    
    /**
     * Crea una nueva instancia de InvalidZipFormatException con un mensaje específico.
     *
     * @param message el mensaje que describe el motivo de la excepción
     */
    public InvalidZipFormatException(String message) {
        super(message);
    }
}
