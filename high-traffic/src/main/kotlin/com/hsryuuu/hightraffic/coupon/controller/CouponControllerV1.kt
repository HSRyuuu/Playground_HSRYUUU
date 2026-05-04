package com.hsryuuu.hightraffic.coupon.controller

import com.hsryuuu.hightraffic.coupon.dto.CouponStatusResponse
import com.hsryuuu.hightraffic.coupon.dto.IssueCouponRequest
import com.hsryuuu.hightraffic.coupon.dto.IssueCouponResponse
import com.hsryuuu.hightraffic.coupon.dto.IssueCountResponse
import com.hsryuuu.hightraffic.coupon.service.CouponIssueService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "쿠폰 V1", description = "선착순 쿠폰 발급 및 조회 API")
@RestController
@RequestMapping("/api/v1/coupons")
class CouponControllerV1(
    private val couponIssueService: CouponIssueService,
) {

    @Operation(
        summary = "쿠폰을 발급한다",
        description = "지정한 쿠폰을 사용자에게 발급한다. 재고가 소진되었거나 이미 발급받은 사용자는 실패한다.",
    )
    @PostMapping("/{couponId}/issue")
    fun issue(
        @PathVariable couponId: Long,
        @Valid @RequestBody request: IssueCouponRequest,
    ): IssueCouponResponse {
        couponIssueService.issueCoupon(couponId, request.userId)
        return IssueCouponResponse(couponId = couponId, userId = request.userId)
    }

    @Operation(
        summary = "쿠폰 상태를 조회한다",
        description = "쿠폰의 이름, 총 수량, 발급된 수량을 반환한다.",
    )
    @GetMapping("/{couponId}")
    fun getCoupon(@PathVariable couponId: Long): CouponStatusResponse =
        couponIssueService.getCouponStatus(couponId)

    @Operation(
        summary = "쿠폰 발급 수량을 조회한다",
        description = "현재까지 발급된 쿠폰의 누적 개수를 반환한다.",
    )
    @GetMapping("/{couponId}/issues/count")
    fun getIssuedCount(@PathVariable couponId: Long): IssueCountResponse =
        IssueCountResponse(couponId, couponIssueService.countIssues(couponId))
}
