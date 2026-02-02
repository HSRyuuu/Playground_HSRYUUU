package com.hsryuuu.traffic.tmp.counting;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/counting/posts")
public class CountingController {

    @PostMapping("/{postId}/like")
    public ResponseEntity<Void> like(@PathVariable Long postId,
                                     @RequestBody LikeRequest request) {
        // TODO: 좋아요 (중복 방지)
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{postId}/like")
    public ResponseEntity<Void> unlike(@PathVariable Long postId,
                                       @RequestParam Long userId) {
        // TODO: 좋아요 취소
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{postId}/count")
    public ResponseEntity<Void> getCount(@PathVariable Long postId) {
        // TODO: 좋아요 수 조회
        return ResponseEntity.ok().build();
    }

    record LikeRequest(Long userId) {}
}
