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
 *
 * @author Omar
 */
public class UserManagerImp implements UserManager {

    private UserRESTClient webClient;

    public UserManagerImp() {
        this.webClient = new UserRESTClient();
    }

    @Override
    public <T> T checkExist(Object requestEntity, Class<T> responseType) throws ClientErrorException {
        return webClient.checkExist(requestEntity, responseType);
    }

    @Override
    public <T> T signUp(User requestEntity, Class<T> responseType) throws CreateException {
        return webClient.signUp(requestEntity, responseType);
    }

    @Override
    public <T> T signIn(User requestEntity, Class<T> responseType) throws SelectException {
        return webClient.signIn(requestEntity, responseType);
    }

    @Override
    public void resetPassword(User requestEntity) {
        webClient.resetPassword(requestEntity);
    }

    @Override
    public <T> T verifyCode(User requestEntity, Class<T> responseType) throws SelectException {
        return webClient.verifyCode(requestEntity, responseType);
    }

    @Override
    public void updatePassword(User requestEntity) throws UpdateException {
        webClient.updatePassword(requestEntity);
    }

    @Override
    public void create(User requestEntity) throws CreateException {

    }

    @Override
    public <T> T findAll(Class<T> responseType) throws SelectException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
