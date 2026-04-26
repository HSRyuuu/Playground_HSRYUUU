package com.hsryuuu.hightraffic.coupon.controller

import com.hsryuuu.hightraffic.coupon.dto.CouponStatusResponse
import com.hsryuuu.hightraffic.coupon.dto.IssueCouponRequest
import com.hsryuuu.hightraffic.coupon.dto.IssueCouponResponse
import com.hsryuuu.hightraffic.coupon.dto.IssueCountResponse
import com.hsryuuu.hightraffic.coupon.service.CouponIssueService
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/coupons")
class CouponController(
    private val couponIssueService: CouponIssueService,
) {

    @PostMapping("/{couponId}/issue")
    fun issue(
        @PathVariable couponId: Long,
        @Valid @RequestBody request: IssueCouponRequest,
    ): IssueCouponResponse {
        couponIssueService.issueCoupon(couponId, request.userId)
        return IssueCouponResponse(couponId = couponId, userId = request.userId)
    }

    @GetMapping("/{couponId}")
    fun getCoupon(@PathVariable couponId: Long): CouponStatusResponse =
        couponIssueService.getCouponStatus(couponId)

    @GetMapping("/{couponId}/issues/count")
    fun getIssuedCount(@PathVariable couponId: Long): IssueCountResponse =
        IssueCountResponse(couponId, couponIssueService.countIssues(couponId))
}
