package com.example.restaurantrestful.dto.payloads

import com.example.restaurantrestful.entity.Menu
import com.example.restaurantrestful.entity.Product

data class MenuPayload @JvmOverloads constructor(

        var id: String,
        var name: String,
        var products: List<Product>?
) {
    companion object {

        @JvmStatic
        fun convert(from: Menu): MenuPayload? {
            return MenuPayload(
                    from.id,
                    from.name,
                    from.products)

        }
    }
}
