package com.library.librarymanagement;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.library.librarymanagement.controller.UserController;
import com.library.librarymanagement.entity.User;
import com.library.librarymanagement.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit tests for {@code UserController}.
 * <p>
 * These tests validate controller behavior by mocking interactions
 * with the {@link UserRepository}. They assert JSON payloads,
 * HTTP responses and repository usage via {@link MockMvc}.
 *
 * <h2>Scope</h2>
 * <ul>
 *     <li>GET /users — list users</li>
 *     <li>POST /users — create a user</li>
 *     <li>PUT /users/{id} — update a user</li>
 *     <li>DELETE /users/{id} — delete a user</li>
 * </ul>
 *
 * <p>
 * Notes:
 * <ul>
 *     <li>No persistence layer is used — repository calls are mocked.</li>
 *     <li>Tests focus exclusively on the HTTP layer contract.</li>
 * </ul>
 * </p>
 */
@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Retrieves all users via GET request and verifies:
     * <ul>
     *     <li>HTTP status is OK</li>
     *     <li>returned array size matches mocked data</li>
     *     <li>JSON contains expected attributes</li>
     * </ul>
     *
     * @throws Exception if MockMvc execution fails
     */
    @Test
    @DisplayName("GET /users - restituisce tutti gli utenti")
    void getAllUsers() throws Exception {
        List<User> users = Arrays.asList(
                new User("Mario Rossi", "mario.rossi@example.com"),
                new User("Laura Bianchi", "laura.bianchi@example.com")
        );
        Mockito.when(userRepository.findAll()).thenReturn(users);

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("Mario Rossi")))
                .andExpect(jsonPath("$[1].email", is("laura.bianchi@example.com")));
    }

    /**
     * Creates a new user via POST request and verifies:
     * <ul>
     *     <li>HTTP status is OK</li>
     *     <li>returned JSON contains expected fields</li>
     * </ul>
     *
     * @throws Exception if MockMvc execution fails
     */
    @Test
    @DisplayName("POST /users - crea un nuovo utente")
    void createUser() throws Exception {
        User input = new User("Giulia Verdi", "giulia.verdi@example.com");
        Mockito.when(userRepository.save(any(User.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Giulia Verdi")))
                .andExpect(jsonPath("$.email", is("giulia.verdi@example.com")));
    }

    /**
     * Updates an existing user via PUT request and verifies:
     * <ul>
     *     <li>repository lookup returns the entity</li>
     *     <li>returned JSON reflects updated values</li>
     * </ul>
     *
     * @throws Exception if MockMvc execution fails
     */
    @Test
    @DisplayName("PUT /users/{id} - aggiorna utente esistente")
    void updateUser() throws Exception {
        Long id = 5L;
        User existing = new User("Old Name", "old@example.com");
        User update = new User("New Name", "new@example.com");

        Mockito.when(userRepository.findById(eq(id))).thenReturn(Optional.of(existing));
        Mockito.when(userRepository.save(any(User.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        mockMvc.perform(put("/users/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("New Name")))
                .andExpect(jsonPath("$.email", is("new@example.com")));
    }

    /**
     * Deletes a user via DELETE request and verifies:
     * <ul>
     *     <li>HTTP status is OK</li>
     *     <li>repository deletion is invoked with provided ID</li>
     * </ul>
     *
     * @throws Exception if MockMvc execution fails
     */
    @Test
    @DisplayName("DELETE /users/{id} - elimina utente")
    void deleteUser() throws Exception {
        Long id = 7L;

        mockMvc.perform(delete("/users/{id}", id))
                .andExpect(status().isOk());

        Mockito.verify(userRepository).deleteById(eq(id));
    }
}
