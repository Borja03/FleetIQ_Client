package exception;

/**
 * Excepción que indica que un campo requerido está vacío.
 * 
 * <p>Esta clase extiende {@link Exception} y se utiliza para señalar
 * situaciones en las que un campo que debe ser completado por el usuario
 * no ha sido llenado, lo que impide el procesamiento adecuado de la información.</p>
 * 
 * @author Omar
 */
public class EmptyFieldException extends Exception {
    
    /**
     * Crea una nueva instancia de EmptyFieldException con un mensaje específico.
     *
     * @param message el mensaje que describe el motivo de la excepción
     */
    public EmptyFieldException(String message) {
        super(message);
    }
}
