package com.example.restaurantrestful.service;

import com.example.restaurantrestful.entity.Food;
import com.example.restaurantrestful.exception.CustomException;
import com.example.restaurantrestful.repository.jpa.FoodRepository;
import org.springframework.stereotype.Service;

@Service
public class FoodService {

    private final FoodRepository foodRepository;

    private final RecipeService recipeService;

    public FoodService(FoodRepository foodRepository, RecipeService recipeService) {
        this.foodRepository = foodRepository;
        this.recipeService = recipeService;
    }

    public Food getFoodById(String id){
        return foodRepository.findById(id).orElseThrow(CustomException::foodNotFound);
    }

    public Food getFoodByName(String name){
        return foodRepository.findByName(name).orElseThrow(CustomException::foodNotFound);
    }
}
