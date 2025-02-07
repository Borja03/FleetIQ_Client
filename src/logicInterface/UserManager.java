/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logicInterface;

import exception.CreateException;
import exception.SelectException;
import exception.UpdateException;
import javax.ws.rs.ClientErrorException;
import models.User;





/**
 * Interface for managing {@link User} entities.
 * <p>
 * This interface defines methods for user authentication, registration, 
 * password management, and retrieval.
 * </p>
 *
 * @since 1.0
 * @version 1.0
 */

/**
 *
 * @author Omar
 */
public interface UserManager {

    /**
     * Checks whether a user exists in the system.
     *
     * @param requestEntity The user details or identifier for verification.
     * @param responseType The expected response type.
     * @param <T> The type of the expected response.
     * @return A response indicating whether the user exists.
     * @throws ClientErrorException If a client-side error occurs.
     */
    <T> T checkExist(Object requestEntity, Class<T> responseType) throws ClientErrorException;

    /**
     * Registers a new user in the system.
     *
     * @param requestEntity The user details to be registered.
     * @param responseType The expected response type.
     * @param <T> The type of the expected response.
     * @return The result of the registration process.
     * @throws CreateException If an error occurs during user creation.
     */
    <T> T signUp(User requestEntity, Class<T> responseType) throws CreateException;

    /**
     * Authenticates a user during sign-in.
     *
     * @param requestEntity The user credentials for authentication.
     * @param responseType The expected response type.
     * @param <T> The type of the expected response.
     * @return The authentication result.
     * @throws SelectException If an error occurs during retrieval.
     */
    <T> T signIn(User requestEntity, Class<T> responseType) throws SelectException;

    /**
     * Resets the password for a user.
     *
     * @param requestEntity The user details required for password reset.
     */
    void resetPassword(User requestEntity);

    /**
     * Verifies a security or authentication code for user validation.
     *
     * @param requestEntity The user details along with the verification code.
     * @param responseType The expected response type.
     * @param <T> The type of the expected response.
     * @return The verification result.
     * @throws SelectException If an error occurs during verification.
     */
    <T> T verifyCode(User requestEntity, Class<T> responseType) throws SelectException;

    /**
     * Updates the password of an existing user.
     *
     * @param requestEntity The user details with the new password.
     * @throws UpdateException If an error occurs during the update.
     */
    void updatePassword(User requestEntity) throws UpdateException;

    /**
     * Creates a new user.
     *
     * @param requestEntity The user details to be created.
     * @throws CreateException If an error occurs during creation.
     */
    void create(User requestEntity) throws CreateException;

    /**
     * Retrieves all users from the system.
     *
     * @param <T> The type of the expected response.
     * @return A list of all users.
     * @throws SelectException If an error occurs during retrieval.
     */
    <T> T findAll() throws SelectException;
}
