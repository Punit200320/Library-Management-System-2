package com.librarymanagement.service;

import com.librarymanagement.dto.BorrowRequestDTO;
import com.librarymanagement.entity.Book;
import com.librarymanagement.entity.BorrowRecord;
import com.librarymanagement.entity.BorrowStatus;
import com.librarymanagement.entity.Member;
import com.librarymanagement.exception.ResourceNotFoundException;
import com.librarymanagement.repository.BookRepository;
import com.librarymanagement.repository.BorrowRepository;
import com.librarymanagement.repository.MemberRepository;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Service
public class BorrowService {

    private final BorrowRepository borrowRepository;
    private final BookRepository bookRepository;
    private final MemberRepository memberRepository;

    public BorrowService(BorrowRepository borrowRepository, BookRepository bookRepository, MemberRepository memberRepository) {
        this.borrowRepository = borrowRepository;
        this.bookRepository = bookRepository;
        this.memberRepository = memberRepository;
    }

    public BorrowRecord borrow(BorrowRequestDTO req) {
        Book book = bookRepository.findById(req.getBookId())
                .orElseThrow(() -> new ResourceNotFoundException("Book not found"));

        Member member = memberRepository.findById(req.getMemberId())
                .orElseThrow(() -> new ResourceNotFoundException("Member not found"));

        if (!member.isActive()) {
            throw new RuntimeException("Inactive members cannot borrow books");
        }

        if (book.getAvailableCopies() <= 0) {
            throw new RuntimeException("No copies available");
        }

        boolean alreadyBorrowed = borrowRepository.existsByBookIdAndMemberIdAndStatusIn(book.getId(), member.getId(), List.of(BorrowStatus.BORROWED, BorrowStatus.OVERDUE));
        if (alreadyBorrowed) {
            throw new RuntimeException("Member already has this book borrowed");
        }

        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
        BorrowRecord r = new BorrowRecord();
        r.setBookId(book.getId());
        r.setMemberId(member.getId());
        r.setBorrowedAt(now);
        r.setDueDate(now.plusDays(14));
        r.setStatus(BorrowStatus.BORROWED);

        // decrement available copies
        book.setAvailableCopies(book.getAvailableCopies() - 1);
        bookRepository.save(book);

        return borrowRepository.save(r);
    }

    public BorrowRecord returnBook(Long borrowId) {
        BorrowRecord r = borrowRepository.findById(borrowId)
                .orElseThrow(() -> new ResourceNotFoundException("Borrow record not found"));

        if (r.getReturnedAt() != null) {
            return r; // already returned
        }

        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
        r.setReturnedAt(now);
        if (now.isAfter(r.getDueDate())) {
            r.setStatus(BorrowStatus.OVERDUE);
        } else {
            r.setStatus(BorrowStatus.RETURNED);
        }

        // increase available copies
        Book book = bookRepository.findById(r.getBookId())
                .orElseThrow(() -> new ResourceNotFoundException("Book not found"));
        book.setAvailableCopies(book.getAvailableCopies() + 1);
        bookRepository.save(book);

        return borrowRepository.save(r);
    }

    public List<BorrowRecord> overdue() {
        return borrowRepository.findByDueDateBeforeAndStatus(OffsetDateTime.now(ZoneOffset.UTC), BorrowStatus.BORROWED);
    }

    public List<BorrowRecord> memberHistory(Long memberId) {
        return borrowRepository.findByMemberId(memberId);
    }

    public List<BorrowRecord> currentBorrows(Long memberId) {
        return borrowRepository.findByMemberIdAndStatusIn(memberId, List.of(BorrowStatus.BORROWED, BorrowStatus.OVERDUE));
    }
}
