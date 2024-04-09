package com.example.restaurantrestful.dto.inputs.product

data class UpdateProductPriceInput(
        var id: String,
        var newPrice: Double
)
