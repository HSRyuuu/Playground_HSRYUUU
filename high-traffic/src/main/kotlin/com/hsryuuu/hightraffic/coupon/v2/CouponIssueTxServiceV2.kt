package com.hsryuuu.hightraffic.coupon.v2

import com.hsryuuu.hightraffic.common.exception.GlobalException
import com.hsryuuu.hightraffic.coupon.entity.CouponIssue
import com.hsryuuu.hightraffic.coupon.repository.CouponIssueRepository
import com.hsryuuu.hightraffic.coupon.repository.CouponRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * V2 발급의 트랜잭션 경계 담당 빈.
 *
 * [CouponIssueServiceV2]의 synchronized 블록 안에서만 호출되는 것을 전제로 한다.
 * 본 빈에 직접 `@Transactional`을 붙여 두지 않으면 락 ↔ 트랜잭션 경계가 뒤바뀌어
 * "락은 풀렸지만 커밋은 아직" 상태에서 다음 스레드가 stale read를 하는 문제가 생긴다.
 */
@Service
class CouponIssueTxServiceV2(
    private val couponRepository: CouponRepository,
    private val couponIssueRepository: CouponIssueRepository,
) {

    @Transactional
    fun issue(couponId: Long, userId: Long) {
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
