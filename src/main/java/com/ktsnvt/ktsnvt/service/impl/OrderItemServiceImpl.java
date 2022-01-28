package com.ktsnvt.ktsnvt.service.impl;

import com.ktsnvt.ktsnvt.exception.*;
import com.ktsnvt.ktsnvt.model.Employee;
import com.ktsnvt.ktsnvt.model.InventoryItem;
import com.ktsnvt.ktsnvt.model.MenuItem;
import com.ktsnvt.ktsnvt.model.OrderItem;
import com.ktsnvt.ktsnvt.model.enums.ItemCategory;
import com.ktsnvt.ktsnvt.model.enums.OrderItemGroupStatus;
import com.ktsnvt.ktsnvt.model.enums.OrderItemStatus;
import com.ktsnvt.ktsnvt.repository.MenuItemRepository;
import com.ktsnvt.ktsnvt.repository.OrderItemGroupRepository;
import com.ktsnvt.ktsnvt.repository.OrderItemRepository;
import com.ktsnvt.ktsnvt.service.EmployeeOrderService;
import com.ktsnvt.ktsnvt.service.EmployeeQueryService;
import com.ktsnvt.ktsnvt.service.LocalDateTimeService;
import com.ktsnvt.ktsnvt.service.OrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class OrderItemServiceImpl extends TransactionalServiceBase implements OrderItemService {
    private final OrderItemRepository orderItemRepository;
    private final OrderItemGroupRepository orderItemGroupRepository;
    private final MenuItemRepository menuItemRepository;

    private final LocalDateTimeService dateTimeService;
    private final EmployeeQueryService employeeQueryService;
    private final EmployeeOrderService employeeOrderService;

    @Autowired
    public OrderItemServiceImpl(OrderItemRepository orderItemRepository,
                                OrderItemGroupRepository orderItemGroupRepository,
                                MenuItemRepository menuItemRepository,
                                LocalDateTimeService dateTimeService,
                                EmployeeQueryService employeeQueryService,
                                EmployeeOrderService employeeOrderService) {
        this.orderItemRepository = orderItemRepository;
        this.orderItemGroupRepository = orderItemGroupRepository;
        this.menuItemRepository = menuItemRepository;
        this.dateTimeService = dateTimeService;
        this.employeeQueryService = employeeQueryService;
        this.employeeOrderService = employeeOrderService;
    }

    @Override
    public Page<OrderItem> getAllItemRequests(Pageable pageable, OrderItemStatus status, ItemCategory category) {
        return orderItemRepository.findAllItemRequests(pageable, status, category);
    }

    @Override
    public void takeItemRequest(Integer itemId, String employeePin) {
        var employeeCurrent = employeeQueryService.findByPin(employeePin);
        var item = orderItemRepository
                .findOneByIdWithItemReference(itemId)
                .orElseThrow(() -> new OrderItemNotFoundException(String.format("Order item with id: %d is not present.", itemId)));

        employeeOrderService.throwIfNotValidEmployeeType(employeeCurrent, item);
        item.setPreparedBy(employeeCurrent);
        item.setStatus(OrderItemStatus.PREPARING);
        item.setTakenAt(dateTimeService.currentTime());
        orderItemRepository.save(item);
    }

    @Override
    public Integer finishItemRequest(Integer itemId, String employeePin) {
        var employeeCurrent = employeeQueryService.findByPin(employeePin);
        var item = orderItemRepository
                .findOneInProgressByIdWithItemReference(itemId)
                .orElseThrow(() -> new OrderItemNotFoundException(String.format("Order item with id: %d is not present.", itemId)));

        if (item.getPreparedBy() != null && !Objects.equals(item.getPreparedBy().getId(), employeeCurrent.getId())) {
            throw new InvalidEmployeeTypeException(employeePin);
        }

        employeeOrderService.throwIfNotValidEmployeeType(employeeCurrent, item);
        item.setStatus(OrderItemStatus.DONE);
        item.setPreparedAt(dateTimeService.currentTime());
        if (item.getPreparedBy() == null) {
            item.setTakenAt(dateTimeService.currentTime());
            item.setPreparedBy(employeeCurrent);
        }
        orderItemRepository.save(item);

        var allFromGroup = orderItemRepository.getAllFromOneGroup(item.getOrderItemGroup().getId());
        if (!allFromGroup.isEmpty() && allFromGroup.stream().allMatch(oi -> oi.getStatus() == OrderItemStatus.DONE)) {
            item.getOrderItemGroup().setStatus(OrderItemGroupStatus.DONE);
            orderItemGroupRepository.save(item.getOrderItemGroup());
            return item.getOrderItemGroup().getOrder().getRestaurantTable().getId();
        }
        return 0;
    }

    @Override
    public boolean hasActiveOrderItems(Employee employee) {
        return orderItemRepository.streamActiveOrderItemsForEmployee(employee.getId()).findAny().isPresent();
    }

    @Override
    public boolean hasActiveOrderItems(MenuItem menuItem) {
        return orderItemRepository.streamActiveOrderItemsForMenuItem(menuItem.getId()).findAny().isPresent();
    }

    @Override
    public boolean hasActiveOrderItems(InventoryItem inventoryItem) {
        return orderItemRepository.streamActiveOrderItemsForInventoryItem(inventoryItem.getId()).findAny().isPresent();
    }

    @Override
    public OrderItem addOrderItem(Integer orderGroupId, Integer menuItemId, Integer amount, String pin) {
        var orderGroup = this.orderItemGroupRepository.
                findById(orderGroupId)
                .orElseThrow(() -> new NotFoundException(String.format("Order item group with id %d does not exist.", orderGroupId)));

        if (Boolean.FALSE.equals(orderGroup.getIsActive()))
            throw new NotFoundException(String.format("Order item group with id %d does not exist.", orderGroupId));

        if (orderGroup.getStatus() != OrderItemGroupStatus.NEW)
            throw new OrderItemGroupInvalidStatusException(String.format("Items cannot be added to order item group with id %d because its status is not NEW.", orderGroupId));

        var menuItem = this.menuItemRepository
                .findById(menuItemId)
                .orElseThrow(() -> new NotFoundException(String.format("Menu item with id %d does not exist.", menuItemId)));

        if (amount <= 0)
            throw new IllegalAmountException(amount);

        employeeOrderService.throwIfWaiterNotResponsible(pin, orderGroup.getOrder().getWaiter().getId());
        var orderItem = new OrderItem(amount, orderGroup, menuItem, OrderItemStatus.NEW);
        return this.orderItemRepository.save(orderItem);
    }

    @Override
    public void updateOrderItem(Integer orderItemId, Integer amount, String pin) {
        var orderItem = this.orderItemRepository
                .findById(orderItemId)
                .orElseThrow(() -> new NotFoundException(String.format("Order item with id %d does not exist.", orderItemId)));

        if (orderItem.getStatus() != OrderItemStatus.NEW)
            throw new OrderItemInvalidStatusException(String.format("Status of order item with id %d is not NEW, and it cannot be updated.", orderItemId));

        if (amount <= 0)
            throw new IllegalAmountException(amount);

        employeeOrderService.throwIfWaiterNotResponsible(pin, orderItem.getOrderItemGroup().getOrder().getWaiter().getId());
        orderItem.setAmount(amount);
    }

    @Override
    public void deleteOrderItem(Integer orderItemId, String pin) {
        var orderItem = this.orderItemRepository
                .findById(orderItemId)
                .orElseThrow(() -> new NotFoundException(String.format("Order item with id %d does not exist.", orderItemId)));

        if (orderItem.getStatus() != OrderItemStatus.NEW) {
            throw new OrderItemInvalidStatusException(String.format("Status of order item with id %s is not NEW, and it cannot be deleted.", orderItemId));
        }

        employeeOrderService.throwIfWaiterNotResponsible(pin, orderItem.getOrderItemGroup().getOrder().getWaiter().getId());
        orderItem.setIsActive(false);
    }
}
