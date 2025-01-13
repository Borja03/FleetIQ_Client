/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logicimplementaion;

import exception.CreateException;
import exception.DeleteException;
import exception.SelectException;
import exception.UpdateException;
import java.util.Date;
import java.util.List;
import javax.ws.rs.core.GenericType;
import logicInterface.PackageManager;
import logicInterface.VehicleManager;
import models.PackageSize;
import models.Paquete;
import models.Vehicle;
import service.PackageRESTClient;
import service.VehicleRESTClient;

/**
 *
 * @author 2dam
 */

    /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Omar
 */
public class VehicleManagerImp implements VehicleManager {

    private VehicleRESTClient webClient;
    
     public VehicleManagerImp() {
        this.webClient = new VehicleRESTClient();
       
    }

    @Override
    public void addVehiculo(Vehicle vehiculo) throws CreateException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void updateVehiculo(Vehicle vehiculo) throws UpdateException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void deleteVehiculo(Integer idVehiculo) throws DeleteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Vehicle> findAllVehiculos() throws SelectException {
        GenericType<List<Vehicle>> responseType = new GenericType<List<Vehicle>>() {
        };
         List<Vehicle> vehicleList = webClient.findAll_XML(responseType);
        return vehicleList;
    }

    @Override
    public List<Vehicle> findAllVehiculosEntreDates(Date firstDate, Date secondDate) throws SelectException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Vehicle> findAllVehiculosByPlate(String matricula) throws SelectException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Vehicle> findAllVehiculosByCapacity(Integer capacity) throws SelectException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }



    


    }




