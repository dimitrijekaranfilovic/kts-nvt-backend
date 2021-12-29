package com.ktsnvt.ktsnvt.integration.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.ktsnvt.ktsnvt.dto.createmenuitem.CreateMenuItemRequest;
import com.ktsnvt.ktsnvt.dto.updatemenuitemprice.UpdateMenuItemPriceRequest;
import com.ktsnvt.ktsnvt.model.enums.ItemCategory;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
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
@Transactional
@AutoConfigureMockMvc
class MenuItemControllerTest extends AuthorizingControllerMockMvcTestBase {

    @LocalServerPort
    private int port;


    @Autowired
    protected MenuItemControllerTest(WebApplicationContext webApplicationContext, ObjectMapper mapper) {
        super(webApplicationContext, mapper);
    }

    @BeforeEach
    public void loginAsSuperUser() throws Exception {
        login("email2@email.com", "password");
    }

    @Test
    void getPaginatedMenuItems_withValidData_isSuccess() throws Exception {
        String name = "";
        int page = 0;
        int size = 8;
        String sort = "item.name";

        String url = String.format("http://localhost:%d/api/menu-items?name=%s&page=%d&size=%d&sort=%s", this.port, name, page, size, sort);

        this.mockMvc.perform(MockMvcRequestBuilders.get(url))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", Matchers.hasSize(5)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalPages").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalElements").value(5));

    }

    @Test
    void getPaginatedMenuItems_whenNameDoesNotExist_isEmptyPage() throws Exception {
        String name = "some-test-name";
        int page = 0;
        int size = 8;
        String sort = "item.name";

        String url = String.format("http://localhost:%d/api/menu-items?name=%s&page=%d&size=%d&sort=%s", this.port, name, page, size, sort);

        this.mockMvc.perform(MockMvcRequestBuilders.get(url))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", Matchers.hasSize(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalPages").value(0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalElements").value(0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.empty").value(true));

    }

    @ParameterizedTest
    @MethodSource("provideValidCreateMenuItemRequests")
    void createMenuItem_calledWithValidData_isSuccess(CreateMenuItemRequest request) throws Exception {
        mockMvc.perform(post("/api/menu-items/")
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .content(mapper.writeValueAsString(request)))
                .andExpectAll(
                        status().isCreated(),
                        jsonPath("$.id", notNullValue()),
                        jsonPath("$.id", greaterThanOrEqualTo(0))
                );
    }

    @Test
    void createMenuItem_calledWithNonExistingInventoryItem_isBadRequest() throws Exception {
        var request = new CreateMenuItemRequest(42, BigDecimal.valueOf(42));
        mockMvc.perform(post("/api/menu-items")
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .content(mapper.writeValueAsString(request)))
                .andExpectAll(
                        status().isBadRequest()
                );
    }

    @ParameterizedTest
    @MethodSource("provideValidDataForMenuItemPriceUpdate")
    void updatePrice_calledWithValidData_isSuccess(Integer id, UpdateMenuItemPriceRequest request) throws Exception {
        mockMvc.perform(post("/api/menu-items/{id}/price", id)
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
    @ValueSource(ints = {28, 42, -1})
    void updatePrice_calledWithNonExistingId_isBadRequest(Integer id) throws Exception {
        var request = new UpdateMenuItemPriceRequest(BigDecimal.valueOf(42));
        mockMvc.perform(post("/api/menu-items/{id}/price", id)
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .content(mapper.writeValueAsString(request)))
                .andExpectAll(
                        status().isBadRequest()
                );
    }

    @ParameterizedTest
    @ValueSource(ints = {4, 5})
    void deactivateMenuItem_calledWithValidId_isSuccess(Integer id) throws Exception {
        mockMvc.perform(delete("/api/menu-items/{id}", id)
                .header("Authorization", "Bearer " + token))
                .andExpectAll(
                        status().isNoContent()
                );
    }

    @ParameterizedTest
    @ValueSource(ints = {6, 7})
    void deactivateMenuItem_calledWithDeactivateMenuItemId_isBadRequest(Integer id) throws Exception {
        mockMvc.perform(delete("/api/menu-items/{id}", id)
                .header("Authorization", "Bearer " + token))
                .andExpectAll(
                        status().isBadRequest()
                );
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3})
    void deactivateMenuItem_calledWithMenuItemWithActiveOrdersId_isBadRequest(Integer id) throws Exception {
        mockMvc.perform(delete("/api/menu-items/{id}", id)
                .header("Authorization", "Bearer " + token))
                .andExpectAll(
                        status().isBadRequest()
                );
    }


    @ParameterizedTest
    @MethodSource("provideArgumentsAndExpectedValueForReadingPaginatedMenuItems")
    void readMenuItems_calledWithValidArguments_isSuccess(String query, BigDecimal basePriceFrom,
                                                          BigDecimal basePriceTo, ItemCategory itemCategory,
                                                          Pageable pageable, int expected) throws Exception {
        mockMvc.perform(get("/api/menu-items/search")
                .header("Authorization", "Bearer" + token)
                .param("query", query)
                .param("priceLowerBound", Objects.requireNonNullElse(basePriceFrom, "").toString())
                .param("priceUpperBound", Objects.requireNonNullElse(basePriceTo, "").toString())
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
    private static Stream<Arguments> provideValidCreateMenuItemRequests() {
        return Stream.of(
                Arguments.of(new CreateMenuItemRequest(1, BigDecimal.valueOf(42))),
                Arguments.of(new CreateMenuItemRequest(2, BigDecimal.valueOf(28))),
                Arguments.of(new CreateMenuItemRequest(3, BigDecimal.valueOf(322))),
                Arguments.of(new CreateMenuItemRequest(4, BigDecimal.valueOf(496))),
                Arguments.of(new CreateMenuItemRequest(5, BigDecimal.valueOf(496))),
                Arguments.of(new CreateMenuItemRequest(6, BigDecimal.valueOf(496))),
                Arguments.of(new CreateMenuItemRequest(7, BigDecimal.valueOf(496))),
                Arguments.of(new CreateMenuItemRequest(8, BigDecimal.valueOf(496))),
                Arguments.of(new CreateMenuItemRequest(9, BigDecimal.valueOf(496))),
                Arguments.of(new CreateMenuItemRequest(10, BigDecimal.valueOf(496))),
                Arguments.of(new CreateMenuItemRequest(11, BigDecimal.valueOf(496))),
                Arguments.of(new CreateMenuItemRequest(12, BigDecimal.valueOf(496)))
        );
    }

    @SuppressWarnings("unused")
    private static Stream<Arguments> provideValidDataForMenuItemPriceUpdate() {
        return Stream.of(
                Arguments.of(1, new UpdateMenuItemPriceRequest(BigDecimal.valueOf(42))),
                Arguments.of(2, new UpdateMenuItemPriceRequest(BigDecimal.valueOf(496))),
                Arguments.of(3, new UpdateMenuItemPriceRequest(BigDecimal.valueOf(28))),
                Arguments.of(4, new UpdateMenuItemPriceRequest(BigDecimal.valueOf(322))),
                Arguments.of(5, new UpdateMenuItemPriceRequest(BigDecimal.valueOf(322)))
        );
    }

    @SuppressWarnings("unused")
    private static Stream<Arguments> provideArgumentsAndExpectedValueForReadingPaginatedMenuItems() {
        return Stream.of(
                Arguments.of("", null, null,
                        null, PageRequest.of(0, 20), 7),
                Arguments.of(" iCe ", BigDecimal.valueOf(0), BigDecimal.valueOf(500),
                        "DRINK", PageRequest.of(0, 10), 1),
                Arguments.of(" iCe ", null, null,
                        null, PageRequest.of(0, 10), 2),
                Arguments.of("ice", BigDecimal.valueOf(1000), BigDecimal.valueOf(2000),
                        null, PageRequest.of(0, 10), 0),
                Arguments.of(" iCe ", BigDecimal.valueOf(0), BigDecimal.valueOf(42),
                        null, PageRequest.of(0, 10), 0),
                Arguments.of("ice", BigDecimal.valueOf(0), BigDecimal.valueOf(500),
                        null, PageRequest.of(0, 5), 2),
                Arguments.of("ice", BigDecimal.valueOf(0), BigDecimal.valueOf(500),
                        null, PageRequest.of(0, 10), 2),
                Arguments.of(" aK ", BigDecimal.valueOf(0), BigDecimal.valueOf(1000),
                        ItemCategory.FOOD, PageRequest.of(0, 10), 3),
                Arguments.of("non existing string", BigDecimal.valueOf(0), BigDecimal.valueOf(1000),
                        ItemCategory.FOOD, PageRequest.of(0, 10), 0)
        );
    }


}
