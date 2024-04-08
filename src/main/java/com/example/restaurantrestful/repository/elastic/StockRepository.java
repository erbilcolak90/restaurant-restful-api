package com.example.restaurantrestful.repository.elastic;

import com.example.restaurantrestful.entity.Stock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.Optional;

public interface StockRepository extends ElasticsearchRepository<Stock, String> {

    @Query("{\"bool\" : {\"must\" : [{\"match\": {\"ingredientId\": \"?0\"}}, {\"match\": {\"isDeleted\": \"false\"}}]}}")
    Page<Stock> findByIngredientIdAndIsDeletedFalse(String ingredientId, Pageable pageable);

    Optional<Stock> findByIdAndIsDeletedFalse(String id);

    @Query("{\"bool\": {\"must\": [{\"match\": {\"ingredientId\": \"?0\"}}, {\"term\": {\"isDeleted\": false}}], \"filter\": {\"range\": {\"expireDate\": {\"gte\": \"now\"}}}}, \"sort\": [{\"expireDate\": {\"order\": \"asc\"}}], \"size\": 1}")
    Optional<Stock> findNearestExpirationStockByItemId(String ingredientId);
}
