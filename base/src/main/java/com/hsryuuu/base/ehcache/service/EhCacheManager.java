package com.hsryuuu.base.ehcache.service;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class EhCacheManager {

  private final CacheManager cacheManager;

  /**
   * 캐시 매니저 반환
   *
   * @return CacheManager
   */
  public CacheManager getCacheManager() {
    return cacheManager;
  }

  /**
   * 캐시 반환
   *
   * @param cacheName 캐시명 (분류) EhCacheType.name()
   * @return cache 반환
   */
  public Cache getCache(String cacheName) {
    return cacheManager.getCache(cacheName);
  }

  /**
   * 캐시 수동 조회
   *
   * @param cacheName 캐시명 (분류) EhCacheType.name()
   * @param id        캐시 id
   * @param clazz     value type
   * @param <T>       cache Value type
   * @return value
   */
  public <T> Optional<T> getCache(String cacheName, Object id, Class<T> clazz) {
    Cache cache = cacheManager.getCache(cacheName);
    if (cache == null) {
      return Optional.empty();
    }
    T value = cache.get(id, clazz);
    return Optional.ofNullable(value);
  }

  /**
   * 캐시 전체 삭제 (모든 entry 삭제)
   *
   * @param cacheName 캐시명 (분류) EhCacheType.name()
   */
  public void clearCache(String cacheName) {
    Cache cache = cacheManager.getCache(cacheName);
    if (cache != null) {
      cache.clear();
    }
  }

  /**
   * 캐시 삭제 by id
   *
   * @param cacheName 캐시명 (분류) EhCacheType.name()
   * @param id        캐시 id
   */
  public void evictCache(String cacheName, Object id) {
    Cache cache = cacheManager.getCache(cacheName);
    if (cache != null) {
      cache.evict(id);
    }
  }

  /**
   * 캐시 업데이트
   *
   * @param cacheName 캐시명 (분류) EhCacheType.name()
   * @param id        캐시 id
   * @param value     값
   */
  public void upsertCache(String cacheName, Object id, Object value) {
    Cache cache = cacheManager.getCache(cacheName);
    if (cache != null) {
      cache.put(id, value);
    }
  }


}
