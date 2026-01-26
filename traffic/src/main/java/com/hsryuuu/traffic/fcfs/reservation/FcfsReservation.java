package com.hsryuuu.traffic.fcfs.reservation;

import com.hsryuuu.traffic.fcfs.event.FcfsEvent;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "fcfs_reservation", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"event_id", "userId"})
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FcfsReservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    private FcfsEvent event;

    @Column(nullable = false)
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReservationStatus status;

    private LocalDateTime createdAt;

    @Builder
    public FcfsReservation(FcfsEvent event, Long userId) {
        this.event = event;
        this.userId = userId;
        this.status = ReservationStatus.CONFIRMED;
        this.createdAt = LocalDateTime.now();
    }

    public enum ReservationStatus {
        CONFIRMED, CANCELLED
    }
}
