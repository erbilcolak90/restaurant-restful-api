package com.example.restaurantrestful.entity

import com.example.restaurantrestful.enums.PaymentTypeEnums
import jakarta.persistence.Id
import org.springframework.data.elasticsearch.annotations.Document
import org.springframework.data.elasticsearch.annotations.Field
import org.springframework.data.elasticsearch.annotations.FieldType

@Document(indexName = "payments")
data class Payment @JvmOverloads constructor(

        @Id
        var id: String,
        @Field(type = FieldType.Keyword)
        var invoiceId: String,
        @Field(type = FieldType.Keyword)
        var paymentType: PaymentTypeEnums,
        @Field(type = FieldType.Double)
        var price: Double,
        @Field(type = FieldType.Boolean)
        var isCompleted: Boolean=false
): BaseEntity()
