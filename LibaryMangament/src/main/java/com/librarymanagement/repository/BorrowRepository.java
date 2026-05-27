package com.librarymanagement.repository;

import com.librarymanagement.entity.BorrowRecord;
import com.librarymanagement.entity.BorrowStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BorrowRepository extends JpaRepository<BorrowRecord, Long> {
    List<BorrowRecord> findByMemberId(Long memberId);

    List<BorrowRecord> findByMemberIdAndStatusIn(Long memberId, List<BorrowStatus> statuses);

    List<BorrowRecord> findByStatus(BorrowStatus status);

    boolean existsByBookIdAndStatus(Long bookId, BorrowStatus status);

    boolean existsByBookIdAndStatusIn(Long bookId, List<BorrowStatus> statuses);

    boolean existsByBookIdAndMemberIdAndStatusIn(Long bookId, Long memberId, List<BorrowStatus> statuses);

    List<BorrowRecord> findByDueDateBeforeAndStatus( java.time.OffsetDateTime time, BorrowStatus status);
}
