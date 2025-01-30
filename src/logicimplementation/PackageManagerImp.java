package logicimplementation;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import exception.CreateException;
import exception.DeleteException;
import exception.SelectException;
import exception.UpdateException;
import java.util.Date;
import java.util.List;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.WebApplicationException;
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
        try {
            GenericType<Paquete> responseType = new GenericType<Paquete>() {
            };
            return webClient.createPackage(paquete, responseType);
        } catch (ProcessingException e) {
            throw new CreateException("Database server connection failed: " + e.getMessage());
        } catch (WebApplicationException e) {
            throw new CreateException("Server error: " + e.getMessage());
        }
    }

    @Override
    public Paquete updatePackage(Paquete paquete) throws UpdateException {
        try {
            GenericType<Paquete> responseType = new GenericType<Paquete>() {
            };
            return webClient.updatePackage(paquete, responseType, paquete.getId());
        } catch (ProcessingException e) {
            throw new UpdateException("Database server connection failed: " + e.getMessage());
        } catch (WebApplicationException e) {
            throw new UpdateException("Server error: " + e.getMessage());
        }
    }

    @Override
    public void deletePackages(Long idPaquete) throws DeleteException {
        try {
            webClient.deletePackage(idPaquete);
        } catch (ProcessingException e) {
            throw new DeleteException("Database server connection failed: " + e.getMessage());
        } catch (WebApplicationException e) {
            throw new DeleteException("Server error: " + e.getMessage());
        }
    }

    @Override
    public List<Paquete> findAllPackages() throws SelectException {
        try {
            GenericType<List<Paquete>> responseType = new GenericType<List<Paquete>>() {
            };
            return webClient.findAllPackages(responseType);
        } catch (ProcessingException e) {
            throw new SelectException("Database server connection failed: " + e.getMessage());
        } catch (WebApplicationException e) {
            throw new SelectException("Server error: " + e.getMessage());
        }
    }

    @Override
    public List<Paquete> findAllPackageBySize(PackageSize size) throws SelectException {
        try {
            GenericType<List<Paquete>> responseType = new GenericType<List<Paquete>>() {
            };
            return webClient.findPackagesBySize(responseType, size.toString());
        } catch (ProcessingException e) {
            throw new SelectException("Database server connection failed: " + e.getMessage());
        } catch (WebApplicationException e) {
            throw new SelectException("Server error: " + e.getMessage());
        }
    }

    @Override
    public List<Paquete> findAllPackagesByName(String senderOrReceiverName) throws SelectException {
        try {
            GenericType<List<Paquete>> responseType = new GenericType<List<Paquete>>() {
            };
            return webClient.findPackagesByName(responseType, senderOrReceiverName);
        } catch (ProcessingException e) {
            throw new SelectException("Database server connection failed: " + e.getMessage());
        } catch (WebApplicationException e) {
            throw new SelectException("Server error: " + e.getMessage());
        }
    }

    @Override
    public List<Paquete> findPackagesBeforeDate(String endDate) throws SelectException {
        try {
            GenericType<List<Paquete>> responseType = new GenericType<List<Paquete>>() {
            };
            return webClient.findPackagesBeforeDate(responseType, endDate);
        } catch (ProcessingException e) {
            throw new SelectException("Database server connection failed: " + e.getMessage());
        } catch (WebApplicationException e) {
            throw new SelectException("Server error: " + e.getMessage());
        }
    }

    @Override
    public List<Paquete> findPackagesAfterDate(String startDate) throws SelectException {
        try {
            GenericType<List<Paquete>> responseType = new GenericType<List<Paquete>>() {
            };
            return webClient.findPackagesAfterDate(responseType, startDate);
        } catch (ProcessingException e) {
            throw new SelectException("Database server connection failed: " + e.getMessage());
        } catch (WebApplicationException e) {
            throw new SelectException("Server error: " + e.getMessage());
        }
    }

    @Override
    public List<Paquete> findPackagesBetweenDates(String endDate, String startDate) throws SelectException {
        try {
            GenericType<List<Paquete>> responseType = new GenericType<List<Paquete>>() {
            };
            return webClient.findPackagesBetweenDates(responseType, endDate, startDate);
        } catch (ProcessingException e) {
            throw new SelectException("Database server connection failed: " + e.getMessage());
        } catch (WebApplicationException e) {
            throw new SelectException("Server error: " + e.getMessage());
        }
    }
}
