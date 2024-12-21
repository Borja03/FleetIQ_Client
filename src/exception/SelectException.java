/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


package exception;

 /*
 * @author Alder
 */
public class SelectException extends Exception {

    /**
     * Creates a new instance of <code>createException</code> without detail
     * message.
     */
    public SelectException() {
        super();
    }

    /**
     * Constructs an instance of <code>createException</code> with the specified
     * detail message.
     *
     * @param msg the detail message.
     */
    public SelectException(String msg) {
        super(msg);
    }

    public SelectException(String error_fetching_packages_by_name, Exception e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
