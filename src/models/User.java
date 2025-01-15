package models;


import java.io.Serializable;
import java.util.List;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Omar
 */

@XmlRootElement
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    protected Long id;
    private String email;
    private String name;
    private String password;
    private String country;
    private String city;
    private String street;
    private Integer zip;
    private String verifcationCode;
    private boolean activo;
    private List<Envio> enviosList;
    
    public User() {
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public User(String email, String name, String password, String country, String city, String street, Integer zip, String verifcationCode, boolean activo, List<Envio> enviosList) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.country = country;
        this.city = city;
        this.street = street;
        this.zip = zip;
        this.verifcationCode = verifcationCode;
        this.activo = activo;
        this.enviosList = enviosList;
    }

    public User(String email, String password, String name, boolean active, int companyID, String street, String city, int zip) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.country = country;
        this.city = city;
        this.street = street;
        this.zip = zip;
        this.activo = activo;
    }

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

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
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

    //
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "User{" + "id=" + id + ", email=" + email + ", name=" + name + ", password=" + password + ", country=" + country + ", city=" + city + ", street=" + street + ", zip=" + zip + ", verifcationCode=" + verifcationCode + ", activo=" + activo + ", enviosList=" + enviosList + '}';
    }

  

}
