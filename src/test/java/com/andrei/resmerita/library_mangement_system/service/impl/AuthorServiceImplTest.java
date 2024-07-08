package com.andrei.resmerita.library_mangement_system.service.impl;

import com.andrei.resmerita.library_mangement_system.dto.AuthorDTO;
import com.andrei.resmerita.library_mangement_system.dto.BookDTO;
import com.andrei.resmerita.library_mangement_system.exception.AuthorAlreadyExistsException;
import com.andrei.resmerita.library_mangement_system.exception.ResourceNotFoundException;
import com.andrei.resmerita.library_mangement_system.mapper.AuthorMapper;
import com.andrei.resmerita.library_mangement_system.mapper.BookMapper;
import com.andrei.resmerita.library_mangement_system.model.Author;
import com.andrei.resmerita.library_mangement_system.model.Book;
import com.andrei.resmerita.library_mangement_system.repository.AuthorRepository;
import com.andrei.resmerita.library_mangement_system.repository.BookRepository;
import com.andrei.resmerita.library_mangement_system.util.Utils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthorServiceImplTest {

    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private AuthorMapper authorMapper;

    @Spy
    private BookMapper bookMapper;

    @InjectMocks
    private AuthorServiceImpl authorService;

    private AuthorDTO authorDTO;
    private BookDTO bookDTO;
    private Author author;
    private Book book;

    @BeforeEach
    void setUp() {
        authorDTO = Utils.createAuthorDTO("Andrei Resmerita", "Overlord");
        bookDTO = Utils.createBookDTO("Galactic Chronicles", "Epic saga", "1234567890", authorDTO);
        author = Utils.createAuthor("Andrei Resmerita", "Overlord");
        book = Utils.createBook("Galactic Chronicles", "Epic saga", "1234567890", author);
    }

    @Test
    void testSaveAuthorShouldReturnSavedAuthor() {
        when(authorMapper.toAuthor(authorDTO)).thenReturn(author);
        when(authorRepository.save(author)).thenReturn(author);
        when(authorMapper.toAuthorDTO(author)).thenReturn(authorDTO);

        AuthorDTO savedAuthor = authorService.saveAuthor(authorDTO);

        assertThat(savedAuthor.getName()).isEqualTo(authorDTO.getName());
        verify(authorRepository, times(1)).save(author);
    }

    @Test
    void testSaveAuthorWithExistingNameShouldThrowException() {
        when(authorRepository.findByName(anyString())).thenReturn(Optional.of(new Author()));

        assertThrows(AuthorAlreadyExistsException.class, () -> authorService.saveAuthor(authorDTO));
    }

    @Test
    void testSaveAuthorWithBooksShouldReturnSavedAuthorAndBooks() {
        Set<BookDTO> bookDTOs = new HashSet<>();
        bookDTOs.add(bookDTO);
        authorDTO.setBooks(bookDTOs);

        when(authorMapper.toAuthor(authorDTO)).thenReturn(author);
        when(bookMapper.toBook(bookDTO)).thenReturn(book);
        when(authorRepository.save(author)).thenReturn(author);
        when(bookRepository.save(book)).thenReturn(book);
        when(authorMapper.toAuthorDTO(author)).thenReturn(authorDTO);

        AuthorDTO savedAuthor = authorService.saveAuthor(authorDTO);

        assertThat(savedAuthor.getBooks()).hasSize(1);
        verify(authorRepository, times(1)).save(author);
        verify(bookRepository, times(1)).save(book);
    }

    @Test
    void testUpdateAuthorShouldReturnUpdatedAuthor() {
        // given
        String authorId = "1";
        author.setId(authorId);
        authorDTO.setBio("Updated bio");

        when(authorRepository.findById(authorId)).thenReturn(Optional.of(author));
        when(authorRepository.save(author)).thenReturn(author);
        when(authorMapper.toAuthorDTO(author)).thenReturn(authorDTO);

        // when
        AuthorDTO updatedAuthor = authorService.updateAuthor(authorId, authorDTO);

        // then
        assertThat(updatedAuthor.getBio()).isEqualTo(authorDTO.getBio());
        verify(authorRepository, times(1)).save(author);
        verify(bookRepository, never()).save(any(Book.class)); // Ensure no book update is called
    }

    @Test
    void testUpdateNonExistingAuthorShouldThrowException() {
        // given
        String authorId = "1";
        authorDTO.setBio("Updated bio");

        when(authorRepository.findById(authorId)).thenReturn(Optional.empty());

        // when / then
        assertThrows(ResourceNotFoundException.class, () -> authorService.updateAuthor(authorId, authorDTO));
    }

    @Test
    void testGetAuthorByIdShouldReturnAuthor() {
        // given
        String authorId = "1";
        author.setId(authorId);

        when(authorRepository.findById(authorId)).thenReturn(Optional.of(author));
        when(authorMapper.toAuthorDTO(author)).thenReturn(authorDTO);

        // when
        AuthorDTO foundAuthor = authorService.getAuthorById(authorId);

        // then
        assertThat(foundAuthor.getName()).isEqualTo(author.getName());
        verify(authorRepository, times(1)).findById(authorId);
    }

    @Test
    void testGetNonExistingAuthorByIdShouldThrowException() {
        // given
        String authorId = "1";

        when(authorRepository.findById(authorId)).thenReturn(Optional.empty());

        // when / then
        assertThrows(ResourceNotFoundException.class, () -> authorService.getAuthorById(authorId));
    }

    @Test
    void testDeleteAuthorShouldDeleteAuthor() {
        // given
        String authorId = "1";

        // when
        authorService.deleteAuthor(authorId);

        // then
        verify(authorRepository, times(1)).deleteById(authorId);
    }

    @Test
    void testDeleteNonExistingAuthorShouldDoNothing() {
        // given
        String authorId = "1";

        doNothing().when(authorRepository).deleteById(authorId);

        // when
        authorService.deleteAuthor(authorId);

        // then
        verify(authorRepository, times(1)).deleteById(authorId);
    }

    @Test
    void testGetAllAuthorsShouldReturnAuthorsList() {
        Author author1 = Utils.createAuthor("Andrei Resmerita", "Overlord");
        Author author2 = Utils.createAuthor("Jane Doe", "Mystery Writer");
        List<Author> authors = List.of(author1, author2);

        when(authorRepository.findAll()).thenReturn(authors);
        when(authorMapper.toAuthorDTO(author1)).thenReturn(Utils.createAuthorDTO("Andrei Resmerita", "Overlord"));
        when(authorMapper.toAuthorDTO(author2)).thenReturn(Utils.createAuthorDTO("Jane J", "Mystery Writer"));

        List<AuthorDTO> authorDTOs = authorService.getAllAuthors();

        assertThat(authorDTOs).hasSize(2).extracting(AuthorDTO::getName).contains("Andrei Resmerita", "Jane J");
        verify(authorRepository, times(1)).findAll();

    }
}
