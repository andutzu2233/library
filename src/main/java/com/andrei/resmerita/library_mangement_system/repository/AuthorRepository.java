package com.andrei.resmerita.library_mangement_system.repository;

import com.andrei.resmerita.library_mangement_system.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthorRepository extends JpaRepository<Author, String> {
    Optional<Author> findByName(String name);
}

