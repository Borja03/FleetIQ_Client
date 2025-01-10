package factories;

import logicInterface.EnvioManager;
import service.EnvioRESTClient;
/*
*
*
* @author Alder
 */
public class EnvioFactory {

    // Rename the variable to avoid conflict with the Java keyword `package`
    private static EnvioManager envioManager;

    public static EnvioManager getPackageInstance() {
        if (envioManager == null) {
            envioManager = (EnvioManager) new EnvioRESTClient();
        }
        return envioManager;
    }
}
