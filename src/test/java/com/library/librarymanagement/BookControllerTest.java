
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
 * Test slice del web layer per BookController.
 * Usa MockMvc per eseguire richieste e @MockitoBean per mockare il BookRepository.
 */
@WebMvcTest(BookController.class)
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BookRepository bookRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("GET /books - restituisce tutti i libri")
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

    @Test
    @DisplayName("POST /books - crea un libro")
    void createBook() throws Exception {
        Book input = new Book("Le città invisibili", "Italo Calvino", 1972);
        Mockito.when(bookRepository.save(any(Book.class))).thenAnswer(inv -> inv.getArgument(0));

        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Le città invisibili")))
                .andExpect(jsonPath("$.author", is("Italo Calvino")))
                .andExpect(jsonPath("$.publicationYear", is(1972)));
    }

    @Test
    @DisplayName("PUT /books/{id} - aggiorna un libro esistente")
    void updateBook() throws Exception {
        Long id = 10L;
        Book existing = new Book("Titolo Vecchio", "Autore Vecchio", 1990);
        Book update = new Book("Titolo Nuovo", "Autore Nuovo", 2020);

        Mockito.when(bookRepository.findById(eq(id))).thenReturn(Optional.of(existing));
        Mockito.when(bookRepository.save(any(Book.class))).thenAnswer(inv -> inv.getArgument(0));

        mockMvc.perform(put("/books/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Titolo Nuovo")))
                .andExpect(jsonPath("$.author", is("Autore Nuovo")))
                .andExpect(jsonPath("$.publicationYear", is(2020)));
    }

    @Test
    @DisplayName("DELETE /books/{id} - elimina libro")
    void deleteBook() throws Exception {
        Long id = 3L;

        mockMvc.perform(delete("/books/{id}", id))
                .andExpect(status().isOk());

        Mockito.verify(bookRepository).deleteById(eq(id));
    }
}
