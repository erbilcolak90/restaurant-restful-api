package com.example.restaurantrestful.dto.inputs.orderproduct

data class UpdateOrderProductQuantityInput(

        var orderId: String,
        var productName: String,
        var quantity: Int
)
