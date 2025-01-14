/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logicInterface;

import exception.CreateException;
import exception.SelectException;
import exception.UpdateException;
import models.User;

/**
 *
 * @author Omar
 */
public interface UserManager {

    public void resetPassword(User requestEntity);

    public boolean verifyCode(User requestEntity) throws SelectException;

    public void updatePassword(User requestEntity) throws UpdateException;

    public <T> T signin(User requestEntity, Class<T> responseType) throws SelectException;

    public void create(User requestEntity) throws CreateException;

    public <T> T findAll(Class<T> responseType) throws SelectException;

    public <T> T signUp(User requestEntity, Class<T> responseType) throws CreateException;

}
