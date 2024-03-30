package com.example.restaurantrestful.exception;

public class CustomException extends RuntimeException{

    public CustomException(String message) {
        super(message);
    }

    public static CustomException ingredientNotFound() {
        return new CustomException("Ingredient not found");
    }

}
