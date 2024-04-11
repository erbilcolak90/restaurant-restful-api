package com.example.restaurantrestful.entity

import jakarta.persistence.Entity
import jakarta.persistence.Table

@Entity
@Table(name = "orders")
data class Order @JvmOverloads constructor(

        var id: String,
        var orderIds: MutableList<String>?,
        var totalPrice: Double,
        var isCompleted: Boolean,
):BaseEntity()
