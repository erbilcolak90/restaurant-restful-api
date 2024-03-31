package com.example.restaurantrestful.exception;

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
}
