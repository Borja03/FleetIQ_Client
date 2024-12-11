package exception;

/**
 * Excepción que indica que el formato de una dirección de correo electrónico es inválido.
 * 
 * <p>Esta clase extiende {@link Exception} y se utiliza para señalar
 * situaciones en las que un correo electrónico no cumple con el formato
 * esperado.</p>
 * 
 * @author Adrian
 */
public class InvalidEmailFormatException1 extends Exception {
    
    /**
     * Crea una nueva instancia de InvalidEmailFormatException con un mensaje específico.
     *
     * @param message el mensaje que describe el motivo de la excepción
     */
    public InvalidEmailFormatException1(String message) {
        super(message);
    }
}
