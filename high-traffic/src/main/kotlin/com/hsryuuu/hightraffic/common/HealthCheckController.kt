package com.hsryuuu.hightraffic.common

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

@RestController
class HealthCheckController {

    @GetMapping("/health")
    fun health(): Map<String, Any> = mapOf(
        "status" to "UP",
        "timestamp" to LocalDateTime.now(),
        "service" to "high-traffic",
    )
}
