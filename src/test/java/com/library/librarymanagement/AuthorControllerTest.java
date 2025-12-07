package com.library.librarymanagement;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.library.librarymanagement.controller.AuthorController;
import com.library.librarymanagement.entity.Author;
import com.library.librarymanagement.repository.AuthorRepository;
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
 * Unit tests for {@code AuthorController}.
 * <p>
 * These tests validate the HTTP layer in isolation by mocking interactions
 * with {@link AuthorRepository}. JSON payloads, HTTP responses and expected
 * repository calls are verified using {@link MockMvc}.
 *
 * <h2>Scope</h2>
 * <ul>
 *     <li>GET /authors — list authors</li>
 *     <li>POST /authors — create author</li>
 *     <li>PUT /authors/{id} — update author</li>
 *     <li>DELETE /authors/{id} — delete author</li>
 * </ul>
 *
 * <p>
 * Notes:
 * <ul>
 *     <li>No persistence layer is used; repository behaviors are fully mocked.</li>
 *     <li>Tests focus solely on controller behavior and HTTP contract.</li>
 * </ul>
 * </p>
 */
@WebMvcTest(AuthorController.class)
class AuthorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthorRepository authorRepository;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Retrieves a list of authors via a GET request and verifies:
     * <ul>
     *     <li>response status is HTTP 200</li>
     *     <li>array size matches the mocked repository output</li>
     *     <li>JSON structure contains expected fields</li>
     * </ul>
     *
     * @throws Exception if MockMvc request execution fails
     */
    @Test
    @DisplayName("GET /authors - ritorna lista autori")
    void getAllAuthors() throws Exception {
        List<Author> authors = Arrays.asList(
                new Author("Italo Calvino", "Italiana"),
                new Author("Haruki Murakami", "Giapponese")
        );
        Mockito.when(authorRepository.findAll()).thenReturn(authors);

        mockMvc.perform(get("/authors"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("Italo Calvino")))
                .andExpect(jsonPath("$[1].nationality", is("Giapponese")));
    }

    /**
     * Creates a new author via POST request and verifies:
     * <ul>
     *     <li>response status is HTTP 200</li>
     *     <li>returned JSON matches input payload</li>
     * </ul>
     *
     * @throws Exception if MockMvc request execution fails
     */
    @Test
    @DisplayName("POST /authors - crea un nuovo autore")
    void createAuthor() throws Exception {
        Author input = new Author("Umberto Eco", "Italiana");

        Mockito.when(authorRepository.save(any(Author.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        mockMvc.perform(post("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Umberto Eco")))
                .andExpect(jsonPath("$.nationality", is("Italiana")));
    }

    /**
     * Updates an existing author via PUT request and validates:
     * <ul>
     *     <li>repository finds the existing entity</li>
     *     <li>updated author values are returned in the response</li>
     * </ul>
     *
     * @throws Exception if MockMvc request execution fails
     */
    @Test
    @DisplayName("PUT /authors/{id} - aggiorna autore esistente")
    void updateAuthor() throws Exception {
        Long id = 1L;
        Author existing = new Author("Autore Vecchio", "Vecchia");
        Author update = new Author("Autore Nuovo", "Nuova");

        Mockito.when(authorRepository.findById(eq(id))).thenReturn(Optional.of(existing));
        Mockito.when(authorRepository.save(any(Author.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        mockMvc.perform(put("/authors/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Autore Nuovo")))
                .andExpect(jsonPath("$.nationality", is("Nuova")));
    }

    /**
     * Deletes an author via DELETE request and verifies that the repository
     * deletion method is invoked with the proper identifier.
     *
     * @throws Exception if MockMvc request execution fails
     */
    @Test
    @DisplayName("DELETE /authors/{id} - elimina autore")
    void deleteAuthor() throws Exception {
        Long id = 1L;

        mockMvc.perform(delete("/authors/{id}", id))
                .andExpect(status().isOk());

        Mockito.verify(authorRepository).deleteById(eq(id));
    }
}
