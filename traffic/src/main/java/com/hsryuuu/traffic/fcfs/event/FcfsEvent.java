package com.hsryuuu.traffic.fcfs.event;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "fcfs_event")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FcfsEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private int totalStock;

    @Column(nullable = false)
    private int remainingStock;

    @Column(nullable = false)
    private LocalDateTime openAt;

    @Column(nullable = false)
    private LocalDateTime closeAt;

    private LocalDateTime createdAt;

    @Builder
    public FcfsEvent(String title, int totalStock, LocalDateTime openAt, LocalDateTime closeAt) {
        this.title = title;
        this.totalStock = totalStock;
        this.remainingStock = totalStock;
        this.openAt = openAt;
        this.closeAt = closeAt;
        this.createdAt = LocalDateTime.now();
    }

    public void decreaseStock() {
        if (this.remainingStock <= 0) {
            throw new IllegalStateException("재고가 소진되었습니다.");
        }
        this.remainingStock--;
    }

    public boolean isOpen() {
        LocalDateTime now = LocalDateTime.now();
        return !now.isBefore(openAt) && !now.isAfter(closeAt);
    }
}
