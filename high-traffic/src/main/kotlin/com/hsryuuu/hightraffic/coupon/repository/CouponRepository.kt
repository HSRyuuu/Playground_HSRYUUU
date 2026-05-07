package com.hsryuuu.hightraffic.coupon.repository

import com.hsryuuu.hightraffic.coupon.entity.Coupon
import io.lettuce.core.dynamic.annotation.Param
import jakarta.persistence.LockModeType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Query
import java.util.Optional

interface CouponRepository : JpaRepository<Coupon, Long>{

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT c FROM Coupon c WHERE c.id = :id")
    fun findByIdWithPessimisticLock(@Param("id") id: Long): Optional<Coupon>
}
