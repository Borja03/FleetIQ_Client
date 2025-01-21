package factories;

import logicInterface.RutaManager;
import service.RutaRESTClient;

/**
 * Factory class to manage the creation of RutaManager instances.
 */
public class RutaManagerFactory {

    // Private static RutaManager instance for singleton behavior
    private static RutaManager rutaManager;

    /**
     * Returns an instance of RutaManager.
     * If the instance doesn't exist, it creates one.
     *
     * @return RutaManager instance
     */
    public static RutaManager getRutaManager() {
        if (rutaManager == null) {
            rutaManager = (RutaManager) new RutaRESTClient();
        }
        return rutaManager;
    }
}
