package com.library.librarymanagement.entity;

import jakarta.persistence.*;

/**
 * Entity class representing an Author in the library system.
 * <p>
 * Each Author has:
 * - an auto-generated ID
 * - a name
 * - a nationality
 * <p>
 * This entity is stored in the database via JPA.
 */
@Entity
public class Author {

    /**
     * Unique identifier for the Author.
     * Generated automatically by the database.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Full name of the author.
     */
    private String name;

    /**
     * Country of origin of the author.
     */
    private String nationality;

    /**
     * Default constructor required by JPA.
     */
    public Author() {
    }

    /**
     * Convenience constructor used when creating
     * a new Author instance programmatically.
     *
     * @param name        full name of the author
     * @param nationality author's nationality
     */
    public Author(String name, String nationality) {
        this.name = name;
        this.nationality = nationality;
    }

    /**
     * @return unique identifier of the author
     */
    public Long getId() {
        return id;
    }

    /**
     * @return full name of the author
     */
    public String getName() {
        return name;
    }

    /**
     * Updates the author's name.
     *
     * @param name new name to assign
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return nationality of the author
     */
    public String getNationality() {
        return nationality;
    }

    /**
     * Updates the nationality of the author.
     *
     * @param nationality new nationality to assign
     */
    public void setNationality(String nationality) {
        this.nationality = nationality;
    }
}
