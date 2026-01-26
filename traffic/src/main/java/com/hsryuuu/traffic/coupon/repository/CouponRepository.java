package com.hsryuuu.traffic.coupon.repository;

import com.hsryuuu.traffic.coupon.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CouponRepository extends JpaRepository<Coupon, Long> {

    boolean existsByCouponEventIdAndUserId(Long couponEventId, Long userId);

    List<Coupon> findAllByUserId(Long userId);
}
