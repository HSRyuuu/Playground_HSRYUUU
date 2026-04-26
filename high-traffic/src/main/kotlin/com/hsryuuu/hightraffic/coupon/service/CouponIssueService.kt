package com.hsryuuu.hightraffic.coupon.service

import com.hsryuuu.hightraffic.coupon.dto.CouponStatusResponse

interface CouponIssueService {

    fun issueCoupon(couponId: Long, userId: Long)

    fun getCouponStatus(couponId: Long): CouponStatusResponse

    fun countIssues(couponId: Long): Long
}
