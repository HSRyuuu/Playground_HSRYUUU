package com.hsryuuu.traffic.tmp.queue.repository;

import com.hsryuuu.traffic.tmp.queue.entity.QueueEntry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QueueEntryRepository extends JpaRepository<QueueEntry, Long> {
}
