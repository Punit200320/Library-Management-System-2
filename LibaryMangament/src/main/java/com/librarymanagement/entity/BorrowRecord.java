package com.librarymanagement.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.OffsetDateTime;

@Entity
@Table(name = "borrow_records")
@Data
public class BorrowRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long bookId;

    private Long memberId;

    private OffsetDateTime borrowedAt;

    private OffsetDateTime dueDate;

    private OffsetDateTime returnedAt;

    @Enumerated(EnumType.STRING)
    private BorrowStatus status;
}
