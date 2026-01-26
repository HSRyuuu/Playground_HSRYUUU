package com.hsryuuu.traffic.fcfs.reservation.strategy;

import com.hsryuuu.traffic.fcfs.event.FcfsEvent;
import com.hsryuuu.traffic.fcfs.event.FcfsEventRepository;
import com.hsryuuu.traffic.fcfs.event.FcfsEventService;
import com.hsryuuu.traffic.fcfs.reservation.FcfsReservation;
import com.hsryuuu.traffic.fcfs.reservation.FcfsReservationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * V2: Redis Lua Script
 *
 * Redis에서 원자적으로 재고 차감 후 DB 저장.
 * DB 락 없이 Redis가 동시성 제어를 담당.
 */
@Slf4j
@Component
@Profile("v2")
@RequiredArgsConstructor
public class RedisLuaStrategy implements ReservationStrategy {

    private final FcfsEventRepository fcfsEventRepository;
    private final FcfsReservationRepository fcfsReservationRepository;
    private final RedisTemplate<String, Long> redisTemplate;
    private final DefaultRedisScript<Long> stockDecrScript;

    @Override
    @Transactional
    public FcfsReservation reserve(Long eventId, Long userId) {
        if (fcfsReservationRepository.existsByEventIdAndUserId(eventId, userId)) {
            throw new IllegalStateException("이미 예약한 이벤트입니다.");
        }

        String stockKey = FcfsEventService.stockKey(eventId);
        Long remaining = redisTemplate.execute(stockDecrScript, List.of(stockKey));

        if (remaining == null || remaining < 0) {
            if (remaining != null && remaining < 0) {
                redisTemplate.opsForValue().increment(stockKey);
            }
            throw new IllegalStateException("재고가 소진되었습니다.");
        }

        FcfsEvent event = fcfsEventRepository.findById(eventId)
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
        return "v2-redis-lua";
    }
}
