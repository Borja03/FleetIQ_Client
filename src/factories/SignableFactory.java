package factories;

import logicInterface.Signable;
import logicimplementation.UserManagerImp;




/**
 * La clase <code>SignableFactory</code> es una fábrica que proporciona una instancia única
 * de un objeto que implementa la interfaz {@link Signable}, permitiendo acceder a los métodos
 * de autenticación y registro de usuarios sin crear múltiples instancias del mismo objeto.
 * 
 * <p>
 * Esta clase sigue el patrón Singleton para garantizar que solo exista una instancia de
 * {@link Signable}
 * </p>
 * <p>
 * Es especialmente útil para mantener una única instancia de un cliente de autenticación en
 * toda la aplicación, evitando conexiones redundantes.
 * </p>
 * 
 * @author Alder
 * @see Signable
 */
public class SignableFactory {

    /**
     * Instancia única de un objeto {@link Signable}.
     */
    private static UserManagerImp signable;

    /**
     * Método que retorna una instancia única de {@link Signable}
     *
     * @return Una instancia de un objeto {@link Signable}.
     * @author Alder
     */
    public static UserManagerImp getSignable(){
        if (signable == null)
            signable = new UserManagerImp();
        return  signable;
     
    }
}