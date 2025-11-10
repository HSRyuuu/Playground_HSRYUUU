package com.hsryuuu.base.ehcache.configurer;


import com.hsryuuu.base.ehcache.config.EhCacheConfigurationBuilder;
import com.hsryuuu.base.ehcache.config.EhCacheType;
import javax.cache.CacheManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DefaultCacheConfigurer implements EhCacheConfigurer {

  @Override
  public void configure(CacheManager cacheManager) {
    log.info("[EhCache] Cache configured name: {}", EhCacheType.DEFAULT_CACHE.name());
    cacheManager.createCache(EhCacheType.DEFAULT_CACHE.name(),
        EhCacheConfigurationBuilder.build(EhCacheType.DEFAULT_CACHE));

  }
}
