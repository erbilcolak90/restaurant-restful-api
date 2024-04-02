package com.example.restaurantrestful.dto.inputs.stock

import com.example.restaurantrestful.enums.SortBy

data class GetStocksByIngredientIdInput(
        var pageSize: Int,
        var pageNumber: Int,
        var sortBy: SortBy,
        var fieldName: String,
        var ingredientId: String
)
