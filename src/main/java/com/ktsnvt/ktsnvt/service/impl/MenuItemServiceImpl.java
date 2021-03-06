package com.ktsnvt.ktsnvt.service.impl;

import com.ktsnvt.ktsnvt.exception.EntityAlreadyDeactivatedException;
import com.ktsnvt.ktsnvt.exception.MenuItemNotFoundException;
import com.ktsnvt.ktsnvt.exception.UsedMenuItemDeletionException;
import com.ktsnvt.ktsnvt.model.MenuItem;
import com.ktsnvt.ktsnvt.model.enums.ItemCategory;
import com.ktsnvt.ktsnvt.repository.MenuItemRepository;
import com.ktsnvt.ktsnvt.service.InventoryItemService;
import com.ktsnvt.ktsnvt.service.LocalDateTimeService;
import com.ktsnvt.ktsnvt.service.MenuItemService;
import com.ktsnvt.ktsnvt.service.OrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class MenuItemServiceImpl extends TransactionalServiceBase implements MenuItemService {
    private final MenuItemRepository menuItemRepository;

    private final InventoryItemService inventoryItemService;
    private final LocalDateTimeService localDateTimeService;
    private final OrderItemService orderItemService;

    @Autowired
    public MenuItemServiceImpl(MenuItemRepository menuItemRepository, InventoryItemService inventoryItemService,
                               LocalDateTimeService localDateTimeService, OrderItemService orderItemService) {
        this.menuItemRepository = menuItemRepository;
        this.inventoryItemService = inventoryItemService;
        this.localDateTimeService = localDateTimeService;
        this.orderItemService = orderItemService;
    }


    @Override
    public Page<MenuItem> getMenuItems(String name, Pageable pageable) {
        return this.menuItemRepository.getMenuItems(name, pageable);
    }

    @Override
    public void removeActiveMenuItemForInventoryItem(Integer inventoryItemId) {
        menuItemRepository
                .findActiveForInventoryItem(inventoryItemId)
                .ifPresent(menuItem -> menuItem.deactivateMenuItem(localDateTimeService.currentTime()));
    }

    @Override
    public Page<MenuItem> read(String query, BigDecimal priceFrom, BigDecimal priceTo,
                               ItemCategory itemCategory, Pageable pageable) {
        return menuItemRepository.findAll(query.trim().toLowerCase(), priceFrom,
                priceTo, itemCategory, pageable);
    }

    @Override
    public void deactivateMenuItem(Integer id) {
        var menuItem = this.readForUpdate(id);
        if (menuItem.getEndDate() != null) {
            throw new EntityAlreadyDeactivatedException(
                    String.format("Menu Item with id: %d is already finalized and has an end date.", id));
        }
        if (orderItemService.hasActiveOrderItems(menuItem)) {
            throw new UsedMenuItemDeletionException(
                    String.format("Menu Item with id: %d is contained in orders that are not finalized.", id));
        }
        menuItem.deactivateMenuItem(localDateTimeService.currentTime());
    }

    @Override
    public MenuItem readForUpdate(Integer id) {
        return menuItemRepository.findOneForUpdate(id)
                .orElseThrow(() -> new MenuItemNotFoundException("Menu item with id: " + id + " not found"));
    }

    @Override
    public MenuItem updateMenuItemPrice(BigDecimal price, Integer menuItemId) {
        var menuItem = readForUpdate(menuItemId);
        return createMenuItem(price, menuItem.getItem().getId());
    }

    @Override
    public MenuItem createMenuItem(BigDecimal price, Integer inventoryItemId) {
        var inventoryItem = inventoryItemService.readForUpdate(inventoryItemId);
        removeActiveMenuItemForInventoryItem(inventoryItemId);
        var menuItem = new MenuItem(price, localDateTimeService.currentTime(), null, inventoryItem);
        inventoryItem.addMenuItem(menuItem);
        return menuItemRepository.save(menuItem);
    }
}
