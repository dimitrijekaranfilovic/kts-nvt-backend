package com.ktsnvt.ktsnvt.service.impl;

import com.ktsnvt.ktsnvt.exception.*;
import com.ktsnvt.ktsnvt.model.Employee;
import com.ktsnvt.ktsnvt.model.OrderItem;
import com.ktsnvt.ktsnvt.model.enums.EmployeeType;
import com.ktsnvt.ktsnvt.model.enums.ItemCategory;
import com.ktsnvt.ktsnvt.model.enums.OrderItemGroupStatus;
import com.ktsnvt.ktsnvt.model.enums.OrderItemStatus;
import com.ktsnvt.ktsnvt.repository.EmployeeRepository;
import com.ktsnvt.ktsnvt.repository.OrderItemRepository;
import com.ktsnvt.ktsnvt.service.LocalDateTimeService;
import com.ktsnvt.ktsnvt.service.OrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
public class OrderItemServiceImpl implements OrderItemService {
    private final EmployeeRepository employeeRepository;
    private final OrderItemRepository orderItemRepository;
    private final LocalDateTimeService dateTimeService;

    @Autowired
    public OrderItemServiceImpl(EmployeeRepository employeeRepository, OrderItemRepository orderItemRepository, LocalDateTimeService dateTimeService) {
        this.employeeRepository = employeeRepository;
        this.orderItemRepository = orderItemRepository;
        this.dateTimeService = dateTimeService;
    }

    @Override
    public Page<OrderItem> getAllItemRequests(Pageable pageable, OrderItemStatus status, ItemCategory category) {
        return orderItemRepository.findAllItemRequests(pageable, status, category);
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void takeItemRequest(Integer itemId, String employeePin) {
        var employee = employeeRepository.findEmployeeByPin(employeePin);
        var orderItem = orderItemRepository.findOneByIdWithItemReference(itemId);

        if (employee.isEmpty()) {
            throw new InvalidEmployeeTypeException(employeePin);
        }

        if (orderItem.isEmpty()) {
            throw new OrderItemNotFoundException("Order item with id " + itemId + " is not present.");
        }

        var item = orderItem.get();
        var employeeCurrent = employee.get();

        if (employeeCurrent.getType() == EmployeeType.CHEF && item.getItem().getItem().getCategory() == ItemCategory.DRINK) {
            throw new InvalidEmployeeTypeException(employeePin);
        } else if (employeeCurrent.getType() == EmployeeType.BARTENDER && item.getItem().getItem().getCategory() == ItemCategory.FOOD) {
            throw new InvalidEmployeeTypeException(employeePin);
        }

        item.setPreparedBy(employee.get());
        item.setStatus(OrderItemStatus.PREPARING);
        item.setTakenAt(dateTimeService.currentTime());

        orderItemRepository.save(item);
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void finishItemRequest(Integer itemId, String employeePin) {
        var employee = employeeRepository.findEmployeeByPin(employeePin);
        var orderItem = orderItemRepository.findOneInProgressByIdWithItemReference(itemId);

        if (employee.isEmpty()) {
            throw new InvalidEmployeeTypeException(employeePin);
        }

        if (orderItem.isEmpty()) {
            throw new OrderItemNotFoundException("Order item with id " + itemId + " is not present.");
        }

        var item = orderItem.get();
        var employeeCurrent = employee.get();

        if (item.getPreparedBy() != null && !Objects.equals(item.getPreparedBy().getId(), employeeCurrent.getId())) {
            throw new InvalidEmployeeTypeException(employeePin);
        }

        if (employeeCurrent.getType() == EmployeeType.CHEF && item.getItem().getItem().getCategory() == ItemCategory.DRINK) {
            throw new InvalidEmployeeTypeException(employeePin);
        } else if (employeeCurrent.getType() == EmployeeType.BARTENDER && item.getItem().getItem().getCategory() == ItemCategory.FOOD) {
            throw new InvalidEmployeeTypeException(employeePin);
        }

        item.setStatus(OrderItemStatus.DONE);
        item.setPreparedAt(dateTimeService.currentTime());

        if (item.getPreparedBy() == null) {
            item.setTakenAt(dateTimeService.currentTime());
            item.setPreparedBy(employee.get());
        }
        orderItemRepository.save(item);

        var allFromGroup = orderItemRepository.getAllFromOneGroup(item.getOrderItemGroup().getId());

        if (allFromGroup.stream().allMatch(oi -> oi.getStatus() == OrderItemStatus.DONE)) {
            item.getOrderItemGroup().setStatus(OrderItemGroupStatus.DONE);
            // NOTIFIKACIJA
        }
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public boolean hasActiveOrderItems(Employee employee) {
        return orderItemRepository.streamActiveOrderItemsForEmployee(employee.getId()).findAny().isPresent();
    }

    @Override
    public void updateOrderItem(Integer orderItemId, Integer amount, String pin) {
        var orderItem = this.orderItemRepository.findById(orderItemId)
                .orElseThrow(()  -> new NotFoundException(String.format("Order item with id %d does not exist.", orderItemId)));
        if(orderItem.getStatus() != OrderItemStatus.NEW)
            throw new OrderItemInvalidStatusException(String.format("Status of order item with id %d is not NEW, and it cannot be updated.", orderItemId));
        if (amount <= 0)
            throw new IllegalAmountException(amount);
        var employee = this.employeeRepository.findEmployeeByPin(pin)
                        .orElseThrow(()->new EmployeeNotFoundException(String.format("Employee with pin %s does not exist.", pin)));
        if (!employee.getId().equals(orderItem.getOrderItemGroup().getOrder().getWaiter().getId()))
            throw new InvalidEmployeeTypeException(pin);

        orderItem.setAmount(amount);
        this.orderItemRepository.save(orderItem);
    }
}
