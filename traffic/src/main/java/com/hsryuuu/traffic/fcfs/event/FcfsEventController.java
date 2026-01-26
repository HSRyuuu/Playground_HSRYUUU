package com.hsryuuu.traffic.fcfs.event;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/fcfs/events")
@RequiredArgsConstructor
public class FcfsEventController {

    private final FcfsEventService fcfsEventService;

    @PostMapping
    public ResponseEntity<Map<String, Object>> create(@RequestBody CreateRequest request) {
        FcfsEvent event = fcfsEventService.create(
                request.title(),
                request.totalStock(),
                request.openAt(),
                request.closeAt()
        );

        return ResponseEntity.ok(Map.of(
                "id", event.getId(),
                "title", event.getTitle(),
                "totalStock", event.getTotalStock(),
                "openAt", event.getOpenAt().toString(),
                "closeAt", event.getCloseAt().toString()
        ));
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<Map<String, Object>> get(@PathVariable Long eventId) {
        FcfsEvent event = fcfsEventService.getEvent(eventId);

        return ResponseEntity.ok(Map.of(
                "id", event.getId(),
                "title", event.getTitle(),
                "totalStock", event.getTotalStock(),
                "remainingStock", event.getRemainingStock(),
                "openAt", event.getOpenAt().toString(),
                "closeAt", event.getCloseAt().toString()
        ));
    }

    record CreateRequest(String title, int totalStock, LocalDateTime openAt, LocalDateTime closeAt) {}
}
