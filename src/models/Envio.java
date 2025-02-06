package models;

import models.*;
import models.Estado;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Entidad JPA que representa un Envío.
 */
@XmlRootElement
public class Envio implements Serializable, Cloneable {

    private Integer id;

    private Date fechaEnvio;

    private Date fechaEntrega;

    private Estado estado;

    private Integer numPaquetes;

    private String creadorEnvio;

    private String ruta;

    private String vehiculo;

    private EnvioRutaVehiculo envioRutaVehiculo;

    private List<User> userList;

    private List<Paquete> packageList;

    // Métodos de acceso (getters y setters) de los campos transitorios
    public void setPackageList(List<Paquete> packageList) {
        this.packageList = packageList;
    }

    public EnvioRutaVehiculo getEnvioRutaVehiculo() {
        return envioRutaVehiculo;
    }

    public void setEnvioRutaVehiculo(EnvioRutaVehiculo envioRutaVehiculo) {
        this.envioRutaVehiculo = envioRutaVehiculo;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

    // Getters y Setters de otros campos
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getFechaEnvio() {
        return fechaEnvio;
    }

    public void setFechaEnvio(Date fechaEnvio) {
        this.fechaEnvio = fechaEnvio;
    }

    public Date getFechaEntrega() {
        return fechaEntrega;
    }

    public void setFechaEntrega(Date fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public Integer getNumPaquetes() {
        return numPaquetes;
    }

    public void setNumPaquetes(Integer numPaquetes) {
        this.numPaquetes = numPaquetes;
    }

    public String getCreadorEnvio() {
        return creadorEnvio;
    }

    public void setCreadorEnvio(String creadorEnvio) {
        this.creadorEnvio = creadorEnvio;
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public String getVehiculo() {
        return vehiculo;
    }

    public void setVehiculo(String vehiculo) {
        this.vehiculo = vehiculo;
    }
    
    @Override
    public Envio clone() throws CloneNotSupportedException{
        return (Envio) super.clone();
    }

    @Override
    public String toString() {
        return "Envio{" + "id=" + id + ", fechaEnvio=" + fechaEnvio + ", fechaEntrega=" + fechaEntrega + ", estado=" + estado + ", numPaquetes=" + numPaquetes + ", creadorEnvio=" + creadorEnvio + ", ruta=" + ruta + ", vehiculo=" + vehiculo + '}';
    }
}
