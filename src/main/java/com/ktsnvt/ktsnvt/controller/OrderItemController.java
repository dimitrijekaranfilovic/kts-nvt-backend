package com.ktsnvt.ktsnvt.controller;

import com.ktsnvt.ktsnvt.dto.readfoodanddrinkrequests.ReadFoodAndDrinkRequestResponse;
import com.ktsnvt.ktsnvt.dto.updateorderitemrequest.UpdateOrderItemRequestsRequest;
import com.ktsnvt.ktsnvt.model.OrderItem;
import com.ktsnvt.ktsnvt.model.enums.ItemCategory;
import com.ktsnvt.ktsnvt.model.enums.OrderItemStatus;
import com.ktsnvt.ktsnvt.service.OrderItemService;
import com.ktsnvt.ktsnvt.support.EntityConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "api/order-items")
public class OrderItemController {
    private final OrderItemService orderItemService;
    private final EntityConverter<OrderItem, ReadFoodAndDrinkRequestResponse> orderItemToReadFoodAndDrinkRequest;

    @Autowired
    public OrderItemController(OrderItemService orderItemService, EntityConverter<OrderItem, ReadFoodAndDrinkRequestResponse> orderItemToReadFoodAndDrinkRequest) {
        this.orderItemService = orderItemService;
        this.orderItemToReadFoodAndDrinkRequest = orderItemToReadFoodAndDrinkRequest;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value ="requests")
    public Page<ReadFoodAndDrinkRequestResponse> getFoodRequests(Pageable pageable, @RequestParam OrderItemStatus status, @RequestParam ItemCategory category) {
        return orderItemService.getAllItemRequests(pageable, status, category).map(orderItemToReadFoodAndDrinkRequest::convert);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping(value ="take")
    public void takeItem(@RequestBody @Valid UpdateOrderItemRequestsRequest request) {
        if (request.getAction().equals("PREPARE")) {
            orderItemService.takeItemRequest(request.getItemId(), request.getEmployeePin());
        } else if (request.getAction().equals("FINISH")) {
            orderItemService.finishItemRequest(request.getItemId(), request.getEmployeePin());
        }
    }
}
