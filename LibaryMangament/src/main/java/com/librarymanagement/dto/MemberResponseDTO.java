package com.librarymanagement.dto;

import lombok.Data;
import java.time.OffsetDateTime;

@Data
public class MemberResponseDTO {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private boolean isActive;
    private OffsetDateTime membershipDate;
}
