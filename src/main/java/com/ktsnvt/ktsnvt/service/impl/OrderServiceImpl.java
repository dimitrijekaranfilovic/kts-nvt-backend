package com.ktsnvt.ktsnvt.service.impl;


import com.ktsnvt.ktsnvt.exception.*;
import com.ktsnvt.ktsnvt.exception.EmployeeNotFoundException;
import com.ktsnvt.ktsnvt.exception.InvalidEmployeeTypeException;
import com.ktsnvt.ktsnvt.exception.NotFoundException;
import com.ktsnvt.ktsnvt.exception.OrderItemGroupInvalidStatusException;
import com.ktsnvt.ktsnvt.model.Order;
import com.ktsnvt.ktsnvt.model.OrderItemGroup;
import com.ktsnvt.ktsnvt.model.enums.EmployeeType;
import com.ktsnvt.ktsnvt.model.enums.OrderItemGroupStatus;
import com.ktsnvt.ktsnvt.model.enums.OrderItemStatus;
import com.ktsnvt.ktsnvt.model.enums.OrderStatus;
import com.ktsnvt.ktsnvt.repository.EmployeeRepository;
import com.ktsnvt.ktsnvt.repository.OrderItemGroupRepository;
import com.ktsnvt.ktsnvt.repository.OrderRepository;

import com.ktsnvt.ktsnvt.model.Employee;

