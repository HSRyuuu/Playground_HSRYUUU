package com.hsryuuu.hightraffic.coupon.service

import com.hsryuuu.hightraffic.common.exception.GlobalException
import com.hsryuuu.hightraffic.coupon.dto.CouponStatusResponse
import com.hsryuuu.hightraffic.coupon.repository.CouponRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CouponMonitorService(
    private val couponRepository: CouponRepository,
) {

    @Transactional(readOnly = true)
    fun getCouponStatus(couponId: Long): CouponStatusResponse {
        val coupon = couponRepository.findById(couponId)
            .orElseThrow { GlobalException(HttpStatus.NOT_FOUND, "존재하지 않는 쿠폰입니다.") }
        return CouponStatusResponse(
            couponId = couponId,
            name = coupon.name,
            totalQuantity = coupon.totalQuantity,
            issuedQuantity = coupon.issuedQuantity,
        )
    }

    @Transactional(readOnly = true)
    fun countIssues(couponId: Long): Int {
        val coupon = couponRepository.findById(couponId)
            .orElseThrow { GlobalException(HttpStatus.NOT_FOUND, "존재하지 않는 쿠폰입니다.") }
        return coupon.issuedQuantity
    }
}
