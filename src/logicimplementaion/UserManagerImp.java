/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logicimplementaion;

import exception.CreateException;
import exception.DeleteException;
import exception.SelectException;
import exception.UpdateException;
import java.util.Date;
import java.util.List;
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
    public void resetPassword(User requestEntity) {
        webClient.resetPassword(requestEntity);
    }

    @Override
    public boolean verifyCode(User requestEntity) throws SelectException {
        return webClient.verifyCode(requestEntity);
    }

    @Override
    public void updatePassword(User requestEntity) throws UpdateException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public <T> T signin(User requestEntity, Class<T> responseType) throws SelectException {
        return webClient.signin(requestEntity, responseType);
    }

    @Override
    public void create(User requestEntity) throws CreateException {

    }

    @Override
    public <T> T findAll(Class<T> responseType) throws SelectException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public <T> T signUp(User requestEntity, Class<T> responseType) throws CreateException {
        return webClient.signUp(requestEntity, responseType);
    }

}
