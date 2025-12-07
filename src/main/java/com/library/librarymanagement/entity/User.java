package com.library.librarymanagement.entity;

import jakarta.persistence.*;

/**
 * Entity class representing a user in the library system.
 * <p>
 * This class is mapped to the {@code users} database table via JPA.
 * The table name is explicitly defined to avoid conflicts with the
 * SQL reserved keyword {@code user}.
 *
 * <h2>Attributes</h2>
 * <ul>
 *     <li><strong>id</strong> — unique identifier, auto-generated</li>
 *     <li><strong>name</strong> — user full name</li>
 *     <li><strong>email</strong> — user email address</li>
 * </ul>
 *
 * <p>
 * The {@code User} entity is used in CRUD operations across the system
 * and persisted through
 * {@link com.library.librarymanagement.repository.UserRepository UserRepository}.
 * </p>
 */
@Entity
@Table(name = "users") // Prevents naming conflict with SQL reserved keyword "user"
public class User {

    /**
     * Unique identifier for the user.
     * <p>
     * The value is generated automatically using the identity strategy.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Full name of the user.
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
     * Creates a new instance of {@code User} with the provided attributes.
     *
     * @param name  the user's full name
     * @param email the user's email address
     */
    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }

    /**
     * Returns the unique identifier of the user.
     *
     * @return the user's ID
     */
    public Long getId() {
        return id;
    }

    /**
     * Returns the full name of the user.
     *
     * @return the user's name
     */
    public String getName() {
        return name;
    }

    /**
     * Updates the full name of the user.
     *
     * @param name new name to assign
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the email address of the user.
     *
     * @return the user's email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Updates the email address of the user.
     *
     * @param email new email to assign
     */
    public void setEmail(String email) {
        this.email = email;
    }
}
