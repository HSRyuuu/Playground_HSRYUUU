package com.hsryuuu.traffic.fcfs.repository;

import com.hsryuuu.traffic.fcfs.entity.FcfsEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FcfsEventRepository extends JpaRepository<FcfsEvent, Long> {
}
