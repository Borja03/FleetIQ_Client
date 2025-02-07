package factories;

import logicInterface.EnvioRutaVehiculoManager;
import service.EnvioRutaVehiculoRESTClient;
/*
*
*
* @author Alder
 */
public class EnvioRutaVehiculoFactory {

    // Rename the variable to avoid conflict with the Java keyword `package`
    private static EnvioRutaVehiculoManager envioRutaVehiculoManager;

    public static EnvioRutaVehiculoManager getEnvioRutaVehiculoInstance() {
        if (envioRutaVehiculoManager == null) {
            envioRutaVehiculoManager = (EnvioRutaVehiculoManager) new EnvioRutaVehiculoRESTClient();
        }
        return envioRutaVehiculoManager;
    }
}
