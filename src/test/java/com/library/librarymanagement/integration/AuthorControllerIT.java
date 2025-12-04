
package com.library.librarymanagement.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.library.librarymanagement.entity.Author;
import com.library.librarymanagement.repository.AuthorRepository;
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
 * Integration tests end-to-end per AuthorController.
 */
@SpringBootTest
@AutoConfigureMockMvc
class AuthorControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        authorRepository.deleteAll();
    }

    @Test
    @DisplayName("POST + GET /authors - crea e lista autori")
    void createAndListAuthors() throws Exception {
        Author a = new Author("Primo Levi", "Italiana");

        mockMvc.perform(post("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(a)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Primo Levi"))
                .andExpect(jsonPath("$.nationality").value("Italiana"));

        mockMvc.perform(get("/authors"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Primo Levi"));
    }

    @Test
    @DisplayName("PUT /authors/{id} - aggiorna autore esistente")
    void updateAuthor() throws Exception {
        // Arrange: persiste un autore
        Author saved = authorRepository.save(new Author("Autore Vecchio", "Vecchia"));
        Long id = saved.getId();

        Author update = new Author("Autore Nuovo", "Nuova");

        mockMvc.perform(put("/authors/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Autore Nuovo"))
                .andExpect(jsonPath("$.nationality").value("Nuova"));

        // Verifica su DB
        Author reloaded = authorRepository.findById(id).orElseThrow();
        assertThat(reloaded.getName()).isEqualTo("Autore Nuovo");
        assertThat(reloaded.getNationality()).isEqualTo("Nuova");
    }

    @Test
    @DisplayName("DELETE /authors/{id} - elimina autore")
    void deleteAuthor() throws Exception {
        Author saved = authorRepository.save(new Author("Da Eliminare", "X"));
        Long id = saved.getId();

        mockMvc.perform(delete("/authors/{id}", id))
                .andExpect(status().isOk());

        assertThat(authorRepository.findById(id)).isEmpty();
    }
}
