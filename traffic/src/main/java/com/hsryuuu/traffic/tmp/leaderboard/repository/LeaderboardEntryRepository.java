package com.hsryuuu.traffic.tmp.leaderboard.repository;

import com.hsryuuu.traffic.tmp.leaderboard.entity.LeaderboardEntry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LeaderboardEntryRepository extends JpaRepository<LeaderboardEntry, Long> {

    Optional<LeaderboardEntry> findByUserId(Long userId);
}
