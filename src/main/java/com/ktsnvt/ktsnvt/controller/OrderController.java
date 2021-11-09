package com.ktsnvt.ktsnvt.controller;


import com.ktsnvt.ktsnvt.dto.createorderitemgroup.CreateOrderItemGroupRequest;
import com.ktsnvt.ktsnvt.dto.createorderitemgroup.CreateOrderItemGroupResponse;
import com.ktsnvt.ktsnvt.dto.readorderitemgroups.OrderItemGroupResponse;
import com.ktsnvt.ktsnvt.model.OrderItemGroup;
import com.ktsnvt.ktsnvt.service.OrderService;
import com.ktsnvt.ktsnvt.support.EntityConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/orders")
public class OrderController {

    private final OrderService orderService;
    private final EntityConverter<OrderItemGroup, CreateOrderItemGroupResponse> orderItemGroupToCreateOrderItemGroupResponse;
    private final EntityConverter<OrderItemGroup, OrderItemGroupResponse> orderItemGroupToOrderItemGroupResponse;

    @Autowired
    public OrderController(OrderService orderService, EntityConverter<OrderItemGroup, CreateOrderItemGroupResponse> orderItemGroupToCreateOrderItemGroupResponse, EntityConverter<OrderItemGroup, OrderItemGroupResponse> orderItemGroupToOrderItemGroupResponse) {
        this.orderService = orderService;
        this.orderItemGroupToCreateOrderItemGroupResponse = orderItemGroupToCreateOrderItemGroupResponse;
        this.orderItemGroupToOrderItemGroupResponse = orderItemGroupToOrderItemGroupResponse;
    }


    @PostMapping(value = "/{id}/groups")
    @ResponseStatus(HttpStatus.CREATED)
    public CreateOrderItemGroupResponse createOrderItemGroup(@PathVariable("id") Integer orderId, @RequestBody @Valid CreateOrderItemGroupRequest request){
        return  this.orderItemGroupToCreateOrderItemGroupResponse.convert(this.orderService.createGroupForOrder(orderId, request.getGroupName()));
    }

    @PutMapping(value = "/{orderId}/groups/{groupId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void sendOrderItemGroup(@PathVariable("orderId") Integer orderId, @PathVariable("groupId") Integer groupId){
        this.orderService.sendOrderItemGroup(orderId, groupId);

    }

    @GetMapping(value = "/{id}/groups")
    public List<OrderItemGroupResponse> getOrderItemGroups(@PathVariable("id") Integer orderId){
        var orderItemGroups = this.orderService.getOrderItemGroups(orderId);
        return orderItemGroups.stream().map(orderItemGroupToOrderItemGroupResponse::convert).collect(Collectors.toList());
    }

    @DeleteMapping(value = "/{orderId}/groups/{groupId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteOrderItemGroup(@PathVariable("orderId") Integer orderId, @PathVariable("groupId") Integer groupId){
        this.orderService.deleteOrderItemGroup(orderId, groupId);
    }

}
