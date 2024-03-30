package com.example.restaurantrestful.dto.inputs.ingredient

import com.example.restaurantrestful.enums.IngredientTypeEnums
import com.example.restaurantrestful.enums.UnitTypeEnums

data class CreateIngredientInput(
        val name: String,
        val type: IngredientTypeEnums,
        val unit: UnitTypeEnums,
)
