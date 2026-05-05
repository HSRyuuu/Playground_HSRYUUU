package com.hsryuuu.hightraffic.coupon.service

interface CouponIssueService {

    fun issueCoupon(couponId: Long, userId: Long)
}
