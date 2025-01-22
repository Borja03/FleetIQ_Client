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
import java.io.Serializable;
import java.util.Date;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Paquete  implements Serializable {
    private Long id;
    private String sender;
    private String receiver;
    private double weight;
    @XmlElement
    private PackageSize size;
    private Date creationDate;
    private boolean fragile;
    public Paquete() {
    }

    
    public Paquete(Long id, String sender, String receiver, double weight, PackageSize size, Date creationDate, boolean fragile) {
        this.id = id;
        this.sender = sender;
        this.receiver = receiver;
        this.weight = weight;
        this.size = size;
        this.creationDate = creationDate;
        this.fragile = fragile;
    }

    
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }


    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public boolean isFragile() {
        return fragile;
    }

    public void setFragile(boolean fragile) {
        this.fragile = fragile;
    }

    public PackageSize getSize() { 
        return size;
    }

    @Override
    public String toString() {
        return "Paquete{" + "id=" + id + ", sender=" + sender + ", receiver=" + receiver + ", weight=" + weight + ", size=" + size + ", creationDate=" + creationDate + ", fragile=" + fragile + '}';
    }
    
    
    
}

