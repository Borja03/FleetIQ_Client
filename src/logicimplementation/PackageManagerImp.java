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

/**
 * Implementation of the {@link logicInterface.PaqueteManager} interface. This
 * class handles package-related operations by communicating with the RESTful
 * web service using {@link service.PackageRESTClient}.
 */
public class PackageManagerImp implements PaqueteManager {

    /**
     * REST client for handling package-related requests.
     */
    private PackageRESTClient packageRESTClient;

    
     public PackageManagerImp(PackageRESTClient client) {
        this.packageRESTClient = client;
    }
    
    
    
    
    /**
     * Constructs a new {@code PackageManagerImp} instance. Initializes the REST
     * client.
     */
//    public PackageManagerImp() {
//        this.packageRESTClient = new PackageRESTClient();
//    }

    /**
     * Adds a new package.
     *
     * @param paquete The package to add.
     * @return The added package with updated details.
     * @throws CreateException If an error occurs while creating the package.
     */
    @Override
    public Paquete addPackage(Paquete paquete) throws CreateException {
        try {
            GenericType<Paquete> responseType = new GenericType<Paquete>() {
            };
            return packageRESTClient.createPackage(paquete, responseType);
        } catch (WebApplicationException | ProcessingException e) {
            throw new CreateException("Server error: " + e.getMessage());
        } catch (Exception e) {
            throw new CreateException("Database server connection failed: " + e.getMessage());
        }
    }

    /**
     * Updates an existing package.
     *
     * @param paquete The package with updated information.
     * @return The updated package.
     * @throws UpdateException If an error occurs while updating the package.
     */
    @Override
    public Paquete updatePackage(Paquete paquete) throws UpdateException {
        try {
            GenericType<Paquete> responseType = new GenericType<Paquete>() {
            };
            return packageRESTClient.updatePackage(paquete, responseType, paquete.getId());
        } catch (WebApplicationException | ProcessingException e) {
            throw new UpdateException("Server error: " + e.getMessage());
        }
    }

    /**
     * Deletes a package by its ID.
     *
     * @param idPaquete The ID of the package to delete.
     * @throws DeleteException If an error occurs while deleting the package.
     */
    public void deletePackages(Long idPaquete) throws DeleteException {
        System.out.println("Deleting package with ID: " + idPaquete);
        try {
            packageRESTClient.deletePackage(idPaquete);
        } catch (WebApplicationException | ProcessingException e) {
            throw new DeleteException("Server error: " + e.getMessage());
        } catch (Exception e) {
            throw new DeleteException("Error: deleting package failed: " + e.getMessage());
        }
    }

    /**
     * Retrieves all packages.
     *
     * @return A list of all packages.
     * @throws SelectException If an error occurs while retrieving packages.
     */
    @Override
    public List<Paquete> findAllPackages() throws SelectException {
        try {
            GenericType<List<Paquete>> responseType = new GenericType<List<Paquete>>() {
            };
            return packageRESTClient.findAllPackages(responseType);
        } catch (WebApplicationException | ProcessingException e) {
            throw new SelectException("Server error: " + e.getMessage());
        } catch (Exception e) {
            throw new SelectException("Database server connection failed: " + e.getMessage());
        }
    }

    /**
     * Retrieves all packages filtered by size.
     *
     * @param size The size to filter packages by.
     * @return A list of packages of the specified size.
     * @throws SelectException If an error occurs while retrieving packages.
     */
    @Override
    public List<Paquete> findAllPackageBySize(PackageSize size) throws SelectException {
        try {
            GenericType<List<Paquete>> responseType = new GenericType<List<Paquete>>() {
            };
            return packageRESTClient.findPackagesBySize(responseType, size.toString());
        } catch (WebApplicationException | ProcessingException e) {
            throw new SelectException("Server error: " + e.getMessage());
        } catch (Exception e) {
            throw new SelectException("Database server connection failed: " + e.getMessage());
        }
    }

    /**
     * Retrieves all packages associated with a sender or receiver name.
     *
     * @param senderOrReceiverName The name to filter packages by.
     * @return A list of packages matching the provided name.
     * @throws SelectException If an error occurs while retrieving packages.
     */
    @Override
    public List<Paquete> findAllPackagesByName(String senderOrReceiverName) throws SelectException {
        try {
            GenericType<List<Paquete>> responseType = new GenericType<List<Paquete>>() {
            };
            return packageRESTClient.findPackagesByName(responseType, senderOrReceiverName);
        } catch (WebApplicationException | ProcessingException e) {
            throw new SelectException("Server error: " + e.getMessage());
        } catch (Exception e) {
            throw new SelectException("Database server connection failed: " + e.getMessage());
        }
    }

    /**
     * Retrieves packages created before a specific date.
     *
     * @param endDate The cutoff date.
     * @return A list of packages created before the given date.
     * @throws SelectException If an error occurs while retrieving packages.
     */
    @Override
    public List<Paquete> findPackagesBeforeDate(String endDate) throws SelectException {
        try {
            GenericType<List<Paquete>> responseType = new GenericType<List<Paquete>>() {
            };
            return packageRESTClient.findPackagesBeforeDate(responseType, endDate);
        } catch (WebApplicationException | ProcessingException e) {
            throw new SelectException("Server error: " + e.getMessage());
        } catch (Exception e) {
            throw new SelectException("Database server connection failed: " + e.getMessage());
        }
    }

    /**
     * Retrieves packages created after a specific date.
     *
     * @param startDate The starting date.
     * @return A list of packages created after the given date.
     * @throws SelectException If an error occurs while retrieving packages.
     */
    @Override
    public List<Paquete> findPackagesAfterDate(String startDate) throws SelectException {
        try {
            GenericType<List<Paquete>> responseType = new GenericType<List<Paquete>>() {
            };
            return packageRESTClient.findPackagesAfterDate(responseType, startDate);
        } catch (WebApplicationException | ProcessingException e) {
            throw new SelectException("Server error: " + e.getMessage());
        } catch (Exception e) {
            throw new SelectException("Database server connection failed: " + e.getMessage());
        }
    }

    /**
     * Retrieves packages within a specific date range.
     *
     * @param endDate The end date of the range.
     * @param startDate The start date of the range.
     * @return A list of packages created within the specified date range.
     * @throws SelectException If an error occurs while retrieving packages.
     */
    @Override
    public List<Paquete> findPackagesBetweenDates(String endDate, String startDate) throws SelectException {
        try {
            GenericType<List<Paquete>> responseType = new GenericType<List<Paquete>>() {
            };
            return packageRESTClient.findPackagesBetweenDates(responseType, endDate, startDate);
        } catch (WebApplicationException | ProcessingException e) {
            throw new SelectException("Server error: " + e.getMessage());
        } catch (Exception e) {
            throw new SelectException("Database server connection failed: " + e.getMessage());
        }
    }

}
