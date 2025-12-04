package com.library.librarymanagement.controller;

import com.library.librarymanagement.entity.User;
import com.library.librarymanagement.repository.UserRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller responsible for handling HTTP requests related to Users.
 * <p>
 * This controller exposes CRUD operations via RESTful endpoints under:
 * /users
 * <p>
 * Provided operations:
 * - GET    /users        -> retrieve all users
 * - POST   /users        -> create a new user
 * - PUT    /users/{id}   -> update an existing user
 * - DELETE /users/{id}   -> delete a user by ID
 * <p>
 * It relies on UserRepository to persist data through Spring Data JPA.
 */
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserRepository userRepository;

    /**
     * Constructor-based injection of the repository.
     * Spring automatically provides a concrete implementation.
     */
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Returns a list of all users stored in the database.
     *
     * @return List<User> containing all users
     */
    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Creates a new user from the request body.
     *
     * @param user the user data provided in the request
     * @return the persisted user instance
     */
    @PostMapping
    public User createUser(@RequestBody User user) {
        return userRepository.save(user);
    }

    /**
     * Updates the user identified by the provided ID.
     * If no user is found, an exception is thrown.
     *
     * @param id          ID of the user to update
     * @param userDetails user data containing updated fields
     * @return the updated user object
     */
    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User userDetails) {
        User user = userRepository.findById(id).orElseThrow();
        user.setName(userDetails.getName());
        user.setEmail(userDetails.getEmail());
        return userRepository.save(user);
    }

    /**
     * Deletes the user with the given ID from the database.
     *
     * @param id ID of the user to delete
     */
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userRepository.deleteById(id);
    }
}
