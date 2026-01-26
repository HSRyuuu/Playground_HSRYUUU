# 캐시 전략 (Cache)

## 상황
상품 상세 페이지에 초당 수만 건의 조회 요청이 몰리는 상황.
DB만으로는 감당이 안 되며, 캐시 만료 시 동시 요청이 DB로 쏠리는 Stampede를 방지해야 한다.

## 사용 기술
- Redis (Cache Aside / Look Aside 패턴)
- Distributed Lock (Cache Stampede 방지)
- Probabilistic Early Expiration (TTL 분산)

## 구현 내용

> TODO
