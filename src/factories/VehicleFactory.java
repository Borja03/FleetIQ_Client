/**
 * Factory class for creating and managing VehicleManager instances.
 * This class implements the Singleton pattern to ensure only one instance
 * of VehicleManager exists throughout the application.
 *
 * @author Adrian
 * @version 1.0
 */
package factories;

import logicInterface.VehicleManager;
import logicimplementation.VehicleManagerImp;

public class VehicleFactory {

    /**
     * Returns the singleton instance of VehicleManager. If no instance exists,
     * creates a new one.
     *
     * @return The singleton instance of VehicleManager
     */
    private static VehicleManager vehicleManager;

    public static VehicleManager getVehicleInstance() {
        if (vehicleManager == null) {
            vehicleManager = new VehicleManagerImp();
        }
        return vehicleManager;
    }

}
