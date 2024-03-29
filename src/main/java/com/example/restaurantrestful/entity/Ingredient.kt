package com.example.restaurantrestful.entity

import com.example.restaurantrestful.enums.IngredientTypeEnums
import com.example.restaurantrestful.enums.UnitTypeEnums
import jakarta.persistence.*
import org.hibernate.annotations.GenericGenerator

@Entity
@Table(name = "ingredients")
data class Ingredient @JvmOverloads constructor(

        @Id
        @GeneratedValue(generator = "UUID")
        @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
        var id : String,
        @Column(name = "name")
        var name: String,
        @Column(name = "type")
        @Enumerated(EnumType.STRING)
        var type: IngredientTypeEnums,
        @Column(name = "unit")
        @Enumerated(EnumType.STRING)
        var unit : UnitTypeEnums,
): BaseEntity()

