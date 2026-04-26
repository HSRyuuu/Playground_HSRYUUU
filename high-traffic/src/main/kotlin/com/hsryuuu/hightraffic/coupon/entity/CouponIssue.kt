package com.hsryuuu.hightraffic.coupon.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Index
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint
import java.time.LocalDateTime

@Entity
@Table(
    name = "coupon_issue",
    uniqueConstraints = [
        UniqueConstraint(
            name = "uk_coupon_issue_coupon_user",
            columnNames = ["coupon_id", "user_id"],
        ),
    ],
    indexes = [
        Index(name = "idx_coupon_issue_coupon_id", columnList = "coupon_id"),
    ],
)
class CouponIssue(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(name = "coupon_id", nullable = false)
    val couponId: Long,

    @Column(name = "user_id", nullable = false)
    val userId: Long,

    @Column(name = "issued_at", nullable = false)
    val issuedAt: LocalDateTime = LocalDateTime.now(),


)
