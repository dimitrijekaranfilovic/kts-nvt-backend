package com.ktsnvt.ktsnvt.service;

import com.ktsnvt.ktsnvt.model.OrderItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface OrderItemService {

    Page<OrderItem> getAllFoodRequests(Pageable pageable);

    Page<OrderItem> getAllDrinkRequests(Pageable pageable);

    Page<OrderItem> getAllFoodInPreparation(Pageable pageable);

    Page<OrderItem> getAllDrinksInPreparation(Pageable pageable);

    void takeItemRequest(Integer itemId, String employeePin);

    void finishItemRequest(Integer itemId, String employeePin);
}
