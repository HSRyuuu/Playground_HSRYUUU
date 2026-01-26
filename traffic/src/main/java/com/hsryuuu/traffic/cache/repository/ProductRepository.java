package com.hsryuuu.traffic.cache.repository;

import com.hsryuuu.traffic.cache.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findTop10ByOrderByViewCountDesc();
}
