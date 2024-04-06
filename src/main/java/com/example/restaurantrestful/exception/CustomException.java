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

    public static CustomException recipeNameIsAlreadyExist(){
        return new CustomException("Recipe name is already exist");
    }

    public static CustomException ingredientListItemNotFound() {
        return new CustomException("Ingredient list item not found");
    }

    public static CustomException ingredientListItemIsAlreadyDeleted() {
        return new CustomException("Ingredient list item is already deleted");
    }

    public static CustomException foodNotFound() {
        return new CustomException("Food not found");
    }
}
