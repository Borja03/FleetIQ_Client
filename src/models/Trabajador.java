/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

/**
 *
 * @author Omar
 */
public class Trabajador extends User {
      private String departamento;

    public Trabajador() {
    }

    public Trabajador(String email, String password, String name, boolean activo, int companyID, String street, String city, int zip) {
        super(email, password, name, activo, companyID, street, city, zip);
    }

    public Trabajador(String departamento) {
        this.departamento = departamento;
    }

    
    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    } 
}
