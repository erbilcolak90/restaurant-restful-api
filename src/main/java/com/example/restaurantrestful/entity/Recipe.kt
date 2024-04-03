package com.example.restaurantrestful.entity

import jakarta.persistence.Id
import org.springframework.data.elasticsearch.annotations.Document
import org.springframework.data.elasticsearch.annotations.Field
import org.springframework.data.elasticsearch.annotations.FieldType

@Document(indexName = "recipe")
data class Recipe @JvmOverloads constructor(

        @Id
        var id: String,
        @Field(type = FieldType.Keyword)
        var name: String,
        @Field(type = FieldType.Nested)
        var ingredientListItem: MutableList<IngredientListItem>

):BaseEntity()