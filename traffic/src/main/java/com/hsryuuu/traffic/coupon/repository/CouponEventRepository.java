package com.hsryuuu.traffic.coupon.repository;

import com.hsryuuu.traffic.coupon.entity.CouponEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponEventRepository extends JpaRepository<CouponEvent, Long> {
}
