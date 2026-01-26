package com.hsryuuu.traffic.fcfs.reservation.strategy;

import com.hsryuuu.traffic.fcfs.event.FcfsEvent;
import com.hsryuuu.traffic.fcfs.event.FcfsEventRepository;
import com.hsryuuu.traffic.fcfs.reservation.FcfsReservation;
import com.hsryuuu.traffic.fcfs.reservation.FcfsReservationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * V1: DB 비관적 락 (Pessimistic Lock)
 *
 * SELECT ... FOR UPDATE로 행 잠금 → 재고 차감 → 예약 저장
 * Redis 없이 DB만으로 동시성 제어.
 */
@Slf4j
@Component
@Profile("v1")
@RequiredArgsConstructor
public class PessimisticLockStrategy implements ReservationStrategy {

    private final FcfsEventRepository fcfsEventRepository;
    private final FcfsReservationRepository fcfsReservationRepository;

    @Override
    @Transactional
    public FcfsReservation reserve(Long eventId, Long userId) {
        if (fcfsReservationRepository.existsByEventIdAndUserId(eventId, userId)) {
            throw new IllegalStateException("이미 예약한 이벤트입니다.");
        }

        FcfsEvent event = fcfsEventRepository.findByIdWithPessimisticLock(eventId)
                .orElseThrow(() -> new IllegalArgumentException("이벤트를 찾을 수 없습니다."));

        event.decreaseStock();

        FcfsReservation reservation = FcfsReservation.builder()
                .event(event)
                .userId(userId)
                .build();

        return fcfsReservationRepository.save(reservation);
    }

    @Override
    public String name() {
        return "v1-pessimistic-lock";
    }
}
