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
import logicInterface.PaqueteManager;

/**
 *
 * @author Omar
 */
public class PackageManagerImp implements PaqueteManager {

    private PackageRESTClient webClient;

    public PackageManagerImp() {
        this.webClient = new PackageRESTClient();
    }

    @Override
    public Paquete addPackage(Paquete paquete) throws CreateException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Paquete updatePackage(Paquete paquete) throws UpdateException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void deletePackages(Integer idPaquete) throws DeleteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Paquete> findAllPackages() throws SelectException {
        GenericType<List<Paquete>> responseType = new GenericType<List<Paquete>>() {
        };
        List<Paquete> packagesList = webClient.findAllPackages(responseType);
        return packagesList;
    }

    @Override
    public List<Paquete> findAllPackageBySize(PackageSize size) throws SelectException {
        GenericType<List<Paquete>> responseType = new GenericType<List<Paquete>>() {
        };
        List<Paquete> packagesList = webClient.findPackagesBySize(responseType, size.toString());
        return packagesList;
    }

    @Override
    public List<Paquete> findAllPackagesByDates(Date firstDate, Date secondDate) throws SelectException {
        GenericType<List<Paquete>> responseType = new GenericType<List<Paquete>>() {
        };
        System.out.println(firstDate.toString());
        System.out.println(secondDate.toString());
        List<Paquete> packagesList = webClient.findPackagesByDates(responseType, firstDate.toString(), secondDate.toString());
        return packagesList;
    }

    @Override
    public List<Paquete> findAllPackagesByName(String senderOrReceiverName) throws SelectException {
        GenericType<List<Paquete>> responseType = new GenericType<List<Paquete>>() {
        };
        List<Paquete> packagesList = webClient.findPackagesByName(responseType, senderOrReceiverName);
        return packagesList;

    }

}