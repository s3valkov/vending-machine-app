package com.s3valkov.app;

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

    private static final String JSON_PAYLOAD_PRODUCT_ADD = "{\"id\": 1, \"name\": \"TestProduct\", \"price\": 1.25, \"quantity\": 5}";
    private static final String JSON_PAYLOAD_INVALID_PRODUCT = "{\"id\": 3, \"name\": \"InvalidProduct\", \"price\": -1.25, \"quantity\": -5}";

    @BeforeEach
    void setup() throws Exception {
        // Reset the vending machine state before each test
        mockMvc.perform(MockMvcRequestBuilders.post("/api/reset"));
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
        addProduct(JSON_PAYLOAD_PRODUCT_ADD);

        // Insert enough coins
        mockMvc.perform(MockMvcRequestBuilders.post("/api/insert-coin/200"));
        mockMvc.perform(MockMvcRequestBuilders.post("/api/insert-coin/50"));

        // Buy the product
        mockMvc.perform(MockMvcRequestBuilders.post("/api/buy/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(PRODUCT_BUY_SUCCESSFUL));
    }

    @Test
    void testInsufficientCoins() throws Exception {
        String jsonPayload = "{\"id\": 2, \"name\": \"TestProduct\", \"price\": 1.25, \"quantity\": 5}";
        mockMvc.perform(MockMvcRequestBuilders.post("/api/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPayload))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(PRODUCT_ADDED_SUCCESSFUL));
        // Assuming productId 2 exists in the inventory and requires more than the inserted coins
        mockMvc.perform(MockMvcRequestBuilders.post("/api/insert-coin/100"))
                .andExpect(MockMvcResultMatchers.status().isOk());

        mockMvc.perform(MockMvcRequestBuilders.post("/api/buy/2"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(INSUFFICIENT_COINS_ERROR_MESSAGE));
    }

    @Test
    void testRemoveProduct() throws Exception {
        addProduct(JSON_PAYLOAD_PRODUCT_ADD);
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/remove/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(PRODUCT_REMOVE_SUCCESS_MESSAGE));
    }


    @Test
    void testInvalidCoin() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/insert-coin/5"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(INVALID_COIN_ERROR_MESSAGE));
    }

    @Test
    void testInsertCoin() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/insert-coin/50"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(VALID_COIN_INSERTION +  50));
    }

    public ResultActions addProduct(String jsonPayload) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.post("/api/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonPayload));
    }
}

