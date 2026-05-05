---
name: version-up
description: Use when 학습용 프로젝트에서 같은 도메인을 여러 구현(V1, V2, V3...)으로 비교 학습하려 다음 버전 분기를 새로 만들어야 할 때. 트리거 — "/version-up", "V2 만들어줘", "다음 버전 만들어", "version up", "{도메인} 다음 버전". 동시성·트래픽·DB·Redis·Kafka 같이 도구별 구현을 박제식으로 비교하는 학습 워크플로우 전용. 일반 API 버저닝이나 단순 리팩토링에는 사용하지 않는다. 스킬은 *비계*만 만들고, 새 도구 적용 같은 비즈니스 로직 변경은 사용자가 직접 작성한다.
---

# Version Up

학습 프로젝트에서 **현재 최상위 버전의 Controller·Service를 그대로 복제해서 다음 버전 분기를 만드는** 범용 스킬. 도메인(coupon, ticket, order …)에 종속되지 않는다.

## Core Principle

**기존 V{N}은 박제(불변)로 두고, V{N+1}을 별도 클래스로 새로 만든다.**
V1·V2·V3가 동시에 살아있어야 같은 부하 테스트로 직접 비교 가능하고, 코드만 봐도 진화 과정을 추적할 수 있다. Git history에 의존하지 않는 *살아있는 비교 환경*이 학습 가치의 핵심이다.

## When to Use

- "V2 만들어줘", "다음 버전 만들어", "version up"
- 동시성·트래픽 처리 학습에서 **같은 contract를 다른 도구**(DB Lock, Redis, Kafka 등)로 구현해 비교할 때
- 박제된 V{N}을 보존하고 V{N+1}부터 변경하고 싶을 때

**Don't use for:**
- 일반 비즈니스 프로젝트의 API 버저닝 (별도 패키지·모듈로)
- 단순 리팩토링 (그냥 같은 클래스 수정)
- 인터페이스·DTO·Entity의 버저닝

## Workflow

### 1. 도메인 식별
사용자 입력에서 도메인 키워드를 추출한다. 명시 없으면 묻는다:
> "어떤 도메인의 다음 버전을 만들까요? (예: coupon, ticket, order)"

### 2. 최상위 버전 탐색

```bash
find src/main/kotlin -type f \( -name "*Controller*V*.kt" -o -name "*Service*V*.kt" \) | grep -i "{domain}"
```

매칭된 파일들 중 **V{숫자}가 가장 큰 한 쌍**(Controller 1개 + Service 1개)을 템플릿으로 선정. 이름 사이에 추가 키워드(`Issue`, `Order` 등)가 들어있어도 그대로 유지한다.

예시:
- `coupon/controller/CouponControllerV1.kt` → 최상위 V1
- `coupon/service/CouponIssueServiceV1.kt` → 최상위 V1

### 3. V+1로 복제

대상 파일을 그대로 복사한 뒤 다음만 수정한다:

| 항목 | 변경 |
|---|---|
| 클래스명 | `XxxV{N}` → `XxxV{N+1}` |
| 파일명 | 동일 규칙 |
| `@RequestMapping` 경로 | `/api/v{N}/...` → `/api/v{N+1}/...` |
| Controller의 Service 주입 타입 | `XxxServiceV{N}` → `XxxServiceV{N+1}` |
| 로거(`LoggerFactory.getLogger(...)`) | 클래스명 기반이면 자동, 문자열 리터럴이면 갱신 |
| KDoc·주석 내 클래스명 참조 | 갱신 |

**복제하지 않는 것**: DTO, Entity, Repository, Service 인터페이스 — 모두 공유 자원이므로 V{N+1}도 같은 인터페이스를 그대로 구현한다.

### 4. 검증

생성 후:
- `./gradlew compileKotlin` 통과 확인
- Bean 충돌 없음 (클래스명이 다르므로 Spring이 자동으로 별개 빈으로 등록)
- 인터페이스가 있다면 V{N+1}도 동일 인터페이스 구현 유지

### 5. 사용자 안내

