# Traffic Playground

대용량 트래픽 처리 패턴을 로컬 환경에서 실험하는 Lab/Playground 프로젝트.

## Tech Stack

- Java 21 + Spring Boot 3.4
- PostgreSQL 16, Redis 7, Kafka 3.8 (KRaft)
- k6 (부하테스트)
- Docker Compose (인프라)

## 프로젝트 구조

```
traffic/
├── src/main/java/com/hsryuuu/traffic/
│   ├── common/            # HealthCheck 등 공통
│   ├── fcfs/              # 선착순 예매
│   ├── queue/             # 대기열
│   ├── coupon/            # 쿠폰 발급
│   ├── counting/          # 좋아요 / 투표
│   ├── leaderboard/       # 실시간 랭킹
│   ├── cache/             # 캐시 전략
│   └── streaming/         # 이벤트 스트리밍
├── infra/
│   └── docker-compose.yml # PostgreSQL, Redis, Kafka, Kafka UI, Redis Insight
└── k6/                    # k6 부하테스트 스크립트
```

각 패키지 구조:
```
{scenario}/
├── README.md              # 상황 설명, 사용 기술
├── Requirements.md        # 요구사항 정의
├── {Scenario}Controller.java  # API 엔드포인트 (TODO)
├── entity/                # JPA 엔티티
└── repository/            # Spring Data Repository
```

## 실행 방법

```bash
# 1. 인프라 실행
docker compose -f infra/docker-compose.yml up -d

# 2. Spring Boot 실행
./gradlew bootRun

# 3. 확인
curl localhost:8080/health
open http://localhost:8080/swagger-ui.html
```

## 부하테스트

```bash
k6 run k6/test.js        # Health Check
k6 run k6/fcfs-test.js   # 선착순 예매
```

## 인프라 포트

| 서비스 | 포트 |
|--------|------|
| Spring Boot | 8080 |
| PostgreSQL | 5432 |
| Redis | 6379 |
| Kafka | 9092 |
| Kafka UI | 8989 |
| Redis Insight | 5540 |

## 예제 목록

| 패키지 | 설명 | 핵심 기술 |
|--------|------|-----------|
| `fcfs` | 선착순 예매 | Redis Lua Script, DB Pessimistic Lock |
| `queue` | 대기열 | Redis Sorted Set, SSE |
| `coupon` | 쿠폰 발급 | Redis Set, Kafka Consumer |
| `counting` | 좋아요 / 투표 | Redis INCR, Write-back Batch |
| `leaderboard` | 실시간 랭킹 | Redis Sorted Set |
| `cache` | 캐시 전략 | Cache Aside, Stampede 방지 |
| `streaming` | 이벤트 스트리밍 | Kafka, Transactional Outbox |
