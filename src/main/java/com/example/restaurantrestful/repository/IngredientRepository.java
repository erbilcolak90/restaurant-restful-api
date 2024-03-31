package com.example.restaurantrestful.repository;

import com.example.restaurantrestful.entity.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IngredientRepository extends JpaRepository<Ingredient, String> {
    Optional<Ingredient> findByName(String name);

    Optional<Ingredient> findByIdAndIsDeletedFalse(String id);
}
