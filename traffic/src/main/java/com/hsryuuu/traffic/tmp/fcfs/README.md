# 선착순 예매 (FCFS - First Come First Served)

## 상황
콘서트 티켓, 한정판 상품 등 제한된 수량을 수만 명이 동시에 요청하는 상황.
정확히 N개만 성공시키고 나머지는 즉시 실패 처리해야 한다.

## 사용 기술
- Redis Lua Script (원자적 재고 차감)
- DB Pessimistic Lock (비관적 락 비교군)
- JPA (영속화)
- Strategy 패턴 (v1/v2 전환)

## 구현 내용

> TODO
