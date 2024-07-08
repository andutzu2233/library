package com.andrei.resmerita.library_mangement_system.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String title;
    private String description;
    private String isbn;
    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private Author author;
}
