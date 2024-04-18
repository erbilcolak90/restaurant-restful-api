package com.example.restaurantrestful.dto.inputs.invoice

import java.time.LocalDate

data class GetInvoiceByDateRangeInput(

        var startDate: LocalDate,
        var endDate: LocalDate
)
