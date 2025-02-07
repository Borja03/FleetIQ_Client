package exception;

/**
 * Excepción que indica que el formato de una ciudad es incorrecto.
 * 
 * <p>Esta clase extiende {@link Exception} y se utiliza para señalar
 * situaciones en las que el nombre de una ciudad no cumple con los criterios
 * de formato válidos establecidos por la aplicación.</p>
 * 
 * @author Omar
 */
public class InvalidCityFormatException extends Exception {
    
    /**
     * Crea una nueva instancia de InvalidCityFormatException con un mensaje específico.
     *
     * @param message el mensaje que describe el motivo de la excepción
     */
    public InvalidCityFormatException(String message) {
        super(message);
    }
}
