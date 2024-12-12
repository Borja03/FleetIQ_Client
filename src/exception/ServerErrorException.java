package exception;

/**
 * Excepción que indica que ha ocurrido un error en el servidor.
 * 
 * <p>Esta clase extiende {@link Exception} y se utiliza para señalar
 * situaciones en las que se produce un error interno en el servidor,
 * impidiendo el procesamiento correcto de la solicitud.</p>
 * 
 * @author Omar
 */
public class ServerErrorException extends Exception {
    
    /**
     * Crea una nueva instancia de ServerErrorException con un mensaje específico.
     *
     * @param message el mensaje que describe el motivo de la excepción
     */
    public ServerErrorException(String message) {
        super(message);
    }
    
       public ServerErrorException() {
            super();
    }
}
