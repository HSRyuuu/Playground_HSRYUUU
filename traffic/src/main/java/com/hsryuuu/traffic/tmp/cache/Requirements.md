# 캐시 전략 (Cache)

## 개요
읽기 폭주 상황에서 캐시를 활용하여 DB 부하를 줄이고 응답 속도를 높인다.

## 핵심 요구사항
- 상품 상세 정보를 조회할 수 있다 (캐시 적용)
- 상품 정보 수정 시 캐시를 갱신한다
- 캐시를 수동으로 무효화할 수 있다
- 인기 상품 목록을 캐시하여 조회한다
- Cache Stampede(Thundering Herd)를 방지한다

## 핵심 기술
- Redis (Cache Aside / Look Aside)
- Cache Stampede 방지 (Lock, Probabilistic Early Expiration)

## 비교 전략
- v1: 캐시 없음 (매번 DB 조회)
- v2: Cache Aside (Redis TTL)
- v3: Cache Stampede 방지 적용
