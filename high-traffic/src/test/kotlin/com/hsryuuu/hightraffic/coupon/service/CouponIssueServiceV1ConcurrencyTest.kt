package com.hsryuuu.hightraffic.coupon.service

import com.hsryuuu.hightraffic.coupon.entity.Coupon
import com.hsryuuu.hightraffic.coupon.repository.CouponIssueRepository
import com.hsryuuu.hightraffic.coupon.repository.CouponRepository
import com.hsryuuu.hightraffic.support.IntegrationTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger

/**
 * 1단계: 동시성 제어 없는 가장 단순한 JPA 구현(`CouponIssueServiceV1`)이
 * 어떤 결함을 드러내는지 직접 재현해서 보여주는 테스트.
 *
 * - 시나리오 A: 1500명 동시 발급 → R1(총 수량 ≤ 1000) 위반 재현
 * - 시나리오 B: 동일 유저 100회 동시 발급 → R2(1인 1회) 시도 충돌 관찰
 * - 시나리오 C: 5000명 동시 발급 → R4(카운터/실제 row 수 일치) 위반 재현
 */
@DisplayName("CouponIssueServiceV1 동시성 테스트 (1단계)")
class CouponIssueServiceV1ConcurrencyTest @Autowired constructor(
    private val service: CouponIssueServiceV1,
    private val couponRepository: CouponRepository,
    private val couponIssueRepository: CouponIssueRepository,
) : IntegrationTest() {

    private val log = LoggerFactory.getLogger(javaClass)
    private var couponId: Long = 0L

    @BeforeEach
    fun setup() {
        couponIssueRepository.deleteAllInBatch()
        couponRepository.deleteAllInBatch()
        val coupon = couponRepository.save(
            Coupon(name = "테스트 쿠폰", totalQuantity = TOTAL_QUANTITY),
        )
        couponId = coupon.id!!
    }

    @AfterEach
    fun cleanup() {
        couponIssueRepository.deleteAllInBatch()
        couponRepository.deleteAllInBatch()
    }

    @Test
    @DisplayName("[A] 1500명이 동시에 발급 → 1000개 한도가 깨진다")
    fun scenarioA_overIssue() {
        val threadCount = 1500

        val result = runConcurrently(threadCount, poolSize = 64) { i ->
            service.issueCoupon(couponId, (i + 1).toLong())
        }

        val rowCount = couponIssueRepository.countByCouponId(couponId)
        val coupon = couponRepository.findById(couponId).get()

        log.info("==== [시나리오 A] threadCount={} ====", threadCount)
        log.info("성공: {}, 실패: {}", result.success, result.failure)
        log.info("coupon.issuedQuantity = {}", coupon.issuedQuantity)
        log.info("coupon_issue row 수   = {}", rowCount)
        log.info("초과 발급량(over)     = {}", rowCount - TOTAL_QUANTITY)

        // 의도된 결함: race condition으로 1000개를 초과하는 발급이 발생해야 한다.
        assertThat(rowCount)
            .withFailMessage(
                "R1 위반이 재현되어야 한다 (row 수 > %d). 실제 row 수=%d. " +
                    "재현이 안 된다면 thread/pool 크기를 키우거나 HikariCP pool size를 늘려라.",
                TOTAL_QUANTITY, rowCount,
            )
            .isGreaterThan(TOTAL_QUANTITY.toLong())
    }

    @Test
    @DisplayName("[B] 동일 유저 100요청 동시 → DB UNIQUE 제약으로 1건만 발급되고 나머지는 예외")
    fun scenarioB_duplicate() {
        val threadCount = 100
        val sameUserId = 7777L

        val result = runConcurrently(threadCount, poolSize = 32) {
            service.issueCoupon(couponId, sameUserId)
        }

        val rowCount = couponIssueRepository.countByCouponId(couponId)

        log.info("==== [시나리오 B] threadCount={} sameUserId={} ====", threadCount, sameUserId)
        log.info("성공: {}, 실패: {}", result.success, result.failure)
        log.info("user_id={} 발급 row 수 = {}", sameUserId, rowCount)

        // 마지막 안전망(DB UNIQUE 제약) 덕분에 결국 1건만 살아남는다.
        assertThat(rowCount).isEqualTo(1L)

        // 그러나 다수의 시도가 충돌하며 raw 예외(DataIntegrityViolationException 등)가 던져진다.
        // → UX/응답 처리 미흡 (1단계의 또 다른 결함)
        assertThat(result.failure)
            .withFailMessage("동시 중복 시도가 예외 없이 통과되었다면 1단계 결함이 재현되지 않은 것이다.")
            .isGreaterThan(0)
    }

    @Test
    @DisplayName("[C] 5000명 동시 → coupon.issuedQuantity 와 실제 row 수가 어긋난다 (lost update)")
    fun scenarioC_counterMismatch() {
        val threadCount = 5000

        val result = runConcurrently(threadCount, poolSize = 100) { i ->
            service.issueCoupon(couponId, (i + 1).toLong())
        }

        val rowCount = couponIssueRepository.countByCouponId(couponId)
        val coupon = couponRepository.findById(couponId).get()
        val diff = rowCount - coupon.issuedQuantity

        log.info("==== [시나리오 C] threadCount={} ====", threadCount)
        log.info("성공: {}, 실패: {}", result.success, result.failure)
        log.info("coupon.issuedQuantity = {}", coupon.issuedQuantity)
        log.info("coupon_issue row 수   = {}", rowCount)
        log.info("카운터 vs row 차이    = {}", diff)

        // R4 위반: lost update로 카운터와 실제 row 수가 어긋난다.
        // (정상 시스템이라면 두 값이 동일해야 함)
        assertThat(rowCount.toInt())
            .withFailMessage(
                "lost update가 재현되어야 한다. row=%d, coupon.issuedQuantity=%d (둘이 일치하면 1단계 결함이 재현되지 않은 것).",
                rowCount, coupon.issuedQuantity,
            )
            .isNotEqualTo(coupon.issuedQuantity)
    }

    /**
     * 동시 출발 게이트(`startGate`)와 종료 동기화 게이트(`doneGate`)를 사용해
     * `threadCount`개의 작업이 거의 같은 순간에 시작되도록 강제한다.
     */
    private fun runConcurrently(
        threadCount: Int,
        poolSize: Int,
        action: (Int) -> Unit,
    ): ConcurrencyResult {
        val executor = Executors.newFixedThreadPool(poolSize)
        val startGate = CountDownLatch(1)
        val doneGate = CountDownLatch(threadCount)
        val success = AtomicInteger(0)
        val failure = AtomicInteger(0)

        try {
            repeat(threadCount) { i ->
                executor.submit {
                    try {
                        startGate.await()
                        action(i)
                        success.incrementAndGet()
                    } catch (e: Throwable) {
                        failure.incrementAndGet()
                    } finally {
                        doneGate.countDown()
                    }
                }
            }

            startGate.countDown()
            doneGate.await()
        } finally {
            executor.shutdown()
            executor.awaitTermination(60, TimeUnit.SECONDS)
        }

        return ConcurrencyResult(success.get(), failure.get())
    }

    private data class ConcurrencyResult(val success: Int, val failure: Int)

    companion object {
        private const val TOTAL_QUANTITY = 1000
    }
}
