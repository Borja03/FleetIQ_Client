package models;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlTransient;

/**
 * Represents a user entity in the system with basic personal and authentication information.
 * This class serves as a base class for specialized user types like Admin and Trabajador.
 * Implements Serializable for object persistence and provides XML binding support through JAXB annotations.
 *
 * @author Omar
 * @see Admin
 * @see Trabajador
 * @see Serializable
 */
@XmlSeeAlso({Admin.class, Trabajador.class}) 
@XmlRootElement
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Unique identifier for the user.
     */
    protected Long id;
    
    /**
     * User's email address. Should be unique in the system.
     */
    private String email;
    
    /**
     * User's full name.
     */
    private String name;
    
    /**
     * User's password for authentication.
     */
    private String password;
    
    /**
     * User's city of residence.
     */
    private String city;
    
    /**
     * User's street address.
     */
    private String street;
    
    /**
     * User's ZIP/postal code.
     */
    private Integer zip;
    
    /**
     * Verification code used for email verification or password reset.
     */
    private String verifcationCode;
    
    /**
     * Flag indicating whether the user account is active.
     */
    private boolean activo;

    /**
     * Type of user in the system. This field is used instead of an enum.
     */
    private String user_type;

    /**
     * Default constructor required for JPA and JAXB.
     */
    public User() {
    }

    /**
     * Constructs a fully initialized User with all fields.
     *
     * @param id Unique identifier
     * @param email User's email address
     * @param name User's full name
     * @param password User's password
     * @param city User's city
     * @param street User's street address
     * @param zip User's ZIP code
     * @param verifcationCode Verification code
     * @param activo Account active status
     * @param user_type Type of user
     * @param enviosList List of associated shipments
     */
    public User(Long id, String email, String name, String password, String city, String street, Integer zip, String verifcationCode, boolean activo, String user_type, List<Envio> enviosList) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.password = password;
        this.city = city;
        this.street = street;
        this.zip = zip;
        this.verifcationCode = verifcationCode;
        this.activo = activo;
        this.user_type = user_type;
        this.enviosList = enviosList;
    }

    /**
     * Constructs a User with basic information and verification details.
     *
     * @param email User's email address
     * @param name User's full name
     * @param password User's password
     * @param city User's city
     * @param street User's street address
     * @param zip User's ZIP code
     * @param verifcationCode Verification code
     * @param activo Account active status
     */
    public User(String email, String name, String password, String city, String street, Integer zip, String verifcationCode, boolean activo) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.city = city;
        this.street = street;
        this.zip = zip;
        this.verifcationCode = verifcationCode;
        this.activo = activo;
    }
    
    /**
     * Constructs a User with basic information without verification details.
     *
     * @param email User's email address
     * @param name User's full name
     * @param password User's password
     * @param city User's city
     * @param street User's street address
     * @param zip User's ZIP code
     */
    public User(String email, String name, String password, String city, String street, Integer zip) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.city = city;
        this.street = street;
        this.zip = zip;
        this.verifcationCode = verifcationCode;
        this.user_type = user_type;
    }

    /**
     * List of shipments associated with this user.
     */
    private List<Envio> enviosList;

    /**
     * Gets the list of shipments associated with this user.
     * This method is annotated with @XmlTransient to prevent XML serialization.
     *
     * @return List of Envio objects associated with the user
     */
    @XmlTransient
    public List<Envio> getEnviosList() {
        return enviosList;
    }

    public void setEnviosList(List<Envio> enviosList) {
        this.enviosList = enviosList;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public Integer getZip() {
        return zip;
    }

    public void setZip(Integer zip) {
        this.zip = zip;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public String getVerifcationCode() {
        return verifcationCode;
    }

    public void setVerifcationCode(String verifcationCode) {
        this.verifcationCode = verifcationCode;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    /**
     * Generates a hash code for the User object based on its ID.
     *
     * @return Hash code value for the object
     */
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    /**
     * Returns a string representation of the User object, including all fields except password.
     *
     * @return String containing all User field values
     */
    @Override
    public String toString() {
        return "User{" + "id=" + id + ", email=" + email + ", name=" + name + ", password=" + password + ", city=" + city + ", street=" + street + ", zip=" + zip + ", verifcationCode=" + verifcationCode + ", activo=" + activo + ", enviosList=" + enviosList + '}';
    }
}