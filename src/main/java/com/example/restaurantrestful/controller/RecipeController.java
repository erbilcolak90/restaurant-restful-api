package com.example.restaurantrestful.controller;

import com.example.restaurantrestful.dto.inputs.recipe.CreateRecipeInput;
import com.example.restaurantrestful.entity.Recipe;
import com.example.restaurantrestful.service.RecipeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/recipe")
public class RecipeController {

    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping("/getRecipeById")
    public ResponseEntity<Recipe> getRecipeById(@RequestParam String id){
        return ResponseEntity.ok(recipeService.getRecipeById(id));
    }

    @GetMapping("/getRecipeByContainsIngredient")
    public ResponseEntity<List<Recipe>> getRecipeByContainsIngredient(@RequestParam String ingredientId){
        return ResponseEntity.ok(recipeService.getRecipeByContainsIngredient(ingredientId));
    }

    @GetMapping("getRecipeIdsByContainsIngredient")
    public ResponseEntity<List<String>> getRecipeIdsByContainsIngredient(String ingredientId){
        return ResponseEntity.ok(recipeService.getRecipeIdsByContainsIngredient(ingredientId));
    }

    @GetMapping("/getRecipeByName")
    public ResponseEntity<Recipe> getRecipeByName(@RequestParam String id){
        return ResponseEntity.ok(recipeService.getRecipeByName(id));
    }

    @PostMapping("/createRecipe")
    public ResponseEntity<Recipe> createRecipe(@RequestBody CreateRecipeInput createRecipeInput){
        return ResponseEntity.ok(recipeService.createRecipe(createRecipeInput));
    }

    @PostMapping("/addIngredientsToRecipe")
    public ResponseEntity<Recipe> addIngredientsToRecipe(@RequestParam String recipeId){
        return ResponseEntity.ok(recipeService.addIngredientsToRecipe(recipeId));
    }
}
