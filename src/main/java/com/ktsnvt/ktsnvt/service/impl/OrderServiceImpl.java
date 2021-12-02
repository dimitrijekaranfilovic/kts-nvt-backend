package com.ktsnvt.ktsnvt.service.impl;

import com.ktsnvt.ktsnvt.exception.*;
import com.ktsnvt.ktsnvt.model.BaseEntity;
import com.ktsnvt.ktsnvt.model.Employee;
import com.ktsnvt.ktsnvt.model.Order;
import com.ktsnvt.ktsnvt.model.OrderItemGroup;
import com.ktsnvt.ktsnvt.model.enums.EmployeeType;
import com.ktsnvt.ktsnvt.model.enums.OrderItemGroupStatus;
import com.ktsnvt.ktsnvt.model.enums.OrderItemStatus;
import com.ktsnvt.ktsnvt.model.enums.OrderStatus;
import com.ktsnvt.ktsnvt.repository.OrderItemGroupRepository;
import com.ktsnvt.ktsnvt.repository.OrderRepository;
import com.ktsnvt.ktsnvt.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Service
public class OrderServiceImpl extends TransactionalServiceBase implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemGroupRepository orderItemGroupRepository;

    private final EmployeeQueryService employeeQueryService;
    private final EmployeeOrderService employeeOrderService;
    private final RestaurantTableService restaurantTableService;
    private final LocalDateTimeService localDateTimeService;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository,
                            OrderItemGroupRepository orderItemGroupRepository,
                            EmployeeQueryService employeeQueryService,
                            EmployeeOrderService employeeOrderService,
                            RestaurantTableService restaurantTableService,
                            LocalDateTimeService localDateTimeService) {
        this.orderRepository = orderRepository;
        this.orderItemGroupRepository = orderItemGroupRepository;
        this.employeeQueryService = employeeQueryService;
        this.employeeOrderService = employeeOrderService;
        this.restaurantTableService = restaurantTableService;
        this.localDateTimeService = localDateTimeService;
    }

    @Override
    public Order getOrder(Integer id) {
        return this.orderRepository
                .findById(id)
                .orElseThrow(() -> new OrderNotFoundException(String.format("Order with id %d not found.", id)));
    }

    @Override
    public Order createOrder(Integer tableId, String waiterPin) {
        var table = restaurantTableService.readForUpdate(tableId);
        if (Boolean.FALSE.equals(table.getAvailable())) {
            throw new OccupiedTableException("Table with id: " + tableId + " is occupied at the moment.");
        }
        var waiter = employeeQueryService.findByPinForUpdate(waiterPin, EmployeeType.WAITER);
        var order = new Order(OrderStatus.CREATED, localDateTimeService.currentTime(), null, table, waiter);
        table.setAvailable(false);
        return orderRepository.save(order);
    }

    @Override
    public Optional<OrderItemGroup> getOrderItemGroup(Integer orderId, String groupName) {
        return this.orderItemGroupRepository.getGroupByNameAndOrderId(orderId, groupName);
    }

    @Override
    public boolean hasAssignedActiveOrders(Employee employee) {
        return orderRepository.streamAssignedActiveOrdersForEmployee(employee.getId()).findAny().isPresent();
    }

    @Override
    public void sendOrderItemGroup(Integer orderId, Integer groupId, String pin) {
        var orderItemGroup = this.getOrderItemGroup(orderId, groupId);
        if (orderItemGroup.getStatus() != OrderItemGroupStatus.NEW) {
            throw new OrderItemGroupInvalidStatusException(String.format("Order group with id %d for order with id %d is not NEW, and cannot be sent.", orderId, groupId));
        }
        employeeOrderService.throwIfWaiterNotResponsible(pin, orderItemGroup.getOrder().getWaiter().getId());
        orderItemGroup.setStatus(OrderItemGroupStatus.SENT);
        orderItemGroup.getOrderItems().stream().filter(BaseEntity::getIsActive).forEach(orderItem -> {
            //send notification here
            orderItem.setStatus(OrderItemStatus.SENT);
            orderItem.setSentAt(localDateTimeService.currentTime());
        });

        this.orderItemGroupRepository.save(orderItemGroup);
    }

    @Override
    public List<OrderItemGroup> getOrderItemGroups(Integer orderId) {
        //maybe throw an exception if order does not exist
        var orderItemGroups = this.orderItemGroupRepository.getOrderItemGroupsForOrder(orderId);
        orderItemGroups.forEach(oig ->
            oig.setOrderItems(oig.getOrderItems().stream().filter(BaseEntity::getIsActive).collect(Collectors.toSet())));
        return new ArrayList<>(orderItemGroups);
    }


    @Override
    public void deleteOrderItemGroup(Integer orderId, Integer groupId, String pin) {
        var orderItemGroup = this.getOrderItemGroup(orderId, groupId);
        if (orderItemGroup.getStatus() != OrderItemGroupStatus.NEW) {
            throw new OrderItemGroupInvalidStatusException(String.format("Order group with id %d for order with id %d cannot be deleted, because its status is not NEW.", orderId, groupId));
        }
        employeeOrderService.throwIfWaiterNotResponsible(pin, orderItemGroup.getOrder().getWaiter().getId());
        orderItemGroup.setIsActive(false);
        orderItemGroup.getOrderItems().forEach(oig -> oig.setIsActive(false));
        this.orderItemGroupRepository.save(orderItemGroup);
    }

    @Override
    public Stream<Order> streamChargedOrdersInTimeRange(LocalDate from, LocalDate to) {
        return orderRepository
                .streamChargedOrdersInTimeRange(from.atStartOfDay(), to.plusDays(1).atStartOfDay());
    }

    @Override
    public void chargeOrder(Integer id, String pin) {
        var order = getOrder(id);
        employeeOrderService.throwIfWaiterNotResponsible(pin, order.getWaiter().getId());
        if (!order.getStatus().equals(OrderStatus.IN_PROGRESS) && !order.getStatus().equals(OrderStatus.CREATED)) {
            throw new IllegalOrderStateException("Order has already been processed and thus cannot be charged.");
        }
        if (order.getItemGroups().stream().anyMatch(ig -> ig.getIsActive() && ig.getStatus() != OrderItemGroupStatus.DONE)) {
            throw new IllegalOrderStateException("Order cannot be charged because not all of its groups are done.");
        }
        order.getItemGroups()
                .stream()
                .filter(BaseEntity::getIsActive)
                .forEach(ig -> ig.getOrderItems()
                        .stream()
                        .filter(BaseEntity::getIsActive)
                        .forEach(item -> {
                            order.setTotalIncome(order.getTotalIncome().add(item.getCurrentMenuPrice().multiply(BigDecimal.valueOf(item.getAmount()))));
                            order.setTotalCost(order.getTotalCost().add(item.getCurrentBasePrice().multiply(BigDecimal.valueOf(item.getAmount()))));
                        }));
        order.setStatus(OrderStatus.CHARGED);
        order.setServedAt(localDateTimeService.currentTime());
        order.getRestaurantTable().freeTable();
    }

    @Override
    public void cancelOrder(Integer id, String pin) {
        var order = getOrder(id);
        employeeOrderService.throwIfWaiterNotResponsible(pin, order.getWaiter().getId());
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
    //TODO: test
    public OrderItemGroup createGroupForOrder(Integer orderId, String groupName, String pin) {
        var order = this.getOrder(orderId);
        if (order.getStatus() != OrderStatus.CREATED && order.getStatus() != OrderStatus.IN_PROGRESS)
            throw new IllegalOrderStateException(String.format("Status of order with id %d is neither CREATED nor IN_PROGRESS and new groups cannot be created for it.", orderId));

        var optionalOrderItemGroup = this.getOrderItemGroup(orderId, groupName);
        if (optionalOrderItemGroup.isPresent())
            throw new OrderItemGroupInvalidStatusException(String.format("Group with name %s already exists for order with id %d.", groupName, orderId));
        employeeOrderService.throwIfWaiterNotResponsible(pin, order.getWaiter().getId());
        var orderItemGroup = new OrderItemGroup(groupName, OrderItemGroupStatus.NEW, order);
        return this.orderItemGroupRepository.save(orderItemGroup);
    }

    public OrderItemGroup getOrderItemGroup(Integer orderId, Integer groupId) {
        var optionalOrderItemGroup = this.orderItemGroupRepository.getGroupByIdAndOrderId(orderId, groupId);
        if (optionalOrderItemGroup.isEmpty())
            throw new NotFoundException(String.format("Order group with id %d for order with id %d does not exist.", groupId, orderId));
        return optionalOrderItemGroup.get();
    }
}


