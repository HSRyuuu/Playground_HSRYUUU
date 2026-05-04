# 선착순 쿠폰 발급

선착순 1,000명에게 쿠폰을 발급하는 학습용 도메인.
도메인 전체 요구사항과 단계별 로드맵은 [`REQUIREMENT.md`](./REQUIREMENT.md) 참고.

---

## 현재 단계: **1단계 — 가장 단순한 JPA 구현**

> 목표: **동시성 제어를 전혀 하지 않은 채** 발급 로직을 가장 직관적으로 짠다.
> 의도적으로 깨지는 코드를 만들고, 부하 테스트로 깨지는 모습을 직접 확인하는 것이 이 단계의 핵심이다.

### 1.1 구현 범위

`CouponIssueServiceV1` 한 개의 구현체로 처리한다.

```
issueCoupon(couponId, userId):
  1. couponRepository.findById(couponId)              // 쿠폰 조회
  2. existsByCouponIdAndUserId(couponId, userId)      // 중복 발급 체크
  3. coupon.isSoldOut 검사                            // 수량 체크
  4. coupon.issue() → save                            // issuedQuantity += 1
  5. couponIssueRepository.save(CouponIssue(...))     // 발급 레코드 생성
```

특징:

- **락 없음** — 트랜잭션 격리 수준 기본값(보통 `READ_COMMITTED`)에 의존
- **read-then-write** — 조회한 값을 바탕으로 분기 후 저장
- 안전망은 오직 `coupon_issue (coupon_id, user_id)` **DB UNIQUE 제약**뿐

### 1.2 검증해야 하는 비즈니스 규칙

| 규칙 | 1단계 검증 방법 |
|---|---|
| R1 (총 수량 ≤ 1,000) | 부하 테스트 후 `SELECT COUNT(*) FROM coupon_issue WHERE coupon_id = ?` 비교 |
| R2 (1인 1회) | 동일 `userId`로 동시에 N회 요청 → 정확히 1건만 성공 |
| R4 (정확성) | `coupon.issued_quantity` 와 `coupon_issue` row 수가 일치해야 함 |

### 1.3 예상되는 문제 (의도된 결함)

1. **수량 초과 발급 (R1 위반)**
   - `isSoldOut` 체크와 `issue()` 사이에 다른 트랜잭션이 끼어들 수 있음.
   - 결과적으로 1,000개 한도를 넘어 1,005개, 1,030개 등으로 초과 발급될 가능성.

2. **카운터-실제 발급 수 불일치 (R4 위반)**
   - `coupon.issuedQuantity`와 `coupon_issue` 실제 row 수가 어긋날 수 있음.
   - 두 update가 별개의 트랜잭션 컨텍스트에서 일어나면서 lost update 발생.

3. **중복 발급 시도의 충돌**
   - 같은 유저의 동시 요청 중 일부는 `existsBy...` 체크를 모두 통과하고 `save`에 도달.
   - DB UNIQUE 제약으로 결국 한 건만 살아남지만, 클라이언트는 **DataIntegrityViolationException**을 그대로 받게 됨 (UX/응답 처리 미흡).

### 1.4 부하 테스트 시나리오

| 시나리오 | 목적 |
|---|---|
| **A. 동시 1,500명, 각자 다른 userId** | 1,000개 한도를 초과 발급하는지 확인 (R1) |
| **B. 동시 100요청, 동일 userId 1명** | 중복 발급이 발생하는지 / 어떤 예외가 던져지는지 확인 (R2) |
| **C. 동시 5,000명** | 카운터 vs 실제 row 수 차이가 얼마나 벌어지는지 확인 (R4) |

> 부하 테스트는 JUnit + `ExecutorService` 또는 `CompletableFuture` 기반의 동시성 통합 테스트로 작성한다.
> 외부 부하 도구(JMeter / k6) 도입은 후속 단계에서 검토.

### 1.5 1단계 종료 조건

다음을 모두 충족하면 1단계를 닫고 2단계로 넘어간다.

- [ ] `CouponIssueServiceV1`의 모든 메서드 정상 구현
- [ ] 단위 테스트(단일 스레드) 시나리오 통과
- [ ] 부하 테스트 A/B/C 작성 및 실행
- [ ] **A 또는 C에서 R1/R4가 깨지는 것을 로그/DB로 재현**
- [ ] 깨진 결과를 `docs/stage-1-result.md` 또는 본 README의 1.6 절에 기록

### 1.6 결과 기록 (작성 예정)

- 실행 일시:
- 동시 요청 수 / 발급 결과:
- 초과 발급량:
- 카운터-실제 row 차이:
- 관찰 메모:

---

## 다음 단계 예고

2단계는 **애플리케이션 레벨 `synchronized` 또는 DB Pessimistic Lock** 으로 1단계의 race condition을 차단한다.
구체적인 구현/한계는 1단계 결과 분석 후 본 README의 "현재 단계" 섹션을 갱신하며 진행한다.
