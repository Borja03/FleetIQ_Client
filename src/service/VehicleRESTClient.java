/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import models.Vehiculo;

/**
 * A REST client for managing vehicle-related operations through web services.
 * This class provides methods to perform CRUD operations and various queries on
 * vehicle data using both XML and JSON formats.
 *
 * Usage example:
 * <pre>
 *     VehicleRESTClient client = new VehicleRESTClient();
 *     // Perform operations
 *     client.close();
 * </pre>
 *
 * @author Adrian
 */
public class VehicleRESTClient {

    /**
     * The web target for the REST service
     */
    private WebTarget webTarget;

    /**
     * The Jersey client instance
     */
    private Client client;
    /**
     * The base URI for the REST service, loaded from configuration
     */
    private static final String BASE_URI = ResourceBundle.getBundle("config/config")
            .getString("RESTful.baseURI");

    /**
     * Creates a new VehicleRESTClient instance using the default base URI from
     * configuration.
     */
    public VehicleRESTClient() {
        client = javax.ws.rs.client.ClientBuilder.newClient();
        webTarget = client.target(BASE_URI).path("vehiculo");
    }

    /**
     * Creates a new VehicleRESTClient instance using a specified URL.
     *
     * @param URL The base URL for the REST service
     */
    public VehicleRESTClient(String URL) {
        client = javax.ws.rs.client.ClientBuilder.newClient();
        webTarget = client.target(URL).path("vehiculo");
    }

    /**
     * Counts the total number of vehicles in the system.
     *
     * @return A string containing the count of vehicles
     * @throws WebApplicationException if there's an error in the REST call
     */
    public String countREST() throws WebApplicationException {
        WebTarget resource = webTarget;
        resource = resource.path("count");
        return resource.request(javax.ws.rs.core.MediaType.TEXT_PLAIN).get(String.class);
    }

    /**
     * Finds vehicles within a specified ITV date range using XML format.
     *
     * @param <T> The response type
     * @param responseType The generic type for the response list
     * @param startDateConverted The start date of the range
     * @param endDateConverted The end date of the range
     * @return A list of vehicles within the specified date range
     * @throws WebApplicationException if there's an error in the REST call
     */
    public <T> T findVehiculosByItvDateRange_XML(GenericType<List<Vehiculo>> responseType, Date startDateConverted, Date endDateConverted) throws WebApplicationException {
        WebTarget resource = webTarget;
        if (endDateConverted != null) {
            resource = resource.queryParam(endDateConverted.toString());
        }
        if (startDateConverted != null) {
            resource = resource.queryParam(startDateConverted.toString());
        }
        resource = resource.path("filterByITVDate");
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get((GenericType<T>) responseType);
    }

    /**
     * Finds vehicles within a specified ITV date range using JSON format.
     *
     * @param <T> The response type
     * @param responseType The class type for the response
     * @param endDate The end date string
     * @param startDate The start date string
     * @return A list of vehicles within the specified date range
     * @throws WebApplicationException if there's an error in the REST call
     */
    public <T> T findVehiculosByItvDateRange_JSON(Class<T> responseType, String endDate, String startDate) throws WebApplicationException {
        WebTarget resource = webTarget;
        if (endDate != null) {
            resource = resource.queryParam("endDate", endDate);
        }
        if (startDate != null) {
            resource = resource.queryParam("startDate", startDate);
        }
        resource = resource.path("filterByITVDate");
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    /**
     * Updates a vehicle using XML format.
     *
     * @param requestEntity The vehicle entity to update
     * @param id The ID of the vehicle to update
     * @throws WebApplicationException if there's an error in the REST call
     */
    public void edit_XML(Vehiculo requestEntity, Integer id) throws WebApplicationException {
        webTarget.path(java.text.MessageFormat.format("{0}", new Object[]{id})).request(javax.ws.rs.core.MediaType.APPLICATION_XML).put(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_XML));
    }

