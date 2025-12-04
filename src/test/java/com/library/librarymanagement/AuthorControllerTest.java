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
 * Unit test for AuthorController.
 * <p>
 * These tests mock the repository layer to isolate the web layer behavior.
 * Each test verifies HTTP responses, JSON payloads and expected interaction
 * with the repository.
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
     * Retrieves a list of authors via GET and verifies JSON structure and content.
     */
    @Test
    @DisplayName("GET /authors - returns all authors")
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
     * Creates a new author via POST and validates returned JSON fields.
     */
    @Test
    @DisplayName("POST /authors - creates a new author")
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
     * Updates an existing author and checks both HTTP result and updated values.
     */
    @Test
    @DisplayName("PUT /authors/{id} - updates existing author")
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
     * Deletes an author via DELETE and verifies the repository interaction.
     */
    @Test
    @DisplayName("DELETE /authors/{id} - deletes author")
    void deleteAuthor() throws Exception {
        Long id = 1L;

        mockMvc.perform(delete("/authors/{id}", id))
                .andExpect(status().isOk());

        Mockito.verify(authorRepository).deleteById(eq(id));
    }
}
