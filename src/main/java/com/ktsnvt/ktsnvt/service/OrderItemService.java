package com.ktsnvt.ktsnvt.service;

import com.ktsnvt.ktsnvt.model.OrderItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface OrderItemService {

    Page<OrderItem> getAllFoodRequests(Pageable pageable, String employeePin);

    Page<OrderItem> getAllDrinkRequests(Pageable pageable, String employeePin);

    Page<OrderItem> getAllFoodInPreparation(Pageable pageable, String employeePin);

    Page<OrderItem> getAllDrinksInPreparation(Pageable pageable, String employeePin);

    void takeItemRequest(Integer itemId, String employeePin);

    void finishItemRequest(Integer itemId, String employeePin);
}
