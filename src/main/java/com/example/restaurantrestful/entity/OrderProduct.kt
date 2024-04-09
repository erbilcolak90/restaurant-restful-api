package com.example.restaurantrestful.entity

import jakarta.persistence.*
import org.hibernate.annotations.GenericGenerator

@Entity
@Table(name = "order_products")
data class OrderProduct @JvmOverloads constructor(

        @Id
        @GeneratedValue(strategy = GenerationType.UUID)
        @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
        var id: String,
        var orderId: String,
        var productId: String,
        var price: Double,
): BaseEntity()
