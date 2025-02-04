package logicimplementation;

import exception.CreateException;
import exception.DeleteException;
import exception.SelectException;
import exception.UpdateException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.GenericType;
import models.Paquete;
import models.PackageSize;
import service.PackageRESTClient;
import logicInterface.PaqueteManager;

public class PackageManagerImp implements PaqueteManager  {

    private PackageRESTClient webClient;

    public PackageManagerImp() {
        this.webClient = new PackageRESTClient();
        
    }

    @Override
    public Paquete addPackage(Paquete paquete) throws CreateException {
        try {
            GenericType<Paquete> responseType = new GenericType<Paquete>() {};
            return webClient.createPackage(paquete, responseType);
        } catch (WebApplicationException e) {
            throw new CreateException("Server error: " + e.getMessage());
        } catch (ProcessingException e) {
            throw new CreateException("Database server connection failed: " + e.getMessage());
        }
    }

    @Override
    public Paquete updatePackage(Paquete paquete) throws UpdateException {
        try {
            GenericType<Paquete> responseType = new GenericType<Paquete>() {};
            return webClient.updatePackage(paquete, responseType, paquete.getId());
        } catch (WebApplicationException e) {
            throw new UpdateException("Server error: " + e.getMessage());
        } 
    }

    @Override
    public void deletePackages(Long idPaquete) throws DeleteException {
        try {
            webClient.deletePackage(idPaquete);
        } catch (WebApplicationException e) {
            throw new DeleteException("Server error: " + e.getMessage());
        } catch (ProcessingException e) {
            throw new DeleteException("Database server connection failed: " + e.getMessage());
        }
    }

    @Override
    public List<Paquete> findAllPackages() throws SelectException {
        try {
            GenericType<List<Paquete>> responseType = new GenericType<List<Paquete>>() {};
            return webClient.findAllPackages(responseType);
        } catch (WebApplicationException e) {
            throw new SelectException("Server error: " + e.getMessage());
        } catch (ProcessingException e) {
            throw new SelectException("Database server connection failed: " + e.getMessage());
        }
    }

    @Override
    public List<Paquete> findAllPackageBySize(PackageSize size) throws SelectException {
        try {
            GenericType<List<Paquete>> responseType = new GenericType<List<Paquete>>() {};
            return webClient.findPackagesBySize(responseType, size.toString());
        } catch (WebApplicationException e) {
            throw new SelectException("Server error: " + e.getMessage());
        } catch (ProcessingException e) {
            throw new SelectException("Database server connection failed: " + e.getMessage());
        }
    }

    @Override
    public List<Paquete> findAllPackagesByName(String senderOrReceiverName) throws SelectException {
        try {
            GenericType<List<Paquete>> responseType = new GenericType<List<Paquete>>() {};
            return webClient.findPackagesByName(responseType, senderOrReceiverName);
        } catch (WebApplicationException e) {
            throw new SelectException("Server error: " + e.getMessage());
        } catch (ProcessingException e) {
            throw new SelectException("Database server connection failed: " + e.getMessage());
        }
    }

    @Override
    public List<Paquete> findPackagesBeforeDate(String endDate) throws SelectException {
        try {
            GenericType<List<Paquete>> responseType = new GenericType<List<Paquete>>() {};
            return webClient.findPackagesBeforeDate(responseType, endDate);
        } catch (WebApplicationException e) {
            throw new SelectException("Server error: " + e.getMessage());
        } catch (ProcessingException e) {
            throw new SelectException("Database server connection failed: " + e.getMessage());
        }
    }

    @Override
    public List<Paquete> findPackagesAfterDate(String startDate) throws SelectException {
        try {
            GenericType<List<Paquete>> responseType = new GenericType<List<Paquete>>() {};
            return webClient.findPackagesAfterDate(responseType, startDate);
        } catch (WebApplicationException e) {
            throw new SelectException("Server error: " + e.getMessage());
        } catch (ProcessingException e) {
            throw new SelectException("Database server connection failed: " + e.getMessage());
        }
    }

    @Override
    public List<Paquete> findPackagesBetweenDates(String endDate, String startDate) throws SelectException {
        try {
            GenericType<List<Paquete>> responseType = new GenericType<List<Paquete>>() {};
            return webClient.findPackagesBetweenDates(responseType, endDate, startDate);
        } catch (WebApplicationException e) {
            throw new SelectException("Server error: " + e.getMessage());
        } catch (ProcessingException e) {
            throw new SelectException("Database server connection failed: " + e.getMessage());
        }
    }

}