package com.example.restaurantrestful.service;

import com.example.restaurantrestful.dto.inputs.stock.AddIngredientToStockInput;
import com.example.restaurantrestful.dto.inputs.stock.GetAllStocksInput;
import com.example.restaurantrestful.dto.inputs.stock.GetStocksByIngredientIdInput;
import com.example.restaurantrestful.dto.payloads.StockPayload;
import com.example.restaurantrestful.entity.Ingredient;
import com.example.restaurantrestful.entity.Stock;
import com.example.restaurantrestful.enums.UnitTypeEnums;
import com.example.restaurantrestful.exception.CustomException;
import com.example.restaurantrestful.repository.elastic.StockRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

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

    @Transactional
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

    @Transactional
    public StockPayload updateStockQuantity(String id, Double quantity) {

        var dbStock = stockRepository.findByIdAndIsDeletedFalse(id).orElseThrow(CustomException::stockNotFound);

        if (quantity == 0.0) {
            throw new IllegalArgumentException("Quantity must be greater or less than 0");
        }

        switch (UnitTypeEnums.valueOf(dbStock.getUnit())) {
            case KG:
            case GR:
                dbStock.setQuantity(dbStock.getQuantity() + quantity);

                if (dbStock.getUnit().equals(UnitTypeEnums.GR.toString()) && dbStock.getQuantity() >= 1000.0) {
                    dbStock.setUnit(UnitTypeEnums.KG.toString());
                    dbStock.setQuantity(dbStock.getQuantity() / 1000.0);
                } else if (dbStock.getUnit().equals(UnitTypeEnums.KG.toString()) && dbStock.getQuantity() < 1.0) {
                    dbStock.setUnit(UnitTypeEnums.GR.toString());
                    dbStock.setQuantity(dbStock.getQuantity() * 1000.0);
                }
                break;
            case LT:
            case MIL:
                dbStock.setQuantity(dbStock.getQuantity() + quantity);

                if (dbStock.getUnit().equals(UnitTypeEnums.MIL.toString()) && dbStock.getQuantity() >= 1000.0) {
                    dbStock.setUnit(UnitTypeEnums.LT.toString());
                    dbStock.setQuantity(dbStock.getQuantity() / 1000.0);
                } else if (dbStock.getUnit().equals(UnitTypeEnums.LT.toString()) && dbStock.getQuantity() < 1.0) {
                    dbStock.setUnit(UnitTypeEnums.MIL.toString());
                    dbStock.setQuantity(dbStock.getQuantity() * 1000.0);
                }
                break;
        }

        dbStock.setUpdateDate(new Date());
        stockRepository.save(dbStock);

        return StockPayload.convert(dbStock);
    }

    public boolean deleteStockById(String id) {
        Stock dbStock = stockRepository.findById(id).orElseThrow(CustomException::stockNotFound);

        if (dbStock.isDeleted()) {
            throw CustomException.stockIsAlreadyDeleted();
        }
        dbStock.setDeleted(true);
        dbStock.setUpdateDate(new Date());
        stockRepository.save(dbStock);

        return true;
    }

}