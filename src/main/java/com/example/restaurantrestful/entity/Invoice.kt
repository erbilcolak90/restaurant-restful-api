package com.example.restaurantrestful.entity

import com.example.restaurantrestful.enums.PaymentTypeEnums
import jakarta.persistence.Id
import org.springframework.data.elasticsearch.annotations.Document
import org.springframework.data.elasticsearch.annotations.Field
import org.springframework.data.elasticsearch.annotations.FieldType

@Document(indexName = "invoices")
data class Invoice @JvmOverloads constructor(

        @Id
        var id: String,
        @Field(type = FieldType.Keyword)
        var orderId: String,
        @Field(type = FieldType.Double)
        var price: Double,
        @Field(type = FieldType.Nested)
        var payment: HashMap<PaymentTypeEnums,Double>?,
        @Field(type = FieldType.Boolean)
        var isCompleted: Boolean=false
): BaseEntity()
