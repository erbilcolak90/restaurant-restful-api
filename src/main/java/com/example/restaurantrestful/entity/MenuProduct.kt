package com.example.restaurantrestful.entity

import jakarta.persistence.*
import org.hibernate.annotations.GenericGenerator

@Entity
@Table(name = "menu_products")
data class MenuProduct @JvmOverloads constructor(

        @Id
        @GeneratedValue(strategy = GenerationType.UUID)
        @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
        var id: String,
        @Column(name = "menu_id")
        var menuId: String,
        @Column(name = "product_id")
        var productId: String
)
