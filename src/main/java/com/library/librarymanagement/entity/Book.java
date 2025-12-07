package com.library.librarymanagement.entity;

import jakarta.persistence.*;

/**
 * Entity class representing a book in the library system.
 * <p>
 * This class is mapped to a relational database table using JPA and contains
 * information relevant to a single book record.
 *
 * <h2>Attributes</h2>
 * <ul>
 *     <li><strong>id</strong> — unique identifier, auto-generated</li>
 *     <li><strong>title</strong> — title of the book</li>
 *     <li><strong>author</strong> — name of the author (stored as a string)</li>
 *     <li><strong>publicationYear</strong> — year in which the book was published</li>
 * </ul>
 *
 * <p>
 * This entity is used throughout the system in CRUD operations and is persisted
 * via Spring Data JPA in cooperation with
 * {@link com.library.librarymanagement.repository.BookRepository BookRepository}.
 * </p>
 */
@Entity
public class Book {

    /**
     * Unique identifier for the book.
     * <p>
     * The value is generated automatically using the identity strategy.
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
     * <p>
     * Note: stored as a simple string rather than as an entity relationship
     * to simplify the model.
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
     * Creates a new instance of {@code Book} with the provided attributes.
     *
     * @param title  the title of the book
     * @param author the name of the author
     * @param year   the publication year
     */
    public Book(String title, String author, int year) {
        this.title = title;
        this.author = author;
        this.publicationYear = year;
    }

    /**
     * Returns the unique identifier of the book.
     *
     * @return the book's ID
     */
    public Long getId() {
        return id;
    }

    /**
     * Returns the title of the book.
     *
     * @return the book title
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
     * Returns the name of the author.
     *
     * @return the author name
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
     * Returns the publication year.
     *
     * @return the year the book was published
     */
    public int getPublicationYear() {
        return publicationYear;
    }

    /**
     * Updates the publication year of the book.
     *
     * @param publicationYear new year value to assign
     */
    public void setPublicationYear(int publicationYear) {
        this.publicationYear = publicationYear;
    }
}
