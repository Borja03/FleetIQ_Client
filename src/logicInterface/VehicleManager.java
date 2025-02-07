/**
 * Interface defining vehicle management operations.
 * This interface provides methods for CRUD operations and various search functionalities
 * related to vehicle management within the system.
 *
 * @author Adrian
 * @version 1.0
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

    /**
     * Creates a new vehicle in the system.
     *
     * @param vehiculo The vehicle object to be created
     * @return The created vehicle object
     * @throws CreateException If there is an error during vehicle creation
     */
    Vehiculo createVehicle(Vehiculo vehiculo) throws CreateException;

    /**
     * Updates an existing vehicle's information.
     *
     * @param vehiculo The vehicle object with updated information
     * @throws UpdateException If there is an error during vehicle update
     */
    void updateVehiculo(Vehiculo vehiculo) throws UpdateException;

    /**
     * Deletes a vehicle from the system by its ID.
     *
     * @param idVehiculo The ID of the vehicle to be deleted
     * @throws DeleteException If there is an error during vehicle deletion
     */
    void deleteVehiculo(Integer idVehiculo) throws DeleteException;

    /**
     * Retrieves all vehicles from the system.
     *
     * @return List of all vehicles
     * @throws SelectException If there is an error retrieving the vehicles
     */
    List<Vehiculo> findAllVehiculos() throws SelectException;

    /**
     * Finds vehicles registered between two dates.
     *
     * @param firstDate Starting date of the range
     * @param secondDate Ending date of the range
     * @return List of vehicles registered within the specified date range
     * @throws SelectException If there is an error retrieving the vehicles
     */
    List<Vehiculo> findAllVehiculosEntreDates(Date firstDate, Date secondDate) throws SelectException;

    /**
     * Finds vehicles by their license plate number.
     *
     * @param matricula The license plate number to search for
     * @return List of vehicles matching the specified plate number
     * @throws SelectException If there is an error retrieving the vehicles
     */
    List<Vehiculo> findAllVehiculosByPlate(String matricula) throws SelectException;

    /**
     * Finds vehicles by their passenger capacity.
     *
     * @param capacity The passenger capacity to search for
     * @return List of vehicles with the specified capacity
     * @throws SelectException If there is an error retrieving the vehicles
     */
    List<Vehiculo> findAllVehiculosByCapacity(Integer capacity) throws SelectException;

    /**
     * Finds vehicles registered between two dates (XML format).
     *
     * @param firstDate Starting date of the range
     * @param secondDate Ending date of the range
     * @return List of vehicles in XML format
     * @throws SelectException If there is an error retrieving the vehicles
     */
    List<Vehiculo> findVehiculosByRegistrationDateRange_XML(Date firstDate, Date secondDate) throws SelectException;

    /**
     * Finds vehicles with ITV (Technical Inspection) date before specified
     * date.
     *
     * @param endDate The end date in string format
     * @return List of vehicles with ITV before the specified date
     * @throws SelectException If there is an error retrieving the vehicles
     */
    public List<Vehiculo> findVehiclesBeforeDateITV(String endDate) throws SelectException;

    /**
     * Finds vehicles with ITV (Technical Inspection) date after specified date.
     *
     * @param startDate The start date in string format
     * @return List of vehicles with ITV after the specified date
     * @throws SelectException If there is an error retrieving the vehicles
     */
    public List<Vehiculo> findVehiclesAfterDateITV(String startDate) throws SelectException;

    /**
     * Finds vehicles with ITV (Technical Inspection) date between specified
     * dates.
     *
     * @param endDate The end date in string format
     * @param startDate The start date in string format
     * @return List of vehicles with ITV between the specified dates
     * @throws SelectException If there is an error retrieving the vehicles
     */
    public List<Vehiculo> findVehiclesBetweenDatesITV(String endDate, String startDate) throws SelectException;

    /**
     * Finds vehicles with registration date before specified date.
     *
     * @param endDate The end date in string format
     * @return List of vehicles registered before the specified date
     * @throws SelectException If there is an error retrieving the vehicles
     */
    public List<Vehiculo> findVehiclesBeforeDateRegistration(String endDate) throws SelectException;

    /**
     * Finds vehicles with registration date after specified date.
     *
     * @param startDate The start date in string format
     * @return List of vehicles registered after the specified date
     * @throws SelectException If there is an error retrieving the vehicles
     */
    public List<Vehiculo> findVehiclesAfterDateRegistration(String startDate) throws SelectException;

    /**
     * Finds vehicles with registration date between specified dates.
     *
     * @param endDate The end date in string format
     * @param startDate The start date in string format
     * @return List of vehicles registered between the specified dates
     * @throws SelectException If there is an error retrieving the vehicles
     */
    public List<Vehiculo> findVehiclesBetweenDatesRegistration(String endDate, String startDate) throws SelectException;

}
