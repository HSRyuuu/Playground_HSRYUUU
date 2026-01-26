package com.hsryuuu.traffic.fcfs.event;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FcfsEventService {

    private static final String STOCK_KEY_PREFIX = "fcfs:stock:";

    private final FcfsEventRepository fcfsEventRepository;
    private final RedisTemplate<String, Long> redisTemplate;

    @Transactional
    public FcfsEvent create(String title, int totalStock, java.time.LocalDateTime openAt, java.time.LocalDateTime closeAt) {
        FcfsEvent event = FcfsEvent.builder()
                .title(title)
                .totalStock(totalStock)
                .openAt(openAt)
                .closeAt(closeAt)
                .build();

        FcfsEvent saved = fcfsEventRepository.save(event);

        // Redis에 재고 초기화
        redisTemplate.opsForValue().set(stockKey(saved.getId()), (long) totalStock);

        return saved;
    }

    @Transactional(readOnly = true)
    public FcfsEvent getEvent(Long eventId) {
        return fcfsEventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("이벤트를 찾을 수 없습니다. id=" + eventId));
    }

    public static String stockKey(Long eventId) {
        return STOCK_KEY_PREFIX + eventId;
    }
}
