package com.hsryuuu.hightraffic.coupon.service

import com.hsryuuu.hightraffic.coupon.entity.Coupon
import com.hsryuuu.hightraffic.coupon.repository.CouponIssueRepository
import com.hsryuuu.hightraffic.coupon.repository.CouponRepository
import com.hsryuuu.hightraffic.support.IntegrationTest
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
 * V1 (동시성 제어 없는 baseline) 동작 관찰용 단순 출력 테스트.
 * 어서션 없이 시나리오를 실행하고 race condition 결함이 어떻게 드러나는지 출력한다.
 */
@DisplayName("CouponIssueServiceV1 동시성 동작 관찰")
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
    @DisplayName("[A] 1500명 동시 발급")
    fun scenarioA() {
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
        log.info("초과 발급량            = {}", rowCount - TOTAL_QUANTITY)

        if (rowCount > TOTAL_QUANTITY) {
            log.info("[관찰] over-issue 발생 — race condition으로 한도({})를 초과해 발급되었다.", TOTAL_QUANTITY)
        } else {
            log.info("[관찰] 한도가 지켜짐 — race condition이 이번 실행에서 드러나지 않았다 (재현은 부하/스케줄링에 의존).")
        }
    }

    @Test
    @DisplayName("[B] 동일 유저 100요청 동시")
    fun scenarioB() {
        val threadCount = 100
        val sameUserId = 7777L
        val result = runConcurrently(threadCount, poolSize = 32) {
            service.issueCoupon(couponId, sameUserId)
        }
        val rowCount = couponIssueRepository.countByCouponId(couponId)

        log.info("==== [시나리오 B] threadCount={} sameUserId={} ====", threadCount, sameUserId)
        log.info("성공: {}, 실패: {}", result.success, result.failure)
        log.info("user_id={} 발급 row 수 = {}", sameUserId, rowCount)

        if (rowCount == 1L) {
            log.info("[관찰] DB UNIQUE 제약이 중복을 막아냈다 — {}건 시도 중 1건만 발급됨.", threadCount)
        } else {
            log.info("[관찰] 중복 발급 발생: row 수 = {} (UNIQUE 제약 부재 또는 별도 결함).", rowCount)
        }
    }

    @Test
    @DisplayName("[C] 5000명 동시 발급")
    fun scenarioC() {
        val threadCount = 5000
        val result = runConcurrently(threadCount, poolSize = 100) { i ->
            service.issueCoupon(couponId, (i + 1).toLong())
        }
        val rowCount = couponIssueRepository.countByCouponId(couponId)
        val coupon = couponRepository.findById(couponId).get()

        log.info("==== [시나리오 C] threadCount={} ====", threadCount)
        log.info("성공: {}, 실패: {}", result.success, result.failure)
        log.info("coupon.issuedQuantity = {}", coupon.issuedQuantity)
        log.info("coupon_issue row 수   = {}", rowCount)
        log.info("카운터 vs row 차이    = {}", rowCount - coupon.issuedQuantity)

        if (rowCount.toInt() != coupon.issuedQuantity) {
            log.info("[관찰] lost update 발생 — 카운터({})와 실제 row 수({})가 어긋났다.", coupon.issuedQuantity, rowCount)
        } else {
            log.info("[관찰] 카운터와 row 수가 일치 — lost update가 이번 실행에서 드러나지 않았다.")
        }
    }

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
