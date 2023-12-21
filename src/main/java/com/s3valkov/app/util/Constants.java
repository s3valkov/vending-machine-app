package com.s3valkov.app.util;

public final class Constants {
    private Constants() {
        throw new IllegalStateException("Constants class");
    }

    public static final String PRODUCT_BUY_SUCCESSFUL = "Product purchased successfully";
    public static final String PRODUCT_ADDED_SUCCESSFUL = "Product added successfully";
    public static final String INVALID_PRODUCT_DETAILS  = "Product price and quantity cannot be negative";
    public static final String PRODUCT_REMOVE_SUCCESS_MESSAGE = "Product removed successfully";
    public static final String PRODUCT_NOT_FOUND = "Product not found";
    public static final String PRODUCT_OUT_OF_STOCK = "Product out of stock";
    public static final String PRODUCT_UPDATED = "Product updated successfully";
    public static final String INSUFFICIENT_COINS_ERROR_MESSAGE = "Insufficient coins to buy the product";
    public static final String COINS_RETURNED = "Coins reset successfully. Returned: ";
    public static final String INVALID_COIN_ERROR_MESSAGE = "Invalid coin. Accepted coins: 10c, 20c, 50c, €1, €2";
    public static final String VALID_COIN_INSERTION = "Coin inserted successfully. Total: ";

    // Assuming the values are in cents
    public static final int[] VALID_COINS = {10, 20, 50, 100, 200}; // in cents
}
