
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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

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

@SpringBootTest(classes = LibraryManagementApplication.class)
@WebMvcTest(AuthorController.class)
public class AuthorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthorRepository authorRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("GET /authors - restituisce tutti gli autori")
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

    @Test
    @DisplayName("POST /authors - crea un autore")
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

    @Test
    @DisplayName("PUT /authors/{id} - aggiorna un autore esistente")
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

    @Test
    @DisplayName("DELETE /authors/{id} - elimina autore")
    void deleteAuthor() throws Exception {
        Long id = 1L;

        mockMvc.perform(delete("/authors/{id}", id))
                .andExpect(status().isOk());

        Mockito.verify(authorRepository).deleteById(eq(id));
    }
}
