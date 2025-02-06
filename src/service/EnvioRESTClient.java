package service;

import java.util.ResourceBundle;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import logicInterface.EnvioManager;
import models.Envio;

/**
 * Cliente REST de Jersey generado para interactuar con el recurso RESTful: EnvioFacadeREST [envio].
 * Este cliente se utiliza para realizar operaciones CRUD sobre los envíos y otros filtros relacionados.
 * 
 * USO:
 * <pre>
 * EnvioRESTClient client = new EnvioRESTClient();
 * Object response = client.XXX(...);
 * // realizar operaciones con la respuesta
 * client.close();
 * </pre>
 * 
 * Métodos disponibles:
 * - Crear, editar, encontrar y eliminar envíos.
 * - Filtrar envíos por estado, número de paquetes, y rango de fechas.
 * 
 * @author Alder
 */
public class EnvioRESTClient implements EnvioManager {

    private WebTarget webTarget;
    private Client client;
    private static final String BASE_URI = ResourceBundle.getBundle("config/config")
            .getString("RESTful.baseURI");

    /**
     * Constructor que inicializa el cliente REST con la URI base y el recurso 'envio'.
     */
    public EnvioRESTClient() {
        client = javax.ws.rs.client.ClientBuilder.newClient();
        webTarget = client.target(BASE_URI).path("envio");
    }

    /**
     * Filtra los envíos por el número de paquetes y devuelve los resultados en formato XML.
     * 
     * @param responseType Tipo genérico de la respuesta esperada.
     * @param numPaquetes Número de paquetes por los cuales filtrar.
     * @return Resultado de la consulta en formato XML.
     * @throws WebApplicationException Si ocurre un error en la solicitud.
     */
    @Override
    public <T> T filterNumPaquetes_XML(GenericType<T> responseType, Integer numPaquetes) throws WebApplicationException {
        WebTarget resource = webTarget;
        if (numPaquetes != null) {
            resource = resource.queryParam("numPaquetes", numPaquetes);
        }
        resource = resource.path("filterNumPaquetes");
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }

    /**
     * Filtra los envíos por el número de paquetes y devuelve los resultados en formato JSON.
     * 
     * @param responseType Tipo genérico de la respuesta esperada.
     * @param numPaquetes Número de paquetes por los cuales filtrar.
     * @return Resultado de la consulta en formato JSON.
     * @throws WebApplicationException Si ocurre un error en la solicitud.
     */
    @Override
    public <T> T filterNumPaquetes_JSON(GenericType<T> responseType, Integer numPaquetes) throws WebApplicationException {
        WebTarget resource = webTarget;
        if (numPaquetes != null) {
            resource = resource.queryParam("numPaquetes", numPaquetes);
        }
        resource = resource.path("filterNumPaquetes");
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    /**
     * Devuelve el número total de envíos disponibles.
     * 
     * @return El conteo de los envíos en formato de texto plano.
     * @throws WebApplicationException Si ocurre un error en la solicitud.
     */
    public String countREST() throws WebApplicationException {
        WebTarget resource = webTarget;
        resource = resource.path("count");
        return resource.request(javax.ws.rs.core.MediaType.TEXT_PLAIN).get(String.class);
    }

    /**
     * Edita un envío en formato XML.
     * 
     * @param requestEntity El objeto Envio a editar.
     * @param id El ID del envío a editar.
     * @throws WebApplicationException Si ocurre un error en la solicitud.
     */
    @Override
    public void edit_XML(Object requestEntity, String id) throws WebApplicationException {
        webTarget.path(java.text.MessageFormat.format("{0}", new Object[]{id})).request(javax.ws.rs.core.MediaType.APPLICATION_XML).put(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_XML), Envio.class);
    }

