/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

/**
 *
 * @author 2dam
 */
import java.time.LocalDate;

/**
 * Represents a vehicle with attributes such as ID, license plate, model,
 * capacity, registration date, ITV date, and active status.
 */
public class Vehiculo {

    private int id;
    private String licensePlate;
    private String model;
    private int capacity;
    private LocalDate registrationDate;
    private LocalDate itvDate;
    private boolean active;

    public Vehiculo(int id, String licensePlate, String model, int capacity, LocalDate registrationDate, LocalDate itvDate, boolean active) {
        this.id = id;
        this.licensePlate = licensePlate;
        this.model = model;
        this.capacity = capacity;
        this.registrationDate = registrationDate;
        this.itvDate = itvDate;
        this.active = active;
    }

    public Vehiculo() {
    }

    /**
     * Gets the ID of the vehicle.
     *
     * @return the ID of the vehicle.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the ID of the vehicle.
     *
     * @param id the ID to set.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the license plate of the vehicle.
     *
     * @return the license plate of the vehicle.
     */
    public String getLicensePlate() {
        return licensePlate;
    }

    /**
     * Sets the license plate of the vehicle.
     *
     * @param licensePlate the license plate to set.
     */
    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    /**
     * Gets the model of the vehicle.
     *
     * @return the model of the vehicle.
     */
    public String getModel() {
        return model;
    }

    /**
     * Sets the model of the vehicle.
     *
     * @param model the model to set.
     */
    public void setModel(String model) {
        this.model = model;
    }

    /**
     * Gets the capacity of the vehicle.
     *
     * @return the capacity of the vehicle.
     */
    public int getCapacity() {
        return capacity;
    }

    /**
     * Sets the capacity of the vehicle.
     *
     * @param capacity the capacity to set.
     */
    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    /**
     * Gets the registration date of the vehicle.
     *
     * @return the registration date of the vehicle.
     */
    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    /**
     * Sets the registration date of the vehicle.
     *
     * @param registrationDate the registration date to set.
     */
    public void setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
    }

    /**
     * Gets the ITV date of the vehicle.
     *
     * @return the ITV date of the vehicle.
     */
    public LocalDate getItvDate() {
        return itvDate;
    }

    /**
     * Sets the ITV date of the vehicle.
     *
     * @param itvDate the ITV date to set.
     */
    public void setItvDate(LocalDate itvDate) {
        this.itvDate = itvDate;
    }

    /**
     * Checks if the vehicle is active.
     *
     * @return true if the vehicle is active, false otherwise.
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Sets the active status of the vehicle.
     *
     * @param active the active status to set.
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * Returns a string representation of the vehicle.
     *
     * @return a string containing vehicle details.
     */
    @Override
    public String toString() {
        return "Vehicle{"
                + "id=" + id
                + ", licensePlate='" + licensePlate + '\''
                + ", model='" + model + '\''
                + ", capacity=" + capacity
                + ", registrationDate=" + registrationDate
                + ", itvDate=" + itvDate
                + ", active=" + active
                + '}';
    }
}
