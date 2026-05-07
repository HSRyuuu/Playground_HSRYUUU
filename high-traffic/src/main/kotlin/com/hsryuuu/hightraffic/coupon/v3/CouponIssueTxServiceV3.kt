package com.hsryuuu.hightraffic.coupon.v3

import com.hsryuuu.hightraffic.common.exception.GlobalException
import com.hsryuuu.hightraffic.coupon.entity.CouponIssue
import com.hsryuuu.hightraffic.coupon.repository.CouponIssueRepository
import com.hsryuuu.hightraffic.coupon.repository.CouponRepository
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * V3 발급의 트랜잭션 경계 담당 빈.
 *
 * [CouponIssueServiceV3] 의 ReentrantLock 임계 영역 안에서만 호출되는 것을 전제로 한다.
 * 락이 트랜잭션 안에 들어가면 "락은 풀렸지만 커밋은 아직" 상태에서 다음 스레드가
 * stale read 를 하므로, 락은 항상 트랜잭션 바깥에 위치해야 한다.
 *
 * `[USER {userId}] issue SUCCESS` 로그는 락이 획득된 순서를 콘솔로 직접 관찰하기 위한 학습용 출력이다 —
 * fair/unfair ReentrantLock 의 진입 순서 차이를 userId 시퀀스로 확인하는 데 쓰인다.
 */
@Service
class CouponIssueTxServiceV3(
    private val couponRepository: CouponRepository,
    private val couponIssueRepository: CouponIssueRepository,
) {

    private val log = LoggerFactory.getLogger(javaClass)

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

        log.info("[USER $userId] issue SUCCESS - ${coupon.issuedQuantity}")

    }
}
