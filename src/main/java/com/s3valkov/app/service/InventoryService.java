package com.s3valkov.app.service;

import com.s3valkov.app.model.ProductDetails;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class InventoryService {
    private static int productId = 1;
    private static int insertedCoins = 0;
    private Map<Integer, ProductDetails> products;

    public InventoryService() {
        this.products = new HashMap<>();
    }

    public Map<Integer, ProductDetails> getProducts() {
        return products;
    }

    public void addProduct(ProductDetails productDetails) {
        products.put(productId, productDetails);
        productId++;
    }

    public void updateProduct(int productId, ProductDetails productDetails) {
        if (products.containsKey(productId)) {
            products.put(productId, productDetails);
        }
    }

    public void deleteProduct(int productId) {
        products.remove(productId);
    }

    public boolean containsProduct(int productId) {
        return products.containsKey(productId);
    }

    public ProductDetails getProduct(int productId) {
        return products.get(productId);
    }

    public int getInsertedCoins() {
        return insertedCoins;
    }

    public void addCoins(int coins) {
        insertedCoins += coins;
    }

    public void insertedCoinsAfterBuy(int price) {
        insertedCoins -= price;
    }

    public void resetCoins() {
        insertedCoins = 0;
    }
}
