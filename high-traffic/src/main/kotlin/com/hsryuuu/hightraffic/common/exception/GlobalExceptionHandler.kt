package com.hsryuuu.hightraffic.common.exception

import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.time.Instant

@RestControllerAdvice
class GlobalExceptionHandler {

    private val log = LoggerFactory.getLogger(javaClass)

    @ExceptionHandler(GlobalException::class)
    fun handleGlobalException(ex: GlobalException): ProblemDetail {
        log.warn("GlobalException: code={}, status={}, message={}", ex.code, ex.status, ex.message)
        return buildProblemDetail(ex.status, ex.message, ex.code)
    }

    @ExceptionHandler(Exception::class)
    fun handleException(ex: Exception): ProblemDetail {
        log.error("Unhandled exception", ex)
        return buildProblemDetail(
            status = HttpStatus.INTERNAL_SERVER_ERROR,
            detail = "예기치 못한 오류가 발생했습니다.",
            code = "INTERNAL_SERVER_ERROR",
        )
    }

    private fun buildProblemDetail(
        status: HttpStatus,
        detail: String,
        code: String,
    ): ProblemDetail = ProblemDetail.forStatusAndDetail(status, detail).apply {
        title = status.reasonPhrase
        setProperty("code", code)
        setProperty("timestamp", Instant.now())
    }
}
