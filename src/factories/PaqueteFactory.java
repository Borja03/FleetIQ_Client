/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package factories;

import logicimplementation.PackageManagerImp;
import logicInterface.PaqueteManager;


/**
 *
 * @author Omar
 */
public class PaqueteFactory {
    // Rename the variable to avoid conflict with the Java keyword `package`
    private static PaqueteManager packageManager;

    public static PaqueteManager getPackageInstance() {
        if (packageManager == null) {
            packageManager = new PackageManagerImp();
        }
        return packageManager;
    }
}
