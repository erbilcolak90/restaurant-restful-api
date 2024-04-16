package com.example.restaurantrestful.entity

import jakarta.persistence.*
import org.springframework.data.elasticsearch.annotations.Document
import org.springframework.data.elasticsearch.annotations.Field
import org.springframework.data.elasticsearch.annotations.FieldType

@Document(indexName = "menus")
data class Menu @JvmOverloads constructor(

        @Id
        var id: String,
        @Field(type = FieldType.Keyword)
        var name: String,
        @Field(type = FieldType.Nested)
        var products: List<Product>
):BaseEntity()
