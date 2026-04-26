# FCFS Basic - 선착순 예약 시스템 (기초)

## 개요
선착순 1,000명이 예약할 수 있는 시스템. 유저 ID와 성공/실패 여부만 처리한다.

## 핵심 규칙
- 총 예약 가능 인원: **1,000명**
- 한 유저당 **1회만** 예약 가능 (중복 예약 불가)
- 결과는 **성공 / 실패** 두 가지뿐

## 처리 흐름

```
[Client] --POST /fcfs/register?userId=xxx-->  [API Server]
                                                    |
                                              Redis SortedSet에 추가 (대기열 진입)
                                                    |
                                              즉시 응답: "대기열 등록 완료"
                                                    |
                                              [Scheduler] 주기적으로 대기열에서 꺼냄
                                                    |
                                         남은 자리 확인 (Redis DECR, 원자적)
                                                    |
                                    ┌───────────────┴───────────────┐
                                 자리 있음                        자리 없음
                                    |                               |
                              예약 성공 처리                    예약 실패 처리
                           (DB 저장 or Redis 저장)          (별도 처리 없음)
```

## 기술 요소

### 1. 대기열 (Redis Sorted Set)
- **key**: `fcfs:queue`
- **score**: 요청 시각 (timestamp) - 선착순 보장
- **member**: userId
- ZADD로 추가, 이미 존재하면 중복 진입 방지 (NX 옵션)

### 2. 남은 자리 카운터 (Redis)
- **key**: `fcfs:remaining`
- 초기값: 1000
- 스케줄러가 예약 처리 시 `DECR` → 0 이하면 실패
- 원자적 처리: Lua script 또는 DECR 반환값으로 판단

### 3. 스케줄러
- 일정 주기(예: 100ms ~ 1초)마다 대기열에서 꺼냄
- `ZPOPMIN`으로 score가 가장 낮은(=가장 먼저 온) 유저부터 처리
- 한 번에 N명씩 배치 처리 가능

### 4. 예약 결과 저장
- 성공한 유저: Redis Set 또는 DB에 저장
  - **key**: `fcfs:success` (Redis Set)
- 실패한 유저: 별도 저장 없음 (또는 로그만)

## API

| Method | Path | Param | 설명 |
|--------|------|-------|------|
| POST | `/fcfs/register` | `userId` (Long) | 대기열 등록 요청 |
| GET | `/fcfs/status/{userId}` | - | 예약 결과 조회 (성공/대기중/실패) |
| GET | `/fcfs/remaining` | - | 남은 자리 수 조회 |

## 예약 상태

| 상태 | 설명 |
|------|------|
| `WAITING` | 대기열에 있음, 아직 처리 안 됨 |
| `SUCCESS` | 예약 성공 |
| `FAILED` | 자리 없음으로 실패 |

## 이후 확장 포인트 (나중에)
- [ ] 동시 요청 부하 테스트 (k6, JMeter)
- [ ] 대기열 진입 시 순번 응답
- [ ] WebSocket/SSE로 결과 실시간 알림
- [ ] 이벤트 시작 시각 제어 (오픈 전 요청 차단)
- [ ] 분산 환경에서의 동시성 처리
