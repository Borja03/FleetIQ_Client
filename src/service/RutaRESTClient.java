/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import java.text.MessageFormat;
import java.util.List;
import java.util.ResourceBundle;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import logicInterface.RutaManager;
import models.Paquete;
import models.Ruta;

/**
 * Jersey REST client generated for REST resource:RutaFacadeREST [ruta]. This
 * client provides various methods for interacting with the Ruta RESTful
 * service, allowing CRUD operations and filtering based on different criteria
 * such as distance, time, and date.
 *
 * USAGE:
 * <pre>
 *        RutaRESTClient client = new RutaRESTClient();
 *        Object response = client.XXX(...);
 *        // Do whatever with response
 *        client.close();
 * </pre>
 *
 * @author Borja
 */
public class RutaRESTClient implements RutaManager {

    /**
     * WebTarget object for accessing the REST resource.
     */
    private WebTarget webTarget;

    /**
     * Client object used for making requests to the REST service.
     */
    private Client client;

    /**
     * Base URI for the RESTful service.
     */
    private static final String BASE_URI = ResourceBundle.getBundle("config/config")
            .getString("RESTful.baseURI");

    /**
     * Constructor that initializes the client and WebTarget for accessing the
     * RESTful service.
     */
    public RutaRESTClient() {
        client = javax.ws.rs.client.ClientBuilder.newClient();
        webTarget = client.target(BASE_URI).path("ruta");
    }

    /**
     * Filters routes based on a distance that is less than the specified value.
     *
     * @param responseType The type of response expected.
     * @param distancia The distance to filter by.
     * @param <T> The type of the response.
     * @return The filtered list of routes.
     * @throws WebApplicationException if the request fails.
     */
    public <T> T filterDistanciaMenor_XML(GenericType<T> responseType, String distancia) throws WebApplicationException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("filterDistanciaMenor/{0}", new Object[]{distancia}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }

