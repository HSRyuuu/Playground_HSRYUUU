package com.hsryuuu.traffic.tmp.streaming.repository;

import com.hsryuuu.traffic.tmp.streaming.entity.OrderEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderEventRepository extends JpaRepository<OrderEvent, Long> {

    List<OrderEvent> findAllByProcessedFalseOrderByCreatedAtAsc();

    List<OrderEvent> findAllByOrderIdOrderByCreatedAtAsc(Long orderId);
}
