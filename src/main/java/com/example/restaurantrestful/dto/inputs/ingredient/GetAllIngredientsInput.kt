package com.example.restaurantrestful.dto.inputs.ingredient

import com.example.restaurantrestful.enums.SortBy

data class GetAllIngredientsInput(
        val pageSize: Int,
        val pageNumber: Int,
        val fieldName: String?="id",
        val sortBy: SortBy
)
