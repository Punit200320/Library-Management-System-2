package com.librarymanagement.controller;

import com.librarymanagement.dto.MemberDetailsResponseDTO;
import com.librarymanagement.dto.MemberRequestDTO;
import com.librarymanagement.entity.Member;
import com.librarymanagement.service.MemberService;
import com.librarymanagement.service.BorrowService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;
    private final BorrowService borrowService;

    public MemberController(MemberService memberService, BorrowService borrowService) {
        this.memberService = memberService;
        this.borrowService = borrowService;
    }

    @GetMapping
    public List<Member> getAll() {
        return memberService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<MemberDetailsResponseDTO> get(@PathVariable Long id) {
        return ResponseEntity.ok(memberService.getDetails(id, borrowService.currentBorrows(id)));
    }

    @PostMapping
    public ResponseEntity<Member> create(@Valid @RequestBody MemberRequestDTO dto) {
        Member m = new Member();
        m.setName(dto.getName());
        m.setEmail(dto.getEmail());
        m.setPhone(dto.getPhone());
        return ResponseEntity.ok(memberService.create(m));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Member> update(@PathVariable Long id, @Valid @RequestBody MemberRequestDTO dto) {
        Member m = new Member();
        m.setName(dto.getName());
        m.setEmail(dto.getEmail());
        m.setPhone(dto.getPhone());
        return ResponseEntity.ok(memberService.update(id, m));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Member> deactivate(@PathVariable Long id) {
        return ResponseEntity.ok(memberService.deactivate(id));
    }

    @GetMapping("/{id}/history")
    public ResponseEntity<?> history(@PathVariable Long id) {
        return ResponseEntity.ok(borrowService.memberHistory(id));
    }
}