    /**
     * Filters routes based on a distance that is less than the specified value.
     *
     * @param responseType The type of response expected.
     * @param distancia The distance to filter by.
     * @param <T> The type of the response.
     * @return The filtered list of routes.
     * @throws WebApplicationException if the request fails.
     */
    public <T> T filterDistanciaMenor_JSON(Class<T> responseType, String distancia) throws WebApplicationException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("filterDistanciaMenor/{0}", new Object[]{distancia}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    /**
     * Filters routes based on a distance that is equal to the specified value.
     *
     * @param responseType The type of response expected.
     * @param distancia The distance to filter by.
     * @param <T> The type of the response.
     * @return The filtered list of routes.
     * @throws WebApplicationException if the request fails.
     */
    public <T> T filterDistanciaIgual_XML(GenericType<T> responseType, String distancia) throws WebApplicationException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("filterDistanciaIgual/{0}", new Object[]{distancia}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }

    /**
     * Filters routes based on a distance that is equal to the specified value.
     *
     * @param responseType The type of response expected.
     * @param distancia The distance to filter by.
     * @param <T> The type of the response.
     * @return The filtered list of routes.
     * @throws WebApplicationException if the request fails.
     */
    public <T> T filterDistanciaIgual_JSON(Class<T> responseType, String distancia) throws WebApplicationException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("filterDistanciaIgual/{0}", new Object[]{distancia}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    /**
     * Edits a route with the specified data in XML format.
     *
     * @param requestEntity The entity to update the route with.
     * @param id The ID of the route to edit.
     * @throws WebApplicationException if the request fails.
     */
    public void edit_XML(Object requestEntity, String id) throws WebApplicationException {
        try {
            Response response = webTarget
                    .path(MessageFormat.format("{0}", new Object[]{id}))
                    .request(MediaType.APPLICATION_XML)
                    .put(Entity.entity(requestEntity, MediaType.APPLICATION_XML));
        } catch (Exception ex) {
            // Captura errores de conexión (servidor caído, timeout, etc)
            throw new WebApplicationException("Error de conexión: " + ex.getMessage(), ex);
        }
    }

    /**
     * Edits a route with the specified data in JSON format.
     *
     * @param requestEntity The entity to update the route with.
     * @param id The ID of the route to edit.
     * @throws WebApplicationException if the request fails.
     */
    public void edit_JSON(Object requestEntity, String id) throws WebApplicationException {
        webTarget.path(java.text.MessageFormat.format("{0}", new Object[]{id})).request(javax.ws.rs.core.MediaType.APPLICATION_JSON).put(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_JSON));
    }

    /**
     * Finds a range of routes specified by the "from" and "to" values in XML
     * format.
     *
     * @param responseType The type of response expected.
     * @param from The start value of the range.
     * @param to The end value of the range.
     * @param <T> The type of the response.
     * @return The list of routes within the specified range.
     * @throws WebApplicationException if the request fails.
     */
    public <T> T findRange_XML(Class<T> responseType, String from, String to) throws WebApplicationException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("{0}/{1}", new Object[]{from, to}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }

    /**
     * Finds a range of routes specified by the "from" and "to" values in JSON
     * format.
     *
     * @param responseType The type of response expected.
     * @param from The start value of the range.
     * @param to The end value of the range.
     * @param <T> The type of the response.
     * @return The list of routes within the specified range.
     * @throws WebApplicationException if the request fails.
     */
    public <T> T findRange_JSON(Class<T> responseType, String from, String to) throws WebApplicationException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("{0}/{1}", new Object[]{from, to}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    /**
     * Filters routes based on a time that is greater than the specified value.
     *
     * @param responseType The type of response expected.
     * @param tiempo The time to filter by.
     * @param <T> The type of the response.
     * @return The filtered list of routes.
     * @throws WebApplicationException if the request fails.
     */
    public <T> T filterTiempoMayor_XML(GenericType<T> responseType, String tiempo) throws WebApplicationException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("filterTiempoMayor/{0}", new Object[]{tiempo}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }

    /**
     * Filters routes based on a time that is greater than the specified value.
     *
     * @param responseType The type of response expected.
     * @param tiempo The time to filter by.
     * @param <T> The type of the response.
     * @return The filtered list of routes.
     * @throws WebApplicationException if the request fails.
     */
    public <T> T filterTiempoMayor_JSON(Class<T> responseType, String tiempo) throws WebApplicationException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("filterTiempoMayor/{0}", new Object[]{tiempo}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    /**
     * Filters routes based on a time that is equal to the specified value.
     *
     * @param responseType The type of response expected.
     * @param tiempo The time to filter by.
     * @param <T> The type of the response.
     * @return The filtered list of routes.
     * @throws WebApplicationException if the request fails.
     */
    public <T> T filterTiempoIgual_XML(GenericType<T> responseType, String tiempo) throws WebApplicationException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("filterTiempoIgual/{0}", new Object[]{tiempo}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }

    /**
     * Filters routes based on a time that is equal to the specified value.
     *
     * @param responseType The type of response expected.
     * @param tiempo The time to filter by.
     * @param <T> The type of the response.
     * @return The filtered list of routes.
     * @throws WebApplicationException if the request fails.
     */
    public <T> T filterTiempoIgual_JSON(Class<T> responseType, String tiempo) throws WebApplicationException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("filterTiempoIgual/{0}", new Object[]{tiempo}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    /**
     * Retrieves all routes in XML format.
     *
     * @param responseType The type of response expected.
     * @param <T> The type of the response.
     * @return A list of all routes.
     * @throws WebApplicationException if the request fails.
     */
    public <T> T findAll_XML(GenericType<T> responseType) throws WebApplicationException {
        WebTarget resource = webTarget;
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }

    /**
     * Retrieves all routes in JSON format.
     *
     * @param responseType The type of response expected.
     * @param <T> The type of the response.
     * @return A list of all routes.
     * @throws WebApplicationException if the request fails.
     */
    public <T> T findAll_JSON(Class<T> responseType) throws WebApplicationException {
        WebTarget resource = webTarget;
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    /**
     * Removes a route by its ID.
     *
     * @param id The ID of the route to remove.
     * @throws WebApplicationException if the request fails.
     */
    public void remove(String id) throws WebApplicationException {
        webTarget.path(java.text.MessageFormat.format("{0}", new Object[]{id})).request().delete();
    }

    /**
     * Filters routes based on a distance that is greater than the specified
     * value.
     *
     * @param responseType The type of response expected.
     * @param distancia The distance to filter by.
     * @param <T> The type of the response.
     * @return The filtered list of routes.
     * @throws WebApplicationException if the request fails.
     */
    public <T> T filterDistanciaMayor_XML(GenericType<T> responseType, String distancia) throws WebApplicationException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("filterDistanciaMayor/{0}", new Object[]{distancia}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }

    /**
     * Filters routes based on a distance that is greater than the specified
     * value.
     *
     * @param responseType The type of response expected.
     * @param distancia The distance to filter by.
     * @param <T> The type of the response.
     * @return The filtered list of routes.
     * @throws WebApplicationException if the request fails.
     */
    public <T> T filterDistanciaMayor_JSON(Class<T> responseType, String distancia) throws WebApplicationException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("filterDistanciaMayor/{0}", new Object[]{distancia}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    /**
     * Counts the total number of routes.
     *
     * @return The total number of routes.
     * @throws WebApplicationException if the request fails.
     */
    public String countREST() throws WebApplicationException {
        WebTarget resource = webTarget;
        resource = resource.path("count");
        return resource.request(javax.ws.rs.core.MediaType.TEXT_PLAIN).get(String.class);
    }

    /**
     * Retrieves a route by its ID in JSON format.
     *
     * @param responseType The type of response expected.
     * @param id The ID of the route to retrieve.
     * @param <T> The type of the response.
     * @return The route with the specified ID.
     * @throws WebApplicationException if the request fails.
     */
    public <T> T find_JSON(Class<T> responseType, String id) throws WebApplicationException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("{0}", new Object[]{id}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    /**
     * Creates a new route with the specified data in XML format.
     *
     * @param requestEntity The entity to create the new route.
     * @throws WebApplicationException if the request fails.
     */
    public void create_XML(Object requestEntity) throws WebApplicationException {
        webTarget.request(javax.ws.rs.core.MediaType.APPLICATION_XML).post(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_XML));
    }

    /**
     * Creates a new route with the specified data in JSON format.
     *
     * @param requestEntity The entity to create the new route.
     * @throws WebApplicationException if the request fails.
     */
    public void create_JSON(Object requestEntity) throws WebApplicationException {
        webTarget.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).post(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_JSON));
    }

    /**
     * Filters records based on two dates in XML format.
     *
     * @param responseType The type of response expected.
     * @param firstDate The first date to filter by.
     * @param secondDate The second date to filter by.
     * @param <T> The type of the response.
     * @return The filtered list of records between the two dates.
     * @throws WebApplicationException If the request fails.
     */
    public <T> T filterBy2Dates_XML(GenericType<T> responseType, String firstDate, String secondDate) throws WebApplicationException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("filterBy2Dates/{0}/{1}", new Object[]{firstDate, secondDate}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }

    /**
     * Filters records based on two dates in JSON format.
     *
     * @param responseType The type of response expected.
     * @param firstDate The first date to filter by.
     * @param secondDate The second date to filter by.
     * @param <T> The type of the response.
     * @return The filtered list of records between the two dates.
     * @throws WebApplicationException If the request fails.
     */
    public <T> T filterBy2Dates_JSON(GenericType<T> responseType, String firstDate, String secondDate) throws WebApplicationException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("filterBy2Dates/{0}/{1}", new Object[]{firstDate, secondDate}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    /**
     * Filters records based on time being less than the specified value in XML
     * format.
     *
     * @param responseType The type of response expected.
     * @param tiempo The time value to filter by.
     * @param <T> The type of the response.
     * @return The filtered list of records with time less than the specified
     * value.
     * @throws WebApplicationException If the request fails.
     */
    public <T> T filterTiempoMenor_XML(GenericType<T> responseType, String tiempo) throws WebApplicationException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("filterTiempoMenor/{0}", new Object[]{tiempo}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }

    /**
     * Filters records based on time being less than the specified value in JSON
     * format.
     *
     * @param responseType The type of response expected.
     * @param tiempo The time value to filter by.
     * @param <T> The type of the response.
     * @return The filtered list of records with time less than the specified
     * value.
     * @throws WebApplicationException If the request fails.
     */
    public <T> T filterTiempoMenor_JSON(Class<T> responseType, String tiempo) throws WebApplicationException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("filterTiempoMenor/{0}", new Object[]{tiempo}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    /**
     * Finds a record based on an Integer localizador value in XML format.
     *
     * @param responseType The type of response expected.
     * @param localizador The localizador value to filter by.
     * @param <T> The type of the response.
     * @return The record with the specified localizador value.
     * @throws WebApplicationException If the request fails.
     */
    public <T> T findByLocalizadorInteger_XML(Class<T> responseType, Integer localizador) throws WebApplicationException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("findByLocalizadorInteger/{0}", new Object[]{localizador}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }

    /**
     * Finds a record based on an Integer localizador value in JSON format.
     *
     * @param responseType The type of response expected.
     * @param localizador The localizador value to filter by.
     * @param <T> The type of the response.
     * @return The record with the specified localizador value.
     * @throws WebApplicationException If the request fails.
     */
    public <T> T findByLocalizadorInteger_JSON(Class<T> responseType, Integer localizador) throws WebApplicationException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("findByLocalizadorInteger/{0}", new Object[]{localizador}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    /**
     * Closes the client and releases resources.
     */
    public void close() {
        client.close();
    }
}
