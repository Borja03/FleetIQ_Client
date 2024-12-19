package logicInterface;

import exception.CreateException;
import exception.DeleteException;
import exception.SelectException;
import exception.UpdateException;
import java.time.LocalDate;
import java.util.List;
import models.Package;
import models.PackageSize;

/**
 * Interface for managing Package entities.
 */
public interface PackageManager {

    /**
     * Adds a new paquete to the system.
     *
     * @param paquete the paquete to add
     * @throws CreateException if an error occurs during creation
     */
    Package addPackage(Package paquete) throws CreateException;

    /**
     * Updates an existing paquete.
     *
     * @param paquete the paquete with updated information
     * @throws UpdateException if an error occurs during the update
     */
    Package  updatePackage(Package paquete) throws UpdateException;

    /**
     * Deletes a paquete by its ID.
     *
     * @param idPaquete the ID of the paquete to delete
     * @throws DeleteException if an error occurs during deletion
     */
    void deletePackages(Integer idPaquete) throws DeleteException;
    // multi delte ??
    // 
    // void deletePaquete(Integer idPaquete) throws DeleteException;
    /**
     * Retrieves all paquetes.
     *
     * @return a list of all paquetes, or an empty list if none found
     * @throws SelectException if an error occurs during retrieval
     */
    List<Package> findAllPackages() throws SelectException;

    /**
     * Retrieves paquetes by their size.
     *
     * @param size the size of paquetes to filter by
     * @return a list of matching paquetes
     * @throws SelectException if an error occurs during retrieval
     */
    List<Package> findAllPackageBySize(PackageSize size) throws SelectException ;

    /**
     * Retrieves paquetes within a date range.
     *
     * @param firstDate the start date of the range
     * @param secondDate the end date of the range
     * @return a list of matching paquetes
     * @throws SelectException if an error occurs during retrieval
     */
    List<Package> findAllPackagesByDates(LocalDate firstDate, LocalDate secondDate) throws SelectException;

    /**
     * Retrieves paquetes by sender or receiver name.
     *
     * @param senderOrReceiverName the name to filter paquetes by name
     * @return a list of matching paquetes
     * @throws SelectException if an error occurs during retrieval
     */
    List<Package> findAllPackagesByName(String senderOrReceiverName) throws SelectException;
}