import com.ktsnvt.ktsnvt.service.LocalDateTimeService;
import com.ktsnvt.ktsnvt.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class OrderServiceImpl implements OrderService {


    private final OrderRepository orderRepository;
    private final OrderItemGroupRepository orderItemGroupRepository;
    private final EmployeeRepository employeeRepository;
    private final LocalDateTimeService localDateTimeService;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, OrderItemGroupRepository orderItemGroupRepository, EmployeeRepository employeeRepository, LocalDateTimeService localDateTimeService) {
        this.orderRepository = orderRepository;
        this.orderItemGroupRepository = orderItemGroupRepository;
        this.employeeRepository = employeeRepository;
        this.localDateTimeService = localDateTimeService;
    }


    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Order getOrder(Integer id) {
        return this.orderRepository.findById(id).orElseThrow(() -> new NotFoundException(String.format("Order with id %d not found.", id)));
    }

    @Override
    public Optional<OrderItemGroup> getOrderItemGroup(Integer orderId, String groupName) {
        return this.orderItemGroupRepository.getGroupByNameAndOrderId(orderId, groupName);
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public boolean hasAssignedActiveOrders(Employee employee) {
        return orderRepository.streamAssignedActiveOrdersForEmployee(employee.getId()).findAny().isPresent();
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void sendOrderItemGroup(Integer orderId, Integer groupId) {
        var orderItemGroup = this.getOrderItemGroup(orderId, groupId);
        if (orderItemGroup.getStatus() != OrderItemGroupStatus.NEW)
            throw new OrderItemGroupInvalidStatusException(String.format("Order group with id %d for order with id %d is not NEW, and cannot be sent.", orderId, groupId));
        orderItemGroup.setStatus(OrderItemGroupStatus.SENT);
        orderItemGroup.getOrderItems().forEach(orderItem -> {
            //send notification here
            orderItem.setStatus(OrderItemStatus.SENT);
            orderItem.setSentAt(localDateTimeService.currentTime());
        });

        this.orderItemGroupRepository.save(orderItemGroup);

    }

    @Override
    public List<OrderItemGroup> getOrderItemGroups(Integer orderId) {
        return new ArrayList<>(this.orderItemGroupRepository.getOrderItemGroupsForOrder(orderId));
    }

    @Override
    public void deleteOrderItemGroup(Integer orderId, Integer groupId, String pin) {
        var orderItemGroup = this.getOrderItemGroup(orderId, groupId);
        if (orderItemGroup.getStatus() != OrderItemGroupStatus.NEW)
            throw new OrderItemGroupInvalidStatusException(String.format("Order group with id %d for order with id %d cannot be deleted, because its status is not NEW.", orderId, groupId));
        var optionalEmployee = this.employeeRepository.findEmployeeByPin(pin);

        if(optionalEmployee.isEmpty())
            throw new EmployeeNotFoundException("Employee does not exist.");
        var employee = optionalEmployee.get();
        if(employee.getType() != EmployeeType.WAITER)
            throw new InvalidEmployeeTypeException(pin);
        if(!employee.getId().equals(orderItemGroup.getOrder().getWaiter().getId())){
            throw new InvalidEmployeeTypeException(pin);
        }


        orderItemGroup.setIsActive(false);
        orderItemGroup.getOrderItems().forEach(oig -> oig.setIsActive(false));
        this.orderItemGroupRepository.save(orderItemGroup);
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void chargeOrder(Integer id, String pin) {
        var employee = employeeRepository
                .findByPin(pin)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee with PIN: " + pin + " not found."));
        var order = getOrder(id);
        if (!order.getWaiter().getId().equals(employee.getId())) {
            throw new InvalidEmployeeException("Employee with PIN: " + pin + " is not responsible for this order.");
        }
        if (!order.getStatus().equals(OrderStatus.IN_PROGRESS)) {
            throw new IllegalOrderStateException("Order is not in IN PROGRESS state and thus cannot be charged.");
        }
        if (order.getItemGroups().stream().anyMatch(ig -> ig.getIsActive() && ig.getStatus() != OrderItemGroupStatus.DONE)) {
            throw new IllegalOrderStateException("Order cannot be charged because not all of its groups are done.");
        }
        order.setStatus(OrderStatus.CHARGED);
        order.setServedAt(localDateTimeService.currentTime());
        order.getRestaurantTable().freeTable();
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void cancelOrder(Integer id, String pin) {
        var employee = employeeRepository
                .findByPin(pin)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee with PIN: " + pin + " not found."));
        var order = getOrder(id);
        if (!order.getWaiter().getId().equals(employee.getId())) {
            throw new InvalidEmployeeException("Employee with PIN: " + pin + " is not responsible for this order.");
        }
        if (order.getStatus().equals(OrderStatus.CHARGED) || order.getStatus().equals(OrderStatus.CANCELLED)) {
            throw new IllegalOrderStateException("Order has already been processed and cannot be canceled.");
        }
        if (order.getItemGroups().stream().anyMatch(ig -> ig.getIsActive() && !ig.getStatus().equals(OrderItemGroupStatus.NEW))) {
            throw new IllegalOrderStateException("Order has already been sent to the kitchen / bar");
        }
        order.getItemGroups().forEach(ig -> ig.setIsActive(false));
        order.setStatus(OrderStatus.CANCELLED);
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public OrderItemGroup createGroupForOrder(Integer orderId, String groupName, String pin) {
        var order = this.getOrder(orderId);
        var optionalOrderItemGroup = this.getOrderItemGroup(orderId, groupName);
        if (optionalOrderItemGroup.isPresent())
            throw new OrderItemGroupInvalidStatusException(String.format("Group with name %s already exists for order with id %d.", groupName, orderId));
        var employee = employeeRepository
                .findByPin(pin)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee with PIN: " + pin + " not found."));
        if(!order.getWaiter().getId().equals(employee.getId()))
            throw new InvalidEmployeeException("Employee with PIN: " + pin + " is not responsible for this order.");


        var orderItemGroup = new OrderItemGroup(groupName, OrderItemGroupStatus.NEW, order);
        return this.orderItemGroupRepository.save(orderItemGroup);
    }

    private OrderItemGroup getOrderItemGroup(Integer orderId, Integer groupId) {
        var optionalOrderItemGroup = this.orderItemGroupRepository.getGroupByIdAndOrderId(orderId, groupId);
        if (optionalOrderItemGroup.isEmpty())
            throw new NotFoundException(String.format("Order group with id %d for order with id %d does not exist.", groupId, orderId));
        return optionalOrderItemGroup.get();
    }

}


