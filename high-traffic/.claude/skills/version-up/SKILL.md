---
name: version-up
description: Use when 학습용 프로젝트에서 같은 도메인을 여러 구현(V1, V2, V3...)으로 비교 학습하려 다음 버전 분기를 새로 만들어야 할 때. 트리거 — "/version-up", "V2 만들어줘", "다음 버전 만들어", "version up", "{도메인} 다음 버전". 동시성·트래픽·DB·Redis·Kafka 같이 도구별 구현을 박제식으로 비교하는 학습 워크플로우 전용. 일반 API 버저닝이나 단순 리팩토링에는 사용하지 않는다. 스킬은 *비계*만 만들고, 새 도구 적용 같은 비즈니스 로직 변경은 사용자가 직접 작성한다.
---

# Version Up

학습 프로젝트에서 **현재 최상위 버전 디렉토리(`v{N}/`)를 통째로 복제해 다음 버전 분기를 만드는** 범용 스킬. 도메인(coupon, ticket, order …)에 종속되지 않는다.

## Core Principle

**기존 V{N}은 박제(불변)로 두고, V{N+1}을 별도 패키지·별도 클래스로 새로 만든다.**
V1·V2·V3가 동시에 살아있어야 같은 부하 테스트로 직접 비교 가능하고, 코드만 봐도 진화 과정을 추적할 수 있다. Git history에 의존하지 않는 *살아있는 비교 환경*이 학습 가치의 핵심이다.

## Directory Layout (전제)

도메인은 다음 구조를 따른다고 가정한다:

```
{domain}/
├── {Interface}.kt                  # 공통 contract (예: CouponIssueService)
├── {Monitor}Service.kt             # 공통 조회/모니터링 (버전 무관)
├── {Monitor}Controller.kt          # 공통 조회 엔드포인트
├── README.md / REQUIREMENT.md      # 공통 문서
├── dto/                            # 공통
├── entity/                         # 공통
├── repository/                     # 공통
├── v1/
│   ├── {Something}ControllerV1.kt
│   ├── {Something}ServiceV1.kt
│   └── (선택) {Something}TxServiceV1.kt 등
└── v2/
    └── ...
```

- **공통(도메인 루트, dto/entity/repository)**: 모든 버전이 공유. 절대 복제하지 않는다.
- **`v{N}/`**: 그 버전 전용 클래스(Controller·Service·TxService·Helper…) 모음. **이 디렉토리 단위로 통째로 복제**한다.

## When to Use

- "V2 만들어줘", "다음 버전 만들어", "version up"
- 동시성·트래픽 처리 학습에서 **같은 contract를 다른 도구**(DB Lock, Redis, Kafka 등)로 구현해 비교할 때
- 박제된 V{N}을 보존하고 V{N+1}부터 변경하고 싶을 때

**Don't use for:**
- 일반 비즈니스 프로젝트의 API 버저닝
- 단순 리팩토링 (그냥 같은 클래스 수정)
- 인터페이스·DTO·Entity·Repository의 버저닝

## Workflow

### 1. 도메인 식별
사용자 입력에서 도메인 키워드를 추출한다. 명시 없으면 묻는다:
> "어떤 도메인의 다음 버전을 만들까요? (예: coupon, ticket, order)"

### 2. 최상위 버전 디렉토리 탐색

```bash
ls -d src/main/kotlin/**/{domain}/v*/ 2>/dev/null | sort -V | tail -1
```

또는:

```bash
find src/main/kotlin -type d -path "*/{domain}/v*" | grep -E "/v[0-9]+$" | sort -V | tail -1
```

매칭된 디렉토리 중 **V{숫자}가 가장 큰 것**(예: `coupon/v2/`)을 템플릿으로 선정. 디렉토리가 하나도 없으면 사용자에게 V1을 먼저 만들어 달라고 안내한다.

### 3. V{N+1} 디렉토리 생성 및 통째로 복제

`v{N}/` 안의 **모든 `.kt` 파일을 v{N+1}/로 복사**한다. 파일이 2개든 5개든 그대로.

