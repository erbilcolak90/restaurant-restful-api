package com.example.restaurantrestful.controller;

import com.example.restaurantrestful.entity.Food;
import com.example.restaurantrestful.service.FoodService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/food")
public class FoodController {

    private final FoodService foodService;

    public FoodController(FoodService foodService) {
        this.foodService = foodService;
    }

    @GetMapping("/getFoodById")
    public ResponseEntity<Food> getFoodById(@RequestParam String id){
        return ResponseEntity.ok(foodService.getFoodById(id));
    }

    @GetMapping("/getFoodByName")
    public ResponseEntity<Food> getFoodByName(@RequestParam String foodName){
        return ResponseEntity.ok(foodService.getFoodByName(foodName));
    }

    @PutMapping("/updateFoodStatus")
    public ResponseEntity<Boolean> updateFoodStatus(@RequestParam String id){
        return ResponseEntity.ok(foodService.updateFoodStatus(id));
    }

    @PostMapping("/makeFood")
    public ResponseEntity<Food> makeFood(@RequestParam String foodName){
        return ResponseEntity.ok(foodService.makeFood(foodName));
    }
}