    /**
     * Updates a vehicle using JSON format.
     *
     * @param requestEntity The vehicle entity to update
     * @param id The ID of the vehicle to update
     * @throws WebApplicationException if there's an error in the REST call
     */
    public void edit_JSON(Object requestEntity, String id) throws WebApplicationException {
        webTarget.path(java.text.MessageFormat.format("{0}", new Object[]{id})).request(javax.ws.rs.core.MediaType.APPLICATION_JSON).put(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_JSON));
    }

    /**
     * Finds a vehicle by ID using XML format.
     *
     * @param <T> The response type
     * @param responseType The class type for the response
     * @param id The ID of the vehicle to find
     * @return The found vehicle
     * @throws WebApplicationException if there's an error in the REST call
     */
    public <T> T find_XML(Class<T> responseType, String id) throws WebApplicationException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("{0}", new Object[]{id}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }

    /**
     * Finds a vehicle by ID using JSON format.
     *
     * @param <T> The response type
     * @param responseType The class type for the response
     * @param id The ID of the vehicle to find
     * @return The found vehicle
     * @throws WebApplicationException if there's an error in the REST call
     */
    public <T> T find_JSON(Class<T> responseType, String id) throws WebApplicationException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("{0}", new Object[]{id}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    /**
     * Finds vehicles within a specified range using XML format.
     *
     * @param <T> The response type
     * @param responseType The class type for the response
     * @param from The starting index
     * @param to The ending index
     * @return A list of vehicles within the specified range
     * @throws WebApplicationException if there's an error in the REST call
     */
    public <T> T findRange_XML(Class<T> responseType, String from, String to) throws WebApplicationException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("{0}/{1}", new Object[]{from, to}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }

    /**
     * Finds vehicles within a specified range using JSON format.
     *
     * @param <T> The response type
     * @param responseType The class type for the response
     * @param from The starting index
     * @param to The ending index
     * @return A list of vehicles within the specified range
     * @throws WebApplicationException if there's an error in the REST call
     */
    public <T> T findRange_JSON(Class<T> responseType, String from, String to) throws WebApplicationException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("{0}/{1}", new Object[]{from, to}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    /**
     * Finds vehicles by capacity using XML format.
     *
     * @param <T> The response type
     * @param responseType The generic type for the response
     * @param capacity The capacity to search for
     * @return A list of vehicles with the specified capacity
     * @throws WebApplicationException if there's an error in the REST call
     */
    public <T> T findByCapacity_XML(GenericType<T> responseType, Integer capacity) throws WebApplicationException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("capacity/{0}", new Object[]{capacity.toString()}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }

    /**
     * Finds vehicles by capacity using JSON format.
     *
     * @param <T> The response type
     * @param responseType The class type for the response
     * @param capacity The capacity to search for
     * @return A list of vehicles with the specified capacity
     * @throws WebApplicationException if there's an error in the REST call
     */
    public <T> T findByCapacity_JSON(Class<T> responseType, String capacity) throws WebApplicationException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("capacity/{0}", new Object[]{capacity}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    /**
     * Creates a new vehicle using JSON format.
     *
     * @param requestEntity The vehicle entity to create
     * @throws WebApplicationException if there's an error in the REST call
     */
    public void create_JSON(Object requestEntity) throws WebApplicationException {
        webTarget.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).post(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_JSON));
    }

    /**
     * Finds vehicles within a registration date range using XML format.
     *
     * @param <T> The response type
     * @param responseType The generic type for the response
     * @param endDate The end date string
     * @param startDate The start date string
     * @return A list of vehicles within the specified registration date range
     * @throws WebApplicationException if there's an error in the REST call
     */
    public <T> T findVehiculosByRegistrationDateRange_XML(GenericType<T> responseType, String endDate, String startDate) throws WebApplicationException {
        WebTarget resource = webTarget;

        if (endDate != null) {
            resource = resource.queryParam("endDate", endDate);
        }
        if (startDate != null) {
            resource = resource.queryParam("startDate", startDate);
        }

        resource = resource.path("filterByITVRegistration");
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }

    /**
     * Finds vehicles within a registration date range using JSON format.
     *
     * @param <T> The response type
     * @param responseType The class type for the response
     * @param endDate The end date string
     * @param startDate The start date string
     * @return A list of vehicles within the specified registration date range
     * @throws WebApplicationException if there's an error in the REST call
     */
    public <T> T findVehiculosByRegistrationDateRange_JSON(Class<T> responseType, String endDate, String startDate) throws WebApplicationException {
        WebTarget resource = webTarget;
        if (endDate != null) {
            resource = resource.queryParam("endDate", endDate);
        }
        if (startDate != null) {
            resource = resource.queryParam("startDate", startDate);
        }
        resource = resource.path("filterByITVRegistration");
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    /**
     * Retrieves all vehicles in XML format.
     *
     * @param responseType The generic type for the response list
     * @return A list of all vehicles
     * @throws WebApplicationException if there's an error in the REST call
     */
    public List<Vehiculo> findAllVehiculos(GenericType<List<Vehiculo>> responseType) throws WebApplicationException {
        WebTarget resource = webTarget;
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML)
                .get(responseType);
    }

    /**
     * Retrieves all vehicles in JSON format.
     *
     * @param <T> The response type
     * @param responseType The class type for the response
     * @return A list of all vehicles
     * @throws ClientErrorException if there's an error in the REST call
     */
    public <T> T findAll_JSON(Class<T> responseType) throws ClientErrorException {
        WebTarget resource = webTarget;
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    /**
     * Finds vehicles by license plate using JSON format.
     *
     * @param <T> The response type
     * @param responseType The class type for the response
     * @param matricula The license plate to search for
     * @return The vehicle with the specified license plate
     * @throws WebApplicationException if there's an error in the REST call
     */
    public <T> T findByPlate_JSON(Class<T> responseType, String matricula) throws WebApplicationException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("plate/{0}", new Object[]{matricula}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    /**
     * Removes a vehicle by ID.
     *
     * @param id The ID of the vehicle to remove
     * @throws WebApplicationException if there's an error in the REST call
     */
    public void remove(String id) throws WebApplicationException {
        webTarget.path(java.text.MessageFormat.format("{0}", new Object[]{id})).request().delete();
    }

    /**
     * Closes the client connection.
     */
    public void close() {
        client.close();
    }

    /**
     * Finds vehicles by license plate using XML format.
     *
     * @param responseType The generic type for the response list
     * @param matricula The license plate to search for
     * @return A list of vehicles with the specified license plate
     */
    public List<Vehiculo> findByPlate_XML(GenericType<List<Vehiculo>> responseType, String matricula) {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("plate/{0}", new Object[]{matricula}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }

    /**
     * Creates a new vehicle using XML format.
     *
     * @param requestEntity The vehicle entity to create
     * @param responseType The generic type for the response
     * @return The created vehicle entity
     * @throws WebApplicationException if there's an error in the REST call
     */
    public Vehiculo createVehicle(Vehiculo requestEntity, GenericType<Vehiculo> responseType) throws WebApplicationException {
        return webTarget.request(javax.ws.rs.core.MediaType.APPLICATION_XML)
                .post(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_XML), responseType);
    }

    /**
     * Finds vehicles with registration dates between two specified dates.
     *
     * @param responseType The generic type for the response list
     * @param endDate The end date for the search range
     * @param startDate The start date for the search range
     * @return A list of vehicles registered between the specified dates
     * @throws WebApplicationException if there's an error in the REST call
     */
    public List<Vehiculo> findVehiclesBetweenDatesRegistration(GenericType<List<Vehiculo>> responseType, String endDate, String startDate) throws WebApplicationException {
        WebTarget resource = webTarget;
        if (endDate != null) {
            resource = resource.queryParam("endDate", endDate);
        }
        if (startDate != null) {
            resource = resource.queryParam("startDate", startDate);
        }
        resource = resource.path("date/betweenRegistration");
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }

    /**
     * Finds vehicles registered after a specified date.
     *
     * @param responseType The generic type for the response list
     * @param startDate The date after which to search for registrations
     * @return A list of vehicles registered after the specified date
     * @throws WebApplicationException if there's an error in the REST call
     */
    public List<Vehiculo> findVehiclessAfterDateRegistration(GenericType<List<Vehiculo>> responseType, String startDate) throws WebApplicationException {
        WebTarget resource = webTarget;
        if (startDate != null) {
            resource = resource.queryParam("startDate", startDate);
        }
        resource = resource.path("date/afterRegistration");
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }

    /**
     * Finds vehicles with ITV dates before a specified date.
     *
     * @param responseType The generic type for the response list
     * @param endDate The date before which to search for ITV dates
     * @return A list of vehicles with ITV dates before the specified date
     * @throws WebApplicationException if there's an error in the REST call
     */
    public List<Vehiculo> findVehiclesBeforeDateITV(GenericType<List<Vehiculo>> responseType, String endDate) throws WebApplicationException {
        WebTarget resource = webTarget;
        if (endDate != null) {
            resource = resource.queryParam("endDate", endDate);
        }
        resource = resource.path("date/beforeITV");
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }

    /**
     * Finds vehicles registered before a specified date.
     *
     * @param responseType The generic type for the response list
     * @param endDate The date before which to search for registrations
     * @return A list of vehicles registered before the specified date
     * @throws WebApplicationException if there's an error in the REST call
     */
    public List<Vehiculo> findVehiclesBeforeDateRegistration(GenericType<List<Vehiculo>> responseType, String endDate) throws WebApplicationException {
        WebTarget resource = webTarget;
        if (endDate != null) {
            resource = resource.queryParam("endDate", endDate);
        }
        resource = resource.path("date/beforerRegistration");
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }

    /**
     * Finds vehicles with ITV dates between two specified dates.
     *
     * @param responseType The generic type for the response list
     * @param endDate The end date for the ITV date range
     * @param startDate The start date for the ITV date range
     * @return A list of vehicles with ITV dates within the specified range
     * @throws WebApplicationException if there's an error in the REST call
     */
    public List<Vehiculo> findByDateRangeITV(GenericType<List<Vehiculo>> responseType, String endDate, String startDate) throws WebApplicationException {
        WebTarget resource = webTarget;
        if (endDate != null) {
            resource = resource.queryParam("endDate", endDate);
        }
        if (startDate != null) {
            resource = resource.queryParam("startDate", startDate);
        }
        resource = resource.path("date/betweenITV");
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }

    /**
     * Finds vehicles with ITV dates after a specified date.
     *
     * @param responseType The generic type for the response list
     * @param startDate The date after which to search for ITV dates
     * @return A list of vehicles with ITV dates after the specified date
     * @throws WebApplicationException if there's an error in the REST call
     */
    public List<Vehiculo> findVehiclessAfterDateITV(GenericType<List<Vehiculo>> responseType, String startDate) throws WebApplicationException {
        WebTarget resource = webTarget;
        if (startDate != null) {
            resource = resource.queryParam("startDate", startDate);
        }
        resource = resource.path("date/afterITV");
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }

}
