/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import java.util.ResourceBundle;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import models.User;

/**
 * Jersey REST client generated for REST resource:UserREST [user]<br>
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

    private WebTarget webTarget;
    private Client client;
    private static final String BASE_URI = ResourceBundle.getBundle("config/config")
                    .getString("RESTful.baseURI");

    public UserRESTClient() {
        client = javax.ws.rs.client.ClientBuilder.newClient();
        webTarget = client.target(BASE_URI).path("user");
    }

    public <T> T checkExist(Object requestEntity, Class<T> responseType) throws ClientErrorException {
        return webTarget.path("check-exist")
                        .request(javax.ws.rs.core.MediaType.APPLICATION_XML)
                        .post(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_XML), responseType);
    }

    public <T> T signIn(User requestEntity, Class<T> responseType) throws ClientErrorException {
        return webTarget.path("login")
                        .request(javax.ws.rs.core.MediaType.APPLICATION_XML)
                        .post(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_XML), responseType);
    }

    public <T> T signUp(User requestEntity, Class<T> responseType) throws ClientErrorException {
        return webTarget.path("signup").request(javax.ws.rs.core.MediaType.APPLICATION_XML)
                        .post(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_XML), responseType);
    }

    public void resetPassword(User requestEntity) throws ClientErrorException {
        webTarget.path("reset").request(javax.ws.rs.core.MediaType.APPLICATION_XML)
                        .post(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_XML));
    }

    public <T> T verifyCode(User requestEntity, Class<T> responseType) throws ClientErrorException {
        return webTarget.path("verify-code")
                        .request(javax.ws.rs.core.MediaType.APPLICATION_XML)
                        .post(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_XML), responseType);
    }

    public void updatePassword(User requestEntity) throws ClientErrorException {
        webTarget.path("update-password")
                        .request(javax.ws.rs.core.MediaType.APPLICATION_XML)
                        .put(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_XML));
    }

    public void create(User requestEntity) throws ClientErrorException {
        webTarget.request(javax.ws.rs.core.MediaType.APPLICATION_XML)
                        .post(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_XML));
    }

    public <T> T findAll(Class<T> responseType) throws ClientErrorException {
        WebTarget resource = webTarget;
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML)
                        .get(responseType);
    }

    public void close() {
        client.close();
    }

}
