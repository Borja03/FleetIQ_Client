package models;

import java.util.Date;
import javax.xml.bind.annotation.XmlRootElement;
import models.User;



/**
 * Represents an administrator user, extending the {@link User} class.
 * This class includes additional functionality specific to administrators,
 * such as tracking their last login session.
 * 
 * <p>
 * The {@code Admin} class inherits common user attributes and behaviors from the
 * {@link User} class while adding a field to store the last session login date.
 * </p>
 * 
 * @author Omar
 */
@XmlRootElement
public class Admin extends User {

    /**
     * The date and time of the last login session of the administrator.
     */
    private Date ultimoInicioSesion;

    /**
     * Default constructor required for JAXB serialization.
     */
    public Admin() {
    }

    /**
     * Constructs an {@code Admin} object with the specified attributes.
     *
     * @param email            the email of the administrator
     * @param name             the name of the administrator
     * @param password         the password of the administrator
     * @param city             the city of the administrator
     * @param street           the street address of the administrator
     * @param zip              the ZIP code of the administrator
     * @param verifcationCode  the verification code of the administrator
     * @param activo           the active status of the administrator
     * @param ultimoInicioSesion the last login session date of the administrator
     */
    public Admin(String email, String name, String password, String city,
                 String street, Integer zip, String verifcationCode,
                 boolean activo, Date ultimoInicioSesion) {
        super(email, name, password, city, street, zip, verifcationCode, activo);
        this.ultimoInicioSesion = ultimoInicioSesion;
    }

    /**
     * Returns the last login session date of the administrator.
     * 
     * @return the date of the last login session
     */
    public Date getUltimoInicioSesion() {
        return ultimoInicioSesion;
    }

    /**
     * Sets the last login session date of the administrator.
     * 
     * @param ultimoInicioSesion the new last login session date
     */
    public void setUltimoInicioSesion(Date ultimoInicioSesion) {
        this.ultimoInicioSesion = ultimoInicioSesion;
    }

    /**
     * Computes a hash code for this administrator based on its attributes.
     * 
     * @return the hash code value
     */
    @Override
    public int hashCode() {
        int hash = 0;
        return hash;
    }

    /**
     * Compares this {@code Admin} instance with another object for equality.
     *
     * @param object the object to compare with
     * @return {@code true} if the objects are equal, {@code false} otherwise
     */
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Admin)) {
            return false;
        }
        Admin other = (Admin) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    /**
     * Returns a string representation of this {@code Admin} object.
     *
     * @return a string containing the administrator's details
     */
    @Override
    public String toString() {
        return super.toString() + "Admin{" + "ultimoInicioSesion=" + ultimoInicioSesion + '}';
    }
}
