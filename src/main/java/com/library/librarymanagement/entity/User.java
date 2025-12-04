package com.library.librarymanagement.entity;

import jakarta.persistence.*;

/**
 * Entity class representing a User in the library system.
 * <p>
 * Each user has:
 * - an auto-generated ID
 * - a name
 * - an email address
 * <p>
 * This entity maps to the "users" table (explicitly renamed
 * to avoid conflicts with the reserved keyword "user").
 */
@Entity
@Table(name = "users") // Prevents naming conflict with SQL reserved keyword "user"
public class User {

    /**
     * Unique identifier for the user.
     * Generated automatically by the database.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Name of the user.
     */
    private String name;

    /**
     * Email address of the user.
     */
    private String email;

    /**
     * Default constructor required by JPA.
     */
    public User() {
    }

    /**
     * Convenience constructor used when creating
     * a new User instance programmatically.
     *
     * @param name  name of the user
     * @param email email of the user
     */
    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }

    /**
     * @return unique identifier of the user
     */
    public Long getId() {
        return id;
    }

    /**
     * @return name of the user
     */
    public String getName() {
        return name;
    }

    /**
     * Updates the name of the user.
     *
     * @param name new name to assign
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return email of the user
     */
    public String getEmail() {
        return email;
    }

    /**
     * Updates the email of the user.
     *
     * @param email new email to assign
     */
    public void setEmail(String email) {
        this.email = email;
    }
}
