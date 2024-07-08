package com.andrei.resmerita.library_mangement_system.service.impl;

import com.andrei.resmerita.library_mangement_system.dto.AuthorDTO;
import com.andrei.resmerita.library_mangement_system.dto.BookDTO;
import com.andrei.resmerita.library_mangement_system.exception.BookAlreadyExistException;
import com.andrei.resmerita.library_mangement_system.exception.ResourceNotFoundException;
import com.andrei.resmerita.library_mangement_system.mapper.BookMapper;
import com.andrei.resmerita.library_mangement_system.model.Author;
import com.andrei.resmerita.library_mangement_system.model.Book;
import com.andrei.resmerita.library_mangement_system.repository.AuthorRepository;
import com.andrei.resmerita.library_mangement_system.repository.BookRepository;
import com.andrei.resmerita.library_mangement_system.util.Utils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private AuthorRepository authorRepository;

    @Spy
    private BookMapper bookMapper = Mappers.getMapper(BookMapper.class);

    @InjectMocks
    private BookServiceImpl bookService;

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
    void testSaveBookShouldReturnSavedBook() {
        when(authorRepository.findByName(authorDTO.getName())).thenReturn(Optional.of(author));
        when(bookMapper.toBook(bookDTO)).thenReturn(book);
        when(bookRepository.save(book)).thenReturn(book);

        BookDTO savedBook = bookService.saveBook(bookDTO);

        assertThat(savedBook.getTitle()).isEqualTo(bookDTO.getTitle());
        verify(bookRepository, times(1)).save(book);
    }

    @Test
    void testSaveBookWithExistingIsbnShouldThrowException() {
        when(bookRepository.findByIsbn(anyString())).thenReturn(Optional.of(new Book()));

        assertThrows(BookAlreadyExistException.class, () -> bookService.saveBook(bookDTO));
    }

    @Test
    void testUpdateBookShouldReturnUpdatedBook() {
        String bookId = "1";
        book.setId(bookId);
        bookDTO.setDescription("Updated description.");

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(authorRepository.findByName(authorDTO.getName())).thenReturn(Optional.of(author));
        when(bookRepository.save(book)).thenReturn(book);

        BookDTO updatedBook = bookService.updateBook(bookId, bookDTO);

        assertThat(updatedBook.getDescription()).isEqualTo(bookDTO.getDescription());
        verify(bookRepository, times(1)).save(book);
    }

    @Test
    void testUpdateNonExistingBookShouldThrowException() {
        String bookId = "1";

        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> bookService.updateBook(bookId, bookDTO));
    }

    @Test
    void testGetBookByIdShouldReturnBook() {
        String bookId = "1";
        book.setId(bookId);

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(bookMapper.toBookDTO(book)).thenReturn(bookDTO);

        BookDTO foundBook = bookService.getBookById(bookId);

        assertThat(foundBook.getTitle()).isEqualTo(book.getTitle());
        verify(bookRepository, times(1)).findById(bookId);
    }

    @Test
    void testGetNonExistingBookByIdShouldThrowException() {
        String bookId = "1";

        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> bookService.getBookById(bookId));
    }

    @Test
    void testDeleteBookShouldDeleteBook() {
        String bookId = "1";

        bookService.deleteBook(bookId);

        verify(bookRepository, times(1)).deleteById(bookId);
    }

    @Test
    void testGetAllBooksShouldReturnBooksList() {
        Author author2 = Utils.createAuthor("Jane Doe", "Mystery Writer");
        Book book2 = Utils.createBook("A Game of Thrones", "Epic saga", "9780553103540", author2);

        when(bookRepository.findAll()).thenReturn(List.of(book, book2));
        when(bookMapper.toBookDTO(book)).thenReturn(bookDTO);
        when(bookMapper.toBookDTO(book2)).thenReturn(Utils.createBookDTO("A Game of Thrones", "Epic saga", "9780553103540", Utils.createAuthorDTO(author2.getName(), author2.getBio())));

        List<BookDTO> bookDTOs = bookService.getAllBooks();

        assertThat(bookDTOs).hasSize(2).extracting(BookDTO::getTitle).contains("Galactic Chronicles", "A Game of Thrones");
        verify(bookRepository, times(1)).findAll();
    }

    @Test
    void testSaveBookWithNonExistingAuthorShouldThrowException() {
        AuthorDTO unknownAuthorDTO = Utils.createAuthorDTO("Unknown Author", "Bio");
        BookDTO bookWithUnknownAuthorDTO = Utils.createBookDTO("Unknown Book", "Description", "1234567890", unknownAuthorDTO);

        when(authorRepository.findByName(unknownAuthorDTO.getName())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> bookService.saveBook(bookWithUnknownAuthorDTO));
    }

    @Test
    void testDeleteNonExistingBookShouldDoNothing() {
        String bookId = "1";

        doNothing().when(bookRepository).deleteById(bookId);

        bookService.deleteBook(bookId);

        verify(bookRepository, times(1)).deleteById(bookId);
    }
}
