package com.example.restaurantrestful.repository.elastic;

import com.example.restaurantrestful.entity.Stock;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface StockRepository extends ElasticsearchRepository<Stock, String> {
}
