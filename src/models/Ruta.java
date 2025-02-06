package models;

import java.io.Serializable;

import java.util.Date;
import java.util.List;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * The Ruta class represents a route with relevant information such as origin,
 * destination, distance, time, creation date, and the number of vehicles
 * assigned to the route. It implements the Serializable and Cloneable
 * interfaces for object persistence and duplication. This class is typically
 * used in routing or logistics systems.
 *
 * <p>
 * The class includes getter and setter methods for each field, allowing
 * manipulation and access to route properties. Additionally, the class supports
 * cloning to create deep copies of the route instances.</p>
 *
 * @see Serializable
 * @see Cloneable
 * @author Borja
 */
@XmlRootElement
public class Ruta implements Serializable, Cloneable {

    /**
     * Unique identifier for the route.
     */
    private Integer localizador;

    /**
     * The origin point of the route.
     */
    private String origen;

    /**
     * The destination point of the route.
     */
    private String destino;

    /**
     * The distance of the route in kilometers.
     */
    private Float distancia;

    /**
     * The time it takes to complete the route, typically in minutes.
     */
    private Integer tiempo;

    /**
     * The creation date of the route.
     */
    private Date fechaCreacion;

    /**
     * The number of vehicles assigned to this route.
     */
    private Integer numVehiculos;

    /**
     * Default constructor for creating a new instance of Ruta.
     */
    public Ruta() {
    }

    /**
     * List of vehicles associated with the route.
     */
    private List<EnvioRutaVehiculo> envioRutaVehiculos;

    /**
     * Gets the unique identifier of the route.
     *
     * @return the localizador (ID) of the route
     */
    public Integer getLocalizador() {
        return localizador;
    }

    /**
     * Sets the unique identifier of the route.
     *
     * @param localizador the unique identifier to set
     */
    public void setLocalizador(Integer localizador) {
        this.localizador = localizador;
    }

    /**
     * Gets the origin point of the route.
     *
     * @return the origin of the route
     */
    public String getOrigen() {
        return origen;
    }

    /**
     * Sets the origin point of the route.
     *
     * @param origen the origin to set
     */
    public void setOrigen(String origen) {
        this.origen = origen;
    }

    /**
     * Gets the destination point of the route.
     *
     * @return the destination of the route
     */
    public String getDestino() {
        return destino;
    }

    /**
     * Sets the destination point of the route.
     *
     * @param destino the destination to set
     */
    public void setDestino(String destino) {
        this.destino = destino;
    }

    /**
     * Gets the distance of the route.
     *
     * @return the distance of the route in kilometers
     */
    public Float getDistancia() {
        return distancia;
    }

    /**
     * Sets the distance of the route.
     *
     * @param distancia the distance to set in kilometers
     */
    public void setDistancia(Float distancia) {
        this.distancia = distancia;
    }

    /**
     * Gets the time required to complete the route.
     *
     * @return the time in minutes for the route
     */
    public Integer getTiempo() {
        return tiempo;
    }

    /**
     * Sets the time required to complete the route.
     *
     * @param tiempo the time to set in minutes
     */
    public void setTiempo(Integer tiempo) {
        this.tiempo = tiempo;
    }

    /**
     * Gets the creation date of the route.
     *
     * @return the creation date of the route
     */
    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    /**
     * Sets the creation date of the route.
     *
     * @param fechaCreacion the creation date to set
     */
    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    /**
     * Gets the list of vehicles associated with the route.
     *
     * @return the list of EnvioRutaVehiculo objects representing vehicles on
     * the route
     */
    public List<EnvioRutaVehiculo> getRutaVehiculos() {
        return envioRutaVehiculos;
    }

    /**
     * Sets the list of vehicles associated with the route.
     *
     * EnvioRutaVehiculo the list of vehicles to set
     */
    public void setRutaVehiculos(List<EnvioRutaVehiculo> rutaVehiculos) {
        this.envioRutaVehiculos = rutaVehiculos;
    }

    /**
     * Gets the list of EnvioRutaVehiculo associated with this Ruta. This list
     * represents the vehicles assigned to a specific route.
     *
     * @return The list of EnvioRutaVehiculo objects.
     */
    public List<EnvioRutaVehiculo> getEnvioRutaVehiculos() {
        return envioRutaVehiculos;
    }

    /**
     * Sets the list of EnvioRutaVehiculo associated with this Ruta. This list
     * represents the vehicles assigned to a specific route.
     *
     * @param envioRutaVehiculos The list of EnvioRutaVehiculo objects to set.
     */
    public void setEnvioRutaVehiculos(List<EnvioRutaVehiculo> envioRutaVehiculos) {
        this.envioRutaVehiculos = envioRutaVehiculos;
    }

    /**
     * Returns the unique identifier of the route (ID).
     *
     * @return the localizador (ID) of the route
     */
    public Integer getId() {
        return localizador;
    }

    /**
     * Gets the number of vehicles assigned to the route.
     *
     * @return the number of vehicles on the route
     */
    public Integer getNumVehiculos() {
        return numVehiculos;
    }

    /**
     * Sets the number of vehicles assigned to the route.
     *
     * @param numVehiculos the number of vehicles to set
     */
    public void setNumVehiculos(Integer numVehiculos) {
        this.numVehiculos = numVehiculos;
    }

    /**
     * Returns a string representation of the Ruta object. The string contains
     * key details of the route such as its localizador, origen, destino,
     * distancia, tiempo, fechaCreacion, and numVehiculos.
     *
     * @return a string representation of the Ruta
     */
    @Override
    public Ruta clone() throws CloneNotSupportedException {
        Ruta cloned = (Ruta) super.clone();
        // Si 'fechaCreacion' es mutable (ej. java.util.Date), clonalo también
        return cloned;
    }

    @Override
    public String toString() {
        return "Ruta{" + "localizador=" + localizador + ", origen=" + origen + ", destino=" + destino + ", distancia=" + distancia + ", tiempo=" + tiempo + ", fechaCreacion=" + fechaCreacion + ", numVehiculos=" + numVehiculos;
    }

}
