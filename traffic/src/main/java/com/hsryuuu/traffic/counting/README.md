# 좋아요 / 투표 (Counting)

## 상황
인기 게시글에 초당 수만 건의 좋아요가 몰리는 상황.
실시간 카운트를 보여주면서도 DB 부하를 최소화해야 한다.

## 사용 기술
- Redis INCR / DECR (실시간 카운터)
- Redis Set (유저별 중복 방지)
- Scheduled Batch (주기적 DB Write-back)

## 구현 내용

> TODO
