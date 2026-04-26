package com.hsryuuu.hightraffic.coupon.dto

data class CouponStatusResponse(
    val couponId: Long,
    val name: String,
    val totalQuantity: Int,
    val issuedQuantity: Int,
)
