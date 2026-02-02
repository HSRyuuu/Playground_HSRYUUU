package com.hsryuuu.traffic.tmp.coupon;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/coupons")
public class CouponController {

    @PostMapping
    public ResponseEntity<Void> createCouponEvent(@RequestBody CreateCouponEventRequest request) {
        // TODO: 쿠폰 이벤트 생성 (총 수량, 기간 설정)
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{couponEventId}/issue")
    public ResponseEntity<Void> issueCoupon(@PathVariable Long couponEventId,
                                            @RequestBody IssueCouponRequest request) {
        // TODO: 쿠폰 발급 요청 (선착순, 1인 1매)
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{couponEventId}/status")
    public ResponseEntity<Void> getCouponEventStatus(@PathVariable Long couponEventId) {
        // TODO: 쿠폰 이벤트 현황 조회 (발급 수량, 잔여 수량)
        return ResponseEntity.ok().build();
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<Void> getUserCoupons(@PathVariable Long userId) {
        // TODO: 사용자 보유 쿠폰 목록 조회
        return ResponseEntity.ok().build();
    }

    record CreateCouponEventRequest(String title, int totalQuantity, String startAt, String endAt) {}
    record IssueCouponRequest(Long userId) {}
}
