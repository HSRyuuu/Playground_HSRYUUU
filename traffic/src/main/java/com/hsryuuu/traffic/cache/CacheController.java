package com.hsryuuu.traffic.cache;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cache/products")
public class CacheController {

    @GetMapping("/{productId}")
    public ResponseEntity<Void> getProduct(@PathVariable Long productId) {
        // TODO: 상품 조회 (캐시 적용)
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{productId}")
    public ResponseEntity<Void> updateProduct(@PathVariable Long productId,
                                              @RequestBody UpdateProductRequest request) {
        // TODO: 상품 수정 + 캐시 갱신
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{productId}/cache")
    public ResponseEntity<Void> evictCache(@PathVariable Long productId) {
        // TODO: 캐시 수동 무효화
        return ResponseEntity.ok().build();
    }

    @GetMapping("/popular")
    public ResponseEntity<Void> getPopularProducts() {
        // TODO: 인기 상품 목록 조회 (캐시)
        return ResponseEntity.ok().build();
    }

    record UpdateProductRequest(String name, int price) {}
}
