package com.ktsnvt.ktsnvt.integration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ktsnvt.ktsnvt.dto.createinventoryitem.CreateInventoryItemRequest;
import com.ktsnvt.ktsnvt.dto.updateinventoryitem.UpdateInventoryItemRequest;
import com.ktsnvt.ktsnvt.model.enums.ItemCategory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
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

    @ParameterizedTest
    @ValueSource(ints = {4, 9, 10, 11, 12})
    void deleteInventoryItem_calledWithValidId_isSuccess(Integer id) throws Exception {
        mockMvc.perform(delete("/api/inventory-items/{id}", id)
                .header("Authorization", "Bearer " + token))
                .andExpectAll(
                        status().isNoContent()
                );
    }

    @ParameterizedTest
    @ValueSource(ints = {42, 28, -1})
    void deleteInventoryItem_calledWithNonExistingId_isBadRequest(Integer id) throws Exception {
        mockMvc.perform(delete("/api/inventory-items/{id}", id)
                .header("Authorization", "Bearer " + token))
                .andExpectAll(
                        status().isBadRequest()
                );
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3})
    void deleteInventoryItem_calledWithInventoryItemThatIsContainedInNonFinalizedOrder_isBadRequest(Integer id)
            throws Exception {
        mockMvc.perform(delete("/api/inventory-items/{id}", id)
                .header("Authorization", "Bearer " + token))
                .andExpectAll(
                        status().isBadRequest()
                );
    }

    @ParameterizedTest
    @ValueSource(ints = {5, 6, 7, 8})
    void deleteInventoryItem_calledWithItemsWithNoBasePrice_isBadRequest(Integer id) throws Exception {
        mockMvc.perform(delete("/api/inventory-items/{id}", id)
                .header("Authorization", "Bearer " + token))
                .andExpectAll(
                        status().isBadRequest()
                );
    }

    @ParameterizedTest
    @MethodSource("provideValidDataForUpdatingInventoryItems")
    void update_calledWithValidData_isSuccess(Integer id, UpdateInventoryItemRequest request) throws Exception {
        mockMvc.perform(put("/api/inventory-items/{id}", id)
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .content(mapper.writeValueAsString(request)))
                .andExpectAll(
                        status().isOk()
                );
    }

    @ParameterizedTest
    @MethodSource("provideTakenNameArgumentsForInventoryItemUpdate")
    void update_calledWithTakenName_isBadRequest(Integer id, UpdateInventoryItemRequest request) throws Exception {
        mockMvc.perform(put("/api/inventory-items/{id}", id)
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .content(mapper.writeValueAsString(request)))
                .andExpectAll(
                        status().isBadRequest()
                );
    }

    @ParameterizedTest
    @MethodSource("provideArgumentsAndExpectedValueForReadingPaginatedInventoryItems")
    void readInventoryItems_calledWithValidData_isSuccess(String query, BigDecimal basePriceFrom,
                                                          BigDecimal basePriceTo, ItemCategory itemCategory,
                                                          Pageable pageable, int expected) throws Exception {
        mockMvc.perform(get("/api/inventory-items")
                .header("Authorization", "Bearer " + token)
                .param("query", query)
                .param("basePriceLowerBound", Objects.requireNonNullElse(basePriceFrom, "").toString())
                .param("basePriceUpperBound", Objects.requireNonNullElse(basePriceTo, "").toString())
                .param("category", Objects.requireNonNullElse(itemCategory, "").toString())
                .param("page", String.valueOf(pageable.getPageNumber()))
                .param("size", String.valueOf(pageable.getPageSize())))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.totalElements", equalTo(expected)),
                        jsonPath("$.totalPages", lessThanOrEqualTo(2)),
                        jsonPath("$.content", hasSize(expected))
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

    @SuppressWarnings("unused")
    private static Stream<Arguments> provideValidDataForUpdatingInventoryItems() {
        return Stream.of(
                Arguments.of(1, new UpdateInventoryItemRequest("unique name", BigDecimal.valueOf(496),
                        "new description", "new allergies", "new image", ItemCategory.DRINK)),
                Arguments.of(1, new UpdateInventoryItemRequest("Ice cream", BigDecimal.valueOf(496),
                        "new description", "new allergies", "new image", ItemCategory.DRINK)),
                Arguments.of(1, new UpdateInventoryItemRequest("unique name", BigDecimal.valueOf(496),
                        "new description", "new allergies", "new image", ItemCategory.FOOD)),
                Arguments.of(1, new UpdateInventoryItemRequest("unique name", BigDecimal.valueOf(30),
                        "new description", "new allergies", "new image", ItemCategory.FOOD)),
                Arguments.of(10, new UpdateInventoryItemRequest("Whiskey", BigDecimal.valueOf(42),
                        "new description", "new allergies", "new image", ItemCategory.FOOD))
        );
    }

    @SuppressWarnings("unused")
    private static Stream<Arguments> provideTakenNameArgumentsForInventoryItemUpdate() {
        return Stream.of(
                Arguments.of(1, new UpdateInventoryItemRequest("Wine", BigDecimal.valueOf(496),
                        "new description", "new allergies", "new image", ItemCategory.DRINK)),
                Arguments.of(1, new UpdateInventoryItemRequest("Cake", BigDecimal.valueOf(496),
                        "new description", "new allergies", "new image", ItemCategory.DRINK)),
                Arguments.of(2, new UpdateInventoryItemRequest("Pizza", BigDecimal.valueOf(30),
                        "new description", "new allergies", "new image", ItemCategory.FOOD)),
                Arguments.of(10, new UpdateInventoryItemRequest("Ice cream", BigDecimal.valueOf(42),
                        "new description", "new allergies", "new image", ItemCategory.FOOD))
        );
    }

    @SuppressWarnings("unused")
    private static Stream<Arguments> provideArgumentsAndExpectedValueForReadingPaginatedInventoryItems() {
        var pageable = PageRequest.of(0, 20, Sort.unsorted());
        return Stream.of(
                Arguments.of("", null, null, null, pageable, 12),
                Arguments.of("  ice", null, null, null, pageable, 2),
                Arguments.of("ice  ", null, null, null, pageable, 2),
                Arguments.of(" ice  ", null, null, null, pageable, 2),
                Arguments.of("ICE", null, null, null, pageable, 2),
                Arguments.of("", BigDecimal.valueOf(40), BigDecimal.valueOf(60), null, pageable, 3),
                Arguments.of("", BigDecimal.valueOf(10), BigDecimal.valueOf(30), null, pageable, 3),
                Arguments.of("", BigDecimal.valueOf(440), BigDecimal.valueOf(500), null, pageable, 1),
                Arguments.of("", BigDecimal.valueOf(440), BigDecimal.valueOf(30), null, pageable, 0),
                Arguments.of("", BigDecimal.valueOf(0), BigDecimal.valueOf(500), null, pageable, 12),
                Arguments.of("", null, null, ItemCategory.FOOD, pageable, 7),
                Arguments.of(" ice ", BigDecimal.valueOf(0), BigDecimal.valueOf(1000),
                        ItemCategory.DRINK, pageable, 1)
        );
    }

}
