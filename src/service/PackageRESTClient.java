/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import exception.SelectException;
import java.io.StringReader;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import models.Paquete;

/**
 * Jersey REST client generated for REST resource:PackageREST [package]<br>
 * USAGE:
 * <pre>
 *        PackageRESTClient client = new PackageRESTClient();
 *        Object response = client.XXX(...);
 *        // do whatever with response
 *        client.close();
 * </pre>
 *
 * @author Omar
 */
public class PackageRESTClient implements AutoCloseable {

    private final Client client;
    private final WebTarget webTarget;
    private static final String BASE_URI = "http://localhost:8080/FleeIQServer/webresources";
    private static final Logger LOGGER = Logger.getLogger(PackageRESTClient.class.getName());

    public PackageRESTClient() {
        try {
            this.client = ClientBuilder.newClient();
            this.webTarget = client.target(BASE_URI);

//               client = javax.ws.rs.client.ClientBuilder.newClient();
//        webTarget = client.target(BASE_URI).path("package");
        } catch (Exception e) {
            LOGGER.severe("Error initializing REST client: " + e.getMessage());
            throw new RuntimeException("Could not initialize REST client", e);
        }
    }

//    public List<Paquete> findAllPackages() throws SelectException {
//        try {
//            // Set the expected response type to XML
//            return webTarget
//                            .request(javax.ws.rs.core.MediaType.APPLICATION_XML) // Change to APPLICATION_XML
//                            .get(new GenericType<List<Paquete>>() {
//                            }); // Ensure Paquete is annotated for JAXB
//        } catch (ClientErrorException e) {
//            LOGGER.severe("Error retrieving packages: " + e.getResponse().readEntity(String.class));
//            throw new SelectException("Error retrieving packages: " + e.getMessage());
//        } catch (Exception e) {
//            LOGGER.severe("Unexpected error retrieving packages: " + e.getMessage());
//            throw new SelectException("Unexpected error retrieving packages");
//        }
//    }
//    public List<Paquete> findAllPackages() throws SelectException {
//        WebTarget targetss = webTarget.path("package");
//        System.out.println("GET_ALL_USERS_PATH: " + targetss.getUri());
//
//        try {
//            Response response = webTarget.path("package")
//                            .request(MediaType.APPLICATION_XML)
//                            .get();
//
//            System.out.println("RESPONSE_STATUS_CODE: " + response.getStatus());
//            System.out.println("RESPONSE_STATUS: " + response.getStatusInfo());
//            System.out.println("RESPONSE_HEADERS: " + response.getHeaders());
////            String responseBody = response.readEntity(String.class);
////            System.out.println("RESPONSE_READ_ENTITY: " + responseBody);
//            if (response.getStatus() == 200) {
//                System.out.println("20000000000000000000");
//                List<Paquete> packages = response.readEntity(new GenericType<List<Paquete>>() {
//                });
//                
//               
//                return packages;
//            } else {
//                System.out.println("Error: " + response.getStatus());
//                return Collections.emptyList();
//            }
//        } catch (Exception e) {
//            System.out.println("Error fetching packages: " + e.getMessage());
//            return Collections.emptyList();
//        }
//    }
    public List<Paquete> findAllPackages() throws SelectException {
        WebTarget targetss = webTarget.path("package");
        System.out.println("GET_ALL_USERS_PATH: " + targetss.getUri());
        try {
            Response response = webTarget.path("package")
                            .request(MediaType.APPLICATION_XML)
                            .get();
            System.out.println("RESPONSE_STATUS_CODE: " + response.getStatus());
            System.out.println("RESPONSE_STATUS: " + response.getStatusInfo());
            System.out.println("RESPONSE_HEADERS: " + response.getHeaders());

            if (response.getStatus() == 200) {
                try {
                    
                System.out.println("20000000000000000000");
                List<Paquete> packages = response.readEntity(new GenericType<List<Paquete>>() {
                });
                
               for(Paquete p : packages){
                   System.out.println(p.toString());
               }
                return packages;
                } catch (Exception e) {
                    System.out.println("Deserialization error: " + e.getMessage());
                    e.printStackTrace();
                    return Collections.emptyList();
                }
            } else {
                System.out.println("Error: " + response.getStatus());
                return Collections.emptyList();
            }
        } catch (Exception e) {
            System.out.println("Error fetching packages: " + e.getMessage());
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    @Override
    public void close() {
        if (client != null) {
            try {
                client.close();
            } catch (Exception e) {
                LOGGER.warning("Error closing client: " + e.getMessage());
            }
        }
    }
}
