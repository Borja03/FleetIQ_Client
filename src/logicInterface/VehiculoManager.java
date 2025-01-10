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
import models.Vehiculo;

/**
 *
 * @author Adrian
 */
public interface VehiculoManager {
    
    public void addVehiculo(Vehiculo vehiculo) throws CreateException;
    public void updateVehiculo(Vehiculo vehiculo) throws UpdateException;
    public void deleteVehiculo(Integer idVehiculo) throws DeleteException;
    
    public List<Vehiculo> findAllVehiculos() throws SelectException;
  
    public List<Vehiculo> findAllVehiculosEntreDates(Date firstDate,Date secondDate) throws SelectException;
    public List<Vehiculo> findAllVehiculosByPlate(String matricula) throws SelectException;
    public List<Vehiculo> findAllVehiculosByCapacity(Integer capacity) throws SelectException;

}
    
    

