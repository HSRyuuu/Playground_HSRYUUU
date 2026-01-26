package com.hsryuuu.traffic.streaming.repository;

import com.hsryuuu.traffic.streaming.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
