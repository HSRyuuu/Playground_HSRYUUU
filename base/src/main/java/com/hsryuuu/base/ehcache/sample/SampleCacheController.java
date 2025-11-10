package com.hsryuuu.base.ehcache.sample;

import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "EhCache")
@RequiredArgsConstructor
@RequestMapping("/api/sample/cache")
@RestController
public class SampleCacheController {

  private final SampleCacheService sampleCacheService;

  /**
   * test
   */
  @GetMapping("/test")
  public void test() {
    sampleCacheService.test();
  }

  /**
   * EhCacheType.DEFAULT_CACHE => key=String, value=Object 저장
   *
   * @param id key
   * @return value
   */
  @GetMapping("/default")
  public SampleCacheService.User getCacheData(@RequestParam String id) {
    return sampleCacheService.getTestUser(id);
  }

  /**
   * EhCacheType.REFERENCE_DATA => key=String, value=HashMap 저장
   *
   * @param id key
   * @return value
   */
  @GetMapping("/ref")
  public Map<String, SampleCacheService.User> getRefData(@RequestParam String id,
      @RequestParam String name) {
    return sampleCacheService.getTestMap(id, name);
  }

  /**
   * 캐시 업데이트
   *
   * @param id   key
   * @param name key에 해당하는 cache value를 name으로 update
   */
  @PutMapping
  public void updateCacheData(@RequestParam String id, @RequestParam String name) {
    sampleCacheService.update(id, name);
  }

  /**
   * 캐시 삭제
   *
   * @param id
   */
  @DeleteMapping
  public void deleteCacheData(@RequestParam String id) {
    sampleCacheService.delete(id);
  }


}
