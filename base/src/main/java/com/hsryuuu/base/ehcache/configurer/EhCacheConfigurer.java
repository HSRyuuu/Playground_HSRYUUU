package com.hsryuuu.base.ehcache.configurer;

import javax.cache.CacheManager;

public interface EhCacheConfigurer {

  void configure(CacheManager cacheManager);
}
