package com.hsryuuu.traffic.queue.repository;

import com.hsryuuu.traffic.queue.entity.QueueEntry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QueueEntryRepository extends JpaRepository<QueueEntry, Long> {
}
