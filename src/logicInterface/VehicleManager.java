/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logicInterface;

import exception.CreateException;
import exception.DeleteException;
import exception.SelectException;
import exception.UpdateException;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import models.Vehicle;

/**
 *
 * @author Adrian
 */
public interface VehicleManager {

    public void addVehiculo(Vehicle vehiculo) throws CreateException;

    public void updateVehiculo(Vehicle vehiculo) throws UpdateException;

    public void deleteVehiculo(Integer idVehiculo) throws DeleteException;

    public List<Vehicle> findAllVehiculos() throws SelectException;

    public List<Vehicle> findAllVehiculosEntreDates(Date firstDate, Date secondDate) throws SelectException;

    public List<Vehicle> findAllVehiculosByPlate(String matricula) throws SelectException;

    public List<Vehicle> findAllVehiculosByCapacity(Integer capacity) throws SelectException;

}
