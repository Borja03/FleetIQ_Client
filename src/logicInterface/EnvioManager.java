package logicInterface;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.GenericType;

import models.Envio;

/**
 * Interface que define las operaciones para gestionar envíos.
 * Proporciona métodos para añadir, actualizar, eliminar y filtrar envíos en el sistema.
 * Cada método puede lanzar una excepción específica si ocurre un error durante su ejecución.
 * 
 * Los métodos permiten trabajar con los envíos en formato JSON y XML, permitiendo realizar operaciones CRUD (crear, leer, actualizar, eliminar) y filtrados por diferentes criterios.
 *
 * @author Alder
 */
public interface EnvioManager {
    
    /**
     * Añade un nuevo envío al sistema en formato JSON.
     *
     * @param requestEntity el objeto JSON que representa el envío a crear.
     * @throws WebApplicationException si ocurre un error al crear el envío.
     */
    public void create_JSON(Object requestEntity) throws WebApplicationException;

    /**
     * Añade un nuevo envío al sistema en formato XML.
     *
     * @param requestEntity el objeto XML que representa el envío a crear.
     * @throws WebApplicationException si ocurre un error al crear el envío.
     */
    public void create_XML(Object requestEntity) throws WebApplicationException;

    /**
     * Actualiza los datos de un envío existente en formato JSON.
     *
     * @param requestEntity el objeto JSON con los datos actualizados del envío.
     * @param id el identificador del envío a actualizar.
     * @throws WebApplicationException si ocurre un error durante la actualización del envío.
     */
    public void edit_JSON(Object requestEntity, String id) throws WebApplicationException;

    /**
     * Actualiza los datos de un envío existente en formato XML.
     *
     * @param requestEntity el objeto XML con los datos actualizados del envío.
     * @param id el identificador del envío a actualizar.
     * @throws WebApplicationException si ocurre un error durante la actualización del envío.
     */
    public void edit_XML(Object requestEntity, String id) throws WebApplicationException;

    /**
     * Elimina un envío del sistema basado en su identificador.
     *
     * @param id el identificador del envío a eliminar.
     * @throws WebApplicationException si ocurre un error al eliminar el envío.
     */
    public void remove(Integer id) throws WebApplicationException;

    /**
     * Obtiene una lista de todos los envíos registrados en el sistema en formato JSON.
     *
     * @param responseType el tipo de clase para mapear la respuesta JSON.
     * @return una lista de objetos {@link Envio} representados en formato JSON.
     * @throws WebApplicationException si ocurre un error al obtener los datos.
     */
    public <T> T findAll_JSON(GenericType<T> responseType) throws WebApplicationException;

    /**
     * Obtiene una lista de todos los envíos registrados en el sistema en formato XML.
     *
     * @param responseType el tipo de clase para mapear la respuesta XML.
     * @return una lista de objetos {@link Envio} representados en formato XML.
     * @throws WebApplicationException si ocurre un error al obtener los datos.
     */
    public <T> T findAll_XML(GenericType<T> responseType) throws WebApplicationException;

    /**
     * Filtra los envíos registrados en el sistema por un rango de fechas en formato JSON.
     *
     * @param responseType el tipo de clase para mapear la respuesta JSON.
     * @param firstDate la fecha inicial del rango.
     * @param secondDate la fecha final del rango.
     * @return una lista de objetos {@link Envio} que cumplen con el rango de fechas especificado.
     * @throws WebApplicationException si ocurre un error al filtrar los datos.
     */
    public <T> T filterByDates_JSON(GenericType<T> responseType, String firstDate, String secondDate) throws WebApplicationException;

    /**
     * Filtra los envíos registrados en el sistema por un rango de fechas en formato XML.
     *
     * @param responseType el tipo de clase para mapear la respuesta XML.
     * @param firstDate la fecha inicial del rango.
     * @param secondDate la fecha final del rango.
     * @return una lista de objetos {@link Envio} que cumplen con el rango de fechas especificado.
     * @throws WebApplicationException si ocurre un error al filtrar los datos.
     */
    public <T> T filterByDates_XML(GenericType<T> responseType, String firstDate, String secondDate) throws WebApplicationException;

    /**
     * Filtra los envíos registrados en el sistema por estado en formato JSON.
     *
     * @param responseType el tipo de clase para mapear la respuesta JSON.
     * @param estado el estado a filtrar (por ejemplo, "PREPARACION", "EN_REPARTO", "ENTREGADO").
     * @return una lista de objetos {@link Envio} que tienen el estado especificado.
     * @throws WebApplicationException si ocurre un error al filtrar los datos.
     */
    public <T> T filterEstado_JSON(GenericType<T> responseType, String estado) throws WebApplicationException;

    /**
     * Filtra los envíos registrados en el sistema por estado en formato XML.
     *
     * @param responseType el tipo de clase para mapear la respuesta XML.
     * @param estado el estado a filtrar (por ejemplo, "PREPARACION", "EN_REPARTO", "ENTREGADO").
     * @return una lista de objetos {@link Envio} que tienen el estado especificado.
     * @throws WebApplicationException si ocurre un error al filtrar los datos.
     */
    public <T> T filterEstado_XML(GenericType<T> responseType, String estado) throws WebApplicationException;

    /**
     * Filtra los envíos registrados en el sistema por el número de paquetes en formato JSON.
     *
     * @param responseType el tipo de clase para mapear la respuesta JSON.
     * @param numPaquetes el número de paquetes a filtrar.
     * @return una lista de objetos {@link Envio} que cumplen con el número de paquetes especificado.
     * @throws WebApplicationException si ocurre un error al filtrar los datos.
     */
    public <T> T filterNumPaquetes_JSON(GenericType<T> responseType, Integer numPaquetes) throws WebApplicationException;

    /**
     * Filtra los envíos registrados en el sistema por el número de paquetes en formato XML.
     *
     * @param responseType el tipo de clase para mapear la respuesta XML.
     * @param numPaquetes el número de paquetes a filtrar.
     * @return una lista de objetos {@link Envio} que cumplen con el número de paquetes especificado.
     * @throws WebApplicationException si ocurre un error al filtrar los datos.
     */
    public <T> T filterNumPaquetes_XML(GenericType<T> responseType, Integer numPaquetes) throws WebApplicationException;
}
