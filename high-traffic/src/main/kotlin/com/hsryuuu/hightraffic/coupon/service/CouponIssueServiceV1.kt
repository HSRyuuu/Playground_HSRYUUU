package com.hsryuuu.hightraffic.coupon.service

import com.hsryuuu.hightraffic.common.exception.GlobalException
import com.hsryuuu.hightraffic.coupon.dto.CouponStatusResponse
import com.hsryuuu.hightraffic.coupon.entity.CouponIssue
import com.hsryuuu.hightraffic.coupon.repository.CouponIssueRepository
import com.hsryuuu.hightraffic.coupon.repository.CouponRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CouponIssueServiceV1(
    private val couponRepository: CouponRepository,
    private val couponIssueRepository: CouponIssueRepository
) : CouponIssueService {

    @Transactional
    override fun issueCoupon(couponId: Long, userId: Long) {
        if(couponIssueRepository.existsByCouponIdAndUserId(couponId, userId)){
            throw GlobalException(HttpStatus.CONFLICT, "이미 발급받은 유저입니다. ")
        }
        val coupon =
             couponRepository.findById(couponId)
                 .orElseThrow { GlobalException(HttpStatus.NOT_FOUND, "존재하지 않는 쿠폰입니다.") }

        if(coupon.isSoldOut) {
            throw GlobalException(HttpStatus.CONFLICT, "SOLD OUT")
        }

        coupon.issuedQuantity = couponIssueRepository.countByCouponId(couponId).toInt()
        coupon.issue()
        couponIssueRepository.save(CouponIssue(couponId = couponId, userId = userId))
    }

    override fun getCouponStatus(couponId: Long): CouponStatusResponse {
        val coupon =
            couponRepository.findById(couponId).orElseThrow { GlobalException(message = "존재하지 않는 쿠폰입니다.") }
        return CouponStatusResponse(couponId = couponId, name = coupon.name, totalQuantity = coupon.totalQuantity, issuedQuantity = coupon.issuedQuantity)
    }

    override fun countIssues(couponId: Long): Int {
        val coupon =
            couponRepository.findById(couponId).orElseThrow { GlobalException(HttpStatus.NOT_FOUND, "존재하지 않는 쿠폰입니다.") }
        return coupon.issuedQuantity;
    }
}
