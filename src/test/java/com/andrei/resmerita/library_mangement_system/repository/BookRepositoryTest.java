package com.andrei.resmerita.library_mangement_system.repository;

import com.andrei.resmerita.library_mangement_system.model.Author;
import com.andrei.resmerita.library_mangement_system.model.Book;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Test
    public void whenFindByIsbn_thenReturnBook() {
        // given
        Author author = new Author();
        author.setName("J.K. Rowling");
        author.setBio("British author, best known for the Harry Potter series.");
        authorRepository.save(author);

        Book book = new Book();
        book.setTitle("Harry Potter and the Philosopher's Stone");
        book.setDescription("The first book in the Harry Potter series.");
        book.setIsbn("9780747532699");
        book.setAuthor(author);
        bookRepository.save(book);

        // when
        Optional<Book> found = bookRepository.findByIsbn(book.getIsbn());

        // then
        assertThat(found).isPresent();
        assertThat(found.get().getIsbn()).isEqualTo(book.getIsbn());
        assertThat(found.get().getTitle()).isEqualTo(book.getTitle());
    }

    @Test
    public void whenFindByIsbnAndIsbnDoesNotExist_thenReturnEmpty() {
        // when
        Optional<Book> found = bookRepository.findByIsbn("nonexistent-isbn");

        // then
        assertThat(found).isNotPresent();
    }
}