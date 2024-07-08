package com.andrei.resmerita.library_mangement_system.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Entity
@Data
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String name;
    private String bio;
    @OneToMany(mappedBy = "author")
    private Set<Book> books;

}