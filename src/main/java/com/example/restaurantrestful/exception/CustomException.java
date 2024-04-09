package com.example.restaurantrestful.exception;

import com.example.restaurantrestful.entity.Product;

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

    public static CustomException foodIsReadyException() {
        return new CustomException("Food is already ready");
    }

    public static CustomException productNotFound() {
        return new CustomException("Product not found");
    }

    public static CustomException productNameIsAlreadyExist() {
        return new CustomException("Product name is already exist");
    }

    public static CustomException productPriceLimitException() {
        return new CustomException("Product price must be greater than 0.0");
    }

    public static CustomException productStatusIsSameWithInput() {
        return new CustomException("Product status is same with given input status");
    }
}
