package models;

import java.io.Serializable;

import java.util.Date;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * Represents a vehicle entity in the system.
 *
 * This class is a JPA entity that stores vehicle information
 *
 * such as license plate, model, load capacity, registration date,
 *
 * ITV inspection date, and active status.
 *
 */
@XmlRootElement

public class Vehiculo implements Serializable, Cloneable {

    private Integer id;

    private String matricula;

    private String modelo;

    private Integer capacidadCarga;

    private Date registrationDate;

    private Date itvDate;

    private boolean activo;

    private List<EnvioRutaVehiculo> envioRutaVehiculoList;

    /**
     *
     * Constructs a new Vehiculo with the specified parameters.
     *
     *
     *
     * @param id the vehicle ID
     *
     * @param matricula the vehicle's license plate
     *
     * @param modelo the model of the vehicle
     *
     * @param capacidadCarga the load capacity of the vehicle
     *
     * @param registrationDate the date the vehicle was registered
     *
     * @param itvDate the date of the last ITV inspection
     *
     * @param activo whether the vehicle is active
     *
     */
    public Vehiculo(Integer id, String matricula, String modelo, Integer capacidadCarga, Date registrationDate, Date itvDate, boolean activo) {

        this.id = id;

        this.matricula = matricula;

        this.modelo = modelo;

        this.capacidadCarga = capacidadCarga;

        this.registrationDate = registrationDate;

        this.itvDate = itvDate;

        this.activo = activo;

    }

    /**
     *
     * Default constructor.
     *
     */
    public Vehiculo() {

    }

    /**
     *
     * Constructs a new Vehiculo with a list of associated shipments.
     *
     *
     *
     * @param id the vehicle ID
     *
     * @param matricula the vehicle's license plate
     *
     * @param modelo the model of the vehicle
     *
     * @param capacidadCarga the load capacity of the vehicle
     *
     * @param registrationDate the date the vehicle was registered
     *
     * @param itvDate the date of the last ITV inspection
     *
     * @param activo whether the vehicle is active
     *
     * @param envioRutaVehiculoList the list of vehicle route shipments
     *
     */
    public Vehiculo(Integer id, String matricula, String modelo, Integer capacidadCarga, Date registrationDate, Date itvDate, boolean activo, List<EnvioRutaVehiculo> envioRutaVehiculoList) {

        this.id = id;

        this.matricula = matricula;

        this.modelo = modelo;

        this.capacidadCarga = capacidadCarga;

        this.registrationDate = registrationDate;

        this.itvDate = itvDate;

        this.activo = activo;

        this.envioRutaVehiculoList = envioRutaVehiculoList;

    }

    /**
     *
     * Returns the vehicle ID.
     *
     * @return the ID of the vehicle
     *
     */
    public Integer getId() {

        return id;

    }

    /**
     *
     * Sets the vehicle ID.
     *
     * @param id the ID to set
     *
     */
    public void setId(Integer id) {

        this.id = id;

    }

    /**
     *
     * Returns the license plate of the vehicle.
     *
     * @return the license plate
     *
     */
    public String getMatricula() {

        return matricula;

    }

    /**
     *
     * Sets the license plate of the vehicle.
     *
     * @param matricula the license plate to set
     *
     */
    public void setMatricula(String matricula) {

        this.matricula = matricula;

    }

    /**
     *
     * Returns the model of the vehicle.
     *
     * @return the vehicle model
     *
     */
    public String getModelo() {

        return modelo;

    }

    /**
     *
     * Sets the model of the vehicle.
     *
     * @param modelo the model to set
     *
     */
    public void setModelo(String modelo) {

        this.modelo = modelo;

    }

    /**
     *
     * Returns the load capacity of the vehicle.
     *
     * @return the load capacity
     *
     */
    public Integer getCapacidadCarga() {

        return capacidadCarga;

    }

    /**
     *
     * Sets the load capacity of the vehicle.
     *
     * @param capacidadCarga the load capacity to set
     *
     */
    public void setCapacidadCarga(Integer capacidadCarga) {

        this.capacidadCarga = capacidadCarga;

    }

    /**
     *
     * Returns the registration date of the vehicle.
     *
     * @return the registration date
     *
     */
    public Date getRegistrationDate() {

        return registrationDate;

    }

    /**
     *
     * Sets the registration date of the vehicle.
     *
     * @param registrationDate the registration date to set
     *
     */
    public void setRegistrationDate(Date registrationDate) {

        this.registrationDate = registrationDate;

    }

    /**
     *
     * Returns the date of the last ITV inspection.
     *
     * @return the ITV inspection date
     *
     */
    public Date getItvDate() {

        return itvDate;

    }

    /**
     *
     * Sets the ITV inspection date of the vehicle.
     *
     * @param itvDate the ITV date to set
     *
     */
    public void setItvDate(Date itvDate) {

        this.itvDate = itvDate;

    }

    /**
     *
     * Checks if the vehicle is active.
     *
     * @return true if the vehicle is active, false otherwise
     *
     */
    public boolean isActivo() {

        return activo;

    }

    /**
     *
     * Sets the active status of the vehicle.
     *
     * @param activo the active status to set
     *
     */
    public void setActivo(boolean activo) {

        this.activo = activo;

    }

    /**
     *
     * Returns the list of associated shipments.
     *
     * @return the list of shipments
     *
     */
    @XmlTransient

    public List<EnvioRutaVehiculo> getEnvioRutaVehiculoList() {

        return envioRutaVehiculoList;

    }

    /**
     *
     * Sets the list of associated shipments.
     *
     * @param envioRutaVehiculoList the shipment list to set
     *
     */
    public void setEnvioRutaVehiculoList(List<EnvioRutaVehiculo> envioRutaVehiculoList) {

        this.envioRutaVehiculoList = envioRutaVehiculoList;

    }

    /**
     *
     * Creates and returns a copy of this vehicle.
     *
     * @return a clone of this vehicle
     *
     * @throws CloneNotSupportedException if cloning is not supported
     *
     */
    @Override

    public Vehiculo clone() throws CloneNotSupportedException {

        return (Vehiculo) super.clone();

    }

    /**
     *
     * Returns a string representation of the vehicle.
     *
     * @return a string with the vehicle details
     *
     */
    @Override
    public String toString() {
        return "Vehiculo{" + "id=" + id + ", matricula=" + matricula + ", modelo=" + modelo + ", capacidadCarga=" + capacidadCarga + ", registrationDate=" + registrationDate + ", itvDate=" + itvDate + ", activo=" + activo + ", envioRutaVehiculoList=" + envioRutaVehiculoList + '}';
    }

}
