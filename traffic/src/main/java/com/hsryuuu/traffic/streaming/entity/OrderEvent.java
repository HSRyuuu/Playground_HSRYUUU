package com.hsryuuu.traffic.streaming.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "order_event")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long orderId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EventType eventType;

    @Column(columnDefinition = "TEXT")
    private String payload;

    @Column(nullable = false)
    private boolean processed;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Builder
    public OrderEvent(Long orderId, EventType eventType, String payload) {
        this.orderId = orderId;
        this.eventType = eventType;
        this.payload = payload;
        this.processed = false;
        this.createdAt = LocalDateTime.now();
    }

    public void markProcessed() {
        this.processed = true;
    }

    public enum EventType {
        ORDER_CREATED, PAYMENT_COMPLETED, PAYMENT_FAILED, NOTIFICATION_SENT
    }
}
