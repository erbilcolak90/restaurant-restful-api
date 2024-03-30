package com.example.restaurantrestful.dto.inputs.ingredient

import com.example.restaurantrestful.enums.IngredientTypeEnums
import com.example.restaurantrestful.enums.UnitTypeEnums

data class UpdateIngredientInput(
        val id : String,
        val name:String?,
        val type: IngredientTypeEnums?,
        val unit: UnitTypeEnums?
)
