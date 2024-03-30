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

}
