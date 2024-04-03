package com.example.restaurantrestful.repository.elastic;

import com.example.restaurantrestful.entity.Recipe;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface RecipeRepository extends ElasticsearchRepository<Recipe, String> {
}
