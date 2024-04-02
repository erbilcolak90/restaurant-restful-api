package com.example.restaurantrestful.dto.inputs.stock

import com.example.restaurantrestful.enums.SortBy

data class GetAllStocksInput constructor(
        var pageNumber: Int,
        var pageSize: Int,
        var sortBy: SortBy,
        var fieldName: String
)
