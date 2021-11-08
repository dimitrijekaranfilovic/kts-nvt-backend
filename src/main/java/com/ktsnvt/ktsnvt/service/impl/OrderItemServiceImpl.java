package com.ktsnvt.ktsnvt.service.impl;

import com.ktsnvt.ktsnvt.exception.InvalidEmployeeTypeException;
import com.ktsnvt.ktsnvt.exception.OrderItemNotFoundException;
import com.ktsnvt.ktsnvt.model.OrderItem;
import com.ktsnvt.ktsnvt.model.enums.EmployeeType;
import com.ktsnvt.ktsnvt.model.enums.ItemCategory;
import com.ktsnvt.ktsnvt.model.enums.OrderItemStatus;
import com.ktsnvt.ktsnvt.repository.EmployeeRepository;
import com.ktsnvt.ktsnvt.repository.OrderItemRepository;
import com.ktsnvt.ktsnvt.service.OrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class OrderItemServiceImpl implements OrderItemService {

    private final EmployeeRepository employeeRepository;
    private final OrderItemRepository orderItemRepository;

    @Autowired
    public OrderItemServiceImpl(EmployeeRepository employeeRepository, OrderItemRepository orderItemRepository) {
        this.employeeRepository = employeeRepository;
        this.orderItemRepository = orderItemRepository;
    }

    @Override
    public Page<OrderItem> getAllFoodRequests(Pageable pageable, String employeePin) {
        var employee = employeeRepository.findEmployeeByPin(employeePin);

        if (employee.isEmpty() || employee.get().getType() == EmployeeType.BARTENDER) {
            throw new InvalidEmployeeTypeException(employeePin);
        }

        return orderItemRepository.findAllFoodRequests(pageable);
    }

    @Override
    public Page<OrderItem> getAllDrinkRequests(Pageable pageable, String employeePin) {
        var employee = employeeRepository.findEmployeeByPin(employeePin);

        if (employee.isEmpty() || employee.get().getType() == EmployeeType.CHEF) {
            throw new InvalidEmployeeTypeException(employeePin);
        }

        return orderItemRepository.findAllDrinkRequests(pageable);
    }

    @Override
    public void takeItemRequest(Integer itemId, String employeePin) {
        var employee = employeeRepository.findEmployeeByPin(employeePin);
        var orderItem = orderItemRepository.findOneByIdWithItemReference(itemId);

        if (employee.isEmpty()) {
            throw new InvalidEmployeeTypeException(employeePin);
        }

        if (orderItem.isEmpty()) {
            throw new OrderItemNotFoundException("Order item with id " + itemId + " is not present.");
        }

        if (employee.get().getType() == EmployeeType.CHEF && orderItem.get().getItem().getItem().getCategory() == ItemCategory.DRINK) {
            throw new InvalidEmployeeTypeException(employeePin);
        } else if (employee.get().getType() == EmployeeType.BARTENDER && orderItem.get().getItem().getItem().getCategory() == ItemCategory.FOOD) {
            throw new InvalidEmployeeTypeException(employeePin);
        }

        orderItem.get().setPreparedBy(employee.get());
        orderItem.get().setStatus(OrderItemStatus.PREPARING);
        orderItem.get().setTakenAt(LocalDateTime.now());

        orderItemRepository.save(orderItem.get());
    }

    @Override
    public void finishItemRequest(Integer itemId, String employeePin) {
        var employee = employeeRepository.findEmployeeByPin(employeePin);
        var orderItem = orderItemRepository.findOneInProgressByIdWithItemReference(itemId);

        if (employee.isEmpty()) {
            throw new InvalidEmployeeTypeException(employeePin);
        }

        if (orderItem.isEmpty()) {
            throw new OrderItemNotFoundException("Order item with id " + itemId + " is not present.");
        }

        if (employee.get().getType() == EmployeeType.CHEF && orderItem.get().getItem().getItem().getCategory() == ItemCategory.DRINK) {
            throw new InvalidEmployeeTypeException(employeePin);
        } else if (employee.get().getType() == EmployeeType.BARTENDER && orderItem.get().getItem().getItem().getCategory() == ItemCategory.FOOD) {
            throw new InvalidEmployeeTypeException(employeePin);
        }

        orderItem.get().setStatus(OrderItemStatus.DONE);
        orderItem.get().setPreparedAt(LocalDateTime.now());

        orderItemRepository.save(orderItem.get());
    }
}
