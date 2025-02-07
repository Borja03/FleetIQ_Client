package models;

import java.io.Serializable;
import java.util.Date;
import javax.xml.bind.annotation.XmlRootElement;
import models.Envio;
import models.PackageSize;

/**
 * Represents a package in a shipping system.
 * 
 * <p>
 * The {@code Paquete} class contains details about a package, including its sender,
 * receiver, weight, size, creation date, fragility status, and associated shipping details.
 * This class implements {@link Serializable} to allow object serialization and {@link Cloneable}
 * to enable object cloning.
 * </p>
 * 
 * @author Omar
 */
@XmlRootElement
public class Paquete implements Serializable, Cloneable {

    /**
     * Unique identifier for the package.
     */
    private Long id;

    /**
     * The sender's name.
     */
    private String sender;

    /**
     * The receiver's name.
     */
    private String receiver;

    /**
     * The weight of the package in kilograms.
     */
    private double weight;

    /**
     * The size category of the package.
     */
    private PackageSize size;

    /**
     * The date when the package was created.
     */
    private Date creationDate;

    /**
     * Indicates whether the package is fragile.
     */
    private boolean fragile;

    /**
     * The associated shipping details for the package.
     */
    private Envio envio;

    /**
     * Default constructor.
     */
    public Paquete() {
    }

    /**
     * Constructs a {@code Paquete} with all attributes, except shipping details.
     *
     * @param id           the unique identifier of the package
     * @param sender       the sender's name
     * @param receiver     the receiver's name
     * @param weight       the weight of the package
     * @param size         the size category of the package
     * @param creationDate the date when the package was created
     * @param fragile      indicates whether the package is fragile
     */
    public Paquete(Long id, String sender, String receiver, double weight,
                   PackageSize size, Date creationDate, boolean fragile) {
        this.id = id;
        this.sender = sender;
        this.receiver = receiver;
        this.weight = weight;
        this.size = size;
        this.creationDate = creationDate;
        this.fragile = fragile;
    }

    /**
     * Constructs a {@code Paquete} without an ID and shipping details.
     *
     * @param sender       the sender's name
     * @param receiver     the receiver's name
     * @param weight       the weight of the package
     * @param packageSize  the size category of the package
     * @param creationDate the date when the package was created
     * @param fragile      indicates whether the package is fragile
     */
    public Paquete(String sender, String receiver, double weight, PackageSize packageSize, Date creationDate, boolean fragile) {
        this.sender = sender;
        this.receiver = receiver;
        this.weight = weight;
        this.size = packageSize;
        this.creationDate = creationDate;
        this.fragile = fragile;
    }

    /**
     * Returns the unique identifier of the package.
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the unique identifier of the package.
     */
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

    public void setSize(PackageSize size) {
        this.size = size;
    }

    public Envio getEnvio() {
        return envio;
    }

    public void setEnvio(Envio envio) {
        this.envio = envio;
    }

    /**
     * Creates and returns a copy of this package.
     *
     * @return a clone of this instance
     * @throws CloneNotSupportedException if cloning is not supported
     */
    @Override
    public Paquete clone() throws CloneNotSupportedException {
        return (Paquete) super.clone();
    }

    /**
     * Returns a string representation of this package.
     *
     * @return a string containing package details
     */
    @Override
    public String toString() {
        return "Paquete{" + "id=" + id + ", sender=" + sender + ", receiver=" + receiver + 
               ", weight=" + weight + ", size=" + size + ", creationDate=" + creationDate + 
               ", fragile=" + fragile + '}';
    }
}
