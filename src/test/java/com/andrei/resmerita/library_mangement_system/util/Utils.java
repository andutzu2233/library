package com.andrei.resmerita.library_mangement_system.util;

import com.andrei.resmerita.library_mangement_system.dto.AuthorDTO;
import com.andrei.resmerita.library_mangement_system.dto.BookDTO;
import com.andrei.resmerita.library_mangement_system.model.Author;
import com.andrei.resmerita.library_mangement_system.model.Book;

import java.util.HashSet;
import java.util.Set;

public class Utils {

    public static AuthorDTO createAuthorDTO(String name, String bio, Set<BookDTO> books) {
        return new AuthorDTO(name, bio, books);
    }

    public static AuthorDTO createAuthorDTO(String name, String bio) {
        return createAuthorDTO(name, bio, new HashSet<>());
    }

    public static BookDTO createBookDTO(String title, String description, String isbn, AuthorDTO authorDTO) {
        return new BookDTO(title, description, isbn, authorDTO);
    }

    public static Author createAuthor(String name, String bio) {
        Author author = new Author();
        author.setName(name);
        author.setBio(bio);
        return author;
    }

    public static Book createBook(String title, String description, String isbn, Author author) {
        Book book = new Book();
        book.setTitle(title);
        book.setDescription(description);
        book.setIsbn(isbn);
        book.setAuthor(author);
        return book;
    }
}
