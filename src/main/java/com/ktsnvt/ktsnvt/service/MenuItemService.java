package com.ktsnvt.ktsnvt.service;

import com.ktsnvt.ktsnvt.model.MenuItem;
import com.ktsnvt.ktsnvt.model.enums.ItemCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;

public interface MenuItemService {


    Page<MenuItem> getMenuItems(String name, Pageable pageable);

    MenuItem createMenuItem(BigDecimal price, Integer inventoryItemId);

    void removeActiveMenuItemForInventoryItem(Integer inventoryItemId);

    Page<MenuItem> read(String query, BigDecimal priceFrom, BigDecimal priceTo,
                        ItemCategory itemCategory, Pageable pageable);

    void deactivateMenuItem(Integer id);

    MenuItem readForUpdate(Integer id);

    MenuItem updateMenuItemPrice(BigDecimal price, Integer menuItemId);
}
