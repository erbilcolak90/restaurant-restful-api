package com.example.restaurantrestful.service;

import com.example.restaurantrestful.entity.Recipe;
import com.example.restaurantrestful.exception.CustomException;
import com.example.restaurantrestful.repository.elastic.IngredientListItemRepository;
import com.example.restaurantrestful.repository.elastic.RecipeRepository;
import org.springframework.stereotype.Service;

@Service
public class RecipeService {

    private final RecipeRepository recipeRepository;

    private final IngredientListItemService ingredientListItemService;

    private final StockService stockService;

    public RecipeService(RecipeRepository recipeRepository, IngredientListItemService ingredientListItemService, StockService stockService) {
        this.recipeRepository = recipeRepository;
        this.ingredientListItemService = ingredientListItemService;
        this.stockService = stockService;
    }

    public Recipe getRecipeById(String id){
        return recipeRepository.findById(id).orElseThrow(CustomException::recipeNotFound);
    }
}
