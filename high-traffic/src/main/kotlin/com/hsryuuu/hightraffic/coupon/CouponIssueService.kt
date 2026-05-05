package com.hsryuuu.hightraffic.coupon

interface CouponIssueService {

    fun issueCoupon(couponId: Long, userId: Long)
}