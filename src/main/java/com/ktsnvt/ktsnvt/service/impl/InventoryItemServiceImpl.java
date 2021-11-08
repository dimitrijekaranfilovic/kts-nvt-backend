package com.ktsnvt.ktsnvt.service.impl;

import com.ktsnvt.ktsnvt.model.InventoryItem;
import com.ktsnvt.ktsnvt.repository.InventoryItemRepository;
import com.ktsnvt.ktsnvt.service.InventoryItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InventoryItemServiceImpl implements InventoryItemService {

    private final InventoryItemRepository inventoryItemRepository;

    @Autowired
    public InventoryItemServiceImpl(InventoryItemRepository inventoryItemRepository) {
        this.inventoryItemRepository = inventoryItemRepository;
    }

    @Override
    public InventoryItem createInventoryItem(InventoryItem inventoryItem) {
        return inventoryItemRepository.save(inventoryItem);
    }
}
