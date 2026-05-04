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

## Swagger / OpenAPI 컨벤션

모든 `@RestController`는 springdoc-openapi 어노테이션으로 API 문서를 자작성한다. Swagger UI(`/swagger-ui.html`)가 곧 API 명세이므로 누락하지 않는다.

- **클래스 단위**: `@Tag(name = "<도메인 한글명>", description = "<도메인 설명>")`을 반드시 부여한다. 같은 도메인의 컨트롤러는 동일한 `name`을 공유한다 (예: `CouponController`/`AdminCouponController` → `"쿠폰"`).
- **메서드 단위**: 모든 핸들러에 `@Operation(summary = "<한 줄 요약>", description = "<상세 설명>")`을 부여한다. `summary`는 동사로 시작하는 한 문장(예: "쿠폰을 발급한다"). `summary`/`description` 외 다른 속성은 사용하지 않는다.
- `@ApiResponse`/`@ApiResponses`는 사용하지 않는다 (응답 코드 문서화는 생략).
- import는 `io.swagger.v3.oas.annotations.tags.Tag`, `io.swagger.v3.oas.annotations.Operation`만 사용한다.