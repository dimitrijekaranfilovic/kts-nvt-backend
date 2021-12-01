package com.ktsnvt.ktsnvt.integration.controller;

import com.ktsnvt.ktsnvt.dto.readfoodanddrinkrequests.ReadFoodAndDrinkRequestResponse;
import com.ktsnvt.ktsnvt.dto.updateorderitemrequest.UpdateOrderItemRequestsRequest;
import com.ktsnvt.ktsnvt.model.enums.ItemCategory;
import com.ktsnvt.ktsnvt.model.enums.OrderItemStatus;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;

import java.util.stream.Stream;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OrderItemControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    public static Stream<Arguments> provideFor_getFoodRequests() {
        Pageable pageable = PageRequest.of(0, 10);
        return Stream.of(
                Arguments.of(0, 10, OrderItemStatus.SENT, ItemCategory.FOOD),
                Arguments.of(0, 10, OrderItemStatus.SENT, ItemCategory.DRINK),
                Arguments.of(0, 10, OrderItemStatus.PREPARING, ItemCategory.FOOD),
                Arguments.of(0, 10, OrderItemStatus.PREPARING, ItemCategory.DRINK)
        );
    }

    public static Stream<Arguments> provideFor_takeItem_success() {
        return Stream.of(
                Arguments.of(new UpdateOrderItemRequestsRequest("FINISH", 13, "1212")),
                Arguments.of(new UpdateOrderItemRequestsRequest("FINISH", 14, "1212"))
        );
    }

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
