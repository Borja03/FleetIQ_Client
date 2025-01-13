/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import models.Paquete;

/**
 * Jersey REST client generated for REST resource:PackageREST [package]<br>
 * USAGE:
 * <pre>
 * PackageRESTClient client = new PackageRESTClient();
 * Object response = client.XXX(...);
 * // do whatever with response
 * client.close();
 * </pre>
 *
 * @author Omar
 */
public class PackageRESTClient {

    private WebTarget webTarget;
    private Client client;

    //RESTful.baseURI = "http://localhost:8080/FleetIQ_Server/webresources"
    private static final String BASE_URI = ResourceBundle.getBundle("config/config")
                    .getString("RESTful.baseURI");
    
    public PackageRESTClient() {
        client = javax.ws.rs.client.ClientBuilder.newClient();
        webTarget = client.target(BASE_URI).path("package");
    }

    public List<Paquete> findPackagesByName(GenericType<List<Paquete>> responseType, String name) throws ClientErrorException {
        WebTarget resource = webTarget;
        System.out.println(webTarget.toString());

        resource = resource.path(java.text.MessageFormat.format("name/{0}", new Object[]{name}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML)
                        .get(responseType);
    }

    public List<Paquete> findPackagesBySize(GenericType<List<Paquete>> responseType, String size) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("size/{0}", new Object[]{size}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML)
                        .get(responseType);
    }

    public Paquete createPackage(Object requestEntity, GenericType<Paquete> responseType) throws ClientErrorException {
        return webTarget.request(javax.ws.rs.core.MediaType.APPLICATION_XML)
                        .post(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_XML), responseType);
    }

    public Paquete updatePackage(Object requestEntity, GenericType<Paquete> responseType, String id) throws ClientErrorException {
        return webTarget.path(java.text.MessageFormat.format("{0}", new Object[]{id}))
                        .request(javax.ws.rs.core.MediaType.APPLICATION_XML)
                        .put(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_XML), responseType);
    }

    public List<Paquete> findAllPackages(GenericType<List<Paquete>> responseType) throws ClientErrorException {
        WebTarget resource = webTarget;
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML)
                        .get(responseType);
    }

    public void deletePackage(String id) throws ClientErrorException {
        webTarget.path(java.text.MessageFormat.format("{0}", new Object[]{id}))
                        .request()
                        .delete();
    }

    public List<Paquete> findPackagesByDates(GenericType<List<Paquete>> responseType, String endDate, String startDate) throws ClientErrorException {
        WebTarget resource = webTarget;
        if (endDate != null) {
            resource = resource.queryParam("endDate", endDate);
        }
        if (startDate != null) {
            resource = resource.queryParam("startDate", startDate);
        }
        resource = resource.path("date");
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }

    public void close() {
        client.close();
    }

}
