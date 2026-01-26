package com.hsryuuu.traffic.leaderboard.repository;

import com.hsryuuu.traffic.leaderboard.entity.LeaderboardEntry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LeaderboardEntryRepository extends JpaRepository<LeaderboardEntry, Long> {

    Optional<LeaderboardEntry> findByUserId(Long userId);
}
