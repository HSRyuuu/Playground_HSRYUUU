# 이벤트 스트리밍 (Event Driven)

## 개요
주문→결제→알림 흐름을 비동기 이벤트 기반으로 처리하여 시스템 간 결합도를 낮춘다.

## 핵심 요구사항
- 주문을 생성하면 이벤트가 발행된다
- 결제 서비스가 이벤트를 소비하여 결제를 처리한다
- 주문 상태를 실시간으로 조회할 수 있다
- 이벤트 발행/소비 로그를 조회할 수 있다

## 핵심 기술
- Kafka (Producer/Consumer)
- Transactional Outbox 패턴
- 멱등성 보장, 순서 보장

## 비교 전략
- v1: 동기 호출 (REST)
- v2: Kafka 비동기 이벤트
- v3: Transactional Outbox 패턴
