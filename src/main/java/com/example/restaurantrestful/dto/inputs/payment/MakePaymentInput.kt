package com.example.restaurantrestful.dto.inputs.payment

import com.example.restaurantrestful.enums.PaymentTypeEnums

data class MakePaymentInput(

        var invoiceId: String,
        var paymentList : HashMap<PaymentTypeEnums, Double>
)
