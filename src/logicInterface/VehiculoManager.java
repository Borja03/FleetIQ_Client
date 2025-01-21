/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package logicInterface;

import exception.createException;
import exception.deleteException;
import exception.selectException;
import exception.updateException;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import models.Vehiculo;
import models.PaqueteSize;

/**
 *
 * @author Adrian
 */
public interface VehiculoManager {
    
    public void addVehiculo(Vehiculo vehiculo) throws createException;
    public void updateVehiculo(Vehiculo vehiculo) throws updateException;
    public void deleteVehiculo(Integer idVehiculo) throws deleteException;
    
    public List<Vehiculo> findAllVehiculos() throws selectException;
  
    public List<Vehiculo> findAllVehiculosEntreDates(Date firstDate,Date secondDate) throws selectException;
    public List<Vehiculo> findAllVehiculosByPlate(String matricula) throws selectException;
    public List<Vehiculo> findAllVehiculosByCapacity(Integer capacity) throws selectException;

}
    
    

