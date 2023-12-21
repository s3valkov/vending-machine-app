package com.s3valkov.app.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static com.s3valkov.app.controller.VendingController.BUY_PRODUCT_URL_PART;
import static com.s3valkov.app.controller.VendingController.INSERT_COIN_URL_PART;
import static com.s3valkov.app.controller.VendingController.INSERT_COIN_URL_RESET;
import static com.s3valkov.app.controller.VendingController.PRODUCTS_URL_PART;
import static com.s3valkov.app.util.Constants.INSUFFICIENT_COINS_ERROR_MESSAGE;
import static com.s3valkov.app.util.Constants.INVALID_COIN_ERROR_MESSAGE;
import static com.s3valkov.app.util.Constants.INVALID_PRODUCT_DETAILS;
import static com.s3valkov.app.util.Constants.PRODUCT_ADDED_SUCCESSFUL;
import static com.s3valkov.app.util.Constants.PRODUCT_BUY_SUCCESSFUL;
import static com.s3valkov.app.util.Constants.PRODUCT_REMOVE_SUCCESS_MESSAGE;
import static com.s3valkov.app.util.Constants.VALID_COIN_INSERTION;

@SpringBootTest
@AutoConfigureMockMvc
class VendingMachineControllerTests {

    @Autowired
    private MockMvc mockMvc;

    private static final String JSON_PAYLOAD_PRODUCT_ADD = "{\"name\": \"TestProduct\", \"price\": 125, \"quantity\": 5}";
    private static final String JSON_PAYLOAD_INVALID_PRODUCT = "{\"name\": \"InvalidProduct\", \"price\": -125, \"quantity\": -5}";

    @BeforeEach
    void setup() throws Exception {
        addProduct(JSON_PAYLOAD_PRODUCT_ADD);
        mockMvc.perform(MockMvcRequestBuilders.post("/api" + INSERT_COIN_URL_RESET));
    }

    @Test
    void testAddProduct() throws Exception {
        ResultActions result = addProduct(JSON_PAYLOAD_PRODUCT_ADD);

        result.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(PRODUCT_ADDED_SUCCESSFUL));
    }

    @Test
    void testProductWithNegativePriceAndQuantity() throws Exception {
        ResultActions result = addProduct(JSON_PAYLOAD_INVALID_PRODUCT);

        result.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(INVALID_PRODUCT_DETAILS));
    }

    @Test
    void testBuyProduct() throws Exception {
        // Insert enough coins
        mockMvc.perform(MockMvcRequestBuilders.post("/api"+ INSERT_COIN_URL_PART + "/200"));
        mockMvc.perform(MockMvcRequestBuilders.post("/api"+ INSERT_COIN_URL_PART + "/50"));

        // Buy the product
        mockMvc.perform(MockMvcRequestBuilders.post("/api" + BUY_PRODUCT_URL_PART + "/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(PRODUCT_BUY_SUCCESSFUL));
    }

    @Test
    void testInsufficientCoins() throws Exception {
        addProduct(JSON_PAYLOAD_PRODUCT_ADD);

        mockMvc.perform(MockMvcRequestBuilders.post("/api"+ INSERT_COIN_URL_PART + "/50"))
                .andExpect(MockMvcResultMatchers.status().isOk());

        mockMvc.perform(MockMvcRequestBuilders.post("/api" + BUY_PRODUCT_URL_PART + "/2"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(INSUFFICIENT_COINS_ERROR_MESSAGE));
    }

    @Test
    void testRemoveProduct() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api" + PRODUCTS_URL_PART + "/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(PRODUCT_REMOVE_SUCCESS_MESSAGE));
    }

    @Test
    void testInvalidCoin() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api"+ INSERT_COIN_URL_PART + "/5"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(INVALID_COIN_ERROR_MESSAGE));
    }

    @Test
    void testInsertCoin() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api"+ INSERT_COIN_URL_PART + "/50"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(VALID_COIN_INSERTION +  50));
    }

    public ResultActions addProduct(String jsonPayload) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.post("/api" + PRODUCTS_URL_PART)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonPayload));
    }
}

