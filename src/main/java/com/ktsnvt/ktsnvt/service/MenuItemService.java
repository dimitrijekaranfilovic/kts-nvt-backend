package com.ktsnvt.ktsnvt.service;

import com.ktsnvt.ktsnvt.model.MenuItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;

public interface MenuItemService {


    Page<MenuItem> getMenuItems(String name, Pageable pageable);

    MenuItem createMenuItem(BigDecimal price, Integer inventoryItemId);

    void removeActiveMenuItemForInventoryItem(Integer inventoryItemId);

    void deactivateMenuItem(Integer id);

    MenuItem readForUpdate(Integer id);

    MenuItem updateMenuItemPrice(BigDecimal price, Integer menuItemId);
}
