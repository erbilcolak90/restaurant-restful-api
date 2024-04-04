package com.example.restaurantrestful.service;

import com.example.restaurantrestful.entity.Ingredient;
import com.example.restaurantrestful.entity.Recipe;
import com.example.restaurantrestful.entity.Stock;
import com.example.restaurantrestful.exception.CustomException;
import com.example.restaurantrestful.repository.elastic.IngredientListItemRepository;
import com.example.restaurantrestful.repository.elastic.RecipeRepository;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RecipeService {

    private final RecipeRepository recipeRepository;

    private final IngredientListItemService ingredientListItemService;

    private final StockService stockService;

    private final IngredientService ingredientService;

    public RecipeService(RecipeRepository recipeRepository, IngredientListItemService ingredientListItemService, StockService stockService, IngredientService ingredientService) {
        this.recipeRepository = recipeRepository;
        this.ingredientListItemService = ingredientListItemService;
        this.stockService = stockService;
        this.ingredientService = ingredientService;
    }

    public Recipe getRecipeById(String id){
        return recipeRepository.findById(id).orElseThrow(CustomException::recipeNotFound);
    }

    public List<Recipe> getRecipeByContainsIngredient(String ingredientId){
        Ingredient dbIngredient = ingredientService.getIngredientById(ingredientId);
        return recipeRepository.findByIngredientId(dbIngredient.getId());
    }

    public Recipe getRecipeByName(String name){
        return recipeRepository.findByName(name.toLowerCase()).orElseThrow(CustomException::recipeNotFound);
    }

}
