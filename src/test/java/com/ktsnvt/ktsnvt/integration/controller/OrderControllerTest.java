package com.ktsnvt.ktsnvt.integration.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.ktsnvt.ktsnvt.dto.createorderitemgroup.CreateOrderItemGroupRequest;
import com.ktsnvt.ktsnvt.dto.deleteorderitemgroup.DeleteOrderItemGroupRequest;
import com.ktsnvt.ktsnvt.dto.sendorderitemgroup.SendOrderItemGroupRequest;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Stream;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Transactional
class OrderControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    private static Stream<Arguments> provideGroupCreationData() {
        return Stream.of(
                Arguments.of("some group 1", 2),
                Arguments.of("some group 2", 3)
        );
    }

    private static Stream<Arguments> provideGroupSendingAndDeletingData() {
        return Stream.of(
                Arguments.of(2, 5),
                Arguments.of(3, 6)
        );
    }

    private static Stream<Arguments> provideGetOrderItemGroupsData(){
        return Stream.of(
                Arguments.of(1, 2),
                Arguments.of(2, 1),
                Arguments.of(3, 2),
                Arguments.of(4, 1)
        );
    }


    @ParameterizedTest
    @MethodSource("provideGroupCreationData")
    void createOrderItemGroup_withValidData_isSuccess(String groupName, int orderId) throws Exception {
        var body = new CreateOrderItemGroupRequest(groupName, "4321");
        this.mockMvc.perform(MockMvcRequestBuilders.post(String.format("http://localhost:%d/api/orders/%d/groups", this.port, orderId))
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"));
    }

    @ParameterizedTest
    @ValueSource(ints = {20, 30, 40})
    void createOrderItemGroup_whenOrderDoesNotExist_isNotFound(int orderId) throws Exception {
        var body = new CreateOrderItemGroupRequest("some group", "4321");
        this.mockMvc.perform(MockMvcRequestBuilders.post(String.format("http://localhost:%d/api/orders/%d/groups", this.port, orderId))
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"9997", "9998", "9999"})
    void createOrderItemGroup_whenNonExistentPinIsUsed_isNotFound(String pin) throws Exception {
        var body = new CreateOrderItemGroupRequest("some group", pin);
        int orderId = 2;
        this.mockMvc.perform(MockMvcRequestBuilders.post(String.format("http://localhost:%d/api/orders/%d/groups", this.port, orderId))
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"1234", "5678"})
    void createOrderItemGroup_whenNotResponsibleEmployeeTriesToCreate_isBadRequest(String pin) throws Exception {
        var body = new CreateOrderItemGroupRequest("some group", pin);
        int orderId = 2;
        this.mockMvc.perform(MockMvcRequestBuilders.post(String.format("http://localhost:%d/api/orders/%d/groups", this.port, orderId))
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"));
    }

    @ParameterizedTest
    @MethodSource("provideGroupSendingAndDeletingData")
    void sendOrderItemGroup_withValidData_isSuccess(int orderId, int groupId) throws Exception {
        var body = new SendOrderItemGroupRequest("4321");
        this.mockMvc.perform(MockMvcRequestBuilders.put(String.format("http://localhost:%d/api/orders/%d/groups/%d", this.port, orderId, groupId))
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @ParameterizedTest
    @ValueSource(ints = {40, 50})
    void sendOrderItemGroup_whenGroupDoesNotExist_isNotFound(int groupId) throws Exception {
        var body = new SendOrderItemGroupRequest("4321");
        int orderId = 2;
        this.mockMvc.perform(MockMvcRequestBuilders.put(String.format("http://localhost:%d/api/orders/%d/groups/%d", this.port, orderId, groupId))
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }


    @ParameterizedTest
    @ValueSource(strings = {"9998", "9999"})
    void sendOrderItemGroup_whenNonExistentPinIsUsed_isNotFound(String pin) throws Exception {
        var body = new SendOrderItemGroupRequest(pin);
        int orderId = 2;
        int groupId = 5;
        this.mockMvc.perform(MockMvcRequestBuilders.put(String.format("http://localhost:%d/api/orders/%d/groups/%d", this.port, orderId, groupId))
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @ParameterizedTest
    @ValueSource(strings = {"1234", "5678"})
    void sendOrderItemGroup_whenNotResponsibleEmployeeTriesToSend_isBadRequest(String pin) throws Exception{
        var body = new SendOrderItemGroupRequest(pin);
        int orderId = 2;
        int groupId = 5;
        this.mockMvc.perform(MockMvcRequestBuilders.put(String.format("http://localhost:%d/api/orders/%d/groups/%d", this.port, orderId, groupId))
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @ParameterizedTest
    @MethodSource("provideGetOrderItemGroupsData")
    void getOrderItemGroups_withValidData_isSuccess(int orderId, int numOfGroups) throws Exception{
        this.mockMvc.perform(MockMvcRequestBuilders.get(String.format("http://localhost:%d/api/orders/%d/groups",this.port, orderId)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(numOfGroups)))
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"));
    }

    @ParameterizedTest
    @ValueSource(ints = {40, 50})
    void getOrderItemGroups_whenGroupDoesNotExist_isResultListEmpty(int orderId) throws Exception{
        this.mockMvc.perform(MockMvcRequestBuilders.get(String.format("http://localhost:%d/api/orders/%d/groups",this.port, orderId)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(0)))
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"));
    }

    @ParameterizedTest
    @MethodSource("provideGroupSendingAndDeletingData")
    void deleteOrderItemGroup_withValidData_isSuccess(int orderId, int groupId) throws Exception {
        var body = new DeleteOrderItemGroupRequest("4321");
        this.mockMvc.perform(MockMvcRequestBuilders.delete(String.format("http://localhost:%d/api/orders/%d/groups/%d", this.port, orderId, groupId))
                        .content(objectMapper.writeValueAsString(body))
                        .contentType("application/json"))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @ParameterizedTest
    @ValueSource(ints = {40, 50})
    void deleteOrderItemGroup_whenGroupDoesNotExist_isNotFound(int groupId) throws Exception {
        var body = new DeleteOrderItemGroupRequest("4321");
        int orderId = 2;
        this.mockMvc.perform(MockMvcRequestBuilders.delete(String.format("http://localhost:%d/api/orders/%d/groups/%d", this.port, orderId, groupId))
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @ParameterizedTest
    @ValueSource(strings = {"9998", "9999"})
    void deleteOrderItemGroup_whenNonExistentPinIsUsed_isNotFound(String pin) throws Exception {
        var body = new DeleteOrderItemGroupRequest(pin);
        int orderId = 2;
        int groupId = 5;
        this.mockMvc.perform(MockMvcRequestBuilders.delete(String.format("http://localhost:%d/api/orders/%d/groups/%d", this.port, orderId, groupId))
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @ParameterizedTest
    @ValueSource(strings = {"1234", "5678"})
    void deleteOrderItemGroup_whenNotResponsibleEmployeeTriesToSend_isBadRequest(String pin) throws Exception{
        var body = new DeleteOrderItemGroupRequest(pin);
        int orderId = 2;
        int groupId = 5;
        this.mockMvc.perform(MockMvcRequestBuilders.delete(String.format("http://localhost:%d/api/orders/%d/groups/%d", this.port, orderId, groupId))
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }


}
