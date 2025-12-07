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
 * Integration tests end-to-end for {@code AuthorController}.
 * <p>
 * These tests exercise the controller layer together with persistence (repository)
 * using {@link MockMvc} to simulate HTTP requests.
 *
 * <h2>Scope</h2>
 * <ul>
 *     <li>POST /authors — create an author</li>
 *     <li>GET  /authors — list authors</li>
 *     <li>PUT  /authors/{id} — update author</li>
 *     <li>DELETE /authors/{id} — delete author</li>
 * </ul>
 *
 * <p>
 * Notes:
 * <ul>
 *     <li>The repository is cleaned before each test to ensure isolation.</li>
 *     <li>Tests assert both HTTP responses and persisted state in the repository.</li>
 * </ul>
 * </p>
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

    /**
     * Clean the repository before every test to ensure tests are independent
     * and do not interfere with each other.
     */
    @BeforeEach
    void setup() {
        authorRepository.deleteAll();
    }

    /**
     * Integration test that creates an author via POST and then verifies
     * the created author is returned and is present in the listing endpoint.
     *
     * @throws Exception if MockMvc request execution fails
     */
    @Test
    @DisplayName("POST + GET /authors - crea e lista autori")
    void createAndListAuthors() throws Exception {
        Author a = new Author("Primo Levi", "Italiana");

        // Create author and assert returned JSON contains expected fields
        mockMvc.perform(post("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(a)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Primo Levi"))
                .andExpect(jsonPath("$.nationality").value("Italiana"));

        // List authors and check that the previously created author is present
        mockMvc.perform(get("/authors"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Primo Levi"));
    }

    /**
     * Integration test that updates an existing author via PUT and verifies both
     * HTTP response and persisted state in the repository.
     *
     * @throws Exception if MockMvc request execution fails
     */
    @Test
    @DisplayName("PUT /authors/{id} - aggiorna autore esistente")
    void updateAuthor() throws Exception {
        // Arrange: persist an author directly via repository
        Author saved = authorRepository.save(new Author("Autore Vecchio", "Vecchia"));
        Long id = saved.getId();

        Author update = new Author("Autore Nuovo", "Nuova");

        // Execute the HTTP PUT to update the author and verify response body
        mockMvc.perform(put("/authors/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Autore Nuovo"))
                .andExpect(jsonPath("$.nationality").value("Nuova"));

        // Verify persisted entity in the repository was updated
        Author reloaded = authorRepository.findById(id).orElseThrow();
        assertThat(reloaded.getName()).isEqualTo("Autore Nuovo");
        assertThat(reloaded.getNationality()).isEqualTo("Nuova");
    }

    /**
     * Integration test that deletes an existing author and verifies it is no longer present.
     *
     * @throws Exception if MockMvc request execution fails
     */
    @Test
    @DisplayName("DELETE /authors/{id} - elimina autore")
    void deleteAuthor() throws Exception {
        // Arrange: persist an author
        Author saved = authorRepository.save(new Author("Da Eliminare", "X"));
        Long id = saved.getId();

        // Execute delete and expect OK
        mockMvc.perform(delete("/authors/{id}", id))
                .andExpect(status().isOk());

        // Verify the repository no longer contains the entity
        assertThat(authorRepository.findById(id)).isEmpty();
    }
}
