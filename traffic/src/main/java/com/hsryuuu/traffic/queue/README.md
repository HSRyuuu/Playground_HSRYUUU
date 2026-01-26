# 대기열 (Virtual Queue)

## 상황
콘서트 티켓 오픈, 수강 신청 등 특정 시점에 수만 명이 동시 접속하는 상황.
서버가 감당할 수 있는 처리량만큼만 순서대로 입장시켜 시스템을 보호한다.

## 사용 기술
- Redis Sorted Set (ZADD, ZRANK, ZRANGE)
- SSE 또는 Polling (대기 순번 실시간 조회)
- Spring Scheduler (주기적 입장 처리)

## 구현 내용

> TODO
