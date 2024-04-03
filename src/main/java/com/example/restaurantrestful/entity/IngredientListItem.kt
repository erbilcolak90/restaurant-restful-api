package com.example.restaurantrestful.entity

import jakarta.persistence.Id
import org.springframework.data.elasticsearch.annotations.Document
import org.springframework.data.elasticsearch.annotations.Field
import org.springframework.data.elasticsearch.annotations.FieldType

@Document(indexName = "ingredient_list_item")
data class IngredientListItem @JvmOverloads constructor(

        @Id
        var id: String,
        @Field(type = FieldType.Keyword)
        var ingredientId: String,
        @Field(type = FieldType.Keyword)
        var ingredientType: String,
        @Field(type = FieldType.Keyword)
        var unit: String,
        var quantity: Double
)
