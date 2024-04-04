package com.example.restaurantrestful.service;

import com.example.restaurantrestful.entity.IngredientListItem;
import com.example.restaurantrestful.entity.Recipe;
import com.example.restaurantrestful.exception.CustomException;
import com.example.restaurantrestful.repository.elastic.IngredientListItemRepository;
import com.example.restaurantrestful.repository.elastic.RecipeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IngredientListItemService {

    private final IngredientListItemRepository ingredientListItemRepository;

    private final RecipeRepository recipeRepository;

    public IngredientListItemService(IngredientListItemRepository ingredientListItemRepository, RecipeRepository recipeRepository) {
        this.ingredientListItemRepository = ingredientListItemRepository;
        this.recipeRepository = recipeRepository;
    }

    public IngredientListItem getIngredientListItemById(String id) {
        return ingredientListItemRepository.findById(id).orElseThrow(CustomException::ingredientListItemNotFound);
    }

    public List<IngredientListItem> getIngredientListItemsByRecipeId(String recipeId) {
        Recipe dbRecipe = recipeRepository.findById(recipeId).orElseThrow(CustomException::recipeNotFound);

        return ingredientListItemRepository.findIngredientListItemsByContainsRecipeId(dbRecipe.getId());
    }
}
