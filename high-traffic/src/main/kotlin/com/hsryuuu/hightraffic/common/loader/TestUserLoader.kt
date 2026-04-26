package com.hsryuuu.hightraffic.common.loader

import org.slf4j.LoggerFactory
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Profile
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Component

@Component
@Profile("local", "test")
class TestUserLoader(
    private val jdbcTemplate: JdbcTemplate,
) : ApplicationRunner {

    private val log = LoggerFactory.getLogger(javaClass)

    override fun run(args: ApplicationArguments) {
        val count = jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM app_user",
            Long::class.java,
        ) ?: 0L

        if (count > 0) {
            log.info("테스트 유저 {}명 이미 존재 → skip", count)
            return
        }

        val inserted = jdbcTemplate.update(
            """
            INSERT INTO app_user (id, name, email, created_at)
            SELECT g, 'user_' || g, 'user_' || g || '@test.com', now()
            FROM generate_series(1, ?) AS g
            """.trimIndent(),
            TARGET,
        )

        // 명시 ID INSERT 후 IDENTITY 시퀀스를 max(id) 뒤로 sync — 이후 JPA INSERT 시 PK 충돌 방지
        jdbcTemplate.queryForObject(
            "SELECT setval(pg_get_serial_sequence('app_user', 'id'), ?)",
            Long::class.java,
            TARGET.toLong(),
        )

        log.info("테스트 유저 {}명 생성 완료", inserted)
    }

    companion object {
        private const val TARGET = 5000
    }
}
