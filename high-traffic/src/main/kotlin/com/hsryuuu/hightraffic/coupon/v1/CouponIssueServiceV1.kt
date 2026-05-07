package com.hsryuuu.hightraffic.coupon.v1

import com.hsryuuu.hightraffic.common.exception.GlobalException
import com.hsryuuu.hightraffic.coupon.CouponIssueService
import com.hsryuuu.hightraffic.coupon.entity.CouponIssue
import com.hsryuuu.hightraffic.coupon.repository.CouponIssueRepository
import com.hsryuuu.hightraffic.coupon.repository.CouponRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * V1: 동시성 제어 없는 단순 JPA 구현 (1단계 baseline).
 * race condition으로 over-issue / lost update가 발생하는 것을 재현하는 용도.
 */
@Service
class CouponIssueServiceV1(
    private val couponRepository: CouponRepository,
    private val couponIssueRepository: CouponIssueRepository,
) : CouponIssueService {

    @Transactional
    override fun issueCoupon(couponId: Long, userId: Long) {
        if (couponIssueRepository.existsByCouponIdAndUserId(couponId, userId)) {
            throw GlobalException(HttpStatus.CONFLICT, "이미 발급받은 유저입니다. ")
        }
        val coupon = couponRepository.findById(couponId)
            .orElseThrow { GlobalException(HttpStatus.NOT_FOUND, "존재하지 않는 쿠폰입니다.") }

        if (coupon.isSoldOut) {
            throw GlobalException(HttpStatus.CONFLICT, "SOLD OUT")
        }

        coupon.issue()
        couponIssueRepository.save(CouponIssue(couponId = couponId, userId = userId))
    }
}