package com.example.restaurantrestful.repository.elastic;

import com.example.restaurantrestful.entity.IngredientListItem;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface IngredientListItemRepository extends ElasticsearchRepository<IngredientListItem, String> {

    @Query("{\"bool\": {\"must\": [{\"match\": {\"recipeId\": \"?0\"}}]}}")
    List<IngredientListItem> findIngredientListItemsByContainsRecipeId(String recipeId);

    @Query("{\"bool\": {\"must\": {\"match\": {\"id\": \"?0\"}, {\"match\": {\"isDeleted\": \"false\"}}}}")
    IngredientListItem findByIdAndIsDeletedFalse(String ingredientListItemId);
}
