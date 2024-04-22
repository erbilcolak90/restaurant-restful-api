package com.example.restaurantrestful.controller;

import com.example.restaurantrestful.dto.inputs.stock.AddIngredientToStockInput;
import com.example.restaurantrestful.dto.inputs.stock.GetAllStocksInput;
import com.example.restaurantrestful.dto.inputs.stock.GetStocksByIngredientIdInput;
import com.example.restaurantrestful.dto.payloads.StockPayload;
import com.example.restaurantrestful.entity.Stock;
import com.example.restaurantrestful.service.StockService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/stock")
public class StockController {

    private final StockService stockService;

    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @GetMapping("/getStockById")
    public ResponseEntity<StockPayload> getStockById(@RequestParam String id){
        return ResponseEntity.ok(stockService.getStockById(id));
    }

    @PostMapping("/getStocksByIngredientId")
    public ResponseEntity<Page<StockPayload>> getStocksByIngredientId(@RequestBody GetStocksByIngredientIdInput getStocksByIngredientIdInput){
        return ResponseEntity.ok(stockService.getStocksByIngredientId(getStocksByIngredientIdInput));
    }

    @PostMapping("/getAllStocks")
    public ResponseEntity<Page<Stock>> getAllStocks(@RequestBody GetAllStocksInput getAllStocksInput){
        return ResponseEntity.ok(stockService.getAllStocks(getAllStocksInput));
    }

    @PostMapping("/addIngredientToStock")
    public ResponseEntity<StockPayload> addIngredientToStock(@RequestBody AddIngredientToStockInput addIngredientToStockInput){
        return ResponseEntity.ok(stockService.addIngredientToStock(addIngredientToStockInput));
    }

    @PutMapping("/updateStockQuantity")
    public ResponseEntity<StockPayload> updateStockQuantity(@RequestParam String id, @RequestParam Double quantity){
        return ResponseEntity.ok(stockService.updateStockQuantity(id, quantity));
    }

    @DeleteMapping("/deleteStockById")
    public ResponseEntity<Boolean> deleteStockById(@RequestParam String id){
        return ResponseEntity.ok(stockService.deleteStockById(id));
    }


}
