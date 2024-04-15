package com.example.restaurantrestful.entity

import jakarta.persistence.*
import org.hibernate.annotations.GenericGenerator

@Entity
@Table(name = "menus")
data class Menu @JvmOverloads constructor(

        @Id
        @GeneratedValue(strategy = GenerationType.UUID)
        @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
        var id: String,
        @Column(name = "name")
        var name: String
):BaseEntity()
