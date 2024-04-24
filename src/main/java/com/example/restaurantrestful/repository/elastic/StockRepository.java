package com.example.restaurantrestful.repository.elastic;

import com.example.restaurantrestful.entity.Stock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;
import java.util.Optional;

public interface StockRepository extends ElasticsearchRepository<Stock, String> {

    @Query("{\"bool\" : {\"must\" : [{\"match\": {\"ingredientId\": \"?0\"}}, {\"match\": {\"isDeleted\": \"false\"}}]}}")
    Page<Stock> findByIngredientIdAndIsDeletedFalse(String ingredientId, Pageable pageable);

    @Query("{\"bool\" : {\"must\" : [{\"match\": {\"_id\": \"?0\"}}, {\"match\": {\"isDeleted\": \"false\"}}]}}")
    Optional<Stock> findByIdAndIsDeletedFalse(String id);

    @Query("{\"bool\": {\"must\": [{\"match\": {\"ingredientId\": \"?0\"}}, {\"match\": {\"isDeleted\": \"false\"}}]}}")
    List<Stock> findNearestExpirationStockByItemId(String ingredientId);
}
