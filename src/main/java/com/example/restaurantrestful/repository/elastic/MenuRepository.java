package com.example.restaurantrestful.repository.elastic;

import com.example.restaurantrestful.entity.Menu;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.Optional;

public interface MenuRepository extends ElasticsearchRepository<Menu, String> {

    @Query("{\"bool\": {\"must\": {\"match\" : {\"name\": \"?0\"}}}}")
    Optional<Menu> findByName(String name);

    @Query("{\"bool\": {\"must\": {\"match\": {\"_id\": \"?0\"}, {\"match\": {\"isDeleted\": \"false\"}}}}")
    Optional<Menu> findByIsDeletedFalse(String id);
}
