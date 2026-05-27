package com.librarymanagement.controller;

import com.librarymanagement.dto.BorrowRequestDTO;
import com.librarymanagement.entity.BorrowRecord;
import com.librarymanagement.service.BorrowService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class BorrowController {

    private final BorrowService borrowService;

    public BorrowController(BorrowService borrowService) {
        this.borrowService = borrowService;
    }

    @PostMapping("/borrow")
    public ResponseEntity<BorrowRecord> borrowBook(@Valid @RequestBody BorrowRequestDTO request) {
        return ResponseEntity.status(201).body(borrowService.borrow(request));
    }

    @PostMapping("/return/{borrowId}")
    public ResponseEntity<BorrowRecord> returnBook(@PathVariable Long borrowId) {
        return ResponseEntity.ok(borrowService.returnBook(borrowId));
    }

    @GetMapping("/borrow/overdue")
    public ResponseEntity<List<BorrowRecord>> getOverdue() {
        return ResponseEntity.ok(borrowService.overdue());
    }
}
