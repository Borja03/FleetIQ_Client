package factories;

import java.util.ResourceBundle;
import logicInterface.PaqueteManager;
import logicimplementation.PackageManagerImp;
import service.PackageRESTClient;

/**
 * Factory class for creating and managing a singleton instance of
 * {@link PaqueteManager}. This ensures that only one instance of
 * {@link PackageManagerImp} is created and reused.
 * <p>
 * Note: The variable {@code packageManager} has been renamed to avoid conflict
 * with the Java keyword {@code package}.
 * </p>
 *
 * @author Omar
 */
public class PaqueteFactory {
     /**
     * Singleton instance of {@link PaqueteManager}.
     */
    private static PaqueteManager instance;

    /**
     * Returns the singleton instance of {@link PaqueteManager}. If the instance
     * does not exist, it is created and returned.
     *
     * @return the singleton instance of {@link PaqueteManager}.
     */
    public static PaqueteManager getPackageInstance() {
        if (instance == null) {
            // Default to the original implementation with config URI
            instance = new PackageManagerImp(new PackageRESTClient(
                            ResourceBundle.getBundle("config/config").getString("RESTful.baseURI")
            ));
        }
        return instance;
    }

    // For testing purposes
    public static void setPackageInstance(PaqueteManager testInstance) {
        instance = testInstance;
    }
}
