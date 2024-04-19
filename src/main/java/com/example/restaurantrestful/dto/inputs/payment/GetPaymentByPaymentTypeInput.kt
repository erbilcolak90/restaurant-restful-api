package com.example.restaurantrestful.dto.inputs.payment

import com.example.restaurantrestful.enums.PaymentTypeEnums
import com.example.restaurantrestful.enums.SortBy

data class GetPaymentByPaymentTypeInput(

        var page: Int,
        var size: Int,
        var sortBy: SortBy,
        var paymentType: PaymentTypeEnums
)
