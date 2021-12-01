package com.ktsnvt.ktsnvt.integration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ktsnvt.ktsnvt.dto.addorderitem.AddOrderItemRequest;
import com.ktsnvt.ktsnvt.dto.deleteorderitem.DeleteOrderItemRequest;
import com.ktsnvt.ktsnvt.dto.updateorderitem.UpdateOrderItemRequest;
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
    void addOrderItem_withValidData_isSuccess() throws Exception {
        var body = new AddOrderItemRequest(2, 1, 1, "4321");
        this.mockMvc.perform(MockMvcRequestBuilders.post(String.format("http://localhost:%d/api/order-items", this.port))
                        .content(objectMapper.writeValueAsString(body))
                        .contentType("application/json"))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @ParameterizedTest
    @ValueSource(ints = {100, 101})
    void addOrderItem_whenGroupDoesNotExist_throwsException(int groupId) throws Exception {
        var body = new AddOrderItemRequest(groupId, 1, 1, "4321");
        this.mockMvc.perform(MockMvcRequestBuilders.post(String.format("http://localhost:%d/api/order-items", this.port))
                        .content(objectMapper.writeValueAsString(body))
                        .contentType("application/json"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @ParameterizedTest
    @ValueSource(ints = {100, 101})
    void addOrderItem_whenMenuitemDoesNotExist_throwsException(int menuItemId) throws Exception {
        var body = new AddOrderItemRequest(2, menuItemId, 1, "4321");
        this.mockMvc.perform(MockMvcRequestBuilders.post(String.format("http://localhost:%d/api/order-items", this.port))
                        .content(objectMapper.writeValueAsString(body))
                        .contentType("application/json"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 0})
    void addOrderItem_whenAmountIsNotPositive_throwsException(int amount) throws Exception {
        var body = new AddOrderItemRequest(2, 1, amount, "4321");
        this.mockMvc.perform(MockMvcRequestBuilders.post(String.format("http://localhost:%d/api/order-items", this.port))
                        .content(objectMapper.writeValueAsString(body))
                        .contentType("application/json"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @ParameterizedTest
    @ValueSource(strings = {"9998", "9999"})
    void addOrderItem_withNonExistentPin_throwsException(String pin) throws Exception {
        var body = new AddOrderItemRequest(2, 1, 1, pin);
        this.mockMvc.perform(MockMvcRequestBuilders.post(String.format("http://localhost:%d/api/order-items", this.port))
                        .content(objectMapper.writeValueAsString(body))
                        .contentType("application/json"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @ParameterizedTest
    @ValueSource(strings = {"1234", "5678"})
    void addOrderItem_whenNotResponsibleEmployeeTriesToAdd_throwsException(String pin) throws Exception {
        var body = new AddOrderItemRequest(2, 1, 1, pin);
        this.mockMvc.perform(MockMvcRequestBuilders.post(String.format("http://localhost:%d/api/order-items", this.port))
                        .content(objectMapper.writeValueAsString(body))
                        .contentType("application/json"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void updateOrderItem_withValidData_isSuccess() throws Exception {
        var body = new UpdateOrderItemRequest(2, "4321");
        int orderItemId = 12;
        this.mockMvc.perform(MockMvcRequestBuilders.put(String.format("http://localhost:%d/api/order-items/%d", this.port, orderItemId))
                .content(objectMapper.writeValueAsString(body))
                .contentType("application/json"))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }


    @ParameterizedTest
    @ValueSource(ints = {100, 101})
    void updateOrderItem_whenItemDoesNotExist_throwsException(int orderItemId) throws Exception {
        var body = new UpdateOrderItemRequest(2, "4321");
        this.mockMvc.perform(MockMvcRequestBuilders.put(String.format("http://localhost:%d/api/order-items/%d", this.port, orderItemId))
                        .content(objectMapper.writeValueAsString(body))
                        .contentType("application/json"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 5})
    void updateOrderItem_whenItemStatusIsNotNew_throwsException(int orderItemId) throws Exception {
        var body = new UpdateOrderItemRequest(2, "4321");
        this.mockMvc.perform(MockMvcRequestBuilders.put(String.format("http://localhost:%d/api/order-items/%d", this.port, orderItemId))
                        .content(objectMapper.writeValueAsString(body))
                        .contentType("application/json"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 0})
    void updateOrderItem_whenAmountIsNotPositive_throwsException(int amount) throws Exception {
        var body = new UpdateOrderItemRequest(amount, "4321");
        int orderItemId = 12;
        this.mockMvc.perform(MockMvcRequestBuilders.put(String.format("http://localhost:%d/api/order-items/%d", this.port, orderItemId))
                        .content(objectMapper.writeValueAsString(body))
                        .contentType("application/json"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }


    @ParameterizedTest
    @ValueSource(strings = {"9998", "9999"})
    void updateOrderItem_withNonExistentPin_throwsException(String pin) throws Exception {
        var body = new UpdateOrderItemRequest(2, pin);
        int orderItemId = 12;
        this.mockMvc.perform(MockMvcRequestBuilders.put(String.format("http://localhost:%d/api/order-items/%d", this.port, orderItemId))
                        .content(objectMapper.writeValueAsString(body))
                        .contentType("application/json"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @ParameterizedTest
    @ValueSource(strings = {"1234", "5678"})
    void updateOrderItem_whenNotResponsibleEmployeeTriesToUpdate_throwsException(String pin) throws Exception {
        var body = new UpdateOrderItemRequest(2, pin);
        int orderItemId = 12;
        this.mockMvc.perform(MockMvcRequestBuilders.put(String.format("http://localhost:%d/api/order-items/%d", this.port, orderItemId))
                        .content(objectMapper.writeValueAsString(body))
                        .contentType("application/json"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void deleteOrderItem_withValidData_isSuccess() throws Exception {
        var body = new DeleteOrderItemRequest("4321");
        int orderItemId = 12;
        this.mockMvc.perform(MockMvcRequestBuilders.delete(String.format("http://localhost:%d/api/order-items/%d", this.port, orderItemId))
                        .content(objectMapper.writeValueAsString(body))
                        .contentType("application/json"))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @ParameterizedTest
    @ValueSource(ints = {100, 101})
    void deleteOrderItem_whenItemDoesNotExist_throwsException(int orderItemId) throws Exception {
        var body = new DeleteOrderItemRequest("4321");
        this.mockMvc.perform(MockMvcRequestBuilders.delete(String.format("http://localhost:%d/api/order-items/%d", this.port, orderItemId))
                        .content(objectMapper.writeValueAsString(body))
                        .contentType("application/json"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 5})
    void deleteOrderItem_whenItemStatusIsNotNew_throwsException(int orderItemId) throws Exception {
        var body = new DeleteOrderItemRequest("4321");
        this.mockMvc.perform(MockMvcRequestBuilders.delete(String.format("http://localhost:%d/api/order-items/%d", this.port, orderItemId))
                        .content(objectMapper.writeValueAsString(body))
                        .contentType("application/json"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @ParameterizedTest
    @ValueSource(strings = {"9998", "9999"})
    void deleteOrderItem_withNonExistentPin_throwsException(String pin) throws Exception {
        var body = new DeleteOrderItemRequest(pin);
        int orderItemId = 12;
        this.mockMvc.perform(MockMvcRequestBuilders.delete(String.format("http://localhost:%d/api/order-items/%d", this.port, orderItemId))
                        .content(objectMapper.writeValueAsString(body))
                        .contentType("application/json"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @ParameterizedTest
    @ValueSource(strings = {"1234", "5678"})
    void deleteOrderItem_whenNotResponsibleEmployeeTriesToUpdate_throwsException(String pin) throws Exception {
        var body = new DeleteOrderItemRequest(pin);
        int orderItemId = 12;
        this.mockMvc.perform(MockMvcRequestBuilders.delete(String.format("http://localhost:%d/api/order-items/%d", this.port, orderItemId))
                        .content(objectMapper.writeValueAsString(body))
                        .contentType("application/json"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }


}
