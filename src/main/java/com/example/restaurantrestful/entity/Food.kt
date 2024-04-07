package com.example.restaurantrestful.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.GenericGenerator

@Entity
@Table(name = "foods")
data class Food @JvmOverloads constructor(

        @Id
        @GeneratedValue(generator = "UUID")
        @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
        var id: String,
        @Column(name = "name")
        var name: String,
        @Column(name = "recipeId")
        var recipeId: String,
        @Column(name = "is_ready")
        var isReady: Boolean=false
) : BaseEntity()
