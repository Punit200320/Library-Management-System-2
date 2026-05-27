package com.librarymanagement.entity;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import lombok.Data;

@Entity
@Table(name = "books")
@Data
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String author;

    @Column(unique = true)
    private String isbn;

    private String genre;

    private int totalCopies;

    private Integer availableCopies;

    private OffsetDateTime createdAt = OffsetDateTime.now(java.time.ZoneOffset.UTC);
}
