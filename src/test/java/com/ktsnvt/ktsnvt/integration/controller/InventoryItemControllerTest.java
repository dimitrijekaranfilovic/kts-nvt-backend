package com.ktsnvt.ktsnvt.integration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ktsnvt.ktsnvt.dto.createinventoryitem.CreateInventoryItemRequest;
import com.ktsnvt.ktsnvt.model.enums.ItemCategory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest()
@Transactional
class InventoryItemControllerTest extends AuthorizingControllerMockMvcTestBase {

    @Autowired
    protected InventoryItemControllerTest(WebApplicationContext webApplicationContext, ObjectMapper mapper) {
        super(webApplicationContext, mapper);
    }

    @BeforeEach
    void loginSuperUser() throws Exception {
        login("email1@email.com", "password");
    }

    @ParameterizedTest
    @MethodSource("provideValidDataForCreatingInventoryItems")
    void createInventoryItem_calledWithValidData_isSuccess(CreateInventoryItemRequest request) throws Exception {
        mockMvc.perform(post("/api/inventory-items/")
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .content(mapper.writeValueAsString(request)))
                .andExpectAll(
                        status().isCreated(),
                        jsonPath("$.id", notNullValue()),
                        jsonPath("$.id", greaterThanOrEqualTo(0))
                );
    }

    @ParameterizedTest
    @ValueSource(strings = {"Ice cream", "Orange juice", "Pizza", "Steak", "Water", "Cake", "Wine"})
    void createInventoryItem_calledWithExistingName_isBadRequest(String name) throws Exception {
        var request = new CreateInventoryItemRequest(name, BigDecimal.valueOf(42),
                "new description", "new image", "new allergies", ItemCategory.DRINK);
        mockMvc.perform(post("/api/inventory-items/")
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .content(mapper.writeValueAsString(request)))
                .andExpectAll(
                        status().isBadRequest()
                );
    }

    @SuppressWarnings("unused")
    private static Stream<Arguments> provideValidDataForCreatingInventoryItems() {
        return Stream.of(
                Arguments.of(new CreateInventoryItemRequest("new name", BigDecimal.valueOf(42),
                        "new description", "new image", "new allergies", ItemCategory.DRINK)),
                Arguments.of(new CreateInventoryItemRequest("new name1", BigDecimal.valueOf(322),
                        "new description", "new image", "new allergies", ItemCategory.FOOD))
        );
    }

}
