package com.example.restaurantrestful.repository.elastic;

import com.example.restaurantrestful.entity.Menu;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface MenuRepository extends ElasticsearchRepository<Menu, String> {
}
