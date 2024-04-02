package com.example.restaurantrestful.service;

import com.example.restaurantrestful.dto.inputs.stock.AddIngredientToStockInput;
import com.example.restaurantrestful.dto.inputs.stock.GetAllStocksInput;
import com.example.restaurantrestful.dto.inputs.stock.GetStocksByIngredientIdInput;
import com.example.restaurantrestful.dto.payloads.StockPayload;
import com.example.restaurantrestful.entity.Ingredient;
import com.example.restaurantrestful.entity.Stock;
import com.example.restaurantrestful.exception.CustomException;
import com.example.restaurantrestful.repository.elastic.StockRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class StockService {

    private final StockRepository stockRepository;

    private final IngredientService ingredientService;

    public StockService(StockRepository stockRepository, IngredientService ingredientService) {
        this.stockRepository = stockRepository;
        this.ingredientService = ingredientService;
    }

    public StockPayload getStockById(String id) {
        Stock dbStock = stockRepository.findById(id).orElseThrow(CustomException::stockNotFound);
        return StockPayload.convert(dbStock);
    }

    public Page<StockPayload> getStocksByIngredientId(GetStocksByIngredientIdInput getStocksByIngredientIdInput) {
        Pageable pageable = PageRequest.of(getStocksByIngredientIdInput.getPageNumber(),
                getStocksByIngredientIdInput.getPageSize(),
                Sort.by(Sort.Direction.valueOf(getStocksByIngredientIdInput.getSortBy().toString()), getStocksByIngredientIdInput.getFieldName()));

        Page<Stock> stockPage = stockRepository.findByIngredientIdAndIsDeletedFalse(getStocksByIngredientIdInput.getIngredientId(), pageable);

        return stockPage.map(StockPayload::convert);
    }

    public Page<Stock> getAllStocks(GetAllStocksInput getAllStocksInput) {
        Pageable pageable = PageRequest.of(getAllStocksInput.getPageNumber(),
                getAllStocksInput.getPageSize(),
                Sort.by(Sort.Direction.valueOf(getAllStocksInput.getSortBy().toString()), getAllStocksInput.getFieldName()));

        return stockRepository.findAll(pageable);
    }

    public StockPayload addIngredientToStock(AddIngredientToStockInput addIngredientToStockInput) {
        Ingredient dbIngredient = ingredientService.getIngredientById(addIngredientToStockInput.getIngredientId());

        Stock stock = new Stock();
        stock.setIngredientId(dbIngredient.getId());
        stock.setType(addIngredientToStockInput.getType().toString());
        stock.setUnit(addIngredientToStockInput.getUnit().toString());
        stock.setQuantity(addIngredientToStockInput.getQuantity());
        stock.setExpireDate(addIngredientToStockInput.getExpireDate());

        Stock dbStock = stockRepository.save(stock);

        return StockPayload.convert(dbStock);
    }

}