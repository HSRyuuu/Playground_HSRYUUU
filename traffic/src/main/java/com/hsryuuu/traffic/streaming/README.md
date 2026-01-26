# 이벤트 스트리밍 (Event Driven)

## 상황
주문 → 결제 → 알림 흐름을 동기 호출하면 하나의 장애가 전체를 마비시키는 상황.
비동기 이벤트 기반으로 시스템 간 결합도를 낮추고 장애를 격리한다.

## 사용 기술
- Kafka (Producer / Consumer)
- Transactional Outbox 패턴
- 멱등성 키 (중복 처리 방지)

## 구현 내용

> TODO
