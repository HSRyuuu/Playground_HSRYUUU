package com.hsryuuu.hightraffic.common.exception

import org.springframework.http.HttpStatus

open class GlobalException(
    val status: HttpStatus,
    override val message: String,
    val code: String = status.name,
    cause: Throwable? = null,
) : RuntimeException(message, cause)
