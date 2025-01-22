/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logicimplementation;

import exception.CreateException;
import exception.DeleteException;
import exception.SelectException;
import exception.UpdateException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import logicInterface.VehicleManager;
import models.PackageSize;
import models.Paquete;
import models.Vehiculo;
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
    public void addVehiculo(Vehiculo vehiculo) throws CreateException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void updateVehiculo(Vehiculo vehiculo) throws UpdateException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void deleteVehiculo(Integer idVehiculo) throws DeleteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Vehiculo> findAllVehiculosEntreDates(Date firstDate, Date secondDate) throws SelectException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Vehiculo> findAllVehiculosByPlate(String matricula) throws SelectException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Vehiculo> findAllVehiculosByCapacity(Integer capacity) throws SelectException {
        // Si la capacidad es 0, llamamos a findAllVehiculos
        if (capacity == 0) {
            return findAllVehiculos(); // Llamada a la función que recupera todos los vehículos
        }

        // Si no es 0, filtramos por capacidad
        GenericType<List<Vehiculo>> responseType = new GenericType<List<Vehiculo>>() {
        };
        List<Vehiculo> vehiclesList = webClient.findByCapacity_XML(responseType, capacity);
        return vehiclesList;
    }

    @Override
    public List<Vehiculo> findAllVehiculos() throws SelectException {
        GenericType<List<Vehiculo>> responseType = new GenericType<List<Vehiculo>>() {
        };
        List<Vehiculo> vehiclesList = webClient.findAllVehiculos(responseType);
        return vehiclesList;

    }

    @Override
    public List<Vehiculo> findVehiculosByRegistrationDateRange_XML(Date firstDate, Date secondDate) throws SelectException {
        try {
            // Convertir Date a String en el formato adecuado (ejemplo: "yyyy-MM-dd")
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String startDate = sdf.format(firstDate);
            String endDate = sdf.format(secondDate);

            // Llamar al método REST con los parámetros correctos
            return webClient.findVehiculosByRegistrationDateRange_XML(
                    new GenericType<List<Vehiculo>>() {
            }, endDate, startDate
            );
        } catch (Exception e) {
            throw new SelectException("Error retrieving vehicles: " + e.getMessage(), e);
        }
    }

}
