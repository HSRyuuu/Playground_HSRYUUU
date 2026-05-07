package com.hsryuuu.hightraffic.coupon.v4

import com.hsryuuu.hightraffic.common.exception.GlobalException
import com.hsryuuu.hightraffic.coupon.entity.CouponIssue
import com.hsryuuu.hightraffic.coupon.repository.CouponIssueRepository
import com.hsryuuu.hightraffic.coupon.repository.CouponRepository
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * V4: DB row-level PESSIMISTIC_WRITE 로 발급을 직렬화한 구현 (4단계).
 *
 * V2(synchronized) / V3(ReentrantLock) 의 JVM 락은 다중 인스턴스 환경에서 같은 쿠폰을
 * 동시에 줄 수 있다는 한계가 있다. V4 는 락 지점을 DB row 로 내려, 모든 애플리케이션
 * 인스턴스가 같은 직렬화 큐를 공유하게 한다.
 *
 * 핵심 — DB 락은 반드시 @Transactional 경계 안에서 잡고, 트랜잭션이 commit/rollback
 * 되는 시점에 함께 해제된다. 따라서 JVM 락에서 문제였던 "락은 풀렸지만 커밋은 아직"
 * 으로 인한 stale read 가 발생하지 않는다 — 자기 트랜잭션 안에서 본 값을 그대로 commit
 * 하므로 다음 트랜잭션은 항상 직전 commit 결과를 본다.
 *
 * 트레이드오프:
 *  - 다중 인스턴스 환경 안전성: V2/V3 ❌ → V4 ✓
 *  - 처리량: DB round-trip + DB 락 경합으로 JVM 락 대비 느려질 수 있음
 *  - 락 단위: coupon row 단위로 직렬화 → 같은 쿠폰의 모든 발급이 한 줄로 처리
 *
 * 한계:
 *  - PostgreSQL 기본 lock_timeout 은 무한대기. 운영에서는 lock_timeout 또는
 *    statement_timeout 설정 필요.
 *  - 동일 유저 중복 검사도 락 안에서 수행 → 점유 시간 길어짐. 처리량이 부족하면
 *    락 바깥 fast-path + (coupon_id, user_id) unique 제약으로 분리하는 안을 고려.
 *
 * `[USER {userId}] issue SUCCESS` 로그는 진입 순서를 콘솔로 관찰하기 위한 학습용 출력이다.
 */
@Service
class CouponIssueServiceV4(
    private val couponRepository: CouponRepository,
    private val couponIssueRepository: CouponIssueRepository,
) {

    private val log = LoggerFactory.getLogger(javaClass)

    @Transactional
    fun issueCoupon(couponId: Long, userId: Long) {
        val coupon = couponRepository.findByIdWithPessimisticLock(couponId)
            .orElseThrow{ GlobalException(HttpStatus.NOT_FOUND, "존재하지 않는 쿠폰입니다.") }

        if (couponIssueRepository.existsByCouponIdAndUserId(couponId, userId)) {
            throw GlobalException(HttpStatus.CONFLICT, "이미 발급받은 유저입니다. ")
        }

        if (coupon.isSoldOut) {
            throw GlobalException(HttpStatus.CONFLICT, "SOLD OUT")
        }
        coupon.issue()
        couponIssueRepository.save(CouponIssue(couponId = couponId, userId = userId))

        log.info("[USER $userId] issue SUCCESS - ${coupon.issuedQuantity}")

    }
}
