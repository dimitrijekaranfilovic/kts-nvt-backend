package com.ktsnvt.ktsnvt.service.impl;

import com.ktsnvt.ktsnvt.exception.InventoryItemNameAlreadyExistsException;
import com.ktsnvt.ktsnvt.exception.InventoryItemNotFoundException;
import com.ktsnvt.ktsnvt.exception.UsedInventoryItemDeletionException;
import com.ktsnvt.ktsnvt.model.BasePrice;
import com.ktsnvt.ktsnvt.model.InventoryItem;
import com.ktsnvt.ktsnvt.model.enums.ItemCategory;
import com.ktsnvt.ktsnvt.repository.InventoryItemRepository;
import com.ktsnvt.ktsnvt.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class InventoryItemServiceImpl implements InventoryItemService {

    private final InventoryItemRepository inventoryItemRepository;
    private final OrderItemService orderItemService;
    private final BasePriceService basePriceService;
    private final MenuItemService menuItemService;
    private final LocalDateTimeService localDateTimeService;

    @Autowired
    public InventoryItemServiceImpl(InventoryItemRepository inventoryItemRepository, OrderItemService orderItemService,
                                    BasePriceService basePriceService, @Lazy MenuItemService menuItemService,
                                    LocalDateTimeService localDateTimeService) {
        this.inventoryItemRepository = inventoryItemRepository;
        this.orderItemService = orderItemService;
        this.basePriceService = basePriceService;
        this.menuItemService = menuItemService;
        this.localDateTimeService = localDateTimeService;
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public InventoryItem createInventoryItem(InventoryItem inventoryItem) {
        var sameInventoryItem = inventoryItemRepository.findByName(inventoryItem.getName());
        if (sameInventoryItem.isPresent()) {
            throw new InventoryItemNameAlreadyExistsException(inventoryItem.getName());
        }
        return inventoryItemRepository.save(inventoryItem);
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public InventoryItem readForUpdate(Integer id) {
        return inventoryItemRepository.findOneForUpdate(id)
                .orElseThrow(() -> new InventoryItemNotFoundException("Inventory Item with id: " + id + " not found"));
    }

    @Override
    public Page<InventoryItem> read(String query, BigDecimal basePriceFrom, BigDecimal basePriceTo,
                                    ItemCategory itemCategory, Pageable pageable) {
        return inventoryItemRepository.findAll(query.trim().toLowerCase(), basePriceFrom,
                basePriceTo, itemCategory, pageable);
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void delete(Integer id) {
        var inventoryItem = readForUpdate(id);
        if (orderItemService.hasActiveOrderItems(inventoryItem)) {
            throw new UsedInventoryItemDeletionException(
                    String.format("Inventory Item with id: %d is contained in orders that are not finalized.", id));
        }
        basePriceService.endActiveBasePriceForInventoryItem(inventoryItem);
        menuItemService.removeActiveMenuItemForInventoryItem(id);
        inventoryItem.setIsActive(Boolean.FALSE);
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void update(Integer id, String name, String description, String allergies, String image, ItemCategory category,
                       BigDecimal basePrice) {
        var inventoryItem = readForUpdate(id);

        if (!name.equals(inventoryItem.getName())) {
            inventoryItemRepository.findByName(name)
                    .ifPresent(item -> {
                        throw new InventoryItemNameAlreadyExistsException(name);
                    });
        }

        basePriceService.updateInventoryItemBasePrice(id,
                new BasePrice(localDateTimeService.currentTime(), null, basePrice, inventoryItem));

        inventoryItem.setName(name);
        inventoryItem.setDescription(description);
        inventoryItem.setAllergies(allergies);
        inventoryItem.setImage(image);
        inventoryItem.setCategory(category);
    }
}
