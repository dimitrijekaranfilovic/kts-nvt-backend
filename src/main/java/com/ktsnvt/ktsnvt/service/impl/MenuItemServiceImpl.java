package com.ktsnvt.ktsnvt.service.impl;

import com.ktsnvt.ktsnvt.model.MenuItem;
import com.ktsnvt.ktsnvt.repository.MenuItemRepository;
import com.ktsnvt.ktsnvt.service.InventoryItemService;
import com.ktsnvt.ktsnvt.service.LocalDateTimeService;
import com.ktsnvt.ktsnvt.service.MenuItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class MenuItemServiceImpl implements MenuItemService {

    private final MenuItemRepository menuItemRepository;
    private final InventoryItemService inventoryItemService;
    private final LocalDateTimeService localDateTimeService;

    @Autowired
    public MenuItemServiceImpl(MenuItemRepository menuItemRepository, InventoryItemService inventoryItemService, LocalDateTimeService localDateTimeService) {
        this.menuItemRepository = menuItemRepository;
        this.inventoryItemService = inventoryItemService;
        this.localDateTimeService = localDateTimeService;
    }


    @Override
    public Page<MenuItem> getMenuItems(String name, Pageable pageable) {
        return this.menuItemRepository.getMenuItems(name, pageable);
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void removeActiveMenuItemForInventoryItem(Integer inventoryItemId) {
        menuItemRepository.findActiveForInventoryItem(inventoryItemId)
                .ifPresent(menuItem -> menuItem.deactivateMenuItem(localDateTimeService.currentTime()));
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public MenuItem createMenuItem(BigDecimal price, Integer inventoryItemId) {
        var inventoryItem = inventoryItemService.readForUpdate(inventoryItemId);
        removeActiveMenuItemForInventoryItem(inventoryItemId);
        MenuItem menuItem = new MenuItem(price, localDateTimeService.currentTime(), null, inventoryItem);
        inventoryItem.addMenuItem(menuItem);
        return menuItemRepository.save(menuItem);
    }
}
