package com.example.restaurantrestful.repository.jpa;

import com.example.restaurantrestful.entity.Food;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FoodRepository extends JpaRepository<Food, String> {

    Optional<Food> findByName(String name);
}
