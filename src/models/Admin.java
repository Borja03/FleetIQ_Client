/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.util.Date;

/**
 *
 * @author Omar
 */
public class Admin extends User{
    
 private Date ultimo_inicio_sesion; 

 
 
    public Admin() {
    }

    public Admin(String email, String password, String name, boolean activo, int companyID, String street, String city, int zip) {
        super(email, password, name, activo, companyID, street, city, zip);
    }

    public Admin(Date ultimo_inicio_sesion) {
        this.ultimo_inicio_sesion = ultimo_inicio_sesion;
    }

 
 
 
    public Date getUltimo_inicio_sesion() {
        return ultimo_inicio_sesion;
    }

    public void setUltimo_inicio_sesion(Date ultimo_inicio_sesion) {
        this.ultimo_inicio_sesion = ultimo_inicio_sesion;
    }
 
 
    
    
}
