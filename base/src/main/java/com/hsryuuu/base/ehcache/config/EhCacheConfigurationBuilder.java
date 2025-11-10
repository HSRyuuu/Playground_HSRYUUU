package com.hsryuuu.base.ehcache.config;

import java.time.Duration;
import javax.cache.configuration.Configuration;
import org.ehcache.config.CacheConfiguration;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheEventListenerConfigurationBuilder;
import org.ehcache.config.builders.ExpiryPolicyBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.config.units.EntryUnit;
import org.ehcache.config.units.MemoryUnit;
import org.ehcache.event.EventType;
import org.ehcache.jsr107.Eh107Configuration;
import org.springframework.stereotype.Component;

@Component
public class EhCacheConfigurationBuilder {

  public static CacheEventListenerConfigurationBuilder defaultListenerBuilder =
      CacheEventListenerConfigurationBuilder
          .newEventListenerConfiguration(
              new EhCacheEventLogger(),
              EventType.CREATED, EventType.UPDATED, EventType.EXPIRED, EventType.REMOVED,
              EventType.EVICTED
          )
          .unordered()
          .asynchronous();

  /**
   *
   * @param heapEntrySize ex: 100_000
   * @param offHeapSize   ex) 10MB
   * @param duration      ex) Duration.ofMinutes(1L)
   */
  public static Configuration build(long heapEntrySize,
      long offHeapSize, Duration duration) {
    CacheConfiguration<Object, Object> ehCacheConfig =
        CacheConfigurationBuilder.newCacheConfigurationBuilder(
                Object.class,
                Object.class,
                ResourcePoolsBuilder.newResourcePoolsBuilder()
                    .heap(heapEntrySize, EntryUnit.ENTRIES)
                    .offheap(offHeapSize, MemoryUnit.MB)
                //.disk(100, MemoryUnit.MB, true)
            )
            .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(duration))
            .withService(defaultListenerBuilder) // 리스너 등록
            .build();

    // JCache 변환 (Eh107Configuration)
    return Eh107Configuration.fromEhcacheCacheConfiguration(ehCacheConfig);
  }

  public static Configuration build(EhCacheType ehCacheType) {
    return build(ehCacheType.getHeapEntrySize(), ehCacheType.getOffHeapSize(),
        ehCacheType.getDuration());
  }
}
