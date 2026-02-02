package com.hsryuuu.traffic.tmp.streaming.repository;

import com.hsryuuu.traffic.tmp.streaming.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
