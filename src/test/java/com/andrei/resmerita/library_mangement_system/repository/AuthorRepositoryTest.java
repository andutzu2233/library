package com.andrei.resmerita.library_mangement_system.repository;

import com.andrei.resmerita.library_mangement_system.model.Author;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@ExtendWith(SpringExtension.class)
@DataJpaTest
class AuthorRepositoryTest {

    @Autowired
    private AuthorRepository authorRepository;

    @Test
    void whenFindByName_thenReturnAuthor() {
        Author author = new Author();
        author.setName("author-name");
        author.setBio("author-bio");
        authorRepository.save(author);

        Optional<Author> found = authorRepository.findByName(author.getName());

        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo(author.getName());
        assertThat(found.get().getBio()).isEqualTo(author.getBio());
    }

    @Test
    public void whenFindByNameAndNameDoesNotExist_thenReturnEmpty() {
        Optional<Author> found = authorRepository.findByName("nonexistent-name");
        assertThat(found).isNotPresent();
    }
}
