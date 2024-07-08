package com.andrei.resmerita.library_mangement_system.service.impl;

import com.andrei.resmerita.library_mangement_system.dto.AuthorDTO;
import com.andrei.resmerita.library_mangement_system.exception.AuthorAlreadyExistsException;
import com.andrei.resmerita.library_mangement_system.exception.ResourceNotFoundException;
import com.andrei.resmerita.library_mangement_system.mapper.AuthorMapper;
import com.andrei.resmerita.library_mangement_system.mapper.BookMapper;
import com.andrei.resmerita.library_mangement_system.model.Author;
import com.andrei.resmerita.library_mangement_system.model.Book;
import com.andrei.resmerita.library_mangement_system.repository.AuthorRepository;
import com.andrei.resmerita.library_mangement_system.repository.BookRepository;
import com.andrei.resmerita.library_mangement_system.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;
    private final AuthorMapper authorMapper;
    private final BookMapper bookMapper;

    @Autowired
    public AuthorServiceImpl(AuthorRepository authorRepository, BookRepository bookRepository, AuthorMapper authorMapper, BookMapper bookMapper) {
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
        this.authorMapper = authorMapper;
        this.bookMapper = bookMapper;
    }

    @Override
    public AuthorDTO saveAuthor(AuthorDTO authorDTO) {
        if (doesAuthorExist(authorDTO.getName())) {
            throw new AuthorAlreadyExistsException("An author with this name already exists.");
        }
        Set<Book> books = authorDTO.getBooks().stream().map(bookDTO -> bookRepository.findByIsbn(bookDTO.getIsbn()).orElseGet(() -> bookRepository.save(bookMapper.toBook(bookDTO)))).collect(Collectors.toSet());
        Author author = authorMapper.toAuthor(authorDTO);
        author.setBooks(books);
        Author savedAuthor = authorRepository.save(author);
        return authorMapper.toAuthorDTO(savedAuthor);
    }

    @Override
    public AuthorDTO updateAuthor(String id, AuthorDTO authorDTO) {
        Author author = authorRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Author not found"));

        // Update only the author's details
        author.setName(authorDTO.getName());
        author.setBio(authorDTO.getBio());

        Author updatedAuthor = authorRepository.save(author);
        return authorMapper.toAuthorDTO(updatedAuthor);
    }


    @Override
    public void deleteAuthor(String id) {
        authorRepository.deleteById(id);
    }

    @Override
    public List<AuthorDTO> getAllAuthors() {
        return authorRepository.findAll().stream().map(authorMapper::toAuthorDTO).collect(Collectors.toList());
    }

    @Override
    public AuthorDTO getAuthorById(String id) {
        Author author = authorRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Author not found"));
        return authorMapper.toAuthorDTO(author);
    }

    @Override
    public boolean doesAuthorExist(String name) {
        return authorRepository.findByName(name).isPresent();
    }
}
