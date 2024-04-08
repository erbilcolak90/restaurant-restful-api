package com.example.restaurantrestful.repository.jpa;

import com.example.restaurantrestful.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, String> {
}