    /**
     * Edita un envío en formato JSON.
     * 
     * @param requestEntity El objeto Envio a editar.
     * @param id El ID del envío a editar.
     * @throws WebApplicationException Si ocurre un error en la solicitud.
     */
    @Override
    public void edit_JSON(Object requestEntity, String id) throws WebApplicationException {
        webTarget.path(java.text.MessageFormat.format("{0}", new Object[]{id})).request(javax.ws.rs.core.MediaType.APPLICATION_JSON).put(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_JSON), Envio.class);
    }

    /**
     * Busca un envío por su ID en formato XML.
     * 
     * @param responseType Tipo genérico de la respuesta esperada.
     * @param id El ID del envío a buscar.
     * @return El envío solicitado en formato XML.
     * @throws WebApplicationException Si ocurre un error en la solicitud.
     */
    public <T> T find_XML(GenericType<T> responseType, String id) throws WebApplicationException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("{0}", new Object[]{id}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }

    /**
     * Busca un envío por su ID en formato JSON.
     * 
     * @param responseType Tipo genérico de la respuesta esperada.
     * @param id El ID del envío a buscar.
     * @return El envío solicitado en formato JSON.
     * @throws WebApplicationException Si ocurre un error en la solicitud.
     */
    public <T> T find_JSON(GenericType<T> responseType, String id) throws WebApplicationException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("{0}", new Object[]{id}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    /**
     * Busca un rango de envíos por su ID en formato XML.
     * 
     * @param responseType Tipo genérico de la respuesta esperada.
     * @param from El primer ID del rango.
     * @param to El último ID del rango.
     * @return Los envíos en el rango solicitado en formato XML.
     * @throws WebApplicationException Si ocurre un error en la solicitud.
     */
    public <T> T findRange_XML(GenericType<T> responseType, String from, String to) throws WebApplicationException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("{0}/{1}", new Object[]{from, to}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }

    /**
     * Busca un rango de envíos por su ID en formato JSON.
     * 
     * @param responseType Tipo genérico de la respuesta esperada.
     * @param from El primer ID del rango.
     * @param to El último ID del rango.
     * @return Los envíos en el rango solicitado en formato JSON.
     * @throws WebApplicationException Si ocurre un error en la solicitud.
     */
    public <T> T findRange_JSON(GenericType<T> responseType, String from, String to) throws WebApplicationException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("{0}/{1}", new Object[]{from, to}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    /**
     * Filtra los envíos por su estado en formato XML.
     * 
     * @param responseType Tipo genérico de la respuesta esperada.
     * @param estado El estado de los envíos a filtrar.
     * @return Los envíos filtrados por estado en formato XML.
     * @throws WebApplicationException Si ocurre un error en la solicitud.
     */
    @Override
    public <T> T filterEstado_XML(GenericType<T> responseType, String estado) throws WebApplicationException {
        WebTarget resource = webTarget;
        if (estado != null) {
            resource = resource.queryParam("estado", estado);
        }
        resource = resource.path("filterEstado");
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }

    /**
     * Filtra los envíos por su estado en formato JSON.
     * 
     * @param responseType Tipo genérico de la respuesta esperada.
     * @param estado El estado de los envíos a filtrar.
     * @return Los envíos filtrados por estado en formato JSON.
     * @throws WebApplicationException Si ocurre un error en la solicitud.
     */
    @Override
    public <T> T filterEstado_JSON(GenericType<T> responseType, String estado) throws WebApplicationException {
        WebTarget resource = webTarget;
        if (estado != null) {
            resource = resource.queryParam("estado", estado);
        }
        resource = resource.path("filterEstado");
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    /**
     * Crea un nuevo envío en formato XML.
     * 
     * @param requestEntity El objeto Envio a crear.
     * @throws WebApplicationException Si ocurre un error en la solicitud.
     */
    @Override
    public void create_XML(Object requestEntity) throws WebApplicationException {
        webTarget.request(javax.ws.rs.core.MediaType.APPLICATION_XML).post(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_XML), Envio.class);
    }

    /**
     * Crea un nuevo envío en formato JSON.
     * 
     * @param requestEntity El objeto Envio a crear.
     * @throws WebApplicationException Si ocurre un error en la solicitud.
     */
    @Override
    public void create_JSON(Object requestEntity) throws WebApplicationException {
        webTarget.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).post(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_JSON), Envio.class);
    }

    /**
     * Obtiene todos los envíos en formato XML.
     * 
     * @param responseType Tipo genérico de la respuesta esperada.
     * @return Todos los envíos en formato XML.
     * @throws WebApplicationException Si ocurre un error en la solicitud.
     */
    @Override
    public <T> T findAll_XML(GenericType<T> responseType) throws WebApplicationException {
        WebTarget resource = webTarget;
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }

    /**
     * Obtiene todos los envíos en formato JSON.
     * 
     * @param responseType Tipo genérico de la respuesta esperada.
     * @return Todos los envíos en formato JSON.
     * @throws WebApplicationException Si ocurre un error en la solicitud.
     */
    @Override
    public <T> T findAll_JSON(GenericType<T> responseType) throws WebApplicationException {
        WebTarget resource = webTarget;
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    /**
     * Filtra los envíos por un rango de fechas en formato XML.
     * 
     * @param responseType Tipo genérico de la respuesta esperada.
     * @param firstDate Fecha de inicio del filtro.
     * @param secondDate Fecha de fin del filtro.
     * @return Los envíos filtrados por fechas en formato XML.
     * @throws WebApplicationException Si ocurre un error en la solicitud.
     */
    @Override
    public <T> T filterByDates_XML(GenericType<T> responseType, String firstDate, String secondDate) throws WebApplicationException {
        WebTarget resource = webTarget;
        if (firstDate != null) {
            resource = resource.queryParam("firstDate", firstDate);
        }
        if (secondDate != null) {
            resource = resource.queryParam("secondDate", secondDate);
        }
        resource = resource.path("filterByDates");
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }

    /**
     * Filtra los envíos por un rango de fechas en formato JSON.
     * 
     * @param responseType Tipo genérico de la respuesta esperada.
     * @param firstDate Fecha de inicio del filtro.
     * @param secondDate Fecha de fin del filtro.
     * @return Los envíos filtrados por fechas en formato JSON.
     * @throws WebApplicationException Si ocurre un error en la solicitud.
     */
    @Override
    public <T> T filterByDates_JSON(GenericType<T> responseType, String firstDate, String secondDate) throws WebApplicationException {
        WebTarget resource = webTarget;
        if (firstDate != null) {
            resource = resource.queryParam("firstDate", firstDate);
        }
        if (secondDate != null) {
            resource = resource.queryParam("secondDate", secondDate);
        }
        resource = resource.path("filterByDates");
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    /**
     * Elimina un envío por su ID.
     * 
     * @param id El ID del envío a eliminar.
     * @throws WebApplicationException Si ocurre un error en la solicitud.
     */
    @Override
    public void remove(Integer id) throws WebApplicationException {
        webTarget.path(java.text.MessageFormat.format("{0}", new Object[]{id})).request().delete(Envio.class);
    }
    
    /**
     * Cierra la conexión del cliente REST.
     */
    public void close() {
        client.close();
    }

}
