package com.hsryuuu.base.ehcache.config;

import lombok.extern.slf4j.Slf4j;
import org.ehcache.event.CacheEvent;
import org.ehcache.event.CacheEventListener;

@Slf4j
public class EhCacheEventLogger implements CacheEventListener<Object, Object> {

  @Override
  public void onEvent(CacheEvent<?, ?> event) {
    log.info("[EHCache Event] Type={}, Key={}, Old={}, New={}",
        event.getType(), event.getKey(), event.getOldValue(), event.getNewValue());
  }
}