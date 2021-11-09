package com.ktsnvt.ktsnvt.service.impl;

import com.ktsnvt.ktsnvt.exception.InvalidEmployeeTypeException;
import com.ktsnvt.ktsnvt.exception.OrderItemNotFoundException;
import com.ktsnvt.ktsnvt.model.OrderItem;
import com.ktsnvt.ktsnvt.model.enums.EmployeeType;
import com.ktsnvt.ktsnvt.model.enums.ItemCategory;
import com.ktsnvt.ktsnvt.model.enums.OrderItemStatus;
import com.ktsnvt.ktsnvt.repository.EmployeeRepository;
import com.ktsnvt.ktsnvt.repository.OrderItemRepository;
import com.ktsnvt.ktsnvt.service.LocalDateTimeService;
import com.ktsnvt.ktsnvt.service.OrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
    public Page<OrderItem> getAllFoodRequests(Pageable pageable) {
        return orderItemRepository.findAllFoodRequests(pageable);
    }

    @Override
    public Page<OrderItem> getAllDrinkRequests(Pageable pageable) {
        return orderItemRepository.findAllDrinkRequests(pageable);
    }

    @Override
    public Page<OrderItem> getAllFoodInPreparation(Pageable pageable) {
        return orderItemRepository.findAllFoodInPreparation(pageable);
    }

    @Override
    public Page<OrderItem> getAllDrinksInPreparation(Pageable pageable) {
        return orderItemRepository.findAllDrinksInPreparation(pageable);
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

        if(item.getPreparedBy() != null && !Objects.equals(item.getPreparedBy().getId(), employeeCurrent.getId())){
            throw new InvalidEmployeeTypeException(employeePin);
        }

        if (employeeCurrent.getType() == EmployeeType.CHEF && item.getItem().getItem().getCategory() == ItemCategory.DRINK) {
            throw new InvalidEmployeeTypeException(employeePin);
        } else if (employeeCurrent.getType() == EmployeeType.BARTENDER && item.getItem().getItem().getCategory() == ItemCategory.FOOD) {
            throw new InvalidEmployeeTypeException(employeePin);
        }

        item.setStatus(OrderItemStatus.DONE);
        item.setPreparedAt(dateTimeService.currentTime());

        if(item.getPreparedBy() == null){
            item.setTakenAt(dateTimeService.currentTime());
            item.setPreparedBy(employee.get());
        }

        orderItemRepository.save(item);
    }
}
