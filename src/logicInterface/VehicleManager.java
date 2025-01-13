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
import javax.ws.rs.ClientErrorException;
import models.Vehicle;

/**
 *
 * @author Adrian
 */
public interface VehicleManager {

    void addVehiculo(Vehicle vehiculo) throws CreateException;

    void updateVehiculo(Vehicle vehiculo) throws UpdateException;

    void deleteVehiculo(Integer idVehiculo) throws DeleteException;

    List<Vehicle> findAllVehiculos() throws SelectException;

    List<Vehicle> findAllVehiculosEntreDates(Date firstDate, Date secondDate) throws SelectException;

    List<Vehicle> findAllVehiculosByPlate(String matricula) throws SelectException;

    List<Vehicle> findAllVehiculosByCapacity(Integer capacity) throws SelectException;

    public <T> T findAll_XML(Class<T> responseType) throws ClientErrorException;
}
