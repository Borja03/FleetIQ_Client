package models;

import java.io.Serializable;

import java.util.Date;
import java.util.List;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement
public class Ruta implements Serializable {

    private Integer localizador;

    private String origen;

    private String destino;

    private Float distancia;

    private Integer tiempo;

    private Date fechaCreacion;

    private Integer numVehiculos;

    public Ruta() {
    }

    private List<EnvioRutaVehiculo> envioRutaVehiculos;

    // Getters and Setters
    public Integer getLocalizador() {
        return localizador;
    }

    public void setLocalizador(Integer localizador) {
        this.localizador = localizador;
    }

    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public Float getDistancia() {
        return distancia;
    }

    public void setDistancia(Float distancia) {
        this.distancia = distancia;
    }

    public Integer getTiempo() {
        return tiempo;
    }

    public void setTiempo(Integer tiempo) {
        this.tiempo = tiempo;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public List<EnvioRutaVehiculo> getRutaVehiculos() {
        return envioRutaVehiculos;
    }

    public void setRutaVehiculos(List<EnvioRutaVehiculo> rutaVehiculos) {
        this.envioRutaVehiculos = rutaVehiculos;
    }

    public List<EnvioRutaVehiculo> getEnvioRutaVehiculos() {
        return envioRutaVehiculos;
    }

    public void setEnvioRutaVehiculos(List<EnvioRutaVehiculo> envioRutaVehiculos) {
        this.envioRutaVehiculos = envioRutaVehiculos;
    }

    public Integer getId() {
        return localizador;
    }
    
      public Integer getNumVehiculos() {
        return numVehiculos;
    }

    public void setNumVehiculos(Integer numVehiculos) {
        this.numVehiculos = numVehiculos;
    }

    @Override
    public String toString() {
        return "Ruta{" + "localizador=" + localizador + ", origen=" + origen + ", destino=" + destino + ", distancia=" + distancia + ", tiempo=" + tiempo + ", fechaCreacion=" + fechaCreacion + ", numVehiculos=" + numVehiculos;
    }

}
