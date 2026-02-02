package com.hsryuuu.traffic.tmp.queue;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/queue")
public class QueueController {

    @PostMapping("/enter")
    public ResponseEntity<Void> enter(@RequestBody EnterRequest request) {
        // TODO: 대기열 진입, 대기번호 발급
        return ResponseEntity.ok().build();
    }

    @GetMapping("/rank/{userId}")
    public ResponseEntity<Void> getRank(@PathVariable Long userId) {
        // TODO: 현재 대기 순번 조회
        return ResponseEntity.ok().build();
    }

    @PostMapping("/process")
    public ResponseEntity<Void> process(@RequestParam(defaultValue = "10") int count) {
        // TODO: 대기열에서 N명 순서대로 입장 처리
        return ResponseEntity.ok().build();
    }

    @GetMapping("/status")
    public ResponseEntity<Void> getStatus() {
        // TODO: 대기열 현황 조회 (총 대기 인원, 처리 속도)
        return ResponseEntity.ok().build();
    }

    record EnterRequest(Long userId) {}
}
