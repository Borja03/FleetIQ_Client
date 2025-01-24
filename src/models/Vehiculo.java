package models;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * Entidad JPA que representa un Vehículo.
 */
@XmlRootElement
public class Vehiculo implements Serializable {

    private Integer id;

    private String matricula;

    private String modelo;

    private Integer capacidadCarga;

    private Date registrationDate;

    private Date itvDate;

    private boolean activo;

    private List<EnvioRutaVehiculo> envioRutaVehiculoList;

    public Vehiculo(Integer id, String matricula, String modelo, Integer capacidadCarga, Date registrationDate, Date itvDate, boolean activo) {
        this.id = id;
        this.matricula = matricula;
        this.modelo = modelo;
        this.capacidadCarga = capacidadCarga;
        this.registrationDate = registrationDate;
        this.itvDate = itvDate;
        this.activo = activo;
    }

    public Vehiculo() {
    }

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

    // Getters y Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public Integer getCapacidadCarga() {
        return capacidadCarga;
    }

    public void setCapacidadCarga(Integer capacidadCarga) {
        this.capacidadCarga = capacidadCarga;
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }

    public Date getItvDate() {
        return itvDate;
    }

    public void setItvDate(Date itvDate) {
        this.itvDate = itvDate;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    @XmlTransient
    public List<EnvioRutaVehiculo> getEnvioRutaVehiculoList() {
        return envioRutaVehiculoList;
    }

    public void setEnvioRutaVehiculoList(List<EnvioRutaVehiculo> envioRutaVehiculoList) {
        this.envioRutaVehiculoList = envioRutaVehiculoList;
    }

    @Override
    public String toString() {
        return "Vehiculo{" + "id=" + id + ", matricula=" + matricula + ", modelo=" + modelo + ", capacidadCarga=" + capacidadCarga + ", registrationDate=" + registrationDate + ", itvDate=" + itvDate + ", activo=" + activo + ", envioRutaVehiculoList=" + envioRutaVehiculoList + '}';
    }

}
