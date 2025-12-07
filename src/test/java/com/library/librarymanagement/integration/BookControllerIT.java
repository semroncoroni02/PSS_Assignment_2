package com.library.librarymanagement.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.library.librarymanagement.entity.Book;
import com.library.librarymanagement.repository.BookRepository;
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
 * Integration tests end-to-end for {@code BookController}.
 * <p>
 * These tests validate REST endpoints and persistence interactions involving
 * {@link Book} entities, using {@link MockMvc} to simulate HTTP requests.
 *
 * <h2>Scope</h2>
 * <ul>
 *     <li>POST /books — create a book</li>
 *     <li>GET  /books — list books</li>
 *     <li>PUT  /books/{id} — update book</li>
 *     <li>DELETE /books/{id} — delete book</li>
 * </ul>
 *
 * <p>
 * Notes:
 * <ul>
 *     <li>The repository is reset before every test to guarantee isolation.</li>
 *     <li>Both HTTP responses and persisted state are verified.</li>
 * </ul>
 * </p>
 */
@SpringBootTest
@AutoConfigureMockMvc
class BookControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Ensure a clean repository before every test case to avoid interference
     * between test executions.
     */
    @BeforeEach
    void setup() {
        bookRepository.deleteAll();
    }

    /**
     * Creates a book via POST request and verifies:
     * <ul>
     *     <li>the returned JSON contains expected fields</li>
     *     <li>the created book is present in the listing</li>
     * </ul>
     *
     * @throws Exception if MockMvc request execution fails
     */
    @Test
    @DisplayName("POST + GET /books - crea e lista libri")
    void createAndListBooks() throws Exception {
        Book b = new Book("Il nome della rosa", "Umberto Eco", 1980);

        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(b)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Il nome della rosa"))
                .andExpect(jsonPath("$.author").value("Umberto Eco"))
                .andExpect(jsonPath("$.publicationYear").value(1980));

        mockMvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Il nome della rosa"));
    }

    /**
     * Updates an existing book via PUT request and verifies:
     * <ul>
     *     <li>the HTTP response contains updated fields</li>
     *     <li>the persisted book state is updated in the repository</li>
     * </ul>
     *
     * @throws Exception if MockMvc request execution fails
     */
    @Test
    @DisplayName("PUT /books/{id} - aggiorna libro esistente")
    void updateBook() throws Exception {
        Book saved = bookRepository.save(new Book("Titolo Vecchio", "Autore Vecchio", 1990));
        Long id = saved.getId();

        Book update = new Book("Titolo Nuovo", "Autore Nuovo", 2020);

        mockMvc.perform(put("/books/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Titolo Nuovo"))
                .andExpect(jsonPath("$.author").value("Autore Nuovo"))
                .andExpect(jsonPath("$.publicationYear").value(2020));

        Book reloaded = bookRepository.findById(id).orElseThrow();
        assertThat(reloaded.getTitle()).isEqualTo("Titolo Nuovo");
        assertThat(reloaded.getAuthor()).isEqualTo("Autore Nuovo");
        assertThat(reloaded.getPublicationYear()).isEqualTo(2020);
    }

    /**
     * Deletes a book via DELETE request and verifies that
     * the repository no longer contains the entity.
     *
     * @throws Exception if MockMvc request execution fails
     */
    @Test
    @DisplayName("DELETE /books/{id} - elimina libro")
    void deleteBook() throws Exception {
        Book saved = bookRepository.save(new Book("Da Eliminare", "A", 1999));
        Long id = saved.getId();

        mockMvc.perform(delete("/books/{id}", id))
                .andExpect(status().isOk());

        assertThat(bookRepository.findById(id)).isEmpty();
    }
}
