/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Omar
 */

@XmlRootElement
public class Trabajador extends User{

//    private static final long serialVersionUID = 1L;

    private String departamento;

    public Trabajador() {
    }

    public Trabajador(String email, String name, String password, 
                    String city, String street, Integer zip, String verifcationCode, boolean activo,String departamento) {
        super(email, name, password, city, street, zip, verifcationCode, activo);
        this.departamento = departamento;
    }


    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Trabajador)) {
            return false;
        }
        Trabajador other = (Trabajador) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
          return "entitie.Trabajador[ id=" + id + " ]";

    }

}