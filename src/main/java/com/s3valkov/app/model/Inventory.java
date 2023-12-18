package com.s3valkov.app.model;

import java.util.HashMap;
import java.util.Map;


public class Inventory {
    private Map<Integer, Product> products;

    public Inventory() {
        this.products = new HashMap<>();
    }

    public Map<Integer, Product> getProducts() {
        return products;
    }

    public void addProduct(Product product) {
        products.put(product.getId(), product);
    }

    public void updateProduct(Product product) {
        int productId = product.getId();
        if (products.containsKey(productId)) {
            products.put(productId, product);
        }
    }

    public void deleteProduct(int productId) {
        products.remove(productId);
    }

    public boolean containsProduct(int productId) {
        return products.containsKey(productId);
    }

    public Product getProduct(int productId) {
        return products.get(productId);
    }
}
