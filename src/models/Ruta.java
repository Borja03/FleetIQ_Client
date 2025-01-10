package models;

import java.util.Date;
import java.util.List;

public class Ruta {

    private Integer localizador;
    private String origen;
    private String destino;
    private Float distancia;
    private Integer tiempo;
    private Date fechaCreacion;
    private Integer numVehiculos;


    // Constructor vacío
    public Ruta() {
    }

    // Constructor parametrizado
    public Ruta(Integer localizador, String origen, String destino, Float distancia, Integer tiempo, Date fechaCreacion, Integer numVehiculos) {
        this.localizador = localizador;
        this.origen = origen;
        this.destino = destino;
        this.distancia = distancia;
        this.tiempo = tiempo;
        this.fechaCreacion = fechaCreacion;
        this.numVehiculos = numVehiculos;
    }

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

    public Integer getNumVehiculos() {
        return numVehiculos;
    }

    public void setNumVehiculos(Integer numVehiculos) {
        this.numVehiculos = numVehiculos;
    }

    @Override
    public String toString() {
        return "Ruta{" +
                "localizador=" + localizador +
                ", origen='" + origen + '\'' +
                ", destino='" + destino + '\'' +
                ", distancia=" + distancia +
                ", tiempo=" + tiempo +
                ", fechaCreacion=" + fechaCreacion +
                ", numVehiculos=" + numVehiculos +
                '}';
    }
}
