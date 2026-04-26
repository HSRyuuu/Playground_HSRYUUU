# high-traffic

대용량 트래픽 처리 학습용 개인 Playground 프로젝트.
선착순 쿠폰/티켓팅 같은 동시성·트래픽 폭주 시나리오를 다양한 방식(DB Lock, Redis, Kafka 등)으로 직접 구현·비교하며 학습한다.

## 기술 스택

- **Language**: Kotlin 1.9.25 / Java 21
- **Framework**: Spring Boot 3.4.1 (Web, Data JPA, Data Redis, Kafka, Actuator, Validation)
- **ORM**: JPA + QueryDSL 5.1.0 (jakarta, kapt)
- **DB**: PostgreSQL 16
- **Cache**: Redis 7
- **MQ**: Kafka 3.8.1 (KRaft mode)
- **API Docs**: springdoc-openapi 2.8.5 (Swagger UI)
- **Build**: Gradle 8.14.3 (Kotlin DSL)
- **Test**: JUnit 5, mockito-kotlin, spring-kafka-test, H2 (test runtime)

## 프로젝트 구조

```
src/main/kotlin/com/hsryuuu/hightraffic
├── HighTrafficApplication.kt
├── common/                    # 공통 도메인/예외/엔티티
│   ├── HealthCheckController.kt
│   ├── entity/User.kt
│   └── exception/             # GlobalException, GlobalExceptionHandler
└── ticket/                    # 선착순 쿠폰/티켓 도메인
    ├── REQUIREMENT.md
    ├── controller/            # CouponController + dto
    ├── entity/                # Coupon, CouponIssue
    └── service/               # CouponIssueService
infra/docker-compose.yml       # PostgreSQL, Redis, Kafka, Kafka UI, Redis Insight
```

## 실행

```bash
# 인프라 기동
cd infra && docker-compose up -d

# 애플리케이션
./gradlew bootRun
```

| 서비스 | 포트 |
|---|---|
| App | 8081 |
| PostgreSQL | 6543 (db/user/pw: hightraffic) |
| Redis | 6379 |
| Kafka | 9092 |
| Kafka UI | 8989 |
| Redis Insight | 5540 |
| Swagger UI | http://localhost:8081/swagger-ui.html |
| Health | http://localhost:8081/health |

## 컨벤션 메모

- JPA Entity는 Kotlin `final` 회피를 위해 `allOpen` 플러그인 적용 (`@Entity`, `@MappedSuperclass`, `@Embeddable`).
- QueryDSL Q클래스는 kapt가 `build/generated/source/kapt/main`에 생성하며 sourceSets에 포함되어 있다.
- `-Xjsr305=strict`로 nullability 엄격 모드.