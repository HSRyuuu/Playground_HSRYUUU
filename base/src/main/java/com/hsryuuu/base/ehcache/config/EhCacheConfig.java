package com.hsryuuu.base.ehcache.config;

import com.hsryuuu.base.ehcache.configurer.EhCacheConfigurer;
import java.util.List;
import javax.cache.Caching;
import javax.cache.spi.CachingProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.jcache.JCacheCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@RequiredArgsConstructor
@EnableCaching
@Configuration
public class EhCacheConfig {

  private final List<EhCacheConfigurer> configurers;

  @Bean
  public CacheManager cacheManager() {
    CachingProvider provider = Caching.getCachingProvider(
        "org.ehcache.jsr107.EhcacheCachingProvider");
    javax.cache.CacheManager jCacheManager = provider.getCacheManager();

    // 각 cache config 등록
    configurers.forEach(configurer -> configurer.configure(jCacheManager));

    return new JCacheCacheManager(jCacheManager);
  }
}
