package com.hsryuuu.hightraffic.coupon.v4

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

@Tag(name = "쿠폰 V4", description = "선착순 쿠폰 발급 API (4단계 — TODO: V4에서 도입할 동시성 제어 도구 명시)")
@RestController
@RequestMapping("/api/v4/coupons")
class CouponControllerV4(
    private val couponIssueServiceV4: CouponIssueServiceV4,
) {

    @Operation(
        summary = "쿠폰을 발급한다 (V4: TODO)",
        description = "TODO: V4에서 적용할 동시성 제어 도구와 그 이유를 채워 넣는다.",
    )
    @PostMapping("/{couponId}/issue")
    fun issue(
        @PathVariable couponId: Long,
        @Valid @RequestBody request: IssueCouponRequest,
    ): IssueCouponResponse {
        couponIssueServiceV4.issueCoupon(couponId, request.userId)
        return IssueCouponResponse(couponId = couponId, userId = request.userId)
    }
}
