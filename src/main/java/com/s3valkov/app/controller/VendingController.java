package com.s3valkov.app.controller;

import com.s3valkov.app.model.ProductDetails;
import com.s3valkov.app.service.InventoryService;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
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
    static final String PRODUCTS_URL_PART = "/products";
    static final String PRODUCTS_ID_URL_PART = PRODUCTS_URL_PART + "/{productId}";
    static final String INSERT_COIN_URL_PART = "/insert-coin";
    static final String INSERT_COIN_URL_PART_VALUE = INSERT_COIN_URL_PART + "/{coin}";
    static final String INSERT_COIN_URL_RESET = INSERT_COIN_URL_PART + "/reset";
    static final String BUY_PRODUCT_URL_PART = "/buy";
    static final String BUY_PRODUCT_ID_URL_PART = BUY_PRODUCT_URL_PART + "/{productId}";

    @Autowired
    private InventoryService inventory;

    @GetMapping(PRODUCTS_URL_PART)
    public Map<Integer, ProductDetails> getInventory() {
        return inventory.getProducts();
    }

    @PostMapping(PRODUCTS_URL_PART)
    public String addProduct(@RequestBody ProductDetails productDetails) {
        if (!isProductValid(productDetails)) {
            return INVALID_PRODUCT_DETAILS;
        }
        inventory.addProduct(productDetails);
        return PRODUCT_ADDED_SUCCESSFUL;
    }

    @PutMapping(PRODUCTS_ID_URL_PART)
    public String updateProduct(@PathVariable int productId, @RequestBody ProductDetails productDetails) {
        if (!inventory.containsProduct(productId)) {
            return PRODUCT_NOT_FOUND;
        }
        inventory.updateProduct(productId, productDetails);
        return PRODUCT_UPDATED;
    }

    @DeleteMapping(PRODUCTS_ID_URL_PART)
    public String deleteProduct(@PathVariable int productId) {
        if (!inventory.containsProduct(productId)) {
            return PRODUCT_NOT_FOUND;
        }
        inventory.deleteProduct(productId);
        return PRODUCT_REMOVE_SUCCESS_MESSAGE;
    }

    @PostMapping(INSERT_COIN_URL_PART_VALUE)
    public String insertCoin(@PathVariable int coin) {
        if (isValidCoin(coin)) {
            inventory.addCoins(coin);
            return VALID_COIN_INSERTION + inventory.getInsertedCoins();
        } else {
            return INVALID_COIN_ERROR_MESSAGE;
        }
    }

    @PostMapping(INSERT_COIN_URL_RESET)
    public String reset() {
        int returnedCoins = inventory.getInsertedCoins();
        inventory.resetCoins();
        return COINS_RETURNED  + returnedCoins;
    }

    @PostMapping(BUY_PRODUCT_ID_URL_PART)
    public String buyProduct(@PathVariable int productId) {
        if (!inventory.containsProduct(productId)) {
            return PRODUCT_NOT_FOUND;
        }

        ProductDetails productDetails = inventory.getProduct(productId);
        if (productDetails.getQuantity() == 0) {
            return PRODUCT_OUT_OF_STOCK;
        }

        if (inventory.getInsertedCoins() < productDetails.getPrice()) {
            return INSUFFICIENT_COINS_ERROR_MESSAGE;
        }

        productDetails.setQuantity(productDetails.getQuantity() - 1);
        inventory.insertedCoinsAfterBuy(productDetails.getPrice());
        return PRODUCT_BUY_SUCCESSFUL;
    }

}
