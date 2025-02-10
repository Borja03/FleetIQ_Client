package logicInterface;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.GenericType;

/**
 * Interface for managing Ruta entities.
 * <p>
 * This interface defines various methods to create, read, update, delete, and
 * filter Ruta entities using both XML and JSON formats. It includes
 * methods to filter by distance and time, as well as methods for working with
 * date ranges and counts of entities.
 *
 * @author Borja
 */
public interface RutaManager {

    /**
     * Filters the Ruta entities by distance less than the specified
     * value (XML format).
     *
     * @param responseType The type of the response object
     * @param distancia The distance value to filter by
     * @param <T> The type of the response object
     * @return A filtered list of Ruta entities
     * @throws WebApplicationException if there is an error during the request
     */
    public <T> T filterDistanciaMenor_XML(GenericType<T> responseType, String distancia) throws WebApplicationException;

    /**
     * Filters the Ruta entities by distance less than the specified
     * value (JSON format).
     *
     * @param responseType The type of the response object
     * @param distancia The distance value to filter by
     * @param <T> The type of the response object
     * @return A filtered list of Ruta entities
     * @throws WebApplicationException if there is an error during the request
     */
    public <T> T filterDistanciaMenor_JSON(Class<T> responseType, String distancia) throws WebApplicationException;

    /**
     * Filters the Ruta entities by distance equal to the specified
     * value (XML format).
     *
     * @param responseType The type of the response object
     * @param distancia The distance value to filter by
     * @param <T> The type of the response object
     * @return A filtered list of Ruta entities
     * @throws WebApplicationException if there is an error during the request
     */
    public <T> T filterDistanciaIgual_XML(GenericType<T> responseType, String distancia) throws WebApplicationException;

    /**
     * Filters the Ruta entities by distance equal to the specified
     * value (JSON format).
     *
     * @param responseType The type of the response object
     * @param distancia The distance value to filter by
     * @param <T> The type of the response object
     * @return A filtered list of Rutaentities
     * @throws WebApplicationException if there is an error during the request
     */
    public <T> T filterDistanciaIgual_JSON(Class<T> responseType, String distancia) throws WebApplicationException;

    /**
     * Updates an existing Ruta entity (XML format).
     *
     * @param requestEntity The updated Ruta entity
     * @param id The ID of the Ruta to be updated
     * @throws WebApplicationException if there is an error during the request
     */
    public void edit_XML(Object requestEntity, String id) throws WebApplicationException;

    /**
     * Updates an existing Ruta entity (JSON format).
     *
     * @param requestEntity The updated Ruta entity
     * @param id The ID of the Ruta to be updated
     * @throws WebApplicationException if there is an error during the request
     */
    public void edit_JSON(Object requestEntity, String id) throws WebApplicationException;

    /**
     * Retrieves a range of Ruta entities (XML format).
     *
     * @param responseType The type of the response object
     * @param from The starting index
     * @param to The ending index
     * @param <T> The type of the response object
     * @return A range of Ruta entities
     * @throws WebApplicationException if there is an error during the request
     */
    public <T> T findRange_XML(Class<T> responseType, String from, String to) throws WebApplicationException;

    /**
     * Retrieves a range of Ruta entities (JSON format).
     *
     * @param responseType The type of the response object
     * @param from The starting index
     * @param to The ending index
     * @param <T> The type of the response object
     * @return A range of Ruta entities
     * @throws WebApplicationException if there is an error during the request
     */
    public <T> T findRange_JSON(Class<T> responseType, String from, String to) throws WebApplicationException;

    /**
     * Filters the Ruta entities by time greater than the specified
     * value (XML format).
     *
     * @param responseType The type of the response object
     * @param tiempo The time value to filter by
     * @param <T> The type of the response object
     * @return A filtered list of Ruta entities
     * @throws WebApplicationException if there is an error during the request
     */
    public <T> T filterTiempoMayor_XML(GenericType<T> responseType, String tiempo) throws WebApplicationException;

    /**
     * Filters the Ruta entities by time greater than the specified
     * value (JSON format).
     *
     * @param responseType The type of the response object
     * @param tiempo The time value to filter by
     * @param <T> The type of the response object
     * @return A filtered list of Ruta entities
     * @throws WebApplicationException if there is an error during the request
     */
    public <T> T filterTiempoMayor_JSON(Class<T> responseType, String tiempo) throws WebApplicationException;

    /**
     * Filters the Ruta entities by time equal to the specified value
     * (XML format).
     *
     * @param responseType The type of the response object
     * @param tiempo The time value to filter by
     * @param <T> The type of the response object
     * @return A filtered list of Ruta entities
     * @throws WebApplicationException if there is an error during the request
     */
    public <T> T filterTiempoIgual_XML(GenericType<T> responseType, String tiempo) throws WebApplicationException;

    /**
     * Filters the Ruta entities by time equal to the specified value
     * (JSON format).
     *
     * @param responseType The type of the response object
     * @param tiempo The time value to filter by
     * @param <T> The type of the response object
     * @return A filtered list of Ruta entities
     * @throws WebApplicationException if there is an error during the request
     */
    public <T> T filterTiempoIgual_JSON(Class<T> responseType, String tiempo) throws WebApplicationException;

