package com.hsryuuu.hightraffic.coupon.controller

import com.hsryuuu.hightraffic.coupon.dto.CouponStatusResponse
import com.hsryuuu.hightraffic.coupon.dto.IssueCountResponse
import com.hsryuuu.hightraffic.coupon.service.CouponMonitorService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "쿠폰 모니터링", description = "쿠폰 상태 조회 API")
@RestController
@RequestMapping("/api/coupons")
class CouponMonitorController(
    private val couponMonitorService: CouponMonitorService,
) {

    @Operation(
        summary = "쿠폰 상태를 조회한다",
        description = "쿠폰의 이름, 총 수량, 발급된 수량을 반환한다.",
    )
    @GetMapping("/{couponId}")
    fun getCoupon(@PathVariable couponId: Long): CouponStatusResponse =
        couponMonitorService.getCouponStatus(couponId)

    @Operation(
        summary = "쿠폰 발급 수량을 조회한다",
        description = "현재까지 발급된 쿠폰의 누적 개수를 반환한다.",
    )
    @GetMapping("/{couponId}/issues/count")
    fun getIssuedCount(@PathVariable couponId: Long): IssueCountResponse =
        IssueCountResponse(couponId, couponMonitorService.countIssues(couponId))
}
