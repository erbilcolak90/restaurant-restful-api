package com.example.restaurantrestful.dto.inputs.order

import com.example.restaurantrestful.enums.SortBy

data class GetAllOrdersInput(
        var pageSize: Int,
        var pageNumber: Int,
        var sortBy: SortBy,
        var fieldName: String
)
