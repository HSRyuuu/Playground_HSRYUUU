package com.hsryuuu.traffic.fcfs.event;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface FcfsEventRepository extends JpaRepository<FcfsEvent, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT e FROM FcfsEvent e WHERE e.id = :id")
    Optional<FcfsEvent> findByIdWithPessimisticLock(Long id);
}
