package com.library.librarymanagement.controller;

import com.library.librarymanagement.entity.User;
import com.library.librarymanagement.repository.UserRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller exposing CRUD operations for {@link User} entities.
 * <p>
 * All requests are mapped under the base URI <strong>/users</strong>. This controller enables
 * clients to retrieve, create, update, and delete user records.
 *
 * <h2>Available endpoints</h2>
 * <ul>
 *     <li><strong>GET /users</strong> — retrieve all users</li>
 *     <li><strong>POST /users</strong> — create a new user</li>
 *     <li><strong>PUT /users/{id}</strong> — update an existing user</li>
 *     <li><strong>DELETE /users/{id}</strong> — delete a user by ID</li>
 * </ul>
 *
 * <p>
 * The persistence layer is delegated to {@link UserRepository}, which uses Spring Data JPA for
 * database interaction.
 * </p>
 *
 * @see User
 * @see UserRepository
 */
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserRepository userRepository;

    /**
     * Constructs a new {@code UserController}.
     *
     * @param userRepository the repository managing {@link User} persistence
     */
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Retrieves all users currently stored in the system.
     *
     * @return a list of all {@link User} entities
     */
    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Creates a new user and persists it.
     *
     * @param user the {@link User} instance received in the request body
     * @return the persisted {@link User} object
     */
    @PostMapping
    public User createUser(@RequestBody User user) {
        return userRepository.save(user);
    }

    /**
     * Updates the user identified by the provided ID.
     * <p>
     * If no user exists with the given ID, an exception is thrown by
     * {@link java.util.Optional#orElseThrow()}.
     *
     * @param id          the identifier of the user to update
     * @param userDetails an instance containing updated field values
     * @return the updated {@link User} entity
     */
    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User userDetails) {
        User user = userRepository.findById(id).orElseThrow();
        user.setName(userDetails.getName());
        user.setEmail(userDetails.getEmail());
        return userRepository.save(user);
    }

    /**
     * Deletes the user matching the provided ID.
     * <p>
     * If no such user exists, Spring automatically handles the exception.
     *
     * @param id the identifier of the user to delete
     */
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userRepository.deleteById(id);
    }
}
