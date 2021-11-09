package com.ktsnvt.ktsnvt.service;

import com.ktsnvt.ktsnvt.model.Order;
import com.ktsnvt.ktsnvt.model.OrderItemGroup;

import java.util.List;
import java.util.Optional;
import com.ktsnvt.ktsnvt.model.Employee;

public interface OrderService {

    Order getOrder(Integer id);

    Optional<OrderItemGroup> getOrderItemGroup(Integer orderId, String groupName);

    OrderItemGroup createGroupForOrder(Integer orderId, String groupName);
    boolean hasAssignedActiveOrders(Employee employee);

    void sendOrderItemGroup(Integer orderId, Integer groupId);

    List<OrderItemGroup> getOrderItemGroups(Integer orderId);

}