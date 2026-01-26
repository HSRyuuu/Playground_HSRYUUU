package com.hsryuuu.traffic.fcfs.repository;

import com.hsryuuu.traffic.fcfs.entity.FcfsReservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FcfsReservationRepository extends JpaRepository<FcfsReservation, Long> {

    boolean existsByEventIdAndUserId(Long eventId, Long userId);

    List<FcfsReservation> findAllByEventId(Long eventId);
}
