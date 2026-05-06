package com.hsryuuu.hightraffic.coupon.v3

import com.hsryuuu.hightraffic.common.exception.GlobalException
import com.hsryuuu.hightraffic.coupon.CouponIssueService
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import java.util.concurrent.TimeUnit
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

/**
 * V3: ReentrantLock + tryLock 타임아웃으로 발급을 직렬화한 구현 (3단계).
 *
 * V2(synchronized) 대비 ReentrantLock의 핵심 차이는 다음 두 가지이며, 본 구현은 둘 다 활용한다:
 *  - tryLock(timeout): 락 대기 큐가 길게 쌓이는 상황에서 무한 대기 대신 빠르게 빠져나간다.
 *    본 구현은 2초 안에 락을 잡지 못하면 503 SERVICE_UNAVAILABLE 을 던져 백프레셔로 작동한다.
 *  - fair 옵션: 생성자 인자로 공정/비공정을 선택할 수 있다. 본 구현은 처리량을 위해 비공정(false)으로 둔다.
 *
 * 공정성 관찰 — [CouponIssueServiceV3ConcurrencyTest] 의 시나리오 A 는 userId 를 1, 2, 3, ... 순으로 증가시키며 요청한다.
 * [CouponIssueTxServiceV3] 의 `[USER {userId}] issue SUCCESS` 로그 출력 순서를 보면
 *  - fair=false (현재): userId 가 들쭉날쭉하게 찍힌다 (barging 발생).
 *  - fair=true 로 바꾸면: userId 가 입력 순서에 가깝게 회전하며 찍힌다 (FIFO 진입).
 *
 * 한계: V2와 마찬가지로 단일 JVM 락이라 다중 인스턴스 환경에서는 안전성 보장 X.
 */
@Service
class CouponIssueServiceV3(
    private val couponIssueTxService: CouponIssueTxServiceV3,
) : CouponIssueService {

    private val lock = ReentrantLock(false)

    override fun issueCoupon(couponId: Long, userId: Long) {
        if(!lock.tryLock(2000, TimeUnit.MILLISECONDS)){
            throw GlobalException(HttpStatus.SERVICE_UNAVAILABLE, "혼잡합니다. 잠시후 다시 시도하세요.")
        }
        try{
            couponIssueTxService.issue(couponId, userId)
        }finally {
            lock.unlock()
        }
    }
}
