package com.example.restaurantrestful.service;

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
}
