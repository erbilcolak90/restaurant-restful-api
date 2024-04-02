package com.example.restaurantrestful.dto.payloads

import com.example.restaurantrestful.entity.Stock
import java.util.Date

data class StockPayload @JvmOverloads constructor(
        var id: String,
        var ingredientId: String,
        var type: String,
        var unit: String,
        var quantity: Double,
        var expireDate: Date= Date()
){
    companion object{
        @JvmStatic
        fun convert(from: Stock): StockPayload?{
            return StockPayload(
                    from.id,
                    from.ingredientId,
                    from.type,
                    from.unit,
                    from.quantity,
                    from.expireDate
            )
        }
    }
}

