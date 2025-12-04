package com.library.librarymanagement.entity;

import jakarta.persistence.*;

/**
 * Entity class representing a Book in the library system.
 * <p>
 * Each book has:
 * - an auto-generated ID
 * - a title
 * - an author name (string field for simplicity)
 * - a publication year
 * <p>
 * This entity is stored in the database via JPA
 * and is used in CRUD operations across the system.
 */
@Entity
public class Book {

    /**
     * Unique identifier for the book.
     * Generated automatically by the database.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Title of the book.
     */
    private String title;

    /**
     * Name of the author.
     * Note: stored as a simple string, not a relationship for simplicity.
     */
    private String author;

    /**
     * Year in which the book was published.
     */
    private int publicationYear;

    /**
     * Default constructor required by JPA.
     */
    public Book() {
    }

    /**
     * Convenience constructor used when creating
     * a new Book instance programmatically.
     *
     * @param title  title of the book
     * @param author name of the author
     * @param year   publication year
     */
    public Book(String title, String author, int year) {
        this.title = title;
        this.author = author;
        this.publicationYear = year;
    }

    /**
     * @return unique identifier of the book
     */
    public Long getId() {
        return id;
    }

    /**
     * @return title of the book
     */
    public String getTitle() {
        return title;
    }

    /**
     * Updates the title of the book.
     *
     * @param title new title to assign
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return author name
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Updates the name of the author.
     *
     * @param author new author name to assign
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * @return publication year of the book
     */
    public int getPublicationYear() {
        return publicationYear;
    }

    /**
     * Updates the publication year of the book.
     *
     * @param publicationYear new year to assign
     */
    public void setPublicationYear(int publicationYear) {
        this.publicationYear = publicationYear;
    }
}
