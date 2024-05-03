package com.f2z.gach.History.Repository;

import com.f2z.gach.History.Entity.UserHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserHistoryRepository extends JpaRepository<UserHistory, Long> {

    Page<UserHistory> findAllByUserId(Long userId, Pageable pageable);
}
