package com.ktsnvt.ktsnvt.controller;

import com.ktsnvt.ktsnvt.dto.readfoodanddrinkrequests.ReadFoodAndDrinkRequestResponse;
import com.ktsnvt.ktsnvt.model.OrderItem;
import com.ktsnvt.ktsnvt.service.OrderItemService;
import com.ktsnvt.ktsnvt.support.EntityConverter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "api/order-items")
public class OrderItemController {
    private final OrderItemService orderItemService;

    private final EntityConverter<OrderItem, ReadFoodAndDrinkRequestResponse> orderItemToReadFoodAndDrinkRequest;

    public OrderItemController(OrderItemService orderItemService, EntityConverter<OrderItem, ReadFoodAndDrinkRequestResponse> orderItemToReadFoodAndDrinkRequest) {
        this.orderItemService = orderItemService;
        this.orderItemToReadFoodAndDrinkRequest = orderItemToReadFoodAndDrinkRequest;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping("food-requests")
    public Page<ReadFoodAndDrinkRequestResponse> getFoodRequests(Pageable pageable) {
        var foodRequests = orderItemService.getAllFoodRequests(pageable);
        return foodRequests.map(orderItemToReadFoodAndDrinkRequest::convert);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping("drink-requests")
    public Page<ReadFoodAndDrinkRequestResponse> getDrinkRequests(Pageable pageable) {
        var drinkRequests = orderItemService.getAllDrinkRequests(pageable);
        return drinkRequests.map(orderItemToReadFoodAndDrinkRequest::convert);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping("food-preparation-requests")
    public Page<ReadFoodAndDrinkRequestResponse> getFoodInPreparation(Pageable pageable) {
        var foodRequests = orderItemService.getAllFoodInPreparation(pageable);
        return foodRequests.map(orderItemToReadFoodAndDrinkRequest::convert);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping("drink-preparation-requests")
    public Page<ReadFoodAndDrinkRequestResponse> getDrinksInPreparation(Pageable pageable) {
        var drinkRequests = orderItemService.getAllDrinksInPreparation(pageable);
        return drinkRequests.map(orderItemToReadFoodAndDrinkRequest::convert);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequestMapping("prepare/{itemId}/{pin}")
    public void takeItem(@PathVariable Integer itemId, @PathVariable String pin) {
        orderItemService.takeItemRequest(itemId, pin);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequestMapping("finish/{itemId}/{pin}")
    public void finishItem(@PathVariable Integer itemId, @PathVariable String pin) {
        orderItemService.finishItemRequest(itemId, pin);
    }
}
