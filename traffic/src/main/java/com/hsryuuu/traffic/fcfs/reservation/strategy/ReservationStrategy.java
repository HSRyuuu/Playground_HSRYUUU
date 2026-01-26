package com.hsryuuu.traffic.fcfs.reservation.strategy;

import com.hsryuuu.traffic.fcfs.reservation.FcfsReservation;

public interface ReservationStrategy {

    FcfsReservation reserve(Long eventId, Long userId);

    String name();
}
