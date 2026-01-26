package com.hsryuuu.traffic.queue.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "queue_entry")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QueueEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private QueueStatus status;

    @Column(nullable = false, updatable = false)
    private LocalDateTime enteredAt;

    private LocalDateTime processedAt;

    @Builder
    public QueueEntry(Long userId) {
        this.userId = userId;
        this.status = QueueStatus.WAITING;
        this.enteredAt = LocalDateTime.now();
    }

    public void process() {
        this.status = QueueStatus.ENTERED;
        this.processedAt = LocalDateTime.now();
    }

    public enum QueueStatus {
        WAITING, ENTERED, EXPIRED
    }
}
