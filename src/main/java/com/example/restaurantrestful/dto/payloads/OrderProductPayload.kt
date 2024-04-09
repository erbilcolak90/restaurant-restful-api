package com.example.restaurantrestful.dto.payloads

import com.example.restaurantrestful.entity.OrderProduct

data class OrderProductPayload @JvmOverloads constructor(

        var id: String,
        var orderId: String,
        var productId: String,
        var price: Double
){
    companion object{

        @JvmStatic
        fun convert(from: OrderProduct): OrderProductPayload?{
            return OrderProductPayload(
                    from.id,
                    from.orderId,
                    from.productId,
                    from.price
            )
        }
    }
}
