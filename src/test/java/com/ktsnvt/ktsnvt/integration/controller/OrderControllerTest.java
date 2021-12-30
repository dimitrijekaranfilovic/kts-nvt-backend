package com.ktsnvt.ktsnvt.integration.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.ktsnvt.ktsnvt.dto.cancelorder.CancelOrderRequest;
import com.ktsnvt.ktsnvt.dto.chargeorder.ChargeOrderRequest;
import com.ktsnvt.ktsnvt.dto.createorder.CreateOrderRequest;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Stream;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


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


    @Test
    void createOrder_whenCalledWithValidData_isSuccess() throws Exception {
        var request = new CreateOrderRequest("4321", 2);
        mockMvc.perform(post("/api/orders")
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(request)))
                .andExpectAll(
                        status().isCreated(),
                        content().contentType("application/json"),
                        jsonPath("$.id", greaterThan(0)),
                        jsonPath("$.waiterPin", equalTo("4321"))
                );
    }


    @ParameterizedTest
    @MethodSource("provideInvalidCreateOrderRequests")
    void createOrder_whenCalledWithInvalidRequest_notCreated(CreateOrderRequest request, int status) throws Exception {
        mockMvc.perform(post("/api/orders")
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(request)))
                .andExpectAll(
                        status().is(status),
                        content().contentType("application/json")
                );
    }

    @ParameterizedTest
    @ValueSource(ints = {3, 8})
    void chargeOrder_whenCalledWithValidData_isSuccess(int orderId) throws Exception {
        var request = new ChargeOrderRequest("4321");
        mockMvc.perform(put("/api/orders/{id}/charge", orderId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpectAll(
                        status().isNoContent()
                );
    }

    @ParameterizedTest
    @MethodSource("provideInvalidChargeOrderRequests")
    void chargeOrder_whenCalledWithInvalidData_notNoContent(ChargeOrderRequest request, int orderId, int status) throws Exception {
        mockMvc.perform(put("/api/orders/{id}/charge", orderId)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(request)))
                .andExpectAll(
                        status().is(status)
                );
    }

    @Test
    void cancelOrder_whenCalledWithValidOrder_isSuccess() throws Exception {
        var request = new CancelOrderRequest("4321");
        var orderId = 2;
        mockMvc.perform(put("/api/orders/{id}/cancel", orderId)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(request)))
                .andExpectAll(
                        status().isNoContent()
                );
    }

    @ParameterizedTest
    @MethodSource("provideInvalidCancelOrderRequests")
    void cancelOrder_whenCalledWithInvalidRequest_notNoContent(CancelOrderRequest request, int orderId, int status) throws Exception {
        mockMvc.perform(put("/api/orders/{id}/cancel", orderId)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(request)))
                .andExpectAll(
                        status().is(status)
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


    @Test
    void getOrderIdForTable_withValidData_isSuccess() throws Exception{
        Integer tableId = 2;
        String url = String.format("http://localhost:%d/api/orders/for-table?tableId=%d", this.port, tableId);
        this.mockMvc.perform(MockMvcRequestBuilders.get(url))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").value(9));
    }

    @Test
    void getOrderIdForTable_whenOrderDoesNotExist_returnsMinusOne() throws Exception{
        Integer tableId = 25;
        String url = String.format("http://localhost:%d/api/orders/for-table?tableId=%d", this.port, tableId);
        this.mockMvc.perform(MockMvcRequestBuilders.get(url))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").value(-1));
    }

    @SuppressWarnings("unused")
    private static Stream<Arguments> provideInvalidCreateOrderRequests() {
        return Stream.of(
                Arguments.of(new CreateOrderRequest(null, null), 400),    // invalid request
                Arguments.of(new CreateOrderRequest("", 2), 400),         // invalid request
                Arguments.of(new CreateOrderRequest("1234", 2), 404),     // waiter not found
                Arguments.of(new CreateOrderRequest("4321", 10), 400),    // busy table
                Arguments.of(new CreateOrderRequest("4321", 153), 404)    // table not found
        );
    }

    @SuppressWarnings("unused")
    private static Stream<Arguments> provideInvalidChargeOrderRequests() {
        return Stream.of(
                Arguments.of(new ChargeOrderRequest(null), 3, 400),             // invalid request
                Arguments.of(new ChargeOrderRequest(""), 3, 400),               // invalid request
                Arguments.of(new ChargeOrderRequest("4321"), 2, 400),           // order not in progress
                Arguments.of(new ChargeOrderRequest("4321"), 6, 400)            // order not finished
        );
    }

    @SuppressWarnings("unused")
    private static Stream<Arguments> provideInvalidCancelOrderRequests() {
        return Stream.of(
                Arguments.of(new CancelOrderRequest(null), 2, 400),             // invalid request
                Arguments.of(new CancelOrderRequest(""), 2, 400),               // invalid request
                Arguments.of(new CancelOrderRequest("4321"), 1, 400),           // order already charged
                Arguments.of(new CancelOrderRequest("4321"), 7, 400),           // order already cancelled
                Arguments.of(new CancelOrderRequest("4321"), 6, 400)            // order preparation started
        );
    }

    @SuppressWarnings("unused")
    private static Stream<Arguments> provideGroupCreationData() {
        return Stream.of(
                Arguments.of("some group 1", 2),
                Arguments.of("some group 2", 3)
        );
    }

    @SuppressWarnings("unused")
    private static Stream<Arguments> provideGroupSendingAndDeletingData() {
        return Stream.of(
                Arguments.of(2, 5),
                Arguments.of(1, 2)
        );
    }

    @SuppressWarnings("unused")
    private static Stream<Arguments> provideGetOrderItemGroupsData(){
        return Stream.of(
                Arguments.of(1, 2),
                Arguments.of(2, 1),
                Arguments.of(3, 2),
                Arguments.of(4, 1)
        );
    }
}
