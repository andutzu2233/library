package com.andrei.resmerita.library_mangement_system.service;

import com.andrei.resmerita.library_mangement_system.dto.AuthorDTO;
import com.andrei.resmerita.library_mangement_system.model.Author;

import java.util.List;

public interface AuthorService {
    AuthorDTO saveAuthor(AuthorDTO authorDTO);

    AuthorDTO updateAuthor(String id, AuthorDTO authorDTO);

    void deleteAuthor(String id);

    List<AuthorDTO> getAllAuthors();

    AuthorDTO getAuthorById(String id);

    boolean doesAuthorExist(String name);
}