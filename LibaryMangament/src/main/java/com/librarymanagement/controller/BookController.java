package com.librarymanagement.controller;

import com.librarymanagement.dto.BookRequestDTO;
import com.librarymanagement.entity.Book;
import com.librarymanagement.service.BookService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public Page<Book> getBooks(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) String genre,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Pageable p = PageRequest.of(page, size);
        return bookService.getBooks(title, author, genre, p);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getBook(@PathVariable Long id) {
        return ResponseEntity.ok(bookService.getBook(id));
    }

    @PostMapping
    public ResponseEntity<Book> createBook(@Valid @RequestBody BookRequestDTO dto) {
        Book b = new Book();
        b.setTitle(dto.getTitle());
        b.setAuthor(dto.getAuthor());
        b.setIsbn(dto.getIsbn());
        b.setGenre(dto.getGenre());
        b.setTotalCopies(dto.getTotalCopies());
        if (dto.getAvailableCopies() != null) {
            b.setAvailableCopies(dto.getAvailableCopies());
        }
        return ResponseEntity.status(201).body(bookService.createBook(b));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable Long id, @Valid @RequestBody BookRequestDTO dto) {
        Book b = new Book();
        b.setTitle(dto.getTitle());
        b.setAuthor(dto.getAuthor());
        b.setIsbn(dto.getIsbn());
        b.setGenre(dto.getGenre());
        b.setTotalCopies(dto.getTotalCopies());
        if (dto.getAvailableCopies() != null) {
            b.setAvailableCopies(dto.getAvailableCopies());
        }
        return ResponseEntity.ok(bookService.updateBook(id, b));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }
}
