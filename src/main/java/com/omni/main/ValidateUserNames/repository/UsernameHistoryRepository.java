package com.omni.main.ValidateUserNames.repository;

import com.omni.main.ValidateUserNames.entity.UsernameHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsernameHistoryRepository extends JpaRepository<UsernameHistory, Long> {
    Page<UsernameHistory> findByUserIdOrderByChangedAtDesc(Long userId, Pageable pageable);
}