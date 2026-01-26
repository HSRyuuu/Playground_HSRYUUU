package com.hsryuuu.traffic.fcfs.reservation;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FcfsReservationRepository extends JpaRepository<FcfsReservation, Long> {

    boolean existsByEventIdAndUserId(Long eventId, Long userId);
}
