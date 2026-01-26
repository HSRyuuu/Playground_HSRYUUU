package com.hsryuuu.traffic.leaderboard.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "leaderboard_entry")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LeaderboardEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private Long userId;

    @Column(nullable = false)
    private double score;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Builder
    public LeaderboardEntry(Long userId, double score) {
        this.userId = userId;
        this.score = score;
        this.updatedAt = LocalDateTime.now();
    }

    public void updateScore(double score) {
        this.score = score;
        this.updatedAt = LocalDateTime.now();
    }
}