생성 완료 후 다음을 안내한다:
1. "V{N+1} 비계 완성. 본문은 V{N}과 동일한 상태입니다."
2. "이제 V{N+1}에 어떤 도구(DB Lock / Redis / Kafka 등)를 도입할지 **직접 구현**하세요."
3. "REQUIREMENT.md(또는 docs/)에 V{N+1} 항목 추가를 권장합니다."

## Critical Rules

1. **V{N}은 절대 수정하지 않는다.** 박제 유지가 학습 가치의 핵심.
2. **V{N+1}의 비즈니스 로직(락 도입, Redis 연동, Kafka 발행 등)은 자동 작성하지 않는다.** 스킬은 *비계*만. 새 도구 적용은 사용자가 학습하며 직접 작성한다.
3. **Endpoint 경로 갱신을 빠뜨리지 않는다.** V{N}과 V{N+1}이 같은 경로를 노리면 Spring이 ambiguous mapping으로 기동 실패한다.
4. **DTO·Entity·Repository·Interface는 복제하지 않는다.** 공유 자원이며, contract 통일이 비교 fairness의 전제다.

## Common Mistakes

| ❌ 실수 | ✅ 올바른 처리 |
|---|---|
| V{N}을 수정해서 락 추가 | V{N}은 박제. V{N+1}을 새로 만들어 거기에 락 추가 |
| Endpoint 경로를 그대로 둠 (`/api/v1/...`) | `/api/v{N+1}/...`로 갱신 |
| `@Service("v2")` 등 빈 이름 부여 | 클래스명이 다르면 자동으로 별개 빈. 불필요 |
| 비즈니스 로직(락 등)까지 자동 작성 | 비계만 생성. 본문은 사용자가 작성 |
| DTO·Entity까지 V{N+1} 복제 | 공유 자원이므로 복제 X |
| 인터페이스 새로 만들기 | 같은 인터페이스 유지 (contract 통일이 비교의 전제) |

## Red Flags - STOP

자기 검사용 시그널. 하나라도 해당하면 잠시 멈추고 재확인:

- V{N+1}을 만들면서 V{N} 파일도 같이 열고 있다 → 박제 깨질 위험
- "어차피 V2에서 락 정도는 자동으로 넣어줘도 되겠지"라는 생각 → 학습 침범
- DTO 또는 인터페이스를 복제하려는 충동 → 공유 자원 분리 위반
- Endpoint 경로를 안 바꿨는데 "지금은 일단 통과시키자" → ambiguous mapping 폭발 시한장치

## Example

```
입력: /version-up coupon

탐색 결과:
- coupon/controller/CouponControllerV1.kt (최상위)
- coupon/service/CouponIssueServiceV1.kt (최상위)

생성:
- coupon/controller/CouponControllerV2.kt
  · @RequestMapping("/api/v2/coupons")
  · 주입: CouponIssueServiceV2
  · 본문: V1과 동일

- coupon/service/CouponIssueServiceV2.kt
  · CouponIssueService 인터페이스 구현 (V1과 동일 인터페이스)
  · 본문: V1과 동일

검증: ./gradlew compileKotlin → 통과 ✓
Bean: 클래스명이 다르므로 자동으로 별개 빈

안내:
"V2 비계 완성. 본문은 V1과 동일합니다.
이제 V2에 어떤 동시성 제어(예: Pessimistic Lock, Optimistic Lock,
Redis 분산락 등)를 적용할지 직접 구현하세요.
REQUIREMENT.md에 V2 항목 추가도 권장합니다."
```

## Quick Reference

| 단계 | 동작 |
|---|---|
| 1. 도메인 추출 | 사용자 입력에서, 없으면 질문 |
| 2. 템플릿 탐색 | `find ... \( -name "*Controller*V*.kt" -o -name "*Service*V*.kt" \) | grep -i {domain}` |
| 3. 최상위 V{N} 선정 | 매칭 파일 중 가장 큰 V숫자 |
| 4. V{N+1} 복제 | 클래스명·파일명·endpoint·주입만 수정 |
| 5. 컴파일 검증 | `./gradlew compileKotlin` |
| 6. 안내 | 본문 작성·REQUIREMENT 업데이트 권유 |

본문(락·캐시·MQ 도입)은 **항상 사용자 영역**. 스킬은 비계까지만.
