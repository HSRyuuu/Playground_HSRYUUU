package com.hsryuuu.hightraffic.coupon.v3

import com.hsryuuu.hightraffic.coupon.dto.IssueCouponRequest
import com.hsryuuu.hightraffic.coupon.dto.IssueCouponResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "쿠폰 V3", description = "선착순 쿠폰 발급 API (3단계 — ReentrantLock + tryLock 타임아웃 백프레셔)")
@RestController
@RequestMapping("/api/v3/coupons")
class CouponControllerV3(
    private val couponIssueServiceV3: CouponIssueServiceV3,
) {

    @Operation(
        summary = "쿠폰을 발급한다 (V3: ReentrantLock)",
        description = "ReentrantLock(비공정)으로 발급을 직렬화한다. " +
            "2초 안에 락을 획득하지 못하면 503 SERVICE_UNAVAILABLE 로 빠르게 실패시켜 백프레셔로 작동한다. " +
            "다중 인스턴스 환경에서는 V2와 마찬가지로 안전성 보장 X.",
    )
    @PostMapping("/{couponId}/issue")
    fun issue(
        @PathVariable couponId: Long,
        @Valid @RequestBody request: IssueCouponRequest,
    ): IssueCouponResponse {
        couponIssueServiceV3.issueCoupon(couponId, request.userId)
        return IssueCouponResponse(couponId = couponId, userId = request.userId)
    }
}
