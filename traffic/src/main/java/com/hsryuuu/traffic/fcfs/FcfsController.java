package com.hsryuuu.traffic.fcfs;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/fcfs")
public class FcfsController {

    @PostMapping("/events")
    public ResponseEntity<Void> createEvent(@RequestBody CreateEventRequest request) {
        // TODO: 이벤트 생성 (제목, 총 재고, 오픈/마감 시간)
        return ResponseEntity.ok().build();
    }

    @GetMapping("/events/{eventId}")
    public ResponseEntity<Void> getEvent(@PathVariable Long eventId) {
        // TODO: 이벤트 상세 조회 (잔여 재고 포함)
        return ResponseEntity.ok().build();
    }

    @PostMapping("/events/{eventId}/reserve")
    public ResponseEntity<Void> reserve(@PathVariable Long eventId,
                                        @RequestBody ReserveRequest request) {
        // TODO: 선착순 예매 요청
        return ResponseEntity.ok().build();
    }

    @GetMapping("/events/{eventId}/reservations")
    public ResponseEntity<Void> getReservations(@PathVariable Long eventId) {
        // TODO: 이벤트별 예매 목록 조회
        return ResponseEntity.ok().build();
    }

    record CreateEventRequest(String title, int totalStock, String openAt, String closeAt) {}
    record ReserveRequest(Long userId) {}
}
