package com.hsryuuu.traffic.tmp.leaderboard;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/leaderboard")
public class LeaderboardController {

    @PostMapping("/score")
    public ResponseEntity<Void> updateScore(@RequestBody ScoreRequest request) {
        // TODO: 점수 등록/갱신
        return ResponseEntity.ok().build();
    }

    @GetMapping("/top/{n}")
    public ResponseEntity<Void> getTopN(@PathVariable int n) {
        // TODO: 상위 N명 랭킹 조회
        return ResponseEntity.ok().build();
    }

    @GetMapping("/rank/{userId}")
    public ResponseEntity<Void> getUserRank(@PathVariable Long userId) {
        // TODO: 특정 유저 순위 및 점수 조회
        return ResponseEntity.ok().build();
    }

    record ScoreRequest(Long userId, double score) {}
}
