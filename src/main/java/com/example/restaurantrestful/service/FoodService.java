package com.example.restaurantrestful.service;

import com.example.restaurantrestful.entity.*;
import com.example.restaurantrestful.enums.UnitTypeEnums;
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

    private final StockService stockService;

    public FoodService(FoodRepository foodRepository, RecipeService recipeService, IngredientService ingredientService, StockService stockService) {
        this.foodRepository = foodRepository;
        this.recipeService = recipeService;
        this.ingredientService = ingredientService;
        this.stockService = stockService;
    }

    public Food getFoodById(String id) {
        return foodRepository.findById(id).orElseThrow(CustomException::foodNotFound);
    }

    public Food getFoodByName(String name) {
        return foodRepository.findByName(name).orElseThrow(CustomException::foodNotFound);
    }

    public List<Food> getFoodsByContainsIngredient(String ingredientName) {

        Ingredient dbIngredient = ingredientService.getIngredientByName(ingredientName.toLowerCase());
        List<String> dbRecipeList = recipeService.getRecipeIdsByContainsIngredient(dbIngredient.getId());
        List<Food> foodList = new ArrayList<>();
        for (String recipeId : dbRecipeList) {
            Food dbFood = foodRepository.findByRecipeId(recipeId).orElseThrow(CustomException::foodNotFound);
            foodList.add(dbFood);
        }

        return foodList;
    }

    @Transactional
    public boolean updateFoodStatus(String foodId) {
        Food dbFood = foodRepository.findById(foodId).orElseThrow(CustomException::foodNotFound);

        if (!dbFood.isReady()) {
            dbFood.setReady(true);
            dbFood.setUpdateDate(new Date());
            foodRepository.save(dbFood);

            return true;
        } else {
            return false;
        }
    }

    @Transactional
    public Food makeFood(String foodName) {
        Recipe dbRecipe = recipeService.getRecipeByName(foodName.toLowerCase());

        for (IngredientListItem item : dbRecipe.getIngredientListItem()) {
            Stock nearestStock = stockService.findNearestExpirationStockByItemId(item.getIngredientId());
            double requiredQuantity = item.getQuantity();
            double stockQuantity = nearestStock.getQuantity();

            if(item.getUnit().toString() != nearestStock.getUnit().toString()){
                requiredQuantity = convertQuantity(requiredQuantity, UnitTypeEnums.valueOf(item.getUnit()), UnitTypeEnums.valueOf(nearestStock.getUnit()));
            }

            while (requiredQuantity > 0) {


                if (stockQuantity - requiredQuantity > 0) {

                    stockService.updateStockQuantity(nearestStock.getId(), -requiredQuantity);
                    requiredQuantity = 0;
                } else {

                    requiredQuantity -= stockQuantity;

                    stockService.updateStockQuantity(nearestStock.getId(), -stockQuantity);
                    stockService.deleteStockById(nearestStock.getId());

                    nearestStock = stockService.findNearestExpirationStockByItemId(item.getIngredientId());
                }
            }
        }

        Food dbFood = new Food();
        dbFood.setRecipeId(dbRecipe.getId());
        dbFood.setName(foodName.toLowerCase());
        dbFood.setReady(true);
        foodRepository.save(dbFood);
        return dbFood;
    }

    public static double convertQuantity(double quantity, UnitTypeEnums sourceUnit, UnitTypeEnums targetUnit) {

        if (sourceUnit == null || targetUnit == null) {
            throw new NullPointerException("Units cannot be null");
        }

        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }

        switch (sourceUnit) {
            case GR:
                switch (targetUnit) {
                    case KG:
                        return quantity / 1000;
                }
                break;
            case KG:
                switch (targetUnit) {
                    case GR:
                        return quantity * 1000;
                }
                break;
            case MIL:
                switch (targetUnit) {
                    case LT:
                        return quantity / 1000;
                }
                break;
            case LT:
                switch (targetUnit) {
                    case MIL:
                        return quantity * 1000;
                }
                break;
        }
        throw new IllegalArgumentException("Undefined unit type " + sourceUnit + " -> " + targetUnit);
    }
}
