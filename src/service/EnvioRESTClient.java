/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import java.util.ResourceBundle;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import logicInterface.EnvioManager;
import models.Envio;

/**
 * Jersey REST client generated for REST resource:EnvioFacadeREST [envio]<br>
 * USAGE:
 * <pre>
 * EnvioRESTClient client = new EnvioRESTClient();
 * Object response = client.XXX(...);
 * // do whatever with response
 * client.close();
 * </pre>
 *
 * @author Alder
 */
public class EnvioRESTClient implements EnvioManager {

    private WebTarget webTarget;
    private Client client;
    private static final String BASE_URI = ResourceBundle.getBundle("config/config")
            .getString("RESTful.baseURI");

    public EnvioRESTClient() {
        client = javax.ws.rs.client.ClientBuilder.newClient();
        webTarget = client.target(BASE_URI).path("envio");
    }

    @Override
    public <T> T filterNumPaquetes_XML(GenericType<T> responseType, Integer numPaquetes) throws WebApplicationException {
        WebTarget resource = webTarget;
        if (numPaquetes != null) {
            resource = resource.queryParam("numPaquetes", numPaquetes);
        }
        resource = resource.path("filterNumPaquetes");
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }

    @Override
    public <T> T filterNumPaquetes_JSON(GenericType<T> responseType, Integer numPaquetes) throws WebApplicationException {
        WebTarget resource = webTarget;
        if (numPaquetes != null) {
            resource = resource.queryParam("numPaquetes", numPaquetes);
        }
        resource = resource.path("filterNumPaquetes");
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    public String countREST() throws WebApplicationException {
        WebTarget resource = webTarget;
        resource = resource.path("count");
        return resource.request(javax.ws.rs.core.MediaType.TEXT_PLAIN).get(String.class);
    }

    @Override
    public void edit_XML(Object requestEntity, String id) throws WebApplicationException {
        webTarget.path(java.text.MessageFormat.format("{0}", new Object[]{id})).request(javax.ws.rs.core.MediaType.APPLICATION_XML).put(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_XML), Envio.class);
    }

    @Override
    public void edit_JSON(Object requestEntity, String id) throws WebApplicationException {
        webTarget.path(java.text.MessageFormat.format("{0}", new Object[]{id})).request(javax.ws.rs.core.MediaType.APPLICATION_JSON).put(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_JSON), Envio.class);
    }

    public <T> T find_XML(GenericType<T> responseType, String id) throws WebApplicationException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("{0}", new Object[]{id}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }

    public <T> T find_JSON(GenericType<T> responseType, String id) throws WebApplicationException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("{0}", new Object[]{id}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    public <T> T findRange_XML(GenericType<T> responseType, String from, String to) throws WebApplicationException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("{0}/{1}", new Object[]{from, to}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }

    public <T> T findRange_JSON(GenericType<T> responseType, String from, String to) throws WebApplicationException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("{0}/{1}", new Object[]{from, to}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    @Override
    public <T> T filterEstado_XML(GenericType<T> responseType, String estado) throws WebApplicationException {
        WebTarget resource = webTarget;
        if (estado != null) {
            resource = resource.queryParam("estado", estado);
        }
        resource = resource.path("filterEstado");
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }

    @Override
    public <T> T filterEstado_JSON(GenericType<T> responseType, String estado) throws WebApplicationException {
        WebTarget resource = webTarget;
        if (estado != null) {
            resource = resource.queryParam("estado", estado);
        }
        resource = resource.path("filterEstado");
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    @Override
    public void create_XML(Object requestEntity) throws WebApplicationException {
        webTarget.request(javax.ws.rs.core.MediaType.APPLICATION_XML).post(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_XML), Envio.class);
    }

    @Override
    public void create_JSON(Object requestEntity) throws WebApplicationException {
        webTarget.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).post(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_JSON), Envio.class);
    }

    @Override
    public <T> T findAll_XML(GenericType<T> responseType) throws WebApplicationException {
        WebTarget resource = webTarget;
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }

    @Override
    public <T> T findAll_JSON(GenericType<T> responseType) throws WebApplicationException {
        WebTarget resource = webTarget;
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

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

    @Override
    public void remove(Integer id) throws WebApplicationException {
        webTarget.path(java.text.MessageFormat.format("{0}", new Object[]{id})).request().delete(Envio.class);
    }

    public void close() {
        client.close();
    }

}
