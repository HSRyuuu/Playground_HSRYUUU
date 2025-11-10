package com.hsryuuu.base.ehcache.config;

import java.time.Duration;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EhCacheType {
  DEFAULT_CACHE(100_000, 10, Duration.ofMinutes(10L)),
  REFERENCE_DATA(10_000, 50, Duration.ofHours(1L));

  private final long heapEntrySize;
  private final long offHeapSize;
  private final Duration duration;

}
