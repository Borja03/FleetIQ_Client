/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package factories;

import logicInterface.PackageManager;
import logicimplementation.PackageImp;

/**
 *
 * @author Omar
 */
public class PackageFactory {
  
    public static PackageManager packageModel;

 
    public static PackageManager getInstance() {
        if (packageModel == null) {
            packageModel = new PackageImp();
        }
        return packageModel;
    }
}
 