package com.librarymanagement.dto;

import com.librarymanagement.entity.BorrowRecord;
import lombok.Data;

import java.util.List;

@Data
public class MemberDetailsResponseDTO {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private boolean isActive;
    private java.time.OffsetDateTime membershipDate;
    private java.time.OffsetDateTime createdAt;
    private List<BorrowRecord> currentBorrows;
}
