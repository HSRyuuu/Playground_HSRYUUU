package com.hsryuuu.hightraffic.coupon.repository

import com.hsryuuu.hightraffic.coupon.entity.CouponIssue
import org.springframework.data.jpa.repository.JpaRepository

interface CouponIssueRepository : JpaRepository<CouponIssue, Long> {

    fun existsByCouponIdAndUserId(couponId: Long, userId: Long): Boolean

    fun countByCouponId(couponId: Long): Long
}
