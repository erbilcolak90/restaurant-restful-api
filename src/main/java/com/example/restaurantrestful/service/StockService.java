package com.example.restaurantrestful.service;

import com.example.restaurantrestful.dto.payloads.StockPayload;
import com.example.restaurantrestful.entity.Stock;
import com.example.restaurantrestful.exception.CustomException;
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

    public StockPayload getStockById(String id){
        Stock dbStock = stockRepository.findById(id).orElseThrow(CustomException::stockNotFound);
        return StockPayload.convert(dbStock);
    }
}