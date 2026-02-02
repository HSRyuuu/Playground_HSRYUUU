# 쿠폰 발급 (Coupon)

## 상황
선착순 1만 장 한정 쿠폰 이벤트에 10만 명이 동시에 발급을 요청하는 상황.
정확히 N장만 발급하고, 1인 1매를 보장해야 한다.

## 사용 기술
- Redis Set (SADD - 중복 발급 방지)
- Redis Lua Script (원자적 수량 차감)
- Kafka Consumer (비동기 발급 처리)

## 구현 내용

> TODO