각 복제된 파일에서 다음을 일괄 치환:

| 항목 | 변경 |
|---|---|
| `package ...v{N}` (선언부) | `package ...v{N+1}` |
| 파일명 `*V{N}.kt` | `*V{N+1}.kt` |
| 클래스명 `XxxV{N}` | `XxxV{N+1}` |
| 클래스 간 참조 (`XxxServiceV{N}`, `XxxTxServiceV{N}` 등) | `XxxServiceV{N+1}`, `XxxTxServiceV{N+1}` |
| `@RequestMapping("/api/v{N}/...")` | `/api/v{N+1}/...` |
| Swagger `@Tag(name = "쿠폰 V{N}")` | `"쿠폰 V{N+1}"` |
| `@Tag` / `@Operation` description 안의 단계·버전 표기 (`"1단계"`, `"V{N}: ..."`) | V{N+1} 기준으로 갱신 (또는 비워두고 사용자가 채우도록 안내) |
| 로거(`LoggerFactory.getLogger(...)`) | 클래스명 기반이면 자동, 문자열 리터럴이면 갱신 |
| KDoc·주석 내 클래스명·버전 참조 | 갱신. 단, 동시성 제어 도구를 *어떻게* 바꾸는지에 대한 설명은 사용자가 직접 채운다 |

**복제하지 않는 것**:
- 도메인 루트의 공통 파일 (인터페이스, Monitor, README 등)
- `dto/`, `entity/`, `repository/` 하위 파일
- 테스트 코드 (테스트는 사용자가 V{N+1}에 맞춰 별도 작성/복제)

### 4. 검증

생성 후:
- `./gradlew compileKotlin` 통과 확인
- Bean 충돌 없음 (클래스명·패키지가 다르므로 Spring이 자동으로 별개 빈으로 등록)
- 인터페이스가 있다면 V{N+1}도 동일 인터페이스를 그대로 구현
- Endpoint 경로가 V{N}과 다른지 한 번 더 확인 (`/api/v{N+1}/...`)

### 5. 사용자 안내

생성 완료 후 다음을 안내한다:
1. "V{N+1} 비계 완성. 본문은 V{N}과 동일한 상태입니다."
2. "복제된 파일 목록: …"
3. "이제 V{N+1}에 어떤 도구(DB Lock / Redis / Kafka 등)를 도입할지 **직접 구현**하세요."
4. "테스트(`*V{N+1}ConcurrencyTest` 등)도 V{N}을 참고해 별도 작성을 권장합니다."
5. "REQUIREMENT.md(또는 docs/)에 V{N+1} 항목 추가를 권장합니다."

## Critical Rules

1. **V{N}은 절대 수정하지 않는다.** 박제 유지가 학습 가치의 핵심.
2. **V{N+1}의 비즈니스 로직(락 도입, Redis 연동, Kafka 발행 등)은 자동 작성하지 않는다.** 스킬은 *비계*만. 새 도구 적용은 사용자가 학습하며 직접 작성한다.
3. **Endpoint 경로 갱신을 빠뜨리지 않는다.** V{N}과 V{N+1}이 같은 경로를 노리면 Spring이 ambiguous mapping으로 기동 실패한다.
4. **공통 파일(인터페이스·DTO·Entity·Repository·Monitor)은 복제하지 않는다.** 공유 자원이며, contract 통일이 비교 fairness의 전제다.
5. **`v{N}/` 디렉토리 안의 모든 파일을 빠짐없이 복제한다.** TxService·Helper 같은 보조 클래스가 빠지면 V{N+1}이 컴파일조차 되지 않는다.
6. **package 선언을 반드시 `v{N+1}`로 갱신한다.** 안 그러면 같은 클래스명이 두 패키지에 분산되거나, 컴파일은 되어도 import 시 V{N}을 참조하는 사고가 난다.

## Common Mistakes

| 실수 | 올바른 처리 |
|---|---|
| V{N}을 수정해서 락 추가 | V{N}은 박제. V{N+1}을 새로 만들어 거기에 락 추가 |
| Controller·Service만 복제하고 TxService·Helper 빠뜨림 | `v{N}/` 디렉토리를 통째로 복제 |
| `package ...v{N}` 그대로 둔 채 클래스명만 변경 | package 선언도 `v{N+1}`로 갱신 |
| Endpoint 경로를 그대로 둠 (`/api/v{N}/...`) | `/api/v{N+1}/...`로 갱신 |
| `@Service("v2")` 등 빈 이름 부여 | 클래스명·패키지가 다르면 자동으로 별개 빈. 불필요 |
| 비즈니스 로직(락 등)까지 자동 작성 | 비계만 생성. 본문은 사용자가 작성 |
| DTO·Entity·Monitor까지 V{N+1} 복제 | 공유 자원이므로 복제 X |
| 인터페이스 새로 만들기 | 같은 인터페이스 유지 (contract 통일이 비교의 전제) |

## Red Flags - STOP

자기 검사용 시그널. 하나라도 해당하면 잠시 멈추고 재확인:

- V{N+1}을 만들면서 V{N} 파일도 같이 *수정*하고 있다 → 박제 깨질 위험
- "어차피 V{N+1}에서 락 정도는 자동으로 넣어줘도 되겠지"라는 생각 → 학습 침범
- DTO·인터페이스·Monitor를 복제하려는 충동 → 공유 자원 분리 위반
- Endpoint 경로를 안 바꿨는데 "지금은 일단 통과시키자" → ambiguous mapping 폭발 시한장치
- `v{N}/`에 있는 파일 중 일부만 복사하고 있다 → 보조 클래스 누락으로 컴파일 실패 예약

## Example

```
입력: /version-up coupon

탐색 결과 (최상위 V{N} 디렉토리):
- coupon/v2/
    - CouponControllerV2.kt
    - CouponIssueServiceV2.kt
    - CouponIssueTxServiceV2.kt

생성:
- coupon/v3/CouponControllerV3.kt
    · package …coupon.v3
    · @RequestMapping("/api/v3/coupons")
    · @Tag(name = "쿠폰 V3", description = "…")  ← 단계·도구 설명은 사용자가 채움
    · 주입: CouponIssueServiceV3
- coupon/v3/CouponIssueServiceV3.kt
    · package …coupon.v3
    · CouponIssueService 인터페이스 구현 (V2와 동일)
    · 주입: CouponIssueTxServiceV3
- coupon/v3/CouponIssueTxServiceV3.kt
    · package …coupon.v3
    · 본문은 V2와 동일

검증: ./gradlew compileKotlin → 통과 ✓
Bean: 클래스명·패키지가 다르므로 자동으로 별개 빈

안내:
"V3 비계 완성. 본문은 V2와 동일합니다.
복제 파일: ControllerV3, ServiceV3, TxServiceV3.
이제 V3에 어떤 동시성 제어(예: DB 비관적 락, Redis 분산 락,
Kafka 파티션 직렬화 등)를 적용할지 직접 구현하세요.
테스트와 REQUIREMENT.md에 V3 항목 추가도 권장합니다."
```

## Quick Reference

| 단계 | 동작 |
|---|---|
| 1. 도메인 추출 | 사용자 입력에서, 없으면 질문 |
| 2. 템플릿 디렉토리 탐색 | `find ... -type d -path "*/{domain}/v*"` 중 가장 큰 V숫자 |
| 3. V{N+1} 디렉토리 생성 | `mkdir .../{domain}/v{N+1}` |
| 4. 모든 `.kt` 파일 복제 | 패키지 선언·파일명·클래스명·endpoint·교차 참조 일괄 갱신 |
| 5. 컴파일 검증 | `./gradlew compileKotlin` |
| 6. 안내 | 본문 작성·테스트·REQUIREMENT 업데이트 권유 |

본문(락·캐시·MQ 도입)은 **항상 사용자 영역**. 스킬은 비계까지만.
