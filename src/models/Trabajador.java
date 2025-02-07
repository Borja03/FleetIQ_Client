package models;

import javax.xml.bind.annotation.XmlRootElement;
import models.User;

/**
 * Represents a worker in the system.
 * 
 * <p>
 * The {@code Trabajador} class extends {@link User} and adds an additional
 * field for the department in which the worker is assigned.
 * </p>
 * 
 * @author Omar
 */
@XmlRootElement
public class Trabajador extends User {

    /**
     * The department where the worker is assigned.
     */
    private String departamento;

    /**
     * Default constructor.
     */
    public Trabajador() {
    }

    /**
     * Constructs a {@code Trabajador} with the specified details.
     *
     * @param email           the worker's email
     * @param name            the worker's name
     * @param password        the worker's password
     * @param city            the city of residence
     * @param street          the street of residence
     * @param zip             the postal code
     * @param verifcationCode the verification code for the worker
     * @param activo          the active status of the worker
     * @param departamento    the department of the worker
     */
    public Trabajador(String email, String name, String password, 
                      String city, String street, Integer zip, String verifcationCode, 
                      boolean activo, String departamento) {
        super(email, name, password, city, street, zip, verifcationCode, activo);
        this.departamento = departamento;
    }

    /**
     * Returns the department of the worker.
     *
     * @return the department name
     */
    public String getDepartamento() {
        return departamento;
    }

    /**
     * Sets the department of the worker.
     *
     * @param departamento the new department name
     */
    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    /**
     * Generates a hash code for this worker.
     *
     * @return the hash code
     */
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    /**
     * Compares this worker with another object for equality.
     *
     * @param object the object to compare
     * @return {@code true} if the objects are equal, {@code false} otherwise
     */
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Trabajador)) {
            return false;
        }
        Trabajador other = (Trabajador) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    /**
     * Returns a string representation of this worker.
     *
     * @return a string containing worker details
     */
    @Override
    public String toString() {
        return "entitie.Trabajador[ id=" + id + " ]";
    }
}
