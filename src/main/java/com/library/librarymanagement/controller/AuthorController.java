package com.library.librarymanagement.controller;

import com.library.librarymanagement.entity.Author;
import com.library.librarymanagement.repository.AuthorRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller responsible for handling HTTP requests related to Authors.
 * <p>
 * This controller exposes CRUD operations via RESTful endpoints under:
 * /authors
 * <p>
 * The operations provided are:
 * - GET    /authors        -> list all authors
 * - POST   /authors        -> create a new author
 * - PUT    /authors/{id}   -> update an existing author
 * - DELETE /authors/{id}   -> delete an author
 * <p>
 * Communication with the database is achieved through the AuthorRepository.
 */
@RestController
@RequestMapping("/authors")
public class AuthorController {

    private final AuthorRepository authorRepository;

    /**
     * Constructor-based dependency injection of the AuthorRepository.
     * Spring automatically provides a concrete instance at runtime.
     */
    public AuthorController(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    /**
     * Returns a list of all authors stored in the system.
     *
     * @return List<Author> - full collection of authors
     */
    @GetMapping
    public List<Author> getAllAuthors() {
        return authorRepository.findAll();
    }

    /**
     * Creates and persists a new author based on the request body.
     *
     * @param author Author object received from the request
     * @return the saved Author instance
     */
    @PostMapping
    public Author createAuthor(@RequestBody Author author) {
        return authorRepository.save(author);
    }

    /**
     * Updates an existing author by ID.
     * If the ID does not exist, an exception is thrown (handled by Spring).
     *
     * @param id            author identifier
     * @param authorDetails object containing updated values
     * @return the updated Author entity
     */
    @PutMapping("/{id}")
    public Author updateAuthor(@PathVariable Long id, @RequestBody Author authorDetails) {
        Author author = authorRepository.findById(id).orElseThrow();
        author.setName(authorDetails.getName());
        author.setNationality(authorDetails.getNationality());
        return authorRepository.save(author);
    }

    /**
     * Deletes an author by ID.
     *
     * @param id author identifier
     */
    @DeleteMapping("/{id}")
    public void deleteAuthor(@PathVariable Long id) {
        authorRepository.deleteById(id);
    }
}
