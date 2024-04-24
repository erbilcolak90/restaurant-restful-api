package com.example.restaurantrestful.controller;


import com.example.restaurantrestful.dto.inputs.ingredientlistitem.CreateIngredientListItemInput;
import com.example.restaurantrestful.dto.inputs.ingredientlistitem.UpdateIngredientListItemInput;
import com.example.restaurantrestful.entity.IngredientListItem;
import com.example.restaurantrestful.service.IngredientListItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ingredientListItem")
public class IngredientListItemController {

    private final IngredientListItemService ingredientListItemService;

    public IngredientListItemController(IngredientListItemService ingredientListItemService) {
        this.ingredientListItemService = ingredientListItemService;
    }

    @GetMapping("/getIngredientListItemById")
    public ResponseEntity<IngredientListItem> getIngredientListItemById(@RequestParam String id){
        return ResponseEntity.ok(ingredientListItemService.getIngredientListItemById(id));
    }

    @GetMapping("/getIngredientListItemsByRecipeId")
    public ResponseEntity<List<IngredientListItem>> getIngredientListItemsByRecipeId(@RequestParam String recipeId){
        return ResponseEntity.ok(ingredientListItemService.getIngredientListItemsByRecipeId(recipeId));
    }

    @PostMapping("/createIngredientListItem")
    public ResponseEntity<IngredientListItem> createIngredientListItem(@RequestBody CreateIngredientListItemInput createIngredientListItemInput){
        return ResponseEntity.ok(ingredientListItemService.createIngredientListItem(createIngredientListItemInput));
    }

    @PutMapping("/updateIngredientListItem")
    public ResponseEntity<IngredientListItem> updateIngredientListItem(@RequestBody UpdateIngredientListItemInput updateIngredientListItemInput){
        return ResponseEntity.ok(ingredientListItemService.updateIngredientListItem(updateIngredientListItemInput));
    }

    @DeleteMapping("/deleteIngredientListItem")
    public ResponseEntity<Boolean> deleteIngredientListItem(@RequestParam String ingredientListItemId){
        return ResponseEntity.ok(ingredientListItemService.deleteIngredientListItem(ingredientListItemId));
    }
}
