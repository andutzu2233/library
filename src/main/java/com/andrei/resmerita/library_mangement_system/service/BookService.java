package com.andrei.resmerita.library_mangement_system.service;

import com.andrei.resmerita.library_mangement_system.dto.BookDTO;

import java.util.List;

public interface BookService {
    BookDTO saveBook(BookDTO bookDTO);

    BookDTO updateBook(String id, BookDTO bookDTO);

    void deleteBook(String id);

    List<BookDTO> getAllBooks();

    BookDTO getBookById(String id);

    boolean isBookAlreadyCreated(String isbn);
}
