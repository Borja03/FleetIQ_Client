/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package exception;

import javax.ws.rs.ClientErrorException;

/**
 *
 * @author Alder
 */
public class CreateException extends Exception {

    /**
     * Creates a new instance of <code>createException</code> without detail
     * message.
     */
    public CreateException() {
        super();
    }

    /**
     * Constructs an instance of <code>createException</code> with the specified
     * detail message.
     *
     * @param msg the detail message.
     */
    public CreateException(String msg) {
        super(msg);
    }

    public CreateException(String string, ClientErrorException e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public CreateException(String string, Exception e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

 
}
