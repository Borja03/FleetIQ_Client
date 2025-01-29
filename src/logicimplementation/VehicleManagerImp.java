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
import java.sql.Connection;
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

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Adrian
 */
public class VehicleManagerImp implements VehicleManager {

    private VehicleRESTClient webClient;

    public VehicleManagerImp() {
        this.webClient = new VehicleRESTClient();

    }

    @Override
    public void updateVehiculo(Vehiculo vehiculo) throws UpdateException {
        GenericType<Vehiculo> responseType = new GenericType<Vehiculo>() {
        };
        webClient.edit_XML(vehiculo, vehiculo.getId());
    }

    @Override
    public void deleteVehiculo(Integer idVehiculo) throws DeleteException {
        if (idVehiculo == null || idVehiculo <= 0) {
            throw new DeleteException("Vehicle ID cannot be null or negative.");
        }

        try {
            webClient.remove(String.valueOf(idVehiculo));
        } catch (ClientErrorException e) {
            throw new DeleteException("Error deleting vehicle with ID: " + idVehiculo + " - " + e.getMessage(), e);
        }
    }

    @Override
    public List<Vehiculo> findAllVehiculosEntreDates(Date firstDate, Date secondDate) throws SelectException {
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

    @Override
    public List<Vehiculo> findAllVehiculosByPlate(String matricula) throws SelectException {
        if (matricula == null || matricula.isEmpty()) {
            throw new SelectException("License plate cannot be null or empty.");
        }

        try {
            GenericType<List<Vehiculo>> responseType = new GenericType<List<Vehiculo>>() {
            };
            return webClient.findByPlate_XML(responseType, matricula);
        } catch (ClientErrorException e) {
            throw new SelectException("Error retrieving vehicles with plate: " + matricula + " - " + e.getMessage(), e);
        }
    }

    @Override
    public Vehiculo createVehicle(Vehiculo vehiculo) throws CreateException {
        GenericType<Vehiculo> responseType = new GenericType<Vehiculo>() {
        };
        return webClient.createVehicle(vehiculo, responseType);

    }
    @Override
    public List<Vehiculo> findVehiclesBeforeDateITV(String endDate) throws SelectException {
        GenericType<List<Vehiculo>> responseType = new GenericType<List<Vehiculo>>() {
        };
        return webClient.findVehiclesBeforeDateITV(responseType, endDate);
    }

    @Override
    public List<Vehiculo> findVehiclesAfterDateITV(String startDate) throws SelectException {
        GenericType<List<Vehiculo>> responseType = new GenericType<List<Vehiculo>>() {
        };
        return webClient.findVehiclessAfterDateITV(responseType, startDate);

    }

    @Override
    public List<Vehiculo> findVehiclesBetweenDatesITV(String endDate, String startDate) throws SelectException {
        GenericType<List<Vehiculo>> responseType = new GenericType<List<Vehiculo>>() {
        };
        return webClient.findByDateRangeITV(responseType, endDate, startDate);

    }

    @Override
    public List<Vehiculo> findVehiclesBeforeDateRegistration(String endDate) throws SelectException {
        GenericType<List<Vehiculo>> responseType = new GenericType<List<Vehiculo>>() {
        };
        return webClient.findVehiclesBeforeDateRegistration(responseType, endDate);
    }

    @Override
    public List<Vehiculo> findVehiclesAfterDateRegistration(String startDate) throws SelectException {
        GenericType<List<Vehiculo>> responseType = new GenericType<List<Vehiculo>>() {
        };
        return webClient.findVehiclessAfterDateRegistration(responseType, startDate);
    }

    @Override
    public List<Vehiculo> findVehiclesBetweenDatesRegistration(String endDate, String startDate) throws SelectException {
        GenericType<List<Vehiculo>> responseType = new GenericType<List<Vehiculo>>() {
        };
        return webClient.findVehiclesBetweenDatesRegistration(responseType, endDate, startDate);

    }

}
