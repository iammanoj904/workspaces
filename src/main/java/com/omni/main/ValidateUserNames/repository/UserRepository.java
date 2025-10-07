package com.omni.main.ValidateUserNames.repository;

import com.omni.main.ValidateUserNames.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
