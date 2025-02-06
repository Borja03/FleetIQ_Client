package factories;

import logicInterface.RutaManager;
import service.RutaRESTClient;

/**
 * Factory class to manage the creation of {@link RutaManager} instances.
 * <p>
 * This class ensures that only one instance of {@link RutaManager} exists (singleton pattern) and provides
 * a global point of access to that instance.
 * @author Borja
 */
public class RutaManagerFactory {

    // Private static RutaManager instance for singleton behavior
    private static RutaManager rutaManager;

    /**
     * Returns an instance of {@link RutaManager}.
     * <p>
     * If the instance doesn't exist, it creates one using the {@link RutaRESTClient}.
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
