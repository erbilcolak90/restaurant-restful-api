package com.example.restaurantrestful.dto.inputs.order

data class AddProductToOrderInput(

        var orderId: String,
        var productName: String,
        var quantity: Int
)
