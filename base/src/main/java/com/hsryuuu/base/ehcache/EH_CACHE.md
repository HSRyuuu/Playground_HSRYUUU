# Spring EHCache

### gradle.kts
```groovy
implementation("org.springframework.boot:spring-boot-starter-cache")
implementation("org.ehcache:ehcache:3.10.8:jakarta")  // Jakarta EE 버전
```

### Config  
#### EhCacheConfig.java
- 여러개의 EhCacheConfigurer를 등록한다.

#### EhCacheConfigurer.java
- Cache 종류에 따라 구현해서 bean 등록
- (`DefaultCacheConfigurer.java`, `ReferenceDataCacheConfigurer.java`)

#### EhCacheType.java
- Cache 종류가 추가될때마다 enum하나 추가, `EhCacheConfigurer` 구현

### Logger
`EhCacheEventLogger.java`
- Cache 이벤트 발생시 로깅
- 등록
```java
  public static CacheEventListenerConfigurationBuilder defaultListenerBuilder =
      CacheEventListenerConfigurationBuilder
          .newEventListenerConfiguration(
              new EhCacheEventLogger(), // logger 등록
              EventType.CREATED, EventType.UPDATED, EventType.EXPIRED, EventType.REMOVED,
              EventType.EVICTED
          )
          .unordered()
          .asynchronous();
```

### EhCacheManager
- Cache 직접 조작(삭제 등) 시 사용

