package com.example.restaurantrestful.exception;

import java.util.function.Supplier;

public class CustomException extends RuntimeException {

    public CustomException(String message) {
        super(message);
    }

    public static CustomException ingredientNotFound() {
        return new CustomException("Ingredient not found");
    }

    public static CustomException ingredientNameNotFound() {
        return new CustomException("Ingredient name not found");
    }

    public static CustomException ingredientNameIsAlreadyExist() {
        return new CustomException("Ingredient name is already exist");
    }

    public static CustomException ingredientIsAlreadyDeleted(){
        return new CustomException("Ingredient is already deleted");
    }

    public static CustomException stockNotFound() {
        return new CustomException("Stock not found");
    }

    public static CustomException stockIsAlreadyDeleted() {
        return new CustomException("Stock is already deleted");
    }

    public static CustomException recipeNotFound(){
        return new CustomException("Recipe not found");
    }
}
