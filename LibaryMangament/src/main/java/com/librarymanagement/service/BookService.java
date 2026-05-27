package com.librarymanagement.service;

import com.librarymanagement.entity.Book;
import com.librarymanagement.entity.BorrowStatus;
import com.librarymanagement.exception.ConflictException;
import com.librarymanagement.exception.ResourceNotFoundException;
import com.librarymanagement.repository.BookRepository;
import com.librarymanagement.repository.BorrowRepository;
import com.librarymanagement.repository.BookSpecifications;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final BorrowRepository borrowRepository;

    public BookService(BookRepository bookRepository, BorrowRepository borrowRepository) {
        this.bookRepository = bookRepository;
        this.borrowRepository = borrowRepository;
    }

    public Page<Book> getBooks(String title, String author, String genre, Pageable pageable) {
        Specification<Book> spec = Specification.where(BookSpecifications.hasTitle(title))
                .and(BookSpecifications.hasAuthor(author))
                .and(BookSpecifications.hasGenre(genre));
        return bookRepository.findAll(spec, pageable);
    }

    public Book getBook(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found"));
    }

    public Book createBook(Book book) {
        bookRepository.findByIsbn(book.getIsbn()).ifPresent(existing -> {
            throw new ConflictException("ISBN already exists");
        });

        if (book.getAvailableCopies() == null) {
            book.setAvailableCopies(book.getTotalCopies());
        }

        if (book.getAvailableCopies() > book.getTotalCopies()) {
            throw new RuntimeException("availableCopies cannot be greater than totalCopies");
        }

        book.setCreatedAt(OffsetDateTime.now(java.time.ZoneOffset.UTC));
        return bookRepository.save(book);
    }

    public Book updateBook(Long id, Book updated) {
        Book book = getBook(id);

        bookRepository.findByIsbn(updated.getIsbn()).ifPresent(existing -> {
            if (!existing.getId().equals(id)) {
                throw new ConflictException("ISBN already exists");
            }
        });

        book.setTitle(updated.getTitle());
        book.setAuthor(updated.getAuthor());
        book.setGenre(updated.getGenre());
        book.setIsbn(updated.getIsbn());
        book.setTotalCopies(updated.getTotalCopies());

        if (updated.getAvailableCopies() != null) {
            if (updated.getAvailableCopies() > updated.getTotalCopies()) {
                throw new RuntimeException("availableCopies cannot be greater than totalCopies");
            }
            book.setAvailableCopies(updated.getAvailableCopies());
        }

        return bookRepository.save(book);
    }

    public void deleteBook(Long id) {
        // block delete if any active borrows exist
        boolean hasActive = borrowRepository.existsByBookIdAndStatusIn(id, List.of(BorrowStatus.BORROWED, BorrowStatus.OVERDUE));
        if (hasActive) {
            throw new RuntimeException("Cannot delete book with active borrow records");
        }
        bookRepository.deleteById(id);
    }
}
