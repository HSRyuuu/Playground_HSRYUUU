package com.hsryuuu.hightraffic.coupon.v2

import com.hsryuuu.hightraffic.coupon.CouponIssueService
import org.springframework.stereotype.Service

/**
 * V2: synchronized 락으로 발급을 직렬화한 구현 (2단계).
 *
 * 락을 트랜잭션 바깥에 두기 위해 트랜잭션 로직을 [CouponIssueTxServiceV2] 빈으로 분리했다.
 * 호출 순서는 lock 획득 → TX BEGIN → ... → TX COMMIT → lock 해제 가 보장되어
 * 단일 JVM 환경에서 over-issue / lost update를 차단한다.
 *
 * 한계: 동일한 lock 객체를 공유하지 못하는 다중 인스턴스 환경에서는 안전성 보장 X.
 */
@Service
class CouponIssueServiceV2(
    private val couponIssueTxService: CouponIssueTxServiceV2,
) : CouponIssueService {

    private val lock = Any()

    override fun issueCoupon(couponId: Long, userId: Long) {
        synchronized(lock) {
            couponIssueTxService.issue(couponId, userId)
        }
    }
}
