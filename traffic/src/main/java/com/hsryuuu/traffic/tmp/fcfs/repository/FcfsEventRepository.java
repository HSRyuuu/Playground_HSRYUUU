package com.hsryuuu.traffic.tmp.fcfs.repository;

import com.hsryuuu.traffic.tmp.fcfs.entity.FcfsEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FcfsEventRepository extends JpaRepository<FcfsEvent, Long> {
}
