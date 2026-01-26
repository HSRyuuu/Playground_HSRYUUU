package com.hsryuuu.traffic.fcfs.entity;

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

    @Builder
    public FcfsEvent(String title, int totalStock, LocalDateTime openAt, LocalDateTime closeAt) {
        this.title = title;
        this.totalStock = totalStock;
        this.remainingStock = totalStock;
        this.openAt = openAt;
        this.closeAt = closeAt;
    }

    public boolean isSoldOut() {
        return remainingStock <= 0;
    }

    public void decreaseStock() {
        if (isSoldOut()) {
            throw new IllegalStateException("재고가 소진되었습니다.");
        }
        this.remainingStock--;
    }
}
