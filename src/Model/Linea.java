package Model;

public class Linea {
    private String id;
    private String fechaEnvio;
    private String fechaEntrega;
    private String estado;
    private String ruta;
    private String creador;
    private String vehiculo;
    private int numPaquetes;

    // Constructor
    public Linea(String id, String fechaEnvio, String fechaEntrega, String estado, String ruta,
                 String creador, String vehiculo, int numPaquetes) {
        this.id = id;
        this.fechaEnvio = fechaEnvio;
        this.fechaEntrega = fechaEntrega;
        this.estado = estado;
        this.ruta = ruta;
        this.creador = creador;
        this.vehiculo = vehiculo;
        this.numPaquetes = numPaquetes;
    }

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getFechaEnvio() { return fechaEnvio; }
    public void setFechaEnvio(String fechaEnvio) { this.fechaEnvio = fechaEnvio; }

    public String getFechaEntrega() { return fechaEntrega; }
    public void setFechaEntrega(String fechaEntrega) { this.fechaEntrega = fechaEntrega; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getRuta() { return ruta; }
    public void setRuta(String ruta) { this.ruta = ruta; }

    public String getCreador() { return creador; }
    public void setCreador(String creador) { this.creador = creador; }

    public String getVehiculo() { return vehiculo; }
    public void setVehiculo(String vehiculo) { this.vehiculo = vehiculo; }

    public int getNumPaquetes() { return numPaquetes; }
    public void setNumPaquetes(int numPaquetes) { this.numPaquetes = numPaquetes; }
}
