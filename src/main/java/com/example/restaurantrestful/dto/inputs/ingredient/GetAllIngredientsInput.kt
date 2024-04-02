package com.example.restaurantrestful.dto.inputs.ingredient

import com.example.restaurantrestful.enums.SortBy

data class GetAllIngredientsInput(
        var pageSize: Int,
        var pageNumber: Int,
        var fieldName: String?="id",
        var sortBy: SortBy
)
