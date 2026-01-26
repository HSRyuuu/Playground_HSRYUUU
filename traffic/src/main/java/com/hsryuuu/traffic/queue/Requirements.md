# 대기열 (Virtual Queue)

## 개요
순간 폭주 트래픽을 대기열로 흡수하여 서버 부하를 평탄화한다.

## 핵심 요구사항
- 사용자가 대기열에 진입하면 대기번호를 발급받는다
- 실시간으로 자신의 대기 순번을 조회할 수 있다
- 서버가 처리 가능한 만큼 순서대로 N명씩 입장시킨다
- 대기열 전체 현황(총 대기 인원, 처리 속도)을 조회할 수 있다

## 핵심 기술
- Redis Sorted Set (ZADD, ZRANK, ZRANGE)
- Polling 또는 SSE (Server-Sent Events)

## 비교 전략
- v1: DB 기반 순번 관리
- v2: Redis Sorted Set
