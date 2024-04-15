package com.example.restaurantrestful.entity

import jakarta.persistence.Entity
import jakarta.persistence.Table

@Entity
@Table(name = "orders")
data class Order @JvmOverloads constructor(

        var id: String,
        var orderProductIds: MutableList<String>?,
        var totalPrice: Double=0.0,
        var isCompleted: Boolean=false,
):BaseEntity()
