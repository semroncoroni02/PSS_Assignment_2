
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
 * Integration tests end-to-end per UserController.
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

    @BeforeEach
    void setup() {
        userRepository.deleteAll();
    }

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
