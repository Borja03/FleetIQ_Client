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

public class VehicleManagerImp implements VehicleManager {

    private VehicleRESTClient webClient;

    public VehicleManagerImp() {
        this.webClient = new VehicleRESTClient();
    }

    @Override
    public void updateVehiculo(Vehiculo vehiculo) throws UpdateException {
        try {
            GenericType<Vehiculo> responseType = new GenericType<Vehiculo>() {};
            webClient.edit_XML(vehiculo, vehiculo.getId());
        } catch (ClientErrorException e) {
            throw new UpdateException("Error actualizando vehículo con ID: " + vehiculo.getId() + " - " + e.getMessage(), e);
        } catch (Exception e) {
            throw new UpdateException("Error inesperado al actualizar vehículo: " + e.getMessage(), e);
        }
    }

    @Override
    public void deleteVehiculo(Integer idVehiculo) throws DeleteException {
        if (idVehiculo == null || idVehiculo <= 0) {
            throw new DeleteException("Vehicle ID cannot be null or negative.");
        }
        try {
            webClient.remove(String.valueOf(idVehiculo));
        } catch (ClientErrorException e) {
            throw new DeleteException("Error eliminando vehículo con ID: " + idVehiculo + " - " + e.getMessage(), e);
        } catch (Exception e) {
            throw new DeleteException("Error inesperado al eliminar vehículo: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Vehiculo> findAllVehiculosEntreDates(Date firstDate, Date secondDate) throws SelectException {
        // Si en el futuro implementas este método, recuerda capturar las excepciones
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Vehiculo> findAllVehiculosByCapacity(Integer capacity) throws SelectException {
        if (capacity == 0) {
            return findAllVehiculos();
        }
        try {
            GenericType<List<Vehiculo>> responseType = new GenericType<List<Vehiculo>>() {};
            List<Vehiculo> vehiclesList = webClient.findByCapacity_XML(responseType, capacity);
            return vehiclesList;
        } catch (ClientErrorException e) {
            throw new SelectException("Error recuperando vehículos por capacidad (" + capacity + "): " + e.getMessage(), e);
        } catch (Exception e) {
            throw new SelectException("Error inesperado al recuperar vehículos por capacidad: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Vehiculo> findAllVehiculos() throws SelectException {
        try {
            GenericType<List<Vehiculo>> responseType = new GenericType<List<Vehiculo>>() {};
            List<Vehiculo> vehiclesList = webClient.findAllVehiculos(responseType);
            return vehiclesList;
        } catch (ClientErrorException e) {
            throw new SelectException("Error recuperando todos los vehículos: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new SelectException("Error inesperado al recuperar todos los vehículos: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Vehiculo> findVehiculosByRegistrationDateRange_XML(Date firstDate, Date secondDate) throws SelectException {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String startDate = sdf.format(firstDate);
            String endDate = sdf.format(secondDate);
            return webClient.findVehiculosByRegistrationDateRange_XML(
                    new GenericType<List<Vehiculo>>() {}, endDate, startDate
            );
        } catch (Exception e) {
            throw new SelectException("Error recuperando vehículos por rango de fechas de registro: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Vehiculo> findAllVehiculosByPlate(String matricula) throws SelectException {
        if (matricula == null || matricula.isEmpty()) {
            throw new SelectException("License plate cannot be null or empty.");
        }
        try {
            GenericType<List<Vehiculo>> responseType = new GenericType<List<Vehiculo>>() {};
            return webClient.findByPlate_XML(responseType, matricula);
        } catch (ClientErrorException e) {
            throw new SelectException("Error recuperando vehículos con matrícula " + matricula + ": " + e.getMessage(), e);
        } catch (Exception e) {
            throw new SelectException("Error inesperado al recuperar vehículos con matrícula " + matricula + ": " + e.getMessage(), e);
        }
    }

    @Override
    public Vehiculo createVehicle(Vehiculo vehiculo) throws CreateException {
        try {
            GenericType<Vehiculo> responseType = new GenericType<Vehiculo>() {};
            return webClient.createVehicle(vehiculo, responseType);
        } catch (ClientErrorException e) {
            throw new CreateException("Error creando vehículo: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new CreateException("Error inesperado al crear vehículo: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Vehiculo> findVehiclesBeforeDateITV(String endDate) throws SelectException {
        try {
            GenericType<List<Vehiculo>> responseType = new GenericType<List<Vehiculo>>() {};
            return webClient.findVehiclesBeforeDateITV(responseType, endDate);
        } catch (ClientErrorException e) {
            throw new SelectException("Error recuperando vehículos con ITV antes de " + endDate + ": " + e.getMessage(), e);
        } catch (Exception e) {
            throw new SelectException("Error inesperado al recuperar vehículos con ITV antes de " + endDate + ": " + e.getMessage(), e);
        }
    }

    @Override
    public List<Vehiculo> findVehiclesAfterDateITV(String startDate) throws SelectException {
        try {
            GenericType<List<Vehiculo>> responseType = new GenericType<List<Vehiculo>>() {};
            return webClient.findVehiclessAfterDateITV(responseType, startDate);
        } catch (ClientErrorException e) {
            throw new SelectException("Error recuperando vehículos con ITV después de " + startDate + ": " + e.getMessage(), e);
        } catch (Exception e) {
            throw new SelectException("Error inesperado al recuperar vehículos con ITV después de " + startDate + ": " + e.getMessage(), e);
        }
    }

    @Override
    public List<Vehiculo> findVehiclesBetweenDatesITV(String endDate, String startDate) throws SelectException {
        try {
            GenericType<List<Vehiculo>> responseType = new GenericType<List<Vehiculo>>() {};
            return webClient.findByDateRangeITV(responseType, endDate, startDate);
        } catch (ClientErrorException e) {
            throw new SelectException("Error recuperando vehículos entre fechas de ITV: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new SelectException("Error inesperado al recuperar vehículos entre fechas de ITV: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Vehiculo> findVehiclesBeforeDateRegistration(String endDate) throws SelectException {
        try {
            GenericType<List<Vehiculo>> responseType = new GenericType<List<Vehiculo>>() {};
            return webClient.findVehiclesBeforeDateRegistration(responseType, endDate);
        } catch (ClientErrorException e) {
            throw new SelectException("Error recuperando vehículos registrados antes de " + endDate + ": " + e.getMessage(), e);
        } catch (Exception e) {
            throw new SelectException("Error inesperado al recuperar vehículos registrados antes de " + endDate + ": " + e.getMessage(), e);
        }
    }

    @Override
    public List<Vehiculo> findVehiclesAfterDateRegistration(String startDate) throws SelectException {
        try {
            GenericType<List<Vehiculo>> responseType = new GenericType<List<Vehiculo>>() {};
            return webClient.findVehiclessAfterDateRegistration(responseType, startDate);
        } catch (ClientErrorException e) {
            throw new SelectException("Error recuperando vehículos registrados después de " + startDate + ": " + e.getMessage(), e);
        } catch (Exception e) {
            throw new SelectException("Error inesperado al recuperar vehículos registrados después de " + startDate + ": " + e.getMessage(), e);
        }
    }

    @Override
    public List<Vehiculo> findVehiclesBetweenDatesRegistration(String endDate, String startDate) throws SelectException {
        try {
            GenericType<List<Vehiculo>> responseType = new GenericType<List<Vehiculo>>() {};
            return webClient.findVehiclesBetweenDatesRegistration(responseType, endDate, startDate);
        } catch (ClientErrorException e) {
            throw new SelectException("Error recuperando vehículos registrados entre " + startDate + " y " + endDate + ": " + e.getMessage(), e);
        } catch (Exception e) {
            throw new SelectException("Error inesperado al recuperar vehículos registrados entre " + startDate + " y " + endDate + ": " + e.getMessage(), e);
        }
    }
}
