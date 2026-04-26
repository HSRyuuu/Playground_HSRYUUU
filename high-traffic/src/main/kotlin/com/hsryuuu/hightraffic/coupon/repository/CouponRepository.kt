package com.hsryuuu.hightraffic.coupon.repository

import com.hsryuuu.hightraffic.coupon.entity.Coupon
import org.springframework.data.jpa.repository.JpaRepository

interface CouponRepository : JpaRepository<Coupon, Long>
