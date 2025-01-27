package models;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Entidad JPA que representa la relación entre Envío, Ruta y Vehículo.
 */
@XmlRootElement
public class EnvioRutaVehiculo implements Serializable {
    private Integer id;
    private List<Envio> envios;
    private Ruta ruta;
    private Vehiculo vehiculo;
    private Date fechaAsignacion;
    private Integer rutaLocalizador;
    private Integer vehiculoID;

    // Getters y Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<Envio> getEnvios() {
        return envios;
    }

    public void setEnvios(List<Envio> envios) {
        this.envios = envios;
    }
    
    public Ruta getRuta() {
        return ruta;
    }

    public void setRuta(Ruta ruta) {
        this.ruta = ruta;
    }

    public Vehiculo getVehiculo() {
        return vehiculo;
    }

    public void setVehiculo(Vehiculo vehiculo) {
        this.vehiculo = vehiculo;
    }

    public Date getFechaAsignacion() {
        return fechaAsignacion;
    }

    public void setFechaAsignacion(Date fechaAsignacion) {
        this.fechaAsignacion = fechaAsignacion;
    }

    public Integer getRutaLocalizador() {
        return rutaLocalizador;
    }

    public void setRutaLocalizador(Integer rutaLocalizador) {
        this.rutaLocalizador = rutaLocalizador;
    }

    public Integer getVehiculoID() {
        return vehiculoID;
    }

    public void setVehiculoID(Integer vehiculoID) {
        this.vehiculoID = vehiculoID;
    }

    @Override
    public String toString() {
        return "EnvioRutaVehiculo{" + 
               "id=" + id + 
               ", envios=" + envios + 
               ", ruta=" + ruta + 
               ", vehiculo=" + vehiculo + 
               ", fechaAsignacion=" + fechaAsignacion + 
               ", rutaLocalizador=" + rutaLocalizador + 
               ", vehiculoID=" + vehiculoID + 
               '}';
    }
}