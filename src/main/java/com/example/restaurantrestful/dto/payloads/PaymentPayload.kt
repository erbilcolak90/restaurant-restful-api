package com.example.restaurantrestful.dto.payloads

import com.example.restaurantrestful.entity.Payment
import com.example.restaurantrestful.enums.PaymentTypeEnums

data class PaymentPayload @JvmOverloads constructor(

        var id: String,
        var invoiceId: String,
        var paymentType: PaymentTypeEnums,
        var price: Double

) {
    companion object{
        @JvmStatic
        fun convert(from: Payment):PaymentPayload?{
            return PaymentPayload(
                    from.id,
                    from.invoiceId,
                    from.paymentType,
                    from.price
            )
        }
    }
}
