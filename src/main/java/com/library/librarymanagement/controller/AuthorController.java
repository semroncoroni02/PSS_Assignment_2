package com.library.librarymanagement.controller;

import com.library.librarymanagement.entity.Author;
import com.library.librarymanagement.repository.AuthorRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller responsible for exposing CRUD operations for {@link Author} resources.
 * <p>
 * The endpoints provided allow interaction with the author catalogue through HTTP requests
 * under the base URI <strong>/authors</strong>.
 *
 * <h2>Available endpoints</h2>
 * <ul>
 *     <li><strong>GET /authors</strong> — retrieve all authors</li>
 *     <li><strong>POST /authors</strong> — create a new author</li>
 *     <li><strong>PUT /authors/{id}</strong> — update an existing author</li>
 *     <li><strong>DELETE /authors/{id}</strong> — delete an author</li>
 * </ul>
 *
 * <p>
 * Database interaction is delegated to the {@link AuthorRepository}.
 * </p>
 *
 * @see Author
 * @see AuthorRepository
 */
@RestController
@RequestMapping("/authors")
public class AuthorController {

    private final AuthorRepository authorRepository;

    /**
     * Creates a new {@code AuthorController} with the required repository dependency.
     *
     * @param authorRepository the repository used to manage persistence of authors
     */
    public AuthorController(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    /**
     * Retrieves all authors stored in the system.
     *
     * @return a list containing all {@link Author} entities
     */
    @GetMapping
    public List<Author> getAllAuthors() {
        return authorRepository.findAll();
    }

    /**
     * Persists a new author in the system.
     *
     * @param author the {@link Author} instance sent in the request body
     * @return the saved {@link Author} entity
     */
    @PostMapping
    public Author createAuthor(@RequestBody Author author) {
        return authorRepository.save(author);
    }

    /**
     * Updates the information of an existing author.
     * <p>
     * If no author exists with the given ID, an exception is thrown and handled by Spring.
     *
     * @param id            the unique identifier of the author to update
     * @param authorDetails an {@link Author} object containing updated fields
     * @return the updated {@link Author} entity
     */
    @PutMapping("/{id}")
    public Author updateAuthor(@PathVariable Long id, @RequestBody Author authorDetails) {
        Author author = authorRepository.findById(id).orElseThrow();
        author.setName(authorDetails.getName());
        author.setNationality(authorDetails.getNationality());
        return authorRepository.save(author);
    }

    /**
     * Deletes the author with the given ID.
     * <p>
     * If no author exists with the ID, Spring automatically handles the exception.
     *
     * @param id the unique identifier of the author to delete
     */
    @DeleteMapping("/{id}")
    public void deleteAuthor(@PathVariable Long id) {
        authorRepository.deleteById(id);
    }
}
