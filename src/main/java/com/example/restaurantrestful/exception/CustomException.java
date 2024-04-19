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

    public static CustomException productThumbnailIdSameWithInput() {
        return new CustomException("Thumbnail id is same with given input");
    }

    public static CustomException orderProductNotFound() {
        return new CustomException("OrderProduct not found");
    }

    public static CustomException orderNotFound() {
        return new CustomException("Order not found");
    }

    public static CustomException orderProductQuantityLimitException(int quantity, int inputQuantity) {
        return new CustomException("Product quantity : "+quantity + " the number of products that want to be delete : " + inputQuantity);
    }

    public static CustomException orderIsAlreadyDeleted() {
        return new CustomException("Order is already deleted");
    }

    public static CustomException orderIsAlreadyCompleted() {
        return new CustomException("Order is already completed");
    }

    public static CustomException menuNotFound() {
        return new CustomException("Menu not found");
    }

    public static CustomException menuNameIsAlreadyExist() {
        return new CustomException("Menu name is already exist");
    }

    public static CustomException productIsAlreadyExistInMenu() {
        return new CustomException("Product is already exist in menu");
    }

    public static CustomException productDoesNotExistInMenu() {
        return new CustomException("Product does not exist in menu");
    }

    public static CustomException menuIsAlreadyDeleted() {
        return new CustomException("Menu is already deleted");
    }

    public static CustomException invoiceNotFound() {
        return new CustomException("Invoice not found");
    }

    public static CustomException orderHasAlreadyInvoiced(String invoiceId) {
        return new CustomException("Order has already invoiced. Invoice id : " + invoiceId);
    }

    public static CustomException invoicePaymentIsAlreadyCompleted(String id) {
        return new CustomException("Invoice payment is already completed. Invoice id : "+ id);
    }

    public static CustomException paymentNotCompleted(double balance) {
        return new CustomException("Payment is not completed. the amount to be complete : "+ balance);
    }

    public static CustomException invoiceIsAlreadyDeleted() {
        return new CustomException("Invoice is already deleted");
    }

    public static CustomException paymentNotFound() {
        return new CustomException("Payment not found");
    }

    public static CustomException invoiceHasNotAlreadyPayment() {
        return new CustomException("Invoice has not already payment");
    }
}
