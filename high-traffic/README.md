# high-traffic

대용량 트래픽 처리 학습용 Kotlin + Spring Boot 프로젝트.

## 기술 스택

- Kotlin 1.9.25
- Java 21
- Spring Boot 3.4.1
- JPA + QueryDSL 5.1.0 (jakarta)
- PostgreSQL 16
- Redis 7
- Kafka 3.8.1 (KRaft mode)
- Gradle 8.14.3 (Kotlin DSL)

## 인프라 실행

```bash
cd infra
docker-compose up -d
```

| 서비스 | 포트 | 용도 |
|---|---|---|
| PostgreSQL | 6543 | DB (db: hightraffic / user: hightraffic / pw: hightraffic) |
| Redis | 6379 | 캐시 |
| Kafka | 9092 | 메시지 브로커 |
| Kafka UI | 8989 | http://localhost:8989 |
| Redis Insight | 5540 | http://localhost:5540 |

## 애플리케이션 실행

```bash
./gradlew bootRun
```

- 서버: http://localhost:8081
- Health check: http://localhost:8081/health
- Swagger UI: http://localhost:8081/swagger-ui.html
