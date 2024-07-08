package com.andrei.resmerita.library_mangement_system.controller;

import com.andrei.resmerita.library_mangement_system.config.SecurityConfig;
import com.andrei.resmerita.library_mangement_system.dto.AuthorDTO;
import com.andrei.resmerita.library_mangement_system.dto.BookDTO;
import com.andrei.resmerita.library_mangement_system.exception.BookAlreadyExistException;
import com.andrei.resmerita.library_mangement_system.exception.GlobalExceptionHandler;
import com.andrei.resmerita.library_mangement_system.exception.ResourceNotFoundException;
import com.andrei.resmerita.library_mangement_system.service.BookService;
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

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(BookController.class)
@Import({GlobalExceptionHandler.class, SecurityConfig.class})
@WithMockUser()
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @Autowired
    private ObjectMapper objectMapper;

    private BookDTO bookDTO;
    private AuthorDTO authorDTO;

    @BeforeEach
    void setUp() {
        authorDTO = new AuthorDTO("J.K. Rowling", "British author, best known for the Harry Potter series.", null);
        bookDTO = new BookDTO("Harry Potter", "Fantasy novel", "1234567890", authorDTO);
    }

    @Test

    void testAddBookShouldReturnCreatedBook() throws Exception {
        when(bookService.saveBook(any(BookDTO.class))).thenReturn(bookDTO);

        String bookJson = objectMapper.writeValueAsString(bookDTO);

        mockMvc.perform(post("/books/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title", containsString("Harry Potter")));
    }

    @Test
    void testAddBookShouldReturnConflictWhenBookAlreadyExists() throws Exception {
        when(bookService.saveBook(any(BookDTO.class))).thenThrow(new BookAlreadyExistException("A book with this ISBN already exists."));

        String bookJson = objectMapper.writeValueAsString(bookDTO);

        mockMvc.perform(post("/books/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookJson))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.errorMessage", containsString("A book with this ISBN already exists.")));
    }

    @Test
    void testAddBookShouldReturnNotFoundWhenAuthorNotFound() throws Exception {
        when(bookService.saveBook(any(BookDTO.class))).thenThrow(new ResourceNotFoundException("Author not found"));

        String bookJson = objectMapper.writeValueAsString(bookDTO);

        mockMvc.perform(post("/books/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookJson))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorMessage", containsString("Author not found")));
    }

    @Test
    void testAddBookShouldReturnBadRequestWhenValidationFails() throws Exception {
        AuthorDTO invalidAuthorDTO = new AuthorDTO("", "", null);
        BookDTO invalidBookDTO = new BookDTO("", "", "", invalidAuthorDTO);

        String invalidBookJson = objectMapper.writeValueAsString(invalidBookDTO);

        mockMvc.perform(post("/books/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidBookJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title", containsString("Title cannot be null or empty")))
                .andExpect(jsonPath("$.isbn", containsString("ISBN cannot be null or empty")))
                .andExpect(jsonPath("$.['author.name']", containsString("Name cannot be null or empty")))
                .andExpect(jsonPath("$.['author.bio']", containsString("Biography cannot be null or empty")));
    }

}
