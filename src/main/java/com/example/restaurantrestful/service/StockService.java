package com.example.restaurantrestful.service;

import com.example.restaurantrestful.repository.elastic.StockRepository;
import org.springframework.stereotype.Service;

@Service
public class StockService {

    private final StockRepository stockRepository;

    private final IngredientService ingredientService;

    public StockService(StockRepository stockRepository, IngredientService ingredientService) {
        this.stockRepository = stockRepository;
        this.ingredientService = ingredientService;
    }
}
