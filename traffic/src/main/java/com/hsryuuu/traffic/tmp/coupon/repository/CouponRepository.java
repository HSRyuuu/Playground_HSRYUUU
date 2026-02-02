package com.hsryuuu.traffic.tmp.coupon.repository;

import com.hsryuuu.traffic.tmp.coupon.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CouponRepository extends JpaRepository<Coupon, Long> {

    boolean existsByCouponEventIdAndUserId(Long couponEventId, Long userId);

    List<Coupon> findAllByUserId(Long userId);
}
