/**
 * Implementation of the VehicleManager interface that handles vehicle-related operations
 * through REST web service calls. This class provides concrete implementations for
 * vehicle management operations including creation, updates, deletion, and various
 * search functionalities.
 *
 * @author Adrian
 * @version 1.0
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
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import logicInterface.VehicleManager;
import models.PackageSize;
import models.Paquete;
import models.Vehiculo;
import service.PackageRESTClient;
import service.VehicleRESTClient;

public class VehicleManagerImp implements VehicleManager {

    /**
     * The REST client used for making web service calls
     */
    private VehicleRESTClient webClient;

    /**
     * Default constructor that initializes a new VehicleRESTClient
     */
    public VehicleManagerImp() {
        this.webClient = new VehicleRESTClient();

    }

    /**
     * Constructor that accepts a specific VehicleRESTClient instance
     *
     * @param cliente The VehicleRESTClient to be used
     */
    public VehicleManagerImp(VehicleRESTClient cliente) {
        this.webClient = cliente;

    }

    /**
     * {@inheritDoc} Validates the vehicle ID and makes a REST call to delete
     * the vehicle.
     */
    @Override
    public void deleteVehiculo(Integer idVehiculo) throws DeleteException {
        if (idVehiculo == null || idVehiculo <= 0) {
            throw new DeleteException("Vehicle ID cannot be null or negative.");
        }
        try {
            webClient.remove(String.valueOf(idVehiculo));
        } catch (Exception e) {
            throw new DeleteException("Error eliminando vehículo con ID: " + idVehiculo + " - " + e.getMessage(), e);
        }
    }

    /**
     * {@inheritDoc} Makes a REST call to update the vehicle information.
     */
    @Override
    public void updateVehiculo(Vehiculo vehiculo) throws UpdateException {
        try {
            GenericType<Vehiculo> responseType = new GenericType<Vehiculo>() {
            };
            webClient.edit_XML(vehiculo, vehiculo.getId());
        } catch (Exception e) {
            throw new UpdateException("Error actualizando vehículo con ID: " + vehiculo.getId() + " - " + e.getMessage());
        }
    }

    /**
     * {@inheritDoc} Currently not implemented.
     *
     * @throws UnsupportedOperationException always, as this method is not yet
     * implemented
     */
    @Override
    public List<Vehiculo> findAllVehiculosEntreDates(Date firstDate, Date secondDate) throws SelectException {
        // Si en el futuro implementas este método, recuerda capturar las excepciones
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * {@inheritDoc} If capacity is 0, returns all vehicles. Otherwise, makes a
     * REST call to find vehicles by the specified capacity.
     */
    @Override
    public List<Vehiculo> findAllVehiculosByCapacity(Integer capacity) throws SelectException {
        if (capacity == 0) {
            return findAllVehiculos();
        }
        try {
            GenericType<List<Vehiculo>> responseType = new GenericType<List<Vehiculo>>() {
            };
            List<Vehiculo> vehiclesList = webClient.findByCapacity_XML(responseType, capacity);
            return vehiclesList;
        } catch (WebApplicationException e) {
            throw new SelectException("Error recuperando vehículos por capacidad (" + capacity + "): " + e.getMessage(), e);
        }
    }

    /**
     * {@inheritDoc} Makes a REST call to retrieve all vehicles in the system.
     */
    @Override
    public List<Vehiculo> findAllVehiculos() throws SelectException {
        try {
            GenericType<List<Vehiculo>> responseType = new GenericType<List<Vehiculo>>() {
            };
            List<Vehiculo> vehiclesList = webClient.findAllVehiculos(responseType);
            return vehiclesList;
        } catch (WebApplicationException e) {
            throw new SelectException("Error recuperando vehículos: " + e.getMessage(), e);
        }
    }

    /**
     * {@inheritDoc} Formats the dates and makes a REST call to find vehicles
     * registered within the specified date range.
     */
    @Override
    public List<Vehiculo> findVehiculosByRegistrationDateRange_XML(Date firstDate, Date secondDate) throws SelectException {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String startDate = sdf.format(firstDate);
            String endDate = sdf.format(secondDate);
            return webClient.findVehiculosByRegistrationDateRange_XML(
                    new GenericType<List<Vehiculo>>() {
            }, endDate, startDate
            );
        } catch (WebApplicationException e) {
            throw new SelectException("Error recuperando vehículos por rango de fechas de registro: " + e.getMessage(), e);
        }
    }

    /**
     * {@inheritDoc} Validates the license plate and makes a REST call to find
     * vehicles by plate number.
     */
    @Override
    public List<Vehiculo> findAllVehiculosByPlate(String matricula) throws SelectException {
        if (matricula == null || matricula.isEmpty()) {
            throw new SelectException("License plate cannot be null or empty.");
        }
        try {
            GenericType<List<Vehiculo>> responseType = new GenericType<List<Vehiculo>>() {
            };
            return webClient.findByPlate_XML(responseType, matricula);
        } catch (WebApplicationException e) {
            throw new SelectException("Error recuperando vehículos con matrícula " + matricula + ": " + e.getMessage(), e);
        }
    }

    /**
     * {@inheritDoc} Makes a REST call to create a new vehicle in the system.
     */
    @Override
    public Vehiculo createVehicle(Vehiculo vehiculo) throws CreateException {
        try {
            GenericType<Vehiculo> responseType = new GenericType<Vehiculo>() {
            };
            return webClient.createVehicle(vehiculo, responseType);
        } catch (WebApplicationException e) {
            throw new CreateException("Error creando vehículo: " + e.getMessage(), e);
        }
    }

    /**
     * {@inheritDoc} Makes a REST call to find vehicles with ITV dates before
     * the specified date.
     */
    @Override
    public List<Vehiculo> findVehiclesBeforeDateITV(String endDate) throws SelectException {
        try {
            GenericType<List<Vehiculo>> responseType = new GenericType<List<Vehiculo>>() {
            };
            return webClient.findVehiclesBeforeDateITV(responseType, endDate);
        } catch (WebApplicationException e) {
            throw new SelectException("Error recuperando vehículos con ITV antes de " + endDate + ": " + e.getMessage(), e);
        }
    }

    /**
     * {@inheritDoc} Makes a REST call to find vehicles with ITV dates after the
     * specified date.
     */
    @Override
    public List<Vehiculo> findVehiclesAfterDateITV(String startDate) throws SelectException {
        try {
            GenericType<List<Vehiculo>> responseType = new GenericType<List<Vehiculo>>() {
            };
            return webClient.findVehiclessAfterDateITV(responseType, startDate);
        } catch (WebApplicationException e) {
            throw new SelectException("Error recuperando vehículos con ITV después de " + startDate + ": " + e.getMessage(), e);
        }
    }

    /**
     * {@inheritDoc} Makes a REST call to find vehicles with ITV dates between
     * the specified dates.
     */
    @Override
    public List<Vehiculo> findVehiclesBetweenDatesITV(String endDate, String startDate) throws SelectException {
        try {
            GenericType<List<Vehiculo>> responseType = new GenericType<List<Vehiculo>>() {
            };
            return webClient.findByDateRangeITV(responseType, endDate, startDate);
        } catch (WebApplicationException e) {
            throw new SelectException("Error recuperando vehículos entre fechas de ITV: " + e.getMessage(), e);
        }
    }

    /**
     * {@inheritDoc} Makes a REST call to find vehicles registered before the
     * specified date.
     */
    @Override
    public List<Vehiculo> findVehiclesBeforeDateRegistration(String endDate) throws SelectException {
        try {
            GenericType<List<Vehiculo>> responseType = new GenericType<List<Vehiculo>>() {
            };
            return webClient.findVehiclesBeforeDateRegistration(responseType, endDate);
        } catch (WebApplicationException e) {
            throw new SelectException("Error recuperando vehículos registrados antes de " + endDate + ": " + e.getMessage(), e);
        }
    }

    /**
     * {@inheritDoc} Makes a REST call to find vehicles registered after the
     * specified date.
     */
    @Override
    public List<Vehiculo> findVehiclesAfterDateRegistration(String startDate) throws SelectException {
        try {
            GenericType<List<Vehiculo>> responseType = new GenericType<List<Vehiculo>>() {
            };
            return webClient.findVehiclessAfterDateRegistration(responseType, startDate);
        } catch (WebApplicationException e) {
            throw new SelectException("Error recuperando vehículos registrados después de " + startDate + ": " + e.getMessage(), e);
        }
    }

    /**
     * {@inheritDoc} Makes a REST call to find vehicles registered between the
     * specified dates.
     */
    @Override
    public List<Vehiculo> findVehiclesBetweenDatesRegistration(String endDate, String startDate) throws SelectException {
        try {
            GenericType<List<Vehiculo>> responseType = new GenericType<List<Vehiculo>>() {
            };
            return webClient.findVehiclesBetweenDatesRegistration(responseType, endDate, startDate);
        } catch (WebApplicationException e) {
            throw new SelectException("Error recuperando vehículos registrados entre " + startDate + " y " + endDate + ": " + e.getMessage(), e);
        }
    }
}
