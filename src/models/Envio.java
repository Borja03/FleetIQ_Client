/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.util.Date;

/**
 *
 * @author Alder
 */
public class Envio {
    private Integer id;
    private Date fecha_envio;
    private Date fecha_entrega;
    private Estado estado;
    private Integer num_paquetes;
    private String creador_envio;
    private String ruta;
    private String vehiculo;

    public Integer getId() {
        return id;
    }

    public Date getFecha_envio() {
        return fecha_envio;
    }

    public Date getFecha_entrega() {
        return fecha_entrega;
    }

    public Estado getEstado() {
        return estado;
    }

    public Integer getNum_paquetes() {
        return num_paquetes;
    }

    public String getCreador_envio() {
        return creador_envio;
    }

    public String getRuta() {
        return ruta;
    }

    public String getVehiculo() {
        return vehiculo;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setFecha_envio(Date fecha_envio) {
        this.fecha_envio = fecha_envio;
    }

    public void setFecha_entrega(Date fecha_entrega) {
        this.fecha_entrega = fecha_entrega;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public void setNum_paquetes(Integer num_paquetes) {
        this.num_paquetes = num_paquetes;
    }

    public void setCreador_envio(String creador_envio) {
        this.creador_envio = creador_envio;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public void setVehiculo(String vehiculo) {
        this.vehiculo = vehiculo;
    }
    
    
    
}
