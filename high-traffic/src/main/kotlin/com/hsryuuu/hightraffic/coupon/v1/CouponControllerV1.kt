package com.hsryuuu.hightraffic.coupon.v1

import com.hsryuuu.hightraffic.coupon.dto.IssueCouponRequest
import com.hsryuuu.hightraffic.coupon.dto.IssueCouponResponse
import com.hsryuuu.hightraffic.coupon.v1.CouponIssueServiceV1
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "쿠폰 V1", description = "선착순 쿠폰 발급 API (1단계 — 동시성 제어 없는 baseline)")
@RestController
@RequestMapping("/api/v1/coupons")
class CouponControllerV1(
    private val couponIssueServiceV1: CouponIssueServiceV1,
) {

    @Operation(
        summary = "쿠폰을 발급한다 (V1: 동시성 제어 없음)",
        description = "단순 JPA 구현. 동시 요청 시 race condition으로 over-issue / lost update가 발생할 수 있다.",
    )
    @PostMapping("/{couponId}/issue")
    fun issue(
        @PathVariable couponId: Long,
        @Valid @RequestBody request: IssueCouponRequest,
    ): IssueCouponResponse {
        couponIssueServiceV1.issueCoupon(couponId, request.userId)
        return IssueCouponResponse(couponId = couponId, userId = request.userId)
    }
}