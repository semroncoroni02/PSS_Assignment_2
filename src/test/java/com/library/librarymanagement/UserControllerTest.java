
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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

// Import corretto per @MockitoBean (Spring Framework - spring-test)
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test slice del web layer per UserController.
 * Usa MockMvc per eseguire richieste e @MockitoBean per mockare il UserRepository.
 */

@SpringBootTest(classes = LibraryManagementApplication.class)
@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

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

    @Test
    @DisplayName("POST /users - crea un utente")
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

    @Test
    @DisplayName("PUT /users/{id} - aggiorna un utente esistente")
    void updateUser() throws Exception {
        Long id = 5L;
        User existing = new User("Nome Vecchio", "old@example.com");
        User update = new User("Nome Nuovo", "new@example.com");

        Mockito.when(userRepository.findById(eq(id))).thenReturn(Optional.of(existing));
        Mockito.when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        mockMvc.perform(put("/users/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Nome Nuovo")))
                .andExpect(jsonPath("$.email", is("new@example.com")));
    }

    @Test
    @DisplayName("DELETE /users/{id} - elimina utente")
    void deleteUser() throws Exception {
        Long id = 7L;

        mockMvc.perform(delete("/users/{id}", id))
                .andExpect(status().isOk());

        Mockito.verify(userRepository).deleteById(eq(id));
    }
}
