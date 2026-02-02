package com.hsryuuu.traffic.tmp.streaming;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/streaming/orders")
public class StreamingController {

    @PostMapping
    public ResponseEntity<Void> createOrder(@RequestBody CreateOrderRequest request) {
        // TODO: 주문 생성 + 이벤트 발행
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{orderId}/status")
    public ResponseEntity<Void> getOrderStatus(@PathVariable Long orderId) {
        // TODO: 주문 상태 조회
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{orderId}/pay")
    public ResponseEntity<Void> pay(@PathVariable Long orderId) {
        // TODO: 결제 처리 (이벤트 소비)
        return ResponseEntity.ok().build();
    }

    @GetMapping("/events")
    public ResponseEntity<Void> getEventLog() {
        // TODO: 이벤트 발행/소비 로그 조회
        return ResponseEntity.ok().build();
    }

    record CreateOrderRequest(Long userId, Long productId, int quantity) {}
}
