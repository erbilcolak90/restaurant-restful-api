package com.example.restaurantrestful.dto.inputs.stock

import com.example.restaurantrestful.enums.IngredientTypeEnums
import com.example.restaurantrestful.enums.UnitTypeEnums
import java.util.*

data class AddIngredientToStockInput @JvmOverloads constructor(
        var ingredientId: String,
        var type: IngredientTypeEnums,
        var unit: UnitTypeEnums,
        var quantity: Double,
        var expireDate: Date = Date()
)
