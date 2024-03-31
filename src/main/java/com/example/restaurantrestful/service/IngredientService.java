package com.example.restaurantrestful.service;

import com.example.restaurantrestful.dto.inputs.ingredient.CreateIngredientInput;
import com.example.restaurantrestful.dto.inputs.ingredient.GetAllIngredientsInput;
import com.example.restaurantrestful.dto.inputs.ingredient.UpdateIngredientInput;
import com.example.restaurantrestful.entity.Ingredient;
import com.example.restaurantrestful.exception.CustomException;
import com.example.restaurantrestful.repository.IngredientRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class IngredientService {


    private final IngredientRepository ingredientRepository;

    public IngredientService(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }

    public Ingredient getIngredientById(String id) {
        return ingredientRepository.findById(id).orElseThrow(CustomException::ingredientNotFound);
    }

    public Ingredient getIngredientByName(String name) {
        String ingredient_name = name.toLowerCase();
        return ingredientRepository.findByName(ingredient_name).orElseThrow(CustomException::ingredientNameNotFound);
    }

    public Page<Ingredient> getAllIngredients(GetAllIngredientsInput getAllIngredientsInput) {

        Pageable pageable = PageRequest.of(getAllIngredientsInput.getPageNumber(),
                getAllIngredientsInput.getPageSize(),
                Sort.by(Sort.Direction.valueOf(getAllIngredientsInput.getSortBy().toString()), "id"));
        return ingredientRepository.findAll(pageable);
    }

    @Transactional
    public Ingredient createIngredient(CreateIngredientInput createIngredientInput) {

        // if ingredient name is exist then map it with and throwing exception. if ingredient name does not exist, then create new ingredient with given input
        return (Ingredient) ingredientRepository.findByName(createIngredientInput.getName()).map(existingIngredient -> {
                    throw CustomException.ingredientNameIsAlreadyExist();
                })
                .orElseGet(() -> {
                    Ingredient ingredient = new Ingredient();
                    ingredient.setName(createIngredientInput.getName().toLowerCase());
                    ingredient.setType(createIngredientInput.getType());
                    ingredient.setUnit(createIngredientInput.getUnit());
                    ingredientRepository.save(ingredient);
                    return ingredient;
                });
    }

    @Transactional
    public Ingredient updateIngredient(UpdateIngredientInput updateIngredientInput) {
        var dbIngredient = ingredientRepository.findById(updateIngredientInput.getId()).orElseThrow(CustomException::ingredientNotFound);

        if (updateIngredientInput.getName() != null) {
            ingredientRepository.findByName(updateIngredientInput.getName().toLowerCase())
                    .ifPresent(existingIngredient -> {
                        throw CustomException.ingredientNameIsAlreadyExist();
                    });
            dbIngredient.setName(updateIngredientInput.getName().toLowerCase());
        }

        dbIngredient.setType(updateIngredientInput.getType() != null ? updateIngredientInput.getType() : dbIngredient.getType());
        dbIngredient.setUnit(updateIngredientInput.getUnit() != null ? updateIngredientInput.getUnit() : dbIngredient.getUnit());
        dbIngredient.setUpdateDate(new Date());

        ingredientRepository.save(dbIngredient);
        return dbIngredient;
    }

    @Transactional
    public String deleteIngredient(String id) {
        Ingredient dbIngredient = ingredientRepository.findById(id).orElseThrow(CustomException::ingredientNotFound);

        if (dbIngredient.isDeleted()) {
            throw CustomException.ingredientIsAlreadyDeleted();
        }

        dbIngredient.setDeleted(true);
        dbIngredient.setUpdateDate(new Date());
        ingredientRepository.save(dbIngredient);

        return "Ingredient id : " + dbIngredient.getId() + " successfully deleted";
    }

}
