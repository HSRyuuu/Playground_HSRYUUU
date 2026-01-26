package com.hsryuuu.traffic.fcfs.reservation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/fcfs/reservations")
@RequiredArgsConstructor
public class FcfsReservationController {

    private final FcfsReservationService fcfsReservationService;

    @PostMapping
    public ResponseEntity<Map<String, Object>> reserve(@RequestBody ReserveRequest request) {
        try {
            FcfsReservation reservation = fcfsReservationService.reserve(request.eventId(), request.userId());

            return ResponseEntity.ok(Map.of(
                    "reservationId", reservation.getId(),
                    "status", reservation.getStatus().name(),
                    "userId", reservation.getUserId()
            ));
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/strategy")
    public ResponseEntity<Map<String, Object>> activeStrategy() {
        return ResponseEntity.ok(Map.of("strategy", fcfsReservationService.activeStrategy()));
    }

    record ReserveRequest(Long eventId, Long userId) {}
}
