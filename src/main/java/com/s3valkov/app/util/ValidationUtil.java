package com.s3valkov.app.util;

import com.s3valkov.app.model.Product;
import java.util.Arrays;

import static com.s3valkov.app.util.Constants.VALID_COINS;

public class ValidationUtil {

    public static boolean isValidCoin(int coin) {
        return Arrays.stream(VALID_COINS)
                .anyMatch(acceptedCoin -> acceptedCoin == coin);
    }

    public static boolean isProductValid(Product product) {
        return product.getPriceInCoins() > 0 && product.getQuantity() > 0;
    }
}
