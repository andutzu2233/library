package com.andrei.resmerita.library_mangement_system.service.impl;

import com.andrei.resmerita.library_mangement_system.dto.BookDTO;
import com.andrei.resmerita.library_mangement_system.exception.BookAlreadyExistException;
import com.andrei.resmerita.library_mangement_system.exception.ResourceNotFoundException;
import com.andrei.resmerita.library_mangement_system.mapper.BookMapper;
import com.andrei.resmerita.library_mangement_system.model.Author;
import com.andrei.resmerita.library_mangement_system.model.Book;
import com.andrei.resmerita.library_mangement_system.repository.AuthorRepository;
import com.andrei.resmerita.library_mangement_system.repository.BookRepository;
import com.andrei.resmerita.library_mangement_system.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final BookMapper bookMapper;

    @Autowired
    public BookServiceImpl(BookRepository bookRepository, AuthorRepository authorRepository, BookMapper bookMapper) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.bookMapper = bookMapper;
    }

    @Override
    public BookDTO saveBook(BookDTO bookDTO) {
        if (isBookAlreadyCreated(bookDTO.getIsbn())) {
            throw new BookAlreadyExistException("A book with this ISBN already exists.");
        }

        Author author = authorRepository.findByName(bookDTO.getAuthor().getName()).orElseThrow(() -> new ResourceNotFoundException("Author not found"));

        Book book = bookMapper.toBook(bookDTO);
        book.setAuthor(author);

        Book savedBook = bookRepository.save(book);
        return bookMapper.toBookDTO(savedBook);
    }

    @Override
    public BookDTO updateBook(String id, BookDTO bookDTO) {
        Book book = bookRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Book not found"));
        Author author = authorRepository.findByName(bookDTO.getAuthor().getName()).orElseThrow(() -> new ResourceNotFoundException("Author not found"));

        book.setTitle(bookDTO.getTitle());
        book.setDescription(bookDTO.getDescription());
        book.setIsbn(bookDTO.getIsbn());
        book.setAuthor(author);

        Book updatedBook = bookRepository.save(book);
        return bookMapper.toBookDTO(updatedBook);
    }

    @Override
    public void deleteBook(String id) {
        bookRepository.deleteById(id);
    }

    @Override
    public List<BookDTO> getAllBooks() {
        return bookRepository.findAll().stream().map(bookMapper::toBookDTO).collect(Collectors.toList());
    }

    @Override
    public BookDTO getBookById(String id) {
        Book book = bookRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Book not found"));
        return bookMapper.toBookDTO(book);
    }

    @Override
    public boolean isBookAlreadyCreated(String isbn) {
        return bookRepository.findByIsbn(isbn).isPresent();
    }
}