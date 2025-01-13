/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package factories;

import logicInterface.VehicleManager;
import logicimplementaion.VehicleManagerImp;

/**
 *
 * @author 2dam
 */
public class VehicleFactory {

    // Rename the variable to avoid conflict with the Java keyword `package`
    private static VehicleManager vehicleManager;

    public static VehicleManager getVehicleInstance() {
        if (vehicleManager == null) {
            vehicleManager = new VehicleManagerImp();
        }
        return vehicleManager;
    }

}
