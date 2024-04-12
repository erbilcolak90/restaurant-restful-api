package com.example.restaurantrestful.dto.inputs.orderproduct

data class CreateOrderProductInput(

        var orderId: String,
        var productName: String,
        var quantity: Int

)
