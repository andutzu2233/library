package com.andrei.resmerita.library_mangement_system.controller;

import com.andrei.resmerita.library_mangement_system.config.SecurityConfig;
import com.andrei.resmerita.library_mangement_system.dto.AuthorDTO;
import com.andrei.resmerita.library_mangement_system.dto.BookDTO;
import com.andrei.resmerita.library_mangement_system.exception.AuthorAlreadyExistsException;
import com.andrei.resmerita.library_mangement_system.exception.GlobalExceptionHandler;
import com.andrei.resmerita.library_mangement_system.exception.ResourceNotFoundException;
import com.andrei.resmerita.library_mangement_system.service.AuthorService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(AuthorController.class)
@Import({GlobalExceptionHandler.class, SecurityConfig.class})
class AuthorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthorService authorService;

    @Autowired
    private ObjectMapper objectMapper;

    private AuthorDTO authorDTO;
    private BookDTO bookDTO;

    @BeforeEach
    void setUp() {
        bookDTO = new BookDTO("Harry Potter", "Fantasy novel", "1234567890", null);
        Set<BookDTO> books = new HashSet<>();
        books.add(bookDTO);
        authorDTO = new AuthorDTO("Jules Verne", "Journey to the Centre of the Earth.", books);
    }

    @Test
    @WithMockUser()
    void testAddAuthorShouldReturnCreatedAuthor() throws Exception {
        when(authorService.saveAuthor(any(AuthorDTO.class))).thenReturn(authorDTO);

        String authorJson = objectMapper.writeValueAsString(authorDTO);

        mockMvc.perform(post("/authors/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", containsString("Jules Verne")));
    }

    @Test
    @WithMockUser()
    void testAddAuthorShouldReturnConflictWhenAuthorAlreadyExists() throws Exception {
        when(authorService.saveAuthor(any(AuthorDTO.class))).thenThrow(new AuthorAlreadyExistsException("An author with this name already exists."));

        String authorJson = objectMapper.writeValueAsString(authorDTO);

        mockMvc.perform(post("/authors/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorJson))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.errorMessage", containsString("An author with this name already exists.")));
    }

    @Test
    @WithMockUser()
    void testAddAuthorShouldReturnBadRequestWhenValidationFails() throws Exception {
        AuthorDTO invalidAuthorDTO = new AuthorDTO("", "", null);

        String invalidAuthorJson = objectMapper.writeValueAsString(invalidAuthorDTO);

        mockMvc.perform(post("/authors/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidAuthorJson))
                .andDo(print()) // Print the response to the console
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.name", containsString("Name cannot be null or empty")))
                .andExpect(jsonPath("$.bio", containsString("Biography cannot be null or empty")));
    }

    @Test
    @WithMockUser()
    void testUpdateAuthorShouldReturnUpdatedAuthor() throws Exception {
        when(authorService.updateAuthor(any(String.class), any(AuthorDTO.class))).thenReturn(authorDTO);

        String authorJson = objectMapper.writeValueAsString(authorDTO);

        mockMvc.perform(put("/authors/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", containsString("Jules Verne")));
    }

    @Test
    @WithMockUser()
    void testUpdateAuthorShouldReturnNotFoundWhenAuthorDoesNotExist() throws Exception {
        when(authorService.updateAuthor(any(String.class), any(AuthorDTO.class))).thenThrow(new ResourceNotFoundException("Author not found"));

        String authorJson = objectMapper.writeValueAsString(authorDTO);

        mockMvc.perform(put("/authors/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorJson))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorMessage", containsString("Author not found")));
    }

    @Test
    @WithMockUser()
    void testDeleteAuthorShouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/authors/delete/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser()
    void testGetAllAuthorsShouldReturnAuthorsList() throws Exception {
        List<AuthorDTO> authors = Collections.singletonList(authorDTO);
        when(authorService.getAllAuthors()).thenReturn(authors);

        mockMvc.perform(get("/authors/getAll")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", containsString("Jules Verne")));
    }

    @Test
    @WithMockUser()
    void testGetAuthorByIdShouldReturnAuthor() throws Exception {
        when(authorService.getAuthorById(any(String.class))).thenReturn(authorDTO);

        mockMvc.perform(get("/authors/getAuthorById/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", containsString("Jules Verne")));
    }

    @Test
    @WithMockUser()
    void testGetAuthorByIdShouldReturnNotFoundWhenAuthorDoesNotExist() throws Exception {
        when(authorService.getAuthorById(any(String.class))).thenThrow(new ResourceNotFoundException("Author not found"));

        mockMvc.perform(get("/authors/getAuthorById/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorMessage", containsString("Author not found")));
    }

    @Test
    void whenAccessAuthorsWithoutAuth_thenUnauthorized() throws Exception {
        mockMvc.perform(get("/authors/getAll")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }
}
