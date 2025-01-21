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
import javax.ws.rs.core.GenericType;
import models.User;

/**
 *
 * @author Omar
 */
public interface UserManager {

    public <T> T checkExist(Object requestEntity, Class<T> responseType) throws ClientErrorException;

    public <T> T signUp(User requestEntity, Class<T> responseType) throws CreateException;

    public <T> T signIn(User requestEntity, Class<T> responseType) throws SelectException;

    public void resetPassword(User requestEntity);

    public <T> T verifyCode(User requestEntity, Class<T> responseType) throws SelectException;

    public void updatePassword(User requestEntity) throws UpdateException;

    public void create(User requestEntity) throws CreateException;

    public <T> T findAll() throws SelectException;

}
