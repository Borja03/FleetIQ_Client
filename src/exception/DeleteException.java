/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


package exception;

 /*
 * @author Alder
 */
public class DeleteException extends Exception {

    /**
     * Creates a new instance of <code>createException</code> without detail
     * message.
     */
    public DeleteException() {
        super();
    }

    /**
     * Constructs an instance of <code>createException</code> with the specified
     * detail message.
     *
     * @param msg the detail message.
     */
    public DeleteException(String msg) {
        super(msg);
    }


}
