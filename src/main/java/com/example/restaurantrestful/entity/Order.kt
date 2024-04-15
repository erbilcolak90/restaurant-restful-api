package com.example.restaurantrestful.entity

import jakarta.persistence.*
import org.hibernate.annotations.GenericGenerator

@Entity
@Table(name = "orders")
data class Order @JvmOverloads constructor(

        @Id
        @GeneratedValue(strategy = GenerationType.UUID)
        @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
        var id: String,
        @Column(name = "order_product_ids")
        var orderProductIds: MutableList<String>?,
        @Column(name = "total_price")
        var totalPrice: Double=0.0,
        @Column(name = "is_completed")
        var isCompleted: Boolean=false,
):BaseEntity()
