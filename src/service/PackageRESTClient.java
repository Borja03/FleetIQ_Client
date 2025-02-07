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
 * This class provides methods to interact with the package REST endpoints.
 * 
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

    /**
     * The web target for the REST endpoint.
     */
    private WebTarget webTarget;
    
    /**
     * The Jersey client instance.
     */
    private Client client;

//    private static final String base_uri = ResourceBundle.getBundle("config/config")
//                    .getString("RESTful.baseURI");
//
//    public PackageRESTClient() {
//        client = javax.ws.rs.client.ClientBuilder.newClient();
//        webTarget = client.target(base_uri).path("paquete");
//    }
//    
//        private WebTarget webTarget;
//    private Client client;

    /**
     * Constructs a new PackageRESTClient with the specified base URI.
     *
     * @param baseUri the base URI of the REST service
     */
    public PackageRESTClient(String baseUri) {
        client = javax.ws.rs.client.ClientBuilder.newClient();
        webTarget = client.target(baseUri).path("paquete");
    }

    /**
     * Finds packages by name.
     *
     * @param responseType the generic type for response conversion
     * @param name the name to search for
     * @return List of packages matching the name
     * @throws WebApplicationException if there's an error during the request
     */
    public List<Paquete> findPackagesByName(GenericType<List<Paquete>> responseType, String name) throws WebApplicationException {
        WebTarget resource = webTarget;
        System.out.println(webTarget.toString());

        resource = resource.path(java.text.MessageFormat.format("name/{0}", new Object[]{name}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML)
                        .get(responseType);
    }

    /**
     * Finds packages by size.
     *
     * @param responseType the generic type for response conversion
     * @param size the size to search for
     * @return List of packages matching the size
     * @throws WebApplicationException if there's an error during the request
     */
    public List<Paquete> findPackagesBySize(GenericType<List<Paquete>> responseType, String size) throws WebApplicationException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("size/{0}", new Object[]{size}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML)
                        .get(responseType);
    }

    /**
     * Creates a new package.
     *
     * @param requestEntity the package entity to create
     * @param responseType the generic type for response conversion
     * @return the created package
     * @throws WebApplicationException if there's an error during the request
     */
    public Paquete createPackage(Paquete requestEntity, GenericType<Paquete> responseType) throws WebApplicationException {
        return webTarget.request(javax.ws.rs.core.MediaType.APPLICATION_XML)
                        .post(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_XML), responseType);
    }

    /**
     * Updates an existing package.
     *
     * @param requestEntity the updated package entity
     * @param responseType the generic type for response conversion
     * @param id the ID of the package to update
     * @return the updated package
     * @throws WebApplicationException if there's an error during the request
     */
    public Paquete updatePackage(Object requestEntity, GenericType<Paquete> responseType, Long id) throws WebApplicationException {
        return webTarget.path(java.text.MessageFormat.format("{0}", new Object[]{id}))
                        .request(javax.ws.rs.core.MediaType.APPLICATION_XML)
                        .put(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_XML), responseType);
    }

    /**
     * Retrieves all packages.
     *
     * @param responseType the generic type for response conversion
     * @return List of all packages
     * @throws WebApplicationException if there's an error during the request
     */
    public List<Paquete> findAllPackages(GenericType<List<Paquete>> responseType) throws WebApplicationException {
        WebTarget resource = webTarget;
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML)
                        .get(responseType);
    }

    /**
     * Deletes a package by ID.
     *
     * @param id the ID of the package to delete
     * @throws WebApplicationException if there's an error during the request
     */
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

    /**
     * Finds packages before a specified date.
     *
     * @param responseType the generic type for response conversion
     * @param endDate the end date to search before
     * @return List of packages before the specified date
     * @throws WebApplicationException if there's an error during the request
     */
    public List<Paquete> findPackagesBeforeDate(GenericType<List<Paquete>> responseType, String endDate) throws WebApplicationException {
        WebTarget resource = webTarget;
        if (endDate != null) {
            resource = resource.queryParam("endDate", endDate);
        }
        resource = resource.path("date/before");
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }

    /**
     * Finds packages after a specified date.
     *
     * @param responseType the generic type for response conversion
     * @param startDate the start date to search after
     * @return List of packages after the specified date
     * @throws WebApplicationException if there's an error during the request
     */
    public List<Paquete> findPackagesAfterDate(GenericType<List<Paquete>> responseType, String startDate) throws WebApplicationException {
        WebTarget resource = webTarget;
        if (startDate != null) {
            resource = resource.queryParam("startDate", startDate);
        }
        resource = resource.path("date/after");
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }

    /**
     * Finds packages between two specified dates.
     *
     * @param responseType the generic type for response conversion
     * @param endDate the end date of the range
     * @param startDate the start date of the range
     * @return List of packages between the specified dates
     * @throws WebApplicationException if there's an error during the request
     */
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

    /**
     * Closes the client connection and releases resources.
     */
    public void close() {
        client.close();
    }
}