# 실시간 랭킹 (Leaderboard)

## 개요
점수가 실시간으로 변동되는 환경에서 랭킹을 빠르게 조회하고 갱신한다.

## 핵심 요구사항
- 사용자의 점수를 등록하거나 갱신할 수 있다
- 상위 N명의 랭킹을 실시간으로 조회할 수 있다
- 특정 사용자의 현재 순위와 점수를 조회할 수 있다

## 핵심 기술
- Redis Sorted Set (ZADD, ZREVRANK, ZREVRANGE)

## 비교 전략
- v1: DB ORDER BY 쿼리
- v2: Redis Sorted Set
