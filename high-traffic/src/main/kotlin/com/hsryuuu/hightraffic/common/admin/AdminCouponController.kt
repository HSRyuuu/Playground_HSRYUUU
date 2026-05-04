package com.hsryuuu.hightraffic.common.admin

import com.hsryuuu.hightraffic.coupon.entity.Coupon
import com.hsryuuu.hightraffic.coupon.repository.CouponRepository
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Positive
import jakarta.validation.constraints.Size
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/admin/coupon")
class AdminCouponController(
    private val couponRepository: CouponRepository,
) {

    @PostMapping("/create")
    fun create(@Valid @RequestBody request: CreateCouponRequest): CreateCouponResponse {
        val saved = couponRepository.save(
            Coupon(
                name = request.name,
                totalQuantity = request.totalQuantity,
            ),
        )
        return CreateCouponResponse(
            id = saved.id!!,
            name = saved.name,
            totalQuantity = saved.totalQuantity,
            issuedQuantity = saved.issuedQuantity,
        )
    }
}

data class CreateCouponRequest(
    @field:NotBlank
    @field:Size(max = 100)
    val name: String,

    @field:Positive
    val totalQuantity: Int,
)

data class CreateCouponResponse(
    val id: Long,
    val name: String,
    val totalQuantity: Int,
    val issuedQuantity: Int,
)
