package com.ktsnvt.ktsnvt.integration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ktsnvt.ktsnvt.dto.addorderitem.AddOrderItemRequest;
import com.ktsnvt.ktsnvt.dto.deleteorderitem.DeleteOrderItemRequest;
import com.ktsnvt.ktsnvt.dto.readfoodanddrinkrequests.ReadFoodAndDrinkRequestResponse;
import com.ktsnvt.ktsnvt.dto.updateorderitem.UpdateOrderItemRequest;
import com.ktsnvt.ktsnvt.dto.updateorderitemrequest.UpdateOrderItemRequestsRequest;
import com.ktsnvt.ktsnvt.model.enums.ItemCategory;
import com.ktsnvt.ktsnvt.model.enums.OrderItemStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.boot.test.web.client.TestRestTemplate;

import javax.validation.constraints.NotNull;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

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
    
    @Autowired
    private TestRestTemplate restTemplate;

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

    @NotNull
    public static Stream<Arguments> provideFor_getFoodRequests() {
        return Stream.of(
                Arguments.of(0, 10, OrderItemStatus.SENT, ItemCategory.FOOD),
                Arguments.of(0, 10, OrderItemStatus.SENT, ItemCategory.DRINK),
                Arguments.of(0, 10, OrderItemStatus.PREPARING, ItemCategory.FOOD),
                Arguments.of(0, 10, OrderItemStatus.PREPARING, ItemCategory.DRINK)
        );
    }

    @NotNull
    public static Stream<Arguments> provideFor_takeItem_success() {
        return Stream.of(
                Arguments.of(new UpdateOrderItemRequestsRequest("FINISH", 13, "1212")),
                Arguments.of(new UpdateOrderItemRequestsRequest("FINISH", 14, "1212"))
        );
    }

    @NotNull
    public static Stream<Arguments> provideFor_takeItem_failed() {
        return Stream.of(
                Arguments.of(new UpdateOrderItemRequestsRequest("PREPARE", 1, "5678")),
                Arguments.of(new UpdateOrderItemRequestsRequest("FINISH", 3, "1234"))
        );
    }

    @ParameterizedTest
    @MethodSource("provideFor_getFoodRequests")
    void getFoodRequests(Integer page, Integer size, OrderItemStatus status, ItemCategory category) {
        var response = restTemplate.getForEntity("/api/order-items/requests?page=" + page + "&size=" + size + "&status=" + status + "&category=" + category, ReadFoodAndDrinkRequestResponse.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @ParameterizedTest
    @MethodSource("provideFor_takeItem_success")
    void takeItem_calledWithValidParams_isSuccess(UpdateOrderItemRequestsRequest request){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        var response = restTemplate.exchange("/api/order-items/take", HttpMethod.PUT, new HttpEntity<>(request, headers), Void.class);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @ParameterizedTest
    @MethodSource("provideFor_takeItem_failed")
    void takeItem_calledWithWrongEmployee_returnsBadRequest(UpdateOrderItemRequestsRequest request){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        var response = restTemplate.exchange("/api/order-items/take", HttpMethod.PUT, new HttpEntity<>(request, headers), Void.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void takeItem_calledWithInvalidItemId_returnsBadRequest(){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        var request = new UpdateOrderItemRequestsRequest("PREPARE", 0, "5678");

        var response = restTemplate.exchange("/api/order-items/take", HttpMethod.PUT, new HttpEntity<>(request, headers), Void.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

}
