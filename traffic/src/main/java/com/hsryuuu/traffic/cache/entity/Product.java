package com.hsryuuu.traffic.cache.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "product")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int price;

    private String description;

    @Column(nullable = false)
    private long viewCount;

    @Builder
    public Product(String name, int price, String description) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.viewCount = 0;
    }

    public void update(String name, int price) {
        this.name = name;
        this.price = price;
    }

    public void incrementViewCount() {
        this.viewCount++;
    }
}
