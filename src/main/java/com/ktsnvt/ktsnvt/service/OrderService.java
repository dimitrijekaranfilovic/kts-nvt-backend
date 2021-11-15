package com.ktsnvt.ktsnvt.service;

import com.ktsnvt.ktsnvt.model.Order;
import com.ktsnvt.ktsnvt.model.OrderItemGroup;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import com.ktsnvt.ktsnvt.model.Employee;

public interface OrderService {

    Order getOrder(Integer id);

    Order createOrder(Integer tableId, String waiterPin);

    Optional<OrderItemGroup> getOrderItemGroup(Integer orderId, String groupName);

    OrderItemGroup createGroupForOrder(Integer orderId, String groupName, String pin);

    void sendOrderItemGroup(Integer orderId, Integer groupId, String pin);

    List<OrderItemGroup> getOrderItemGroups(Integer orderId);
    
    boolean hasAssignedActiveOrders(Employee employee);

    void chargeOrder(Integer id, String pin);
    
    void cancelOrder(Integer id, String pin);

    void deleteOrderItemGroup(Integer orderId, Integer groupId, String pin);

    Stream<Order> streamChargedOrdersInTimeRange(LocalDate from, LocalDate to);
}
