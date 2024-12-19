package models;

import java.time.LocalDate;

/**
 * Modelo que representa una Ruta.
 * Este modelo se utiliza en la lógica de negocio y las capas superiores de la aplicación.
 * 
 * Autor: Borja
 */
public class Ruta {

    private Integer localizador;
    private String origen;
    private String destino;
    private Float distancia;
    private Integer tiempo;
    private String type; // Representa el tipo (MAYOR, MENOR, IGUAL)
    private LocalDate fechaCreacion;
    private Integer numVehiculos;

    // Constructores
    public Ruta() {
    }

    public Ruta(Integer localizador, String origen, String destino, Float distancia, Integer tiempo, String type, LocalDate fechaCreacion, Integer numVehiculos) {
        this.localizador = localizador;
        this.origen = origen;
        this.destino = destino;
        this.distancia = distancia;
        this.tiempo = tiempo;
        this.type = type;
        this.fechaCreacion = fechaCreacion;
        this.numVehiculos = numVehiculos;
    }

    // Getters y Setters
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDate getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDate fechaCreacion) {
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
                ", type='" + type + '\'' +
                ", fechaCreacion=" + fechaCreacion +
                ", numVehiculos=" + numVehiculos +
                '}';
    }
}
