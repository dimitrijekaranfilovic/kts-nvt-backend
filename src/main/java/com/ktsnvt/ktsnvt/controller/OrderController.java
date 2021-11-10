package com.ktsnvt.ktsnvt.controller;


import com.ktsnvt.ktsnvt.dto.chargeorder.ChargeOrderRequest;
import com.ktsnvt.ktsnvt.dto.cancelorder.CancelOrderRequest;
import com.ktsnvt.ktsnvt.dto.createorder.CreateOrderRequest;
import com.ktsnvt.ktsnvt.dto.createorder.CreateOrderResponse;
import com.ktsnvt.ktsnvt.dto.createorderitemgroup.CreateOrderItemGroupRequest;
import com.ktsnvt.ktsnvt.dto.createorderitemgroup.CreateOrderItemGroupResponse;
import com.ktsnvt.ktsnvt.dto.deleteorderitem.DeleteOrderItemRequest;
import com.ktsnvt.ktsnvt.dto.readorderitemgroups.OrderItemGroupResponse;
import com.ktsnvt.ktsnvt.dto.sendorderitemgroup.SendOrderItemGroupRequest;
import com.ktsnvt.ktsnvt.model.Order;
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

    private final EntityConverter<Order, CreateOrderResponse> orderToCreateOrder;

    @Autowired
    public OrderController(OrderService orderService,
                           EntityConverter<OrderItemGroup, CreateOrderItemGroupResponse> orderItemGroupToCreateOrderItemGroupResponse,
                           EntityConverter<OrderItemGroup, OrderItemGroupResponse> orderItemGroupToOrderItemGroupResponse,
                           EntityConverter<Order, CreateOrderResponse> orderToCreateOrder) {
        this.orderService = orderService;
        this.orderItemGroupToCreateOrderItemGroupResponse = orderItemGroupToCreateOrderItemGroupResponse;
        this.orderItemGroupToOrderItemGroupResponse = orderItemGroupToOrderItemGroupResponse;
        this.orderToCreateOrder = orderToCreateOrder;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CreateOrderResponse createOrder(@RequestBody @Valid CreateOrderRequest request) {
        var result = orderService.createOrder(request.getTableId(), request.getPin());
        return orderToCreateOrder.convert(result);
    }

    @PutMapping("/{id}/charge")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void chargeOrder(@PathVariable Integer id, @RequestBody @Valid ChargeOrderRequest request) {
        orderService.chargeOrder(id, request.getPin());
    }

    @PutMapping("/{id}/cancel")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancelOrder(@PathVariable Integer id, @RequestBody @Valid CancelOrderRequest request) {
        orderService.cancelOrder(id, request.getPin());
    }

    @PostMapping(value = "/{id}/groups")
    @ResponseStatus(HttpStatus.CREATED)
    public CreateOrderItemGroupResponse createOrderItemGroup(@PathVariable("id") Integer orderId, @RequestBody @Valid CreateOrderItemGroupRequest request){
        return this.orderItemGroupToCreateOrderItemGroupResponse.convert(this.orderService.createGroupForOrder(orderId, request.getGroupName(), request.getPin()));
    }

    @PutMapping(value = "/{orderId}/groups/{groupId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void sendOrderItemGroup(@PathVariable("orderId") Integer orderId, @PathVariable("groupId") Integer groupId, @RequestBody @Valid SendOrderItemGroupRequest request){
        this.orderService.sendOrderItemGroup(orderId, groupId, request.getPin());
    }

    @GetMapping(value = "/{id}/groups")
    public List<OrderItemGroupResponse> getOrderItemGroups(@PathVariable("id") Integer orderId){
        var orderItemGroups = this.orderService.getOrderItemGroups(orderId);
        return orderItemGroups.stream().map(orderItemGroupToOrderItemGroupResponse::convert).collect(Collectors.toList());
    }

    @DeleteMapping(value = "/{orderId}/groups/{groupId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteOrderItemGroup(@PathVariable("orderId") Integer orderId, @PathVariable("groupId") Integer groupId, @RequestBody @Valid DeleteOrderItemRequest request){
        this.orderService.deleteOrderItemGroup(orderId, groupId, request.getPin());
    }

}
