package com.example.restaurantrestful.service;

import com.example.restaurantrestful.repository.elastic.IngredientListItemRepository;
import org.springframework.stereotype.Service;

@Service
public class IngredientListItemService {

    private final IngredientListItemRepository ingredientListItemRepository;

    public IngredientListItemService(IngredientListItemRepository ingredientListItemRepository) {
        this.ingredientListItemRepository = ingredientListItemRepository;
    }
}
