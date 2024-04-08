package com.example.restaurantrestful.entity

import com.example.restaurantrestful.enums.ProductStatusEnums
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.GenericGenerator

@Entity
@Table(name = "products")
data class Product @JvmOverloads constructor(

        @Id
        @GeneratedValue(generator = "UUID")
        @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
        var id: String,
        @Column(name = "name")
        var name: String,
        @Column(name = "food_id")
        var foodId: String,
        @Column(name = "thumbnail_id")
        var thumbnailId:String?,
        @Column(name = "price")
        var price: Double,
        @Enumerated(EnumType.STRING)
        @Column(name = "status")
        var status: ProductStatusEnums
): BaseEntity()
