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
 * Unit test for BookController.
 * <p>
 * These tests mock the repository layer to isolate the web layer behavior.
 * Each test verifies HTTP responses, JSON payloads and expected interaction
 * with the repository.
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
     * Retrieves a list of books via GET and verifies JSON structure and content.
     */
    @Test
    @DisplayName("GET /books - returns all books")
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
     * Creates a new book via POST and validates returned JSON fields.
     */
    @Test
    @DisplayName("POST /books - creates a new book")
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
     * Updates an existing book and checks both HTTP result and updated values.
     */
    @Test
    @DisplayName("PUT /books/{id} - updates existing book")
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
     * Deletes a book via DELETE and verifies the repository interaction.
     */
    @Test
    @DisplayName("DELETE /books/{id} - deletes book")
    void deleteBook() throws Exception {
        Long id = 3L;

        mockMvc.perform(delete("/books/{id}", id))
                .andExpect(status().isOk());

        Mockito.verify(bookRepository).deleteById(eq(id));
    }
}
