package com.example.restaurantrestful.controller;

import com.example.restaurantrestful.dto.inputs.ingredient.CreateIngredientInput;
import com.example.restaurantrestful.dto.inputs.ingredient.GetAllIngredientsInput;
import com.example.restaurantrestful.dto.inputs.ingredient.UpdateIngredientInput;
import com.example.restaurantrestful.entity.Ingredient;
import com.example.restaurantrestful.service.IngredientService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ingredient")
public class IngredientController {

    private final IngredientService ingredientService;

    public IngredientController(IngredientService ingredientService) {
        this.ingredientService = ingredientService;
    }

    @GetMapping("/getIngredientById")
    public ResponseEntity<Ingredient> getIngredientById(@RequestParam String id){
        return ResponseEntity.ok(ingredientService.getIngredientById(id));
    }

    @GetMapping("/getIngredientByName")
    public ResponseEntity<Ingredient> getIngredientByName(@RequestParam String name){
        return ResponseEntity.ok(ingredientService.getIngredientByName(name));
    }

    @PostMapping("/getAllIngredients")
    public ResponseEntity<Page<Ingredient>> getAllIngredients(@RequestBody GetAllIngredientsInput getAllIngredientsInput){
        return ResponseEntity.ok(ingredientService.getAllIngredients(getAllIngredientsInput));
    }

    @PostMapping("/createIngredient")
    public ResponseEntity<Ingredient> createIngredient(@RequestBody CreateIngredientInput createIngredientInput){
        return ResponseEntity.ok(ingredientService.createIngredient(createIngredientInput));
    }

    @PutMapping("/updateIngredient")
    public ResponseEntity<Ingredient> updateIngredient(@RequestBody UpdateIngredientInput updateIngredientInput){
        return ResponseEntity.ok(ingredientService.updateIngredient(updateIngredientInput));
    }

    @DeleteMapping("/deleteIngredient")
    public ResponseEntity<String> deleteIngredient(@RequestParam String id){
        return ResponseEntity.ok(ingredientService.deleteIngredient(id));
    }
}
