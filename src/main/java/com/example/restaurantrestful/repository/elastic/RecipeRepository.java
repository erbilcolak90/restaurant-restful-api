package com.example.restaurantrestful.repository.elastic;

import com.example.restaurantrestful.entity.Recipe;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;
import java.util.Optional;

public interface RecipeRepository extends ElasticsearchRepository<Recipe, String> {

    @Query("{\"bool\": {\"must\": [{\"nested\": {\"path\": \"ingredientListItem\",\"query\": {\"match\": {\"ingredientListItem.ingredientId\": \"?0\"}}}}]}}")
    List<Recipe> findByIngredientId(String ingredientId);

    @Query("{\"bool\":{\"must\": {\"match\": {\"name\": \"?0\"}}}}")
    Optional<Recipe> findByName(String name);

    @Query("{\"bool\": {\"must\": [{\"nested\": {\"path\": \"ingredientListItem\",\"query\": {\"match\": {\"ingredientListItem.ingredientId\": \"?0\"}}}}]}, \"_source\": [\"id\"]}")
    List<String> findRecipeIdsByIngredientId(String ingredientId);
}
