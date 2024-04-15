package com.example.restaurantrestful.dto.inputs.order

import java.time.LocalDate

data class GetAllOrdersByDateRangeInput(

        var startDate: LocalDate,
        var endDate: LocalDate
)
