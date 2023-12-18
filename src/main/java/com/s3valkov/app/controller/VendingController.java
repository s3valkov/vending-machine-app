package com.s3valkov.app.controller;

import com.s3valkov.app.model.Inventory;
import com.s3valkov.app.model.Product;
import java.util.Map;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.s3valkov.app.util.Constants.COINS_RETURNED;
import static com.s3valkov.app.util.Constants.INSUFFICIENT_COINS_ERROR_MESSAGE;
import static com.s3valkov.app.util.Constants.INVALID_COIN_ERROR_MESSAGE;
import static com.s3valkov.app.util.Constants.INVALID_PRODUCT_DETAILS;
import static com.s3valkov.app.util.Constants.PRODUCT_ADDED_SUCCESSFUL;
import static com.s3valkov.app.util.Constants.PRODUCT_BUY_SUCCESSFUL;
import static com.s3valkov.app.util.Constants.PRODUCT_EXISTS;
import static com.s3valkov.app.util.Constants.PRODUCT_NOT_FOUND;
import static com.s3valkov.app.util.Constants.PRODUCT_OUT_OF_STOCK;
import static com.s3valkov.app.util.Constants.PRODUCT_UPDATED;
import static com.s3valkov.app.util.Constants.PRODUCT_REMOVE_SUCCESS_MESSAGE;
import static com.s3valkov.app.util.Constants.VALID_COIN_INSERTION;
import static com.s3valkov.app.util.ValidationUtil.isProductValid;
import static com.s3valkov.app.util.ValidationUtil.isValidCoin;

@RestController
@RequestMapping("/api")
public class VendingController {
    private final Inventory inventory = new Inventory();
    private int insertedCoins = 0;

    @GetMapping("/products")
    public Map<Integer, Product> getInventory() {
        return inventory.getProducts();
    }

    @PostMapping("/add")
    public String addProduct(@RequestBody Product product) {
        if (inventory.containsProduct(product.getId())) {
            return PRODUCT_EXISTS;
        }
        if (!isProductValid(product)) {
            return INVALID_PRODUCT_DETAILS;
        }
        inventory.addProduct(product);
        return PRODUCT_ADDED_SUCCESSFUL;
    }

    @PutMapping("/update/{productId}")
    public String updateProduct(@PathVariable int productId, @RequestBody Product product) {
        if (!inventory.containsProduct(productId)) {
            return PRODUCT_NOT_FOUND;
        }
        inventory.updateProduct(product);
        return PRODUCT_UPDATED;
    }

    @DeleteMapping("/remove/{productId}")
    public String removeProduct(@PathVariable int productId) {
        if (!inventory.containsProduct(productId)) {
            return PRODUCT_NOT_FOUND;
        }
        inventory.deleteProduct(productId);
        return PRODUCT_REMOVE_SUCCESS_MESSAGE;
    }

    @PostMapping("/insert-coin/{coin}")
    public String insertCoin(@PathVariable int coin) {
        if (isValidCoin(coin)) {
            insertedCoins += coin;
            return VALID_COIN_INSERTION + insertedCoins;
        } else {
            return INVALID_COIN_ERROR_MESSAGE;
        }
    }

    @PostMapping("/reset")
    public String reset() {
        int returnedCoins = insertedCoins;
        insertedCoins = 0;
        return COINS_RETURNED  + returnedCoins;
    }

    @PostMapping("/buy/{productId}")
    public String buyProduct(@PathVariable int productId) {
        if (!inventory.containsProduct(productId)) {
            return PRODUCT_NOT_FOUND;
        }

        Product product = inventory.getProduct(productId);
        if (product.getQuantity() == 0) {
            return PRODUCT_OUT_OF_STOCK;
        }

        if (insertedCoins < product.getPriceInCoins()) {
            return INSUFFICIENT_COINS_ERROR_MESSAGE;
        }

        product.setQuantity(product.getQuantity() - 1);
        insertedCoins -= product.getPriceInCoins();
        return PRODUCT_BUY_SUCCESSFUL;
    }

}
