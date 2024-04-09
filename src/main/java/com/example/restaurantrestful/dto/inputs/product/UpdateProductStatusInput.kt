package com.example.restaurantrestful.dto.inputs.product

import com.example.restaurantrestful.enums.ProductStatusEnums

data class UpdateProductStatusInput(
        var id: String,
        var status: ProductStatusEnums
)
