package com.example.restaurantrestful.repository.elastic;

import com.example.restaurantrestful.entity.IngredientListItem;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface IngredientListItemRepository extends ElasticsearchRepository<IngredientListItem, String> {
}
