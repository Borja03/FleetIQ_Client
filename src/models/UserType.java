package models;

/**
 * Represents the types of users in the system. This enumeration defines the
 * possible roles that a user can have.
 *
 * @author Omar
 * @version 1.0
 * @see User
 */
public enum UserType {
    /**
     * Represents an administrator user with full system privileges.
     */
    ADMIN,
    /**
     * Represents a worker/employee user with standard operational privileges.
     * "Trabajador" is Spanish for "worker".
     */
    TRABAJADOR
}
