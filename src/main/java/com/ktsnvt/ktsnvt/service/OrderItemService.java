package com.ktsnvt.ktsnvt.service;

import com.ktsnvt.ktsnvt.model.Employee;
import com.ktsnvt.ktsnvt.model.InventoryItem;
import com.ktsnvt.ktsnvt.model.MenuItem;
import com.ktsnvt.ktsnvt.model.OrderItem;
import com.ktsnvt.ktsnvt.model.enums.ItemCategory;
import com.ktsnvt.ktsnvt.model.enums.OrderItemStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface OrderItemService {

    Page<OrderItem> getAllItemRequests(Pageable pageable, OrderItemStatus status, ItemCategory category);

    void takeItemRequest(Integer itemId, String employeePin);

    Integer finishItemRequest(Integer itemId, String employeePin);

    boolean hasActiveOrderItems(Employee employee);

    boolean hasActiveOrderItems(MenuItem menuItem);

    boolean hasActiveOrderItems(InventoryItem inventoryItem);

    OrderItem addOrderItem(Integer orderGroupId, Integer menuItemId, Integer amount, String pin);

    void updateOrderItem(Integer orderItemId, Integer amount, String pin);

    void deleteOrderItem(Integer orderItemId, String pin);
}
