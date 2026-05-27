package com.librarymanagement.service;

import com.librarymanagement.dto.MemberDetailsResponseDTO;
import com.librarymanagement.entity.BorrowRecord;
import com.librarymanagement.entity.Member;
import com.librarymanagement.exception.ConflictException;
import com.librarymanagement.exception.ResourceNotFoundException;
import com.librarymanagement.repository.BorrowRepository;
import com.librarymanagement.repository.MemberRepository;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final BorrowRepository borrowRepository;

    public MemberService(MemberRepository memberRepository, BorrowRepository borrowRepository) {
        this.memberRepository = memberRepository;
        this.borrowRepository = borrowRepository;
    }

    public List<Member> getAll() {
        return memberRepository.findAll();
    }

    public Member get(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found"));
    }

    public MemberDetailsResponseDTO getDetails(Long id, List<BorrowRecord> currentBorrows) {
        Member member = get(id);
        MemberDetailsResponseDTO details = new MemberDetailsResponseDTO();
        details.setId(member.getId());
        details.setName(member.getName());
        details.setEmail(member.getEmail());
        details.setPhone(member.getPhone());
        details.setActive(member.isActive());
        details.setMembershipDate(member.getMembershipDate());
        details.setCreatedAt(member.getCreatedAt());
        details.setCurrentBorrows(currentBorrows);
        return details;
    }

    public Member create(Member member) {
        memberRepository.findByEmail(member.getEmail()).ifPresent(existing -> {
            throw new ConflictException("Email already exists");
        });

        member.setCreatedAt(OffsetDateTime.now(java.time.ZoneOffset.UTC));
        member.setMembershipDate(OffsetDateTime.now(java.time.ZoneOffset.UTC));
        member.setActive(true);
        return memberRepository.save(member);
    }

    public Member update(Long id, Member updated) {
        Member m = get(id);
        memberRepository.findByEmail(updated.getEmail()).ifPresent(existing -> {
            if (!existing.getId().equals(id)) {
                throw new ConflictException("Email already exists");
            }
        });
        m.setName(updated.getName());
        m.setEmail(updated.getEmail());
        m.setPhone(updated.getPhone());
        return memberRepository.save(m);
    }

    public Member deactivate(Long id) {
        Member m = get(id);
        m.setActive(false);
        return memberRepository.save(m);
    }
}
