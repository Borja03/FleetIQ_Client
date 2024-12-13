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
import java.time.LocalDate;
import java.util.Date;

public class Paquete {
    private Integer id;
    private String sender;
    private String receiver;
    private double weight;
    private PaqueteSize size;
    private LocalDate creationDate;
    private boolean fragile;

    
    public Paquete(Integer id, String sender, String receiver, double weight, 
                   PaqueteSize size, LocalDate creationDate, boolean fragile) {
        this.id = id;
        this.sender = sender;
        this.receiver = receiver;
        this.weight = weight;
        this.size = size;
        this.creationDate = creationDate;
        this.fragile = fragile;
    }

    
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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


    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public boolean isFragile() {
        return fragile;
    }

    public void setFragile(boolean fragile) {
        this.fragile = fragile;
    }

    public PaqueteSize getSize() { 
        return size;
    }
}

