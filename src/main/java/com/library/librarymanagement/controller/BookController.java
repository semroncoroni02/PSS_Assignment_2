package com.library.librarymanagement.controller;

import com.library.librarymanagement.entity.Book;
import com.library.librarymanagement.repository.BookRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller responsible for handling HTTP requests related to Books.
 * <p>
 * This controller exposes CRUD operations via RESTful endpoints under:
 * /books
 * <p>
 * Provided operations:
 * - GET    /books        -> retrieve all books
 * - POST   /books        -> create a new book
 * - PUT    /books/{id}   -> update an existing book
 * - DELETE /books/{id}   -> delete a book by ID
 * <p>
 * The controller delegates persistence operations to the BookRepository,
 * which interacts with the database using Spring Data JPA.
 */
@RestController
@RequestMapping("/books")
public class BookController {

    private final BookRepository bookRepository;

    /**
     * Constructor-based dependency injection.
     * Spring will provide a BookRepository implementation at runtime.
     */
    public BookController(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    /**
     * Retrieves all books from the database.
     *
     * @return List<Book> containing all stored books.
     */
    @GetMapping
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    /**
     * Creates and persists a new book based on the request body.
     *
     * @param book the Book object received in the request.
     * @return the newly created Book instance.
     */
    @PostMapping
    public Book createBook(@RequestBody Book book) {
        return bookRepository.save(book);
    }

    /**
     * Updates an existing book.
     * Throws an exception if the provided ID is not found.
     *
     * @param id          identifier of the book to update
     * @param bookDetails object containing the updated fields
     * @return the updated Book instance
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
     * Deletes a book by its ID.
     *
     * @param id identifier of the book to delete
     */
    @DeleteMapping("/{id}")
    public void deleteBook(@PathVariable Long id) {
        bookRepository.deleteById(id);
    }
}
