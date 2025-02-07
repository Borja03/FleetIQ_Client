package models;

import javax.xml.bind.annotation.XmlEnum;

/**
 * Enumeration representing different package sizes.
 * 
 * <p>
 * This enum defines standard package size categories: {@code SMALL}, {@code MEDIUM}, 
 * {@code LARGE}, and {@code EXTRA_LARGE}. It can be used to classify packages based on 
 * their dimensions or weight.
 * </p>
 * 
 * @author Omar
 */
@XmlEnum
public enum PackageSize {
    /**
     * Represents a small-sized package.
     */
    SMALL,
    
    /**
     * Represents a medium-sized package.
     */
    MEDIUM,
    
    /**
     * Represents a large-sized package.
     */
    LARGE,
    
    /**
     * Represents an extra-large-sized package.
     */
    EXTRA_LARGE
}
