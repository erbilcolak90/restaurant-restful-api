package com.example.restaurantrestful.dto.inputs.stock

import com.example.restaurantrestful.enums.UnitTypeEnums
import java.time.LocalDate

data class AddIngredientToStockInput @JvmOverloads constructor(
        var ingredientId: String,
        var unit: UnitTypeEnums,
        var quantity: Double,
        var expireDate: LocalDate
)
