package com.hsryuuu.base.ehcache.sample;

import com.hsryuuu.base.ehcache.service.EhCacheManager;
import com.hsryuuu.base.ehcache.config.EhCacheType;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SampleCacheService {

  private final EhCacheManager ehCacheManager;

  public void test() {
    System.out.println("### SAMPLE SampleCacheService");

    System.out.println("### testData 1 ###");
    String cacheName1 = EhCacheType.DEFAULT_CACHE.name();
    String cacheId1 = "test-user";
    User cacheValue1 = new User("1", "user-1");
    System.out.printf("cacheName: %s, cacheId: %s, cacheValue: %s \n", cacheName1, cacheId1,
        cacheValue1);

    System.out.println("### testData 2 ###");
    String cacheName2 = EhCacheType.REFERENCE_DATA.name();
    String cacheId2 = "test-user-map";
    HashMap<String, User> userMap = new HashMap<>();
    for (int i = 1; i <= 10; i++) {
      userMap.put(String.valueOf(i), new User("" + i, "USER-" + i));
    }
    System.out.printf("cacheName: %s, cacheId: %s, cacheValue: %s \n", cacheName2, cacheId2,
        userMap);

    System.out.println("### Cache INSERT ###");
    ehCacheManager.upsertCache(cacheName1, cacheId1, cacheValue1);
    ehCacheManager.upsertCache(cacheName2, cacheId2, userMap);

    System.out.println("### Cache GET 1 ###");
    ehCacheManager.getCache(cacheName1, cacheId1, User.class).ifPresent(System.out::println);
    System.out.println("### Cache GET 2 ###");
    ehCacheManager.getCache(cacheName2, cacheId2, HashMap.class).ifPresent(System.out::println);

    System.out.println("### Cache DELETE ###");
    ehCacheManager.evictCache(cacheName1, cacheId1);
    ehCacheManager.evictCache(cacheName2, cacheId2);

    System.out.println("### AFTER-DELETE Cache GET 1 ###");
    Optional<User> cacheAfterDelete1 = ehCacheManager.getCache(cacheName1, cacheId1, User.class);
    System.out.println(cacheAfterDelete1);
    System.out.println("### AFTER-DELETE Cache GET 2 ###");
    Optional<HashMap> cacheAfterDelete2 = ehCacheManager.getCache(cacheName2, cacheId2,
        HashMap.class);
    System.out.println(cacheAfterDelete2);

  }

  /**
   * User 조회 -> 최초 호출 시 User 생성 후 반환, Cache 등록 => ### no-cache -> 캐시 존재 시 @Cacheable 어노테이션이 캐시에서 찾아서
   * 반환 => cache HIT
   *
   * @param id
   * @return
   */
  @Cacheable(cacheNames = "DEFAULT_CACHE", key = "#id")
  public User getTestUser(String id) {
    System.out.println("### no-cache");
    return new User(id, "User-" + id);
  }

  /**
   * Map 조회 -> 최초 호출 시 User 생성 후 반환, Cache 등록 => ### no-cache -> 캐시 존재 시 @Cacheable 어노테이션이 캐시에서 찾아서
   * 반환 => cache HIT
   *
   * @param id
   * @param name
   * @return
   */
  @Cacheable(cacheNames = "REFERENCE_DATA", key = "'users'")
  public Map<String, User> getTestMap(String id, String name) {
    HashMap<String, User> map = new HashMap<>();
    for (int i = 1; i <= 10; i++) {
      map.put(String.valueOf(i), new User("" + i, "USER-" + i));
    }
    return map;
  }

  /**
   * 캐시 업데이트 -> cache key = id 의 value를 User(id, name)으로 업데이트
   *
   * @param id
   * @param name
   */
  public void update(String id, String name) {
    ehCacheManager.upsertCache(EhCacheType.DEFAULT_CACHE.name(), id, new User(id, name));
  }

  /**
   * 캐시 삭제 -> id에 해당하는 캐시 삭제
   *
   * @param id
   */
  public void delete(String id) {
    ehCacheManager.evictCache(EhCacheType.DEFAULT_CACHE.name(), id);
  }

  public record User(String id, String name) implements Serializable {

  }
}
