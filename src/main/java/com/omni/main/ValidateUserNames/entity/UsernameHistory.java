package com.omni.main.ValidateUserNames.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "username_history")
public class UsernameHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "old_username", nullable = false)
    private String oldUsername;

    private LocalDateTime changedAt;

    @PrePersist
    public void onCreate() {
        this.changedAt = LocalDateTime.now();
    }
}
