/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.util.Date;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Omar
 */
@XmlRootElement
public class Admin extends User {

    //private static final long serialVersionUID = 1L;
    private Date ultimoInicioSesion;

    public Admin() {
    }

    // Modified constructor to match User's constructor pattern
    public Admin(String email, String name, String password, String city,
                    String street, Integer zip, String verifcationCode,
                    boolean activo, Date ultimoInicioSesion) {
        super(email, name, password, city, street, zip, verifcationCode, activo);
        this.ultimoInicioSesion = ultimoInicioSesion;
    }

    public Date getUltimoInicioSesion() {
        return ultimoInicioSesion;
    }

    public void setUltimoInicioSesion(Date ultimoInicioSesion) {
        this.ultimoInicioSesion = ultimoInicioSesion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        //  hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Admin)) {
            return false;
        }
        Admin other = (Admin) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return   super.toString() + "Admin{" + "ultimoInicioSesion=" + ultimoInicioSesion + '}';
    }


}
