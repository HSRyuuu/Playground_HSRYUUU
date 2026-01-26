package com.hsryuuu.traffic.fcfs.reservation;

import com.hsryuuu.traffic.fcfs.reservation.strategy.ReservationStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class FcfsReservationService {

    private final ReservationStrategy reservationStrategy;

    public FcfsReservation reserve(Long eventId, Long userId) {
        log.info("[{}] 예약 요청 - eventId={}, userId={}", reservationStrategy.name(), eventId, userId);
        return reservationStrategy.reserve(eventId, userId);
    }

    public String activeStrategy() {
        return reservationStrategy.name();
    }
}
