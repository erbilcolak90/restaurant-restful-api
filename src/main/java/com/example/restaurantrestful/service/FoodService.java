package com.example.restaurantrestful.service;

import com.example.restaurantrestful.entity.Food;
import com.example.restaurantrestful.entity.Ingredient;
import com.example.restaurantrestful.exception.CustomException;
import com.example.restaurantrestful.repository.jpa.FoodRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class FoodService {

    private final FoodRepository foodRepository;

    private final RecipeService recipeService;

    private final IngredientService ingredientService;

    public FoodService(FoodRepository foodRepository, RecipeService recipeService, IngredientService ingredientService) {
        this.foodRepository = foodRepository;
        this.recipeService = recipeService;
        this.ingredientService = ingredientService;
    }
    public Food getFoodById(String id){
        return foodRepository.findById(id).orElseThrow(CustomException::foodNotFound);
    }
    public Food getFoodByName(String name){
        return foodRepository.findByName(name).orElseThrow(CustomException::foodNotFound);
    }
    public List<Food> getFoodsByContainsIngredient(String ingredientName){

        Ingredient dbIngredient = ingredientService.getIngredientByName(ingredientName.toLowerCase());
        List<String> dbRecipeList = recipeService.getRecipeIdsByContainsIngredient(dbIngredient.getId());
        List<Food> foodList = new ArrayList<>();
        for(String recipeId: dbRecipeList){
            Food dbFood = foodRepository.findByRecipeId(recipeId).orElseThrow(CustomException::foodNotFound);
            foodList.add(dbFood);
        }

        return foodList;
    }

    @Transactional
    public boolean updateFoodStatus(String foodId){
        Food dbFood = foodRepository.findById(foodId).orElseThrow(CustomException::foodNotFound);

        if(!dbFood.isReady()){
            dbFood.setReady(true);
            dbFood.setUpdateDate(new Date());
            foodRepository.save(dbFood);

            return true;
        }else{
            return false;
        }
    }
}
