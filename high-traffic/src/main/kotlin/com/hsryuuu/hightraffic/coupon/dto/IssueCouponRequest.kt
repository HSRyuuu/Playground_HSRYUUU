package com.hsryuuu.hightraffic.coupon.dto

import jakarta.validation.constraints.Positive

data class IssueCouponRequest(
    @field:Positive
    val userId: Long,
)
