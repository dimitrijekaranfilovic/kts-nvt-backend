package com.ktsnvt.ktsnvt.controller;


import com.ktsnvt.ktsnvt.dto.createorderitemgroup.CreateOrderItemGroupRequest;
import com.ktsnvt.ktsnvt.dto.createorderitemgroup.CreateOrderItemGroupResponse;
import com.ktsnvt.ktsnvt.model.OrderItemGroup;
import com.ktsnvt.ktsnvt.service.OrderService;
import com.ktsnvt.ktsnvt.support.EntityConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/api/orders")
public class OrderController {

    private final OrderService orderService;
    private final EntityConverter<OrderItemGroup, CreateOrderItemGroupResponse> orderItemGroupToCreateOrderItemGroupResponse;

    @Autowired
    public OrderController(OrderService orderService, EntityConverter<OrderItemGroup, CreateOrderItemGroupResponse> orderItemGroupToCreateOrderItemGroupResponse) {
        this.orderService = orderService;
        this.orderItemGroupToCreateOrderItemGroupResponse = orderItemGroupToCreateOrderItemGroupResponse;
    }


    @PostMapping(value = "/{id}/groups")
    @ResponseStatus(HttpStatus.CREATED)
    public CreateOrderItemGroupResponse createOrderItemGroup(@PathVariable("id") Integer orderId, @RequestBody @Valid CreateOrderItemGroupRequest request){
        return  this.orderItemGroupToCreateOrderItemGroupResponse.convert(this.orderService.createGroupForOrder(orderId, request.getGroupName()));
    }

}
