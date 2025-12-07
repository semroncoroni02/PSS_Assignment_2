package com.library.librarymanagement;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.library.librarymanagement.controller.BookController;
import com.library.librarymanagement.entity.Book;
import com.library.librarymanagement.repository.BookRepository;
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
 * Unit tests for {@code BookController}.
 * <p>
 * These tests validate the HTTP layer by mocking persistence interactions
 * with the {@link BookRepository}. They assert both JSON responses and
 * expected repository calls using {@link MockMvc}.
 *
 * <h2>Scope</h2>
 * <ul>
 *     <li>GET /books — list books</li>
 *     <li>POST /books — create book</li>
 *     <li>PUT /books/{id} — update book</li>
 *     <li>DELETE /books/{id} — delete book</li>
 * </ul>
 *
 * <p>
 * Notes:
 * <ul>
 *     <li>No real database is used — repository methods are mocked.</li>
 *     <li>Tests focus exclusively on controller behavior and HTTP contract.</li>
 * </ul>
 * </p>
 */
@WebMvcTest(BookController.class)
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BookRepository bookRepository;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Retrieves all books via GET request and verifies:
     * <ul>
     *     <li>HTTP status is OK</li>
     *     <li>JSON array size matches mocked result</li>
     *     <li>returned objects contain expected attributes</li>
     * </ul>
     *
     * @throws Exception if MockMvc request execution fails
     */
    @Test
    @DisplayName("GET /books - restituisce lista libri")
    void getAllBooks() throws Exception {
        List<Book> books = Arrays.asList(
                new Book("Il nome della rosa", "Umberto Eco", 1980),
                new Book("Norwegian Wood", "Haruki Murakami", 1987)
        );
        Mockito.when(bookRepository.findAll()).thenReturn(books);

        mockMvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].title", is("Il nome della rosa")))
                .andExpect(jsonPath("$[1].publicationYear", is(1987)));
    }

    /**
     * Creates a book via POST request and verifies:
     * <ul>
     *     <li>response contains expected JSON fields</li>
     *     <li>returned object matches input</li>
     * </ul>
     *
     * @throws Exception if MockMvc request execution fails
     */
    @Test
    @DisplayName("POST /books - crea un nuovo libro")
    void createBook() throws Exception {
        Book input = new Book("Le città invisibili", "Italo Calvino", 1972);
        Mockito.when(bookRepository.save(any(Book.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Le città invisibili")))
                .andExpect(jsonPath("$.author", is("Italo Calvino")))
                .andExpect(jsonPath("$.publicationYear", is(1972)));
    }

    /**
     * Updates an existing book via PUT request and verifies:
     * <ul>
     *     <li>repository lookup returns the entity</li>
     *     <li>updated attributes are reflected in JSON response</li>
     * </ul>
     *
     * @throws Exception if MockMvc request execution fails
     */
    @Test
    @DisplayName("PUT /books/{id} - aggiorna libro esistente")
    void updateBook() throws Exception {
        Long id = 10L;
        Book existing = new Book("Old Title", "Old Author", 1990);
        Book update = new Book("New Title", "New Author", 2020);

        Mockito.when(bookRepository.findById(eq(id))).thenReturn(Optional.of(existing));
        Mockito.when(bookRepository.save(any(Book.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        mockMvc.perform(put("/books/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("New Title")))
                .andExpect(jsonPath("$.author", is("New Author")))
                .andExpect(jsonPath("$.publicationYear", is(2020)));
    }

    /**
     * Deletes a book via DELETE request and verifies:
     * <ul>
     *     <li>HTTP response status is OK</li>
     *     <li>repository deletion is invoked with correct ID</li>
     * </ul>
     *
     * @throws Exception if MockMvc request execution fails
     */
    @Test
    @DisplayName("DELETE /books/{id} - elimina libro")
    void deleteBook() throws Exception {
        Long id = 3L;

        mockMvc.perform(delete("/books/{id}", id))
                .andExpect(status().isOk());

        Mockito.verify(bookRepository).deleteById(eq(id));
    }
}
