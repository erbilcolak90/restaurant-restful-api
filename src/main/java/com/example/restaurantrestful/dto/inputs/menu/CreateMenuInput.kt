package com.example.restaurantrestful.dto.inputs.menu

data class CreateMenuInput(

        var name: String,
        var productIds: MutableList<String>?
)
