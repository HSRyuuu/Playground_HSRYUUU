package com.hsryuuu.hightraffic.coupon.v2

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

@Tag(name = "쿠폰 V2", description = "선착순 쿠폰 발급 API (2단계 — 단일 JVM synchronized 락)")
@RestController
@RequestMapping("/api/v2/coupons")
class CouponControllerV2(
    private val couponIssueServiceV2: CouponIssueServiceV2,
) {

    @Operation(
        summary = "쿠폰을 발급한다 (V2: synchronized)",
        description = "단일 JVM synchronized 락으로 발급을 직렬화한다. 다중 인스턴스 환경에서는 안전성 보장 X.",
    )
    @PostMapping("/{couponId}/issue")
    fun issue(
        @PathVariable couponId: Long,
        @Valid @RequestBody request: IssueCouponRequest,
    ): IssueCouponResponse {
        couponIssueServiceV2.issueCoupon(couponId, request.userId)
        return IssueCouponResponse(couponId = couponId, userId = request.userId)
    }
}