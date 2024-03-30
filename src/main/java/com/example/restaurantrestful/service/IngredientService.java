package com.example.restaurantrestful.service;

import com.example.restaurantrestful.entity.Ingredient;
import com.example.restaurantrestful.exception.CustomException;
import com.example.restaurantrestful.repository.IngredientRepository;

import org.springframework.stereotype.Service;

@Service
public class IngredientService {


    private final IngredientRepository ingredientRepository;

    public IngredientService(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }

    public Ingredient getIngredientById(String id) {
        return ingredientRepository.findById(id).orElseThrow(CustomException::ingredientNotFound);
    }

    public Ingredient getIngredientByName(String name) {
        String ingredient_name = name.toLowerCase();
        return ingredientRepository.findByName(ingredient_name).orElseThrow(CustomException::ingredientNameNotFound);
    }



}
