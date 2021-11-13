package com.ktsnvt.ktsnvt.service.impl;

import com.ktsnvt.ktsnvt.exception.InventoryItemNameAlreadyExistsException;
import com.ktsnvt.ktsnvt.exception.InventoryItemNotFoundException;
import com.ktsnvt.ktsnvt.model.InventoryItem;
import com.ktsnvt.ktsnvt.repository.InventoryItemRepository;
import com.ktsnvt.ktsnvt.service.InventoryItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InventoryItemServiceImpl implements InventoryItemService {

    private final InventoryItemRepository inventoryItemRepository;

    @Autowired
    public InventoryItemServiceImpl(InventoryItemRepository inventoryItemRepository) {
        this.inventoryItemRepository = inventoryItemRepository;
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
}
