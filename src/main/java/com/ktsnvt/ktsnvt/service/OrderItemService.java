package com.ktsnvt.ktsnvt.service;

import com.ktsnvt.ktsnvt.model.Employee;
import com.ktsnvt.ktsnvt.model.OrderItem;
import com.ktsnvt.ktsnvt.model.enums.ItemCategory;
import com.ktsnvt.ktsnvt.model.enums.OrderItemStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface OrderItemService {

    Page<OrderItem> getAllItemRequests(Pageable pageable, OrderItemStatus status, ItemCategory category);

    void takeItemRequest(Integer itemId, String employeePin);

    void finishItemRequest(Integer itemId, String employeePin);

    boolean hasActiveOrderItems(Employee employee);

    OrderItem addOrderItem(Integer orderGroupId, Integer menuItemId, Integer amount, String pin);

    void updateOrderItem(Integer orderItemId, Integer amount, String pin);
}
