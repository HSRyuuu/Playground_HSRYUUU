package com.hsryuuu.hightraffic.coupon.service

import com.hsryuuu.hightraffic.common.exception.GlobalException
import com.hsryuuu.hightraffic.coupon.dto.CouponStatusResponse
import com.hsryuuu.hightraffic.coupon.repository.CouponIssueRepository
import com.hsryuuu.hightraffic.coupon.repository.CouponRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service

@Service
class CouponIssueServiceV1(
    private val couponRepository: CouponRepository,
    private val couponIssueRepository: CouponIssueRepository,
) : CouponIssueService {

    override fun issueCoupon(couponId: Long, userId: Long) {
        val coupon =
            couponRepository.findById(couponId).orElseThrow { GlobalException(HttpStatus.NOT_FOUND, "존재하지 않는 쿠폰입니다.") }
        TODO("Not yet implemented")
    }

    override fun getCouponStatus(couponId: Long): CouponStatusResponse {
        TODO("Not yet implemented")
    }

    override fun countIssues(couponId: Long): Long {
        TODO("Not yet implemented")
    }
}
