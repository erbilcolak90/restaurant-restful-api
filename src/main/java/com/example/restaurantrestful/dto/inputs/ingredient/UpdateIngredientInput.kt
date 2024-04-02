package com.example.restaurantrestful.dto.inputs.ingredient

import com.example.restaurantrestful.enums.IngredientTypeEnums
import com.example.restaurantrestful.enums.UnitTypeEnums

data class UpdateIngredientInput(
        var id : String,
        var name:String?,
        var type: IngredientTypeEnums?,
        var unit: UnitTypeEnums?
)
