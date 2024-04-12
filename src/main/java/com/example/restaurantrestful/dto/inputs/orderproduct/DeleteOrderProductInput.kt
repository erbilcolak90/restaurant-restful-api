package com.example.restaurantrestful.dto.inputs.orderproduct

data class DeleteOrderProductInput(

        var orderId: String,
        var productName: String,
        var quantity: Int=1,
)
