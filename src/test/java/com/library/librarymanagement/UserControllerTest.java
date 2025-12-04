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
 * Unit test for UserController.
 * <p>
 * These tests mock the repository layer to isolate the web layer behavior.
 * Each test verifies HTTP responses, JSON payloads and expected interaction
 * with the repository.
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
     * Retrieves a list of users via GET and verifies JSON structure and content.
     */
    @Test
    @DisplayName("GET /users - returns all users")
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
     * Creates a new user via POST and validates returned JSON fields.
     */
    @Test
    @DisplayName("POST /users - creates a new user")
    void createUser() throws Exception {
        User input = new User("Giulia Verdi", "giulia.verdi@example.com");
        Mockito.when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Giulia Verdi")))
                .andExpect(jsonPath("$.email", is("giulia.verdi@example.com")));
    }

    /**
     * Updates an existing user and checks both HTTP result and updated values.
     */
    @Test
    @DisplayName("PUT /users/{id} - updates existing user")
    void updateUser() throws Exception {
        Long id = 5L;
        User existing = new User("Old Name", "old@example.com");
        User update = new User("New Name", "new@example.com");

        Mockito.when(userRepository.findById(eq(id))).thenReturn(Optional.of(existing));
        Mockito.when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        mockMvc.perform(put("/users/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("New Name")))
                .andExpect(jsonPath("$.email", is("new@example.com")));
    }

    /**
     * Deletes a user via DELETE and verifies the repository interaction.
     */
    @Test
    @DisplayName("DELETE /users/{id} - deletes user")
    void deleteUser() throws Exception {
        Long id = 7L;

        mockMvc.perform(delete("/users/{id}", id))
                .andExpect(status().isOk());

        Mockito.verify(userRepository).deleteById(eq(id));
    }
}
