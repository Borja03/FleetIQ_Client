/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package factories;

import logicInterface.PackageManager;
import logicimplementaion.PackageManagerImp;


/**
 *
 * @author Omar
 */
public class PackageFactory {
    // Rename the variable to avoid conflict with the Java keyword `package`
    private static PackageManager packageManager;

    public static PackageManager getPackageInstance() {
        if (packageManager == null) {
            packageManager = new PackageManagerImp();
        }
        return packageManager;
    }
}
