package com.example.restaurantrestful.repository;

import com.example.restaurantrestful.entity.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IngredientRepository extends JpaRepository<Ingredient, String> {

}
