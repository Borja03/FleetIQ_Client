/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import java.util.List;
import java.util.ResourceBundle;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import models.User;

/**
 * Jersey REST client generated for REST resource:UserREST [user]<br>
 * This class provides methods to interact with user-related REST endpoints including
 * authentication, registration, and user management operations.
 * 
 * USAGE:
 * <pre>
 *        UserRESTClient client = new UserRESTClient();
 *        Object response = client.XXX(...);
 *        // do whatever with response
 *        client.close();
 * </pre>
 *
 * @author Omar
 */
public class UserRESTClient {
    
    /**
     * The web target for the REST endpoint.
     */
    private WebTarget webTarget;
    
    /**
     * The Jersey client instance.
     */
    private Client client;
    
    /**
     * The base URI for the REST service, loaded from configuration.
     */
    private static final String BASE_URI = ResourceBundle.getBundle("config/config")
                    .getString("RESTful.baseURI");
    
    /**
     * Constructs a new UserRESTClient and initializes the web target.
     */
    public UserRESTClient() {
        client = javax.ws.rs.client.ClientBuilder.newClient();
        webTarget = client.target(BASE_URI).path("user");
    }
    
    /**
     * Checks if a user exists in the system.
     *
     * @param <T> the type of response
     * @param requestEntity the user object to check
     * @param responseType the expected response type class
     * @return the response of type T
     * @throws WebApplicationException if there's an error during the request
     */
    public <T> T checkExist(Object requestEntity, Class<T> responseType) throws WebApplicationException {
        return webTarget.path("check-exist")
                        .request(javax.ws.rs.core.MediaType.APPLICATION_XML)
                        .post(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_XML), responseType);
    }
    
    /**
     * Authenticates a user (sign in).
     *
     * @param <T> the type of response
     * @param requestEntity the user credentials
     * @param responseType the expected response type class
     * @return the authenticated user response of type T
     * @throws WebApplicationException if authentication fails or there's an error
     */
    public <T> T signIn(User requestEntity, Class<T> responseType) throws WebApplicationException {
        return webTarget.path("login")
                        .request(javax.ws.rs.core.MediaType.APPLICATION_XML)
                        .post(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_XML), responseType);
    }
    
    /**
     * Registers a new user (sign up).
     *
     * @param <T> the type of response
     * @param requestEntity the new user information
     * @param responseType the expected response type class
     * @return the created user response of type T
     * @throws WebApplicationException if registration fails or there's an error
     */
    public <T> T signUp(User requestEntity, Class<T> responseType) throws WebApplicationException {
        return webTarget.path("signup").request(javax.ws.rs.core.MediaType.APPLICATION_XML)
                        .post(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_XML), responseType);
    }
    
    /**
     * Initiates a password reset for a user.
     *
     * @param requestEntity the user requesting password reset
     * @throws WebApplicationException if the reset request fails
     */
    public void resetPassword(User requestEntity) throws WebApplicationException {
        webTarget.path("reset").request(javax.ws.rs.core.MediaType.APPLICATION_XML)
                        .post(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_XML));
    }
    
    /**
     * Verifies a verification code sent to the user.
     *
     * @param <T> the type of response
     * @param requestEntity the user with verification code
     * @param responseType the expected response type class
     * @return the verification response of type T
     * @throws WebApplicationException if verification fails or there's an error
     */
    public <T> T verifyCode(User requestEntity, Class<T> responseType) throws WebApplicationException {
        return webTarget.path("verify-code")
                        .request(javax.ws.rs.core.MediaType.APPLICATION_XML)
                        .post(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_XML), responseType);
    }
    
    /**
     * Updates a user's password.
     *
     * @param requestEntity the user with new password information
     * @throws WebApplicationException if the password update fails
     */
    public void updatePassword(User requestEntity) throws WebApplicationException {
        webTarget.path("update-password")
                        .request(javax.ws.rs.core.MediaType.APPLICATION_XML)
                        .put(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_XML));
    }
    
    /**
     * Creates a new user in the system.
     *
     * @param requestEntity the user to create
     * @throws WebApplicationException if user creation fails
     */
    public void create(User requestEntity) throws WebApplicationException {
        webTarget.request(javax.ws.rs.core.MediaType.APPLICATION_XML)
                        .post(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_XML));
    }
    
    /**
     * Retrieves all users from the system.
     *
     * @param <T> the type of response
     * @param responseType the generic type for the list of users
     * @return List of all users
     * @throws WebApplicationException if there's an error retrieving users
     */
    public <T> T findAll(GenericType<List<User>> responseType) throws WebApplicationException {
        WebTarget resource = webTarget;
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML)
                        .get((GenericType<T>) responseType);
    }
    
    /**
     * Closes the client connection and releases resources.
     */
    public void close() {
        client.close();
    }
}