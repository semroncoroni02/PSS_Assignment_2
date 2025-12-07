package com.library.librarymanagement.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.library.librarymanagement.entity.User;
import com.library.librarymanagement.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests end-to-end for {@code UserController}.
 * <p>
 * These tests validate REST endpoints and persistence interactions involving
 * {@link User} entities, using {@link MockMvc} to simulate HTTP requests.
 *
 * <h2>Scope</h2>
 * <ul>
 *     <li>POST /users — create a user</li>
 *     <li>GET  /users — list users</li>
 *     <li>PUT  /users/{id} — update user</li>
 *     <li>DELETE /users/{id} — delete user</li>
 * </ul>
 *
 * <p>
 * Notes:
 * <ul>
 *     <li>The repository is cleaned before each test to ensure isolation.</li>
 *     <li>Both HTTP responses and persisted state are asserted.</li>
 * </ul>
 * </p>
 */
@SpringBootTest
@AutoConfigureMockMvc
class UserControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Removes all users before each test execution to guarantee that
     * tests do not interfere with each other.
     */
    @BeforeEach
    void setup() {
        userRepository.deleteAll();
    }

    /**
     * Creates a user via POST request and verifies:
     * <ul>
     *     <li>the returned JSON contains expected fields</li>
     *     <li>the created user is present in the listing</li>
     * </ul>
     *
     * @throws Exception if MockMvc request execution fails
     */
    @Test
    @DisplayName("POST + GET /users - crea e lista utenti")
    void createAndListUsers() throws Exception {
        User u = new User("Mario Rossi", "mario.rossi@example.com");

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(u)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Mario Rossi"))
                .andExpect(jsonPath("$.email").value("mario.rossi@example.com"));

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].email").value("mario.rossi@example.com"));
    }

    /**
     * Updates an existing user via PUT request and verifies:
     * <ul>
     *     <li>the HTTP response contains updated attributes</li>
     *     <li>the persisted state matches the expected values</li>
     * </ul>
     *
     * @throws Exception if MockMvc request execution fails
     */
    @Test
    @DisplayName("PUT /users/{id} - aggiorna utente esistente")
    void updateUser() throws Exception {
        User saved = userRepository.save(new User("Nome Vecchio", "old@example.com"));
        Long id = saved.getId();

        User update = new User("Nome Nuovo", "new@example.com");

        mockMvc.perform(put("/users/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Nome Nuovo"))
                .andExpect(jsonPath("$.email").value("new@example.com"));

        User reloaded = userRepository.findById(id).orElseThrow();
        assertThat(reloaded.getName()).isEqualTo("Nome Nuovo");
        assertThat(reloaded.getEmail()).isEqualTo("new@example.com");
    }

    /**
     * Deletes a user via DELETE request and verifies that
     * the repository no longer contains the entity.
     *
     * @throws Exception if MockMvc request execution fails
     */
    @Test
    @DisplayName("DELETE /users/{id} - elimina utente")
    void deleteUser() throws Exception {
        User saved = userRepository.save(new User("Da Eliminare", "del@example.com"));
        Long id = saved.getId();

        mockMvc.perform(delete("/users/{id}", id))
                .andExpect(status().isOk());

        assertThat(userRepository.findById(id)).isEmpty();
    }
}
