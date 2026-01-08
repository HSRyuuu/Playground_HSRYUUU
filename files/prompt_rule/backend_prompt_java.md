# 백엔드 개발 규칙 (Java/Spring/JPA/QueryDSL)

## 1. 일반적인 아키텍처 및 패키지 구조
- **도메인 중심 설계**: 로직은 도메인별로 구성됩니다.
    - `controller`: REST 컨트롤러
    - `service`: 비즈니스 로직
    - `repository`: JpaRepository 및 Custom QueryDSL 리포지토리
    - `model.{feature}.entity`: JPA 엔티티
    - `model.{feature}.dto`: 데이터 전송 객체(DTO)
    - `model.{feature}.type`: 열거형(Enums / Types)
    - `utils`: 유틸리티 클래스 및 공통 메서드

## 2. 컨트롤러(Controller) 컨벤션
- **어노테이션**: `@RestController`, `@RequiredArgsConstructor`를 기본으로 사용합니다.
- **매핑**: `@RequestMapping("/api/...")`를 사용하여 API 프리픽스를 적용합니다.
- **응답 구조**:
    - 조회 작업의 경우 JPA Entity를 직접 반환하지 않고, DTO로 변환하여 `List<Dto>` 또는 단일 `Dto`를 직접 반환합니다.
    - 저장/수정/삭제 작업의 경우 반환하지 않거나(void), 대상 Entity를 DTO로 변환하여 반환합니다.
- **파라미터 검증**: Service에서 private 메서드를 validateXxx()로 구현하고, public 메서드에서 해당 메서드를 호출하도록 합니다.

## 3. 서비스(Service) 컨벤션
- **어노테이션**: `@Service`, `@Transactional`, `@RequiredArgsConstructor`를 사용합니다.
- **트랜잭션 관리**:
    - 조회 전용 메서드에는 성능 최적화를 위해 `@Transactional(readOnly = true)`를 사용합니다.
    - 데이터 변경이 발생하는 메서드에는 `@Transactional`을 명시합니다.
- **DTO 매핑**: 엔티티와 DTO 간의 변환 로직은 서비스 레이어에서 처리하거나 DTO 생성자, 정적 팩토리 메서드를 활용합니다.
- **메서드 분리**: 반복되는 로직 또는 하나의 의미있는 값을 가지는 메서드는 private 메서드로 분리합니다.

## 4. 리포지토리(Repository) 및 QueryDSL 컨벤션
- **JpaRepository**: 단순 CRUD 및 기본 쿼리 메서드 정의 시 사용합니다.
- **QueryDSL Custom Repository**:
    - 명명 규칙: `Custom{EntityName}Repository` 형식을 따릅니다.
    - 구현: 생성자 주입을 받은 `JPAQueryFactory`를 사용합니다.
    - 프로젝션(Projection): `Projections.fields()`를 사용하여 쿼리 결과를 DTO로 매핑합니다.
    - 위치: `JpaRepository`와 동일한 `repository` 패키지에 위치시킵니다.

## 5. 엔티티(Entity) 컨벤션
- **어노테이션**: `@Entity`, `@Table`, `@NoArgsConstructor(access = AccessLevel.PROTECTED)`, `@AllArgsConstructor`를 사용합니다.
- **매핑 명시**: `@Column(name = "...")`를 사용하여 DB 컬럼명을 명시적으로 지정합니다.
- **메서드**: DTO를 인자로 받는 생성자나 `update` 메서드를 구현하여 엔티티 관리를 단순화합니다.
- **엔티티 매핑**: 단방향 매핑 @ManyToOne, @OneToOne 기본으로 사용합니다. 양방향 매핑은 개발자가 직접 관리합니다.

## 6. 모델(Model/DTO) 컨벤션
- **어노테이션**: `@Getter`, `@NoArgsConstructor`, `@AllArgsConstructor`, `@Builder`를 사용합니다.
- **변환 로직**: 엔티티를 인자로 받는 생성자를 제공하여 Entity -> DTO 변환을 용이하게 합니다.

## 7. 공통 유틸리티 및 타입
- **YNFlag**: DB의 Boolean 플래그는 `YNFlag` (Y/N) 열거형을 사용합니다.
