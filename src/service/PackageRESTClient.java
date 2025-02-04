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
import javax.ws.rs.WebApplicationException;
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

    private static  String base_uri = ResourceBundle.getBundle("config/config")
                    .getString("RESTful.baseURI");

    public PackageRESTClient() {
        client = javax.ws.rs.client.ClientBuilder.newClient();
        webTarget = client.target(base_uri).path("paquete");
    }

    public static String getBASE_URI() {
        return base_uri;
    }

    public static void setBASE_URI(String BASE_URI) {
        PackageRESTClient.base_uri = BASE_URI;
    }
    
    

    public List<Paquete> findPackagesByName(GenericType<List<Paquete>> responseType, String name) throws WebApplicationException {
        WebTarget resource = webTarget;
        System.out.println(webTarget.toString());

        resource = resource.path(java.text.MessageFormat.format("name/{0}", new Object[]{name}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML)
                        .get(responseType);
    }

    public List<Paquete> findPackagesBySize(GenericType<List<Paquete>> responseType, String size) throws WebApplicationException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("size/{0}", new Object[]{size}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML)
                        .get(responseType);
    }

    public Paquete createPackage(Paquete requestEntity, GenericType<Paquete> responseType) throws WebApplicationException {
        return webTarget.request(javax.ws.rs.core.MediaType.APPLICATION_XML)
                        .post(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_XML), responseType);
    }

    public Paquete updatePackage(Object requestEntity, GenericType<Paquete> responseType, Long id) throws WebApplicationException {
        return webTarget.path(java.text.MessageFormat.format("{0}", new Object[]{id}))
                        .request(javax.ws.rs.core.MediaType.APPLICATION_XML)
                        .put(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_XML), responseType);
    }

    public List<Paquete> findAllPackages(GenericType<List<Paquete>> responseType) throws WebApplicationException {
        WebTarget resource = webTarget;
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML)
                        .get(responseType);
    }

    public void deletePackage(Long id) throws WebApplicationException {
        webTarget.path(java.text.MessageFormat.format("{0}", new Object[]{id}))
                        .request()
                        .delete(Paquete.class);
    }

//    public List<Paquete> findPackagesByDates(GenericType<List<Paquete>> responseType, String endDate, String startDate) throws WebApplicationException {
//        WebTarget resource = webTarget;
//        if (endDate != null) {
//            resource = resource.queryParam("endDate", endDate);
//        }
//        if (startDate != null) {
//            resource = resource.queryParam("startDate", startDate);
//        }
//        resource = resource.path("date");
//        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
//    }

    public List<Paquete> findPackagesBeforeDate(GenericType<List<Paquete>> responseType, String endDate) throws WebApplicationException {
        WebTarget resource = webTarget;
        if (endDate != null) {
            resource = resource.queryParam("endDate", endDate);
        }
        resource = resource.path("date/before");
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }

    public List<Paquete> findPackagesAfterDate(GenericType<List<Paquete>> responseType, String startDate) throws WebApplicationException {
        WebTarget resource = webTarget;
        if (startDate != null) {
            resource = resource.queryParam("startDate", startDate);
        }
        resource = resource.path("date/after");
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }

    public List<Paquete> findPackagesBetweenDates(GenericType<List<Paquete>> responseType, String endDate, String startDate) throws WebApplicationException {
        WebTarget resource = webTarget;
        if (endDate != null) {
            resource = resource.queryParam("endDate", endDate);
        }
        if (startDate != null) {
            resource = resource.queryParam("startDate", startDate);
        }
        resource = resource.path("date/between");
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }

    public void close() {
        client.close();
    }

}
