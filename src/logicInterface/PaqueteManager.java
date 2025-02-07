package logicInterface;

import exception.CreateException;
import exception.DeleteException;
import exception.SelectException;
import exception.UpdateException;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import models.Paquete;
import models.PackageSize;

/**
 * Interface for managing {@link Paquete} entities.
 * <p>
 * This interface defines methods for creating, updating, deleting,
 * and retrieving package data within the system.
 * </p>
 *
 * @since 1.0
 * @version 1.0
 */
public interface PaqueteManager {

    /**
     * Adds a new package to the system.
     *
     * @param paquete The package to add.
     * @return The added package with updated details.
     * @throws CreateException If an error occurs during package creation.
     */
    Paquete addPackage(Paquete paquete) throws CreateException;

    /**
     * Updates an existing package.
     *
     * @param paquete The package with updated information.
     * @return The updated package.
     * @throws UpdateException If an error occurs during the update process.
     */
    Paquete updatePackage(Paquete paquete) throws UpdateException;

    /**
     * Deletes a package by its ID.
     *
     * @param idPaquete The ID of the package to delete.
     * @throws DeleteException If an error occurs during package deletion.
     */
    void deletePackages(Long idPaquete) throws DeleteException;

    /**
     * Retrieves all packages.
     *
     * @return A list of all packages, or an empty list if none are found.
     * @throws SelectException If an error occurs during retrieval.
     */
    List<Paquete> findAllPackages() throws SelectException;

    /**
     * Retrieves all packages filtered by size.
     *
     * @param size The size to filter packages by.
     * @return A list of packages of the specified size.
     * @throws SelectException If an error occurs during retrieval.
     */
    List<Paquete> findAllPackageBySize(PackageSize size) throws SelectException;

    /**
     * Retrieves all packages associated with a sender or receiver name.
     *
     * @param senderOrReceiverName The name to filter packages by.
     * @return A list of packages matching the provided name.
     * @throws SelectException If an error occurs during retrieval.
     */
    List<Paquete> findAllPackagesByName(String senderOrReceiverName) throws SelectException;

    /**
     * Retrieves all packages created before a specific date.
     *
     * @param endDate The cut-off date for retrieving packages (inclusive).
     * @return A list of packages created before the specified date.
     * @throws SelectException If an error occurs during retrieval.
     */
    List<Paquete> findPackagesBeforeDate(String endDate) throws SelectException;

    /**
     * Retrieves all packages created after a specific date.
     *
     * @param startDate The start date for retrieving packages (inclusive).
     * @return A list of packages created after the specified date.
     * @throws SelectException If an error occurs during retrieval.
     */
    List<Paquete> findPackagesAfterDate(String startDate) throws SelectException;

    /**
     * Retrieves all packages created within a specific date range.
     *
     * @param endDate The end date of the range (inclusive).
     * @param startDate The start date of the range (inclusive).
     * @return A list of packages created within the specified date range.
     * @throws SelectException If an error occurs during retrieval.
     */
    List<Paquete> findPackagesBetweenDates(String endDate, String startDate) throws SelectException;
}
