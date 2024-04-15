package com.example.restaurantrestful.dto.inputs.order

data class DeleteProductFromOrderInput(

        var orderId: String,
        var productName: String,
        var quantity: Int
)
