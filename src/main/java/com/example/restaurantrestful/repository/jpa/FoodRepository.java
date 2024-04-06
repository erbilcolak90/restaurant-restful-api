package com.example.restaurantrestful.repository.jpa;

import com.example.restaurantrestful.entity.Food;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FoodRepository extends JpaRepository<Food, String> {
}
