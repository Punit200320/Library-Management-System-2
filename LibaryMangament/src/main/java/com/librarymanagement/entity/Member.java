package com.librarymanagement.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.OffsetDateTime;

@Entity
@Table(name = "members")
@Data
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true)
    private String email;

    private String phone;

    private OffsetDateTime membershipDate = OffsetDateTime.now(java.time.ZoneOffset.UTC);

    private boolean isActive = true;

    private OffsetDateTime createdAt = OffsetDateTime.now(java.time.ZoneOffset.UTC);
}
