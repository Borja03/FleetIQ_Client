package models;

import java.io.Serializable;

/**
 * Clase que representa a un usuario en el sistema.
 * Contiene la información del usuario, como email, contraseña, nombre,
 * DNI, teléfono y ID de la empresa.
 * 
 * @author Alder
 */
public class User implements Serializable {
    private String email;
    private String password;
    private String name;
    private boolean activo;
    private int companyID;
    private String street;
    private String city;
    private int zip;

    public User() {
    }

    /**
     * Constructor de la clase User.
     * 
     * @param email     El correo electrónico del usuario.
     * @param password  La contraseña del usuario.
     * @param name      El nombre del usuario.
     * @param activo    Estado de actividad del usuario.
     * @param companyID El ID de la empresa a la que pertenece el usuario.
     * @param street    La dirección del usuario.
     * @param city      La ciudad del usuario.
     * @param zip       El código postal del usuario.
     */
    public User(String email, String password, String name, boolean activo, int companyID, String street, String city, int zip) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.activo = activo;
        this.companyID = companyID;
        this.street = street;
        this.city = city;
        this.zip = zip;
    }

    /**
     * Obtiene el correo electrónico del usuario.
     * 
     * @return El correo electrónico del usuario.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Establece el correo electrónico del usuario.
     * 
     * @param email El nuevo correo electrónico del usuario.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Obtiene la contraseña del usuario.
     * 
     * @return La contraseña del usuario.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Establece la contraseña del usuario.
     * 
     * @param password La nueva contraseña del usuario.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Obtiene el nombre del usuario.
     * 
     * @return El nombre del usuario.
     */
    public String getName() {
        return name;
    }

    /**
     * Establece el nombre del usuario.
     * 
     * @param name El nuevo nombre del usuario.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Obtiene el estado de actividad del usuario.
     * 
     * @return true si el usuario está activo, false en caso contrario.
     */
    public boolean isActivo() {
        return activo;
    }

    /**
     * Establece el estado de actividad del usuario.
     * 
     * @param activo El nuevo estado de actividad del usuario.
     */
    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    /**
     * Obtiene el ID de la empresa a la que pertenece el usuario.
     * 
     * @return El ID de la empresa del usuario.
     */
    public int getCompanyID() {
        return companyID;
    }

    /**
     * Establece el ID de la empresa a la que pertenece el usuario.
     * 
     * @param companyID El nuevo ID de la empresa del usuario.
     */
    public void setCompanyID(int companyID) {
        this.companyID = companyID;
    }

    /**
     * Obtiene la dirección del usuario.
     * 
     * @return La dirección del usuario.
     */
    public String getStreet() {
        return street;
    }

    /**
     * Establece la dirección del usuario.
     * 
     * @param street La nueva dirección del usuario.
     */
    public void setStreet(String street) {
        this.street = street;
    }

    /**
     * Obtiene la ciudad del usuario.
     * 
     * @return La ciudad del usuario.
     */
    public String getCity() {
        return city;
    }

    /**
     * Establece la ciudad del usuario.
     * 
     * @param city La nueva ciudad del usuario.
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * Obtiene el código postal del usuario.
     * 
     * @return El código postal del usuario.
     */
    public int getZip() {
        return zip;
    }

    /**
     * Establece el código postal del usuario.
     * 
     * @param zip El nuevo código postal del usuario.
     */
    public void setZip(int zip) {
        this.zip = zip;
    }
}

