package com.ktsnvt.ktsnvt.integration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ktsnvt.ktsnvt.dto.addorderitem.AddOrderItemRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Transactional
class OrderItemControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    void addOrderItem_withValidData_isSuccess() throws Exception{
        var body = new AddOrderItemRequest(2, 1, 1, "4321");
        this.mockMvc.perform(MockMvcRequestBuilders.post(String.format("http://localhost:%d/api/order-items", this.port))
                .content(objectMapper.writeValueAsString(body))
                .contentType("application/json"))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @ParameterizedTest
    @ValueSource(ints = {100, 101})
    void addOrderItem_whenGroupDoesNotExist_throwsException (int groupId) throws Exception {
        var body = new AddOrderItemRequest(groupId, 1, 1, "4321");
        this.mockMvc.perform(MockMvcRequestBuilders.post(String.format("http://localhost:%d/api/order-items", this.port))
                        .content(objectMapper.writeValueAsString(body))
                        .contentType("application/json"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @ParameterizedTest
    @ValueSource(ints = {100, 101})
    void addOrderItem_whenMenuitemDoesNotExist_throwsException (int menuItemId) throws Exception {
        var body = new AddOrderItemRequest(2, menuItemId, 1, "4321");
        this.mockMvc.perform(MockMvcRequestBuilders.post(String.format("http://localhost:%d/api/order-items", this.port))
                        .content(objectMapper.writeValueAsString(body))
                        .contentType("application/json"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 0})
    void addOrderItem_whenAmountIsNotPositive_throwsException (int amount) throws Exception {
        var body = new AddOrderItemRequest(2, 1, amount, "4321");
        this.mockMvc.perform(MockMvcRequestBuilders.post(String.format("http://localhost:%d/api/order-items", this.port))
                        .content(objectMapper.writeValueAsString(body))
                        .contentType("application/json"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @ParameterizedTest
    @ValueSource(strings = {"9998", "9999"})
    void addOrderItem_withNonExistentPin_throwsException (String pin) throws Exception {
        var body = new AddOrderItemRequest(2, 1, 1, pin);
        this.mockMvc.perform(MockMvcRequestBuilders.post(String.format("http://localhost:%d/api/order-items", this.port))
                        .content(objectMapper.writeValueAsString(body))
                        .contentType("application/json"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @ParameterizedTest
    @ValueSource(strings = {"1234", "5678"})
    void addOrderItem_whenNotResponsibleEmployeeTriesToAdd_throwsException (String pin) throws Exception {
        var body = new AddOrderItemRequest(2, 1, 1, pin);
        this.mockMvc.perform(MockMvcRequestBuilders.post(String.format("http://localhost:%d/api/order-items", this.port))
                        .content(objectMapper.writeValueAsString(body))
                        .contentType("application/json"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

}
