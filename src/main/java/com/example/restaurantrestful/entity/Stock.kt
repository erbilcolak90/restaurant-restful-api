package com.example.restaurantrestful.entity

import jakarta.persistence.Entity
import jakarta.persistence.Id
import org.springframework.data.elasticsearch.annotations.Document
import org.springframework.data.elasticsearch.annotations.Field
import org.springframework.data.elasticsearch.annotations.FieldType
import java.util.*

@Entity
@Document(indexName = "stock")
data class Stock @JvmOverloads constructor(

        @Id
        var id: String,
        @Field(type = FieldType.Keyword)
        var ingredientId: String,
        @Field(type = FieldType.Keyword)
        var type: String ,
        @Field(type = FieldType.Keyword)
        var unit: String,
        @Field(type= FieldType.Double)
        var quantity: Double,
        @Field(type= FieldType.Date)
        var expireDate: Date =Date()): BaseEntity()


