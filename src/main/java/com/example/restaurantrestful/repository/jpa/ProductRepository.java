package com.example.restaurantrestful.repository.jpa;

import com.example.restaurantrestful.entity.Product;
import com.example.restaurantrestful.enums.ProductStatusEnums;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, String> {
    Optional<Product> findByName(String name);

    List<Product> findAllByStatusOrderByUpdateDateDesc(ProductStatusEnums status);
}
