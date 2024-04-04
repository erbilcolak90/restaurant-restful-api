package com.example.restaurantrestful.dto.inputs.ingredientlistitem

import com.example.restaurantrestful.enums.IngredientTypeEnums
import com.example.restaurantrestful.enums.UnitTypeEnums

data class UpdateIngredientListItemInput(

        var ingredientListItemId: String,
        var ingredientId: String?,
        var recipeId: String?,
        var type: IngredientTypeEnums?,
        var unit: UnitTypeEnums?,
        var quantity: Double?
)
