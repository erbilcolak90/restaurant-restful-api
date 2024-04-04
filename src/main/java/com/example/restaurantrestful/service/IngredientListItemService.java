package com.example.restaurantrestful.service;

import com.example.restaurantrestful.dto.inputs.ingredientlistitem.CreateIngredientListItemInput;
import com.example.restaurantrestful.dto.inputs.ingredientlistitem.UpdateIngredientListItemInput;
import com.example.restaurantrestful.entity.Ingredient;
import com.example.restaurantrestful.entity.IngredientListItem;
import com.example.restaurantrestful.entity.Recipe;
import com.example.restaurantrestful.exception.CustomException;
import com.example.restaurantrestful.repository.elastic.IngredientListItemRepository;
import com.example.restaurantrestful.repository.elastic.RecipeRepository;
import com.example.restaurantrestful.repository.jpa.IngredientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class IngredientListItemService {

    private final IngredientListItemRepository ingredientListItemRepository;

    private final RecipeRepository recipeRepository;

    private final IngredientRepository ingredientRepository;

    public IngredientListItemService(IngredientListItemRepository ingredientListItemRepository, RecipeRepository recipeRepository, IngredientRepository ingredientRepository) {
        this.ingredientListItemRepository = ingredientListItemRepository;
        this.recipeRepository = recipeRepository;
        this.ingredientRepository = ingredientRepository;
    }

    public IngredientListItem getIngredientListItemById(String id) {
        return ingredientListItemRepository.findById(id).orElseThrow(CustomException::ingredientListItemNotFound);
    }

    public List<IngredientListItem> getIngredientListItemsByRecipeId(String recipeId) {
        Recipe dbRecipe = recipeRepository.findById(recipeId).orElseThrow(CustomException::recipeNotFound);

        return ingredientListItemRepository.findIngredientListItemsByContainsRecipeId(dbRecipe.getId());
    }

    @Transactional
    public IngredientListItem createIngredientListItem(CreateIngredientListItemInput createIngredientListItemInput) {

        Ingredient dbIngredient = ingredientRepository.findById(createIngredientListItemInput.getIngredientId()).orElseThrow(CustomException::ingredientNotFound);
        Recipe dbRecipe = recipeRepository.findById(createIngredientListItemInput.getRecipeId()).orElseThrow(CustomException::recipeNotFound);

        IngredientListItem newItem = new IngredientListItem();
        newItem.setRecipeId(dbRecipe.getId());
        newItem.setIngredientId(dbIngredient.getId());
        newItem.setIngredientType(createIngredientListItemInput.getType().toString());
        newItem.setUnit(createIngredientListItemInput.getUnit().toString());
        newItem.setQuantity(createIngredientListItemInput.getQuantity());

        ingredientListItemRepository.save(newItem);

        return newItem;
    }

    @Transactional
    public IngredientListItem updateIngredientListItem(UpdateIngredientListItemInput updateIngredientListItemInput) {

        IngredientListItem dbIngredientListItem = ingredientListItemRepository.findById(updateIngredientListItemInput.getIngredientListItemId()).orElseThrow(CustomException::ingredientListItemNotFound);


        if (updateIngredientListItemInput.getIngredientId() != null && ingredientRepository.existsById(updateIngredientListItemInput.getIngredientId())) {
            dbIngredientListItem.setIngredientId(updateIngredientListItemInput.getIngredientId());
        }
        if (updateIngredientListItemInput.getRecipeId() != null && recipeRepository.existsById(updateIngredientListItemInput.getRecipeId())) {
            dbIngredientListItem.setRecipeId(updateIngredientListItemInput.getRecipeId());
        }

        if (updateIngredientListItemInput.getType() != null) {
            dbIngredientListItem.setIngredientType(updateIngredientListItemInput.getType().toString());
        }
        if (updateIngredientListItemInput.getUnit() != null) {
            dbIngredientListItem.setUnit(updateIngredientListItemInput.getUnit().toString());
        }
        if (updateIngredientListItemInput.getQuantity() != null && updateIngredientListItemInput.getQuantity() > 0) {
            dbIngredientListItem.setQuantity(updateIngredientListItemInput.getQuantity());
        }
        dbIngredientListItem.setUpdateDate(new Date());

        ingredientListItemRepository.save(dbIngredientListItem);

        return dbIngredientListItem;

    }

    @Transactional
    public boolean deleteIngredientListItem(String ingredientListItemId){
        IngredientListItem dbIngredientListItem = ingredientListItemRepository.findById(ingredientListItemId).orElseThrow(CustomException::ingredientListItemNotFound);
        if(dbIngredientListItem.isDeleted()){
            throw CustomException.ingredientListItemIsAlreadyDeleted();
        }else{
            dbIngredientListItem.setDeleted(true);
            dbIngredientListItem.setUpdateDate(new Date());
            ingredientListItemRepository.save(dbIngredientListItem);
            return true;
        }
    }
}
