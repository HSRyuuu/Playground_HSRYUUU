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
│   ├── common/                  # HealthCheck 등 공통
│   └── fcfs/                    # 선착순 예매 (Redis Lua + JPA)
├── infra/
│   └── docker-compose.yml       # PostgreSQL, Redis, Kafka, Kafka UI, Redis Insight
└── k6/
    ├── test.js                  # Health Check 부하테스트
    └── fcfs-test.js             # 선착순 예매 부하테스트
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
# Health Check
k6 run k6/test.js

# 선착순 예매
k6 run k6/fcfs-test.js
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
| `fcfs` | 선착순 예매 | Redis Lua Script, JPA |
