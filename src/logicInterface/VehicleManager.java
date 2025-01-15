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
import java.util.Date;
import java.util.List;
import static javafx.scene.input.KeyCode.T;
import javax.ws.rs.core.GenericType;
import models.Vehiculo;

/**
 *
 * @author Adrian
 */
public interface VehicleManager {

    void addVehiculo(Vehiculo vehiculo) throws CreateException;

    void updateVehiculo(Vehiculo vehiculo) throws UpdateException;

    void deleteVehiculo(Integer idVehiculo) throws DeleteException;

    List<Vehiculo> findAllVehiculos() throws SelectException;

    List<Vehiculo> findAllVehiculosEntreDates(Date firstDate, Date secondDate) throws SelectException;

    List<Vehiculo> findAllVehiculosByPlate(String matricula) throws SelectException;

    List<Vehiculo> findAllVehiculosByCapacity(Integer capacity) throws SelectException;

    List<Vehiculo> findVehiculosByRegistrationDateRange_XML(Date firstDate, Date secondDate) throws SelectException;

}
