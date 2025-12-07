package com.library.librarymanagement.controller;

import com.library.librarymanagement.entity.Book;
import com.library.librarymanagement.repository.BookRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller exposing CRUD operations for {@link Book} resources.
 * <p>
 * Requests are mapped under the base URI <strong>/books</strong> and allow interaction with
 * the book catalogue through standard HTTP operations.
 *
 * <h2>Available endpoints</h2>
 * <ul>
 *     <li><strong>GET /books</strong> — retrieve all books</li>
 *     <li><strong>POST /books</strong> — create a new book</li>
 *     <li><strong>PUT /books/{id}</strong> — update an existing book</li>
 *     <li><strong>DELETE /books/{id}</strong> — delete a book by ID</li>
 * </ul>
 *
 * <p>
 * Persistence operations are delegated to the {@link BookRepository}, which uses Spring Data JPA
 * to communicate with the underlying database.
 * </p>
 *
 * @see Book
 * @see BookRepository
 */
@RestController
@RequestMapping("/books")
public class BookController {

    private final BookRepository bookRepository;

    /**
     * Constructs a new {@code BookController} instance.
     *
     * @param bookRepository the repository managing {@link Book} persistence
     */
    public BookController(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    /**
     * Retrieves all books currently stored in the database.
     *
     * @return a list of all {@link Book} entities
     */
    @GetMapping
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    /**
     * Creates and stores a new book.
     *
     * @param book the {@link Book} instance received in the request body
     * @return the persisted {@link Book} object
     */
    @PostMapping
    public Book createBook(@RequestBody Book book) {
        return bookRepository.save(book);
    }

    /**
     * Updates the information of an existing book identified by the given ID.
     * <p>
     * If no book exists with the provided ID, an exception is thrown by {@link java.util.Optional#orElseThrow()}.
     *
     * @param id          the unique identifier of the book to update
     * @param bookDetails a {@link Book} instance containing updated field values
     * @return the updated {@link Book} entity
     */
    @PutMapping("/{id}")
    public Book updateBook(@PathVariable Long id, @RequestBody Book bookDetails) {
        Book book = bookRepository.findById(id).orElseThrow();
        book.setTitle(bookDetails.getTitle());
        book.setAuthor(bookDetails.getAuthor());
        book.setPublicationYear(bookDetails.getPublicationYear());
        return bookRepository.save(book);
    }

    /**
     * Deletes the book matching the given ID.
     * <p>
     * If no such ID exists, Spring automatically handles the exception.
     *
     * @param id the unique identifier of the book to delete
     */
    @DeleteMapping("/{id}")
    public void deleteBook(@PathVariable Long id) {
        bookRepository.deleteById(id);
    }
}