    /**
     * Retrieves all Ruta entities (XML format).
     *
     * @param responseType The type of the response object
     * @param <T> The type of the response object
     * @return A list of all Ruta entities
     * @throws WebApplicationException if there is an error during the request
     */
    public <T> T findAll_XML(GenericType<T> responseType) throws WebApplicationException;

    /**
     * Retrieves all Ruta entities (JSON format).
     *
     * @param responseType The type of the response object
     * @param <T> The type of the response object
     * @return A list of all Ruta entities
     * @throws WebApplicationException if there is an error during the request
     */
    public <T> T findAll_JSON(Class<T> responseType) throws WebApplicationException;

    /**
     * Removes a Rutaentity by its ID.
     *
     * @param id The ID of the Ruta to be removed
     * @throws WebApplicationException if there is an error during the request
     */
    public void remove(String id) throws WebApplicationException;

    /**
     * Filters the Ruta entities by distance greater than the specified
     * value (XML format).
     *
     * @param responseType The type of the response object
     * @param distancia The distance value to filter by
     * @param <T> The type of the response object
     * @return A filtered list of Ruta entities
     * @throws WebApplicationException if there is an error during the request
     */
    public <T> T filterDistanciaMayor_XML(GenericType<T> responseType, String distancia) throws WebApplicationException;

    /**
     * Filters the Ruta entities by distance greater than the specified
     * value (JSON format).
     *
     * @param responseType The type of the response object
     * @param distancia The distance value to filter by
     * @param <T> The type of the response object
     * @return A filtered list of Ruta entities
     * @throws WebApplicationException if there is an error during the request
     */
    public <T> T filterDistanciaMayor_JSON(Class<T> responseType, String distancia) throws WebApplicationException;

    /**
     * Retrieves the count of Rutaentities.
     *
     * @return The count of Ruta entities
     * @throws WebApplicationException if there is an error during the request
     */
    public String countREST() throws WebApplicationException;

    /**
     * Retrieves a specific Ruta entity by its ID (JSON format).
     *
     * @param responseType The type of the response object
     * @param id The ID of the Ruta to retrieve
     * @param <T> The type of the response object
     * @return The specified Ruta entity
     * @throws WebApplicationException if there is an error during the request
     */
    public <T> T find_JSON(Class<T> responseType, String id) throws WebApplicationException;

    /**
     * Creates a new Ruta entity (XML format).
     *
     * @param requestEntity The Ruta entity to create
     * @throws WebApplicationException if there is an error during the request
     */
    public void create_XML(Object requestEntity) throws WebApplicationException;

    /**
     * Creates a new Ruta entity (JSON format).
     *
     * @param requestEntity The Ruta entity to create
     * @throws WebApplicationException if there is an error during the request
     */
    public void create_JSON(Object requestEntity) throws WebApplicationException;

    /**
     * Filters the Ruta entities by two date values (XML format).
     *
     * @param responseType The type of the response object
     * @param firstDate The first date to filter by
     * @param secondDate The second date to filter by
     * @param <T> The type of the response object
     * @return A filtered list of Ruta entities
     * @throws WebApplicationException if there is an error during the request
     */
    public <T> T filterBy2Dates_XML(GenericType<T> responseType, String firstDate, String secondDate) throws WebApplicationException;

    /**
     * Filters the Ruta entities by two date values (JSON format).
     *
     * @param responseType The type of the response object
     * @param firstDate The first date to filter by
     * @param secondDate The second date to filter by
     * @param <T> The type of the response object
     * @return A filtered list of Ruta entities
     * @throws WebApplicationException if there is an error during the request
     */
    public <T> T filterBy2Dates_JSON(GenericType<T> responseType, String firstDate, String secondDate) throws WebApplicationException;

    /**
     * Filters the Ruta entities by time less than the specified value
     * (XML format).
     *
     * @param responseType The type of the response object
     * @param tiempo The time value to filter by
     * @param <T> The type of the response object
     * @return A filtered list of Ruta entities
     * @throws WebApplicationException if there is an error during the request
     */
    public <T> T filterTiempoMenor_XML(GenericType<T> responseType, String tiempo) throws WebApplicationException;

    /**
     * Filters the Ruta entities by time less than the specified value
     * (JSON format).
     *
     * @param responseType The type of the response object
     * @param tiempo The time value to filter by
     * @param <T> The type of the response object
     * @return A filtered list of Ruta entities
     * @throws WebApplicationException if there is an error during the request
     */
    public <T> T filterTiempoMenor_JSON(Class<T> responseType, String tiempo) throws WebApplicationException;

    /**
     * Retrieves a Ruta entity by its localizador ID (XML format).
     *
     * @param responseType The type of the response object
     * @param localizador The localizador ID of the Ruta to retrieve
     * @param <T> The type of the response object
     * @return The specified Ruta entity
     * @throws WebApplicationException if there is an error during the request
     */
    public <T> T findByLocalizadorInteger_XML(Class<T> responseType, Integer localizador) throws WebApplicationException;

    /**
     * Retrieves a Ruta entity by its localizador ID (JSON format).
     *
     * @param responseType The type of the response object
     * @param localizador The localizador ID of the Ruta to retrieve
     * @param <T> The type of the response object
     * @return The specified Rutaentity
     * @throws WebApplicationException if there is an error during the request
     */
    public <T> T findByLocalizadorInteger_JSON(Class<T> responseType, Integer localizador) throws WebApplicationException;

    /**
     * Closes the RutaManager instance.
     */
    public void close();
}
