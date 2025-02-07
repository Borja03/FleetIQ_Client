/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logicimplementation;

import exception.CreateException;
import exception.DeleteException;
import exception.SelectException;
import exception.UpdateException;
import java.util.Date;
import java.util.List;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.core.GenericType;
import models.Paquete;
import models.PackageSize;
import service.PackageRESTClient;
import logicInterface.UserManager;
import models.User;
import service.UserRESTClient;


/**
 * Implementation of the {@link UserManager} interface.
 * This class handles user-related operations by communicating with the
 * RESTful web service using {@link UserRESTClient}.
 *
 * <p>The operations include user authentication, registration, password management,
 * and retrieving user information.</p>
 * */

 /*
 * @author Omar
 * @since 1.0
 * @version 1.0
 */
public class UserManagerImp implements UserManager {

    /** REST client for handling user-related requests. */
    private UserRESTClient webClient;

    /**
     * Constructs a new {@code UserManagerImp} instance.
     * Initializes the REST client.
     */
    public UserManagerImp() {
        this.webClient = new UserRESTClient();
    }

    /**
     * Checks if a user exists.
     *
     * @param requestEntity The request data to check user existence.
     * @param responseType The expected response type.
     * @param <T> The type of the response.
     * @return The response indicating whether the user exists.
     * @throws ClientErrorException If a client error occurs during the request.
     */
    @Override
    public <T> T checkExist(Object requestEntity, Class<T> responseType) throws ClientErrorException {
        return webClient.checkExist(requestEntity, responseType);
    }

    /**
     * Registers a new user.
     *
     * @param requestEntity The user data for registration.
     * @param responseType The expected response type.
     * @param <T> The type of the response.
     * @return The response after user registration.
     * @throws CreateException If an error occurs while creating the user.
     */
    @Override
    public <T> T signUp(User requestEntity, Class<T> responseType) throws CreateException {
        return webClient.signUp(requestEntity, responseType);
    }

    /**
     * Authenticates a user.
     *
     * @param requestEntity The user credentials for authentication.
     * @param responseType The expected response type.
     * @param <T> The type of the response.
     * @return The authenticated user details.
     * @throws SelectException If an error occurs during authentication.
     */
    @Override
    public <T> T signIn(User requestEntity, Class<T> responseType) throws SelectException {
        return webClient.signIn(requestEntity, responseType);
    }

    /**
     * Sends a password reset request for the given user.
     *
     * @param requestEntity The user data for password reset.
     */
    @Override
    public void resetPassword(User requestEntity) {
        webClient.resetPassword(requestEntity);
    }

    /**
     * Verifies a user-provided verification code.
     *
     * @param requestEntity The user data containing the verification code.
     * @param responseType The expected response type.
     * @param <T> The type of the response.
     * @return The response indicating verification success or failure.
     * @throws SelectException If an error occurs during verification.
     */
    @Override
    public <T> T verifyCode(User requestEntity, Class<T> responseType) throws SelectException {
        return webClient.verifyCode(requestEntity, responseType);
    }

    /**
     * Updates a user's password.
     *
     * @param requestEntity The user data containing the new password.
     * @throws UpdateException If an error occurs while updating the password.
     */
    @Override
    public void updatePassword(User requestEntity) throws UpdateException {
        webClient.updatePassword(requestEntity);
    }

    /**
     * Creates a new user (Not yet implemented).
     *
     * @param requestEntity The user data for creation.
     * @throws CreateException If an error occurs while creating the user.
     */
    @Override
    public void create(User requestEntity) throws CreateException {
        // Not yet implemented
    }

    /**
     * Retrieves a list of all users.
     *
     * @return A list of all users.
     */
    @Override
    public List<User> findAll() {
        GenericType<List<User>> responseType = new GenericType<List<User>>() {};
        return webClient.findAll(responseType);
    }
}
