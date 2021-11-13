package com.ktsnvt.ktsnvt.service.impl;

import com.ktsnvt.ktsnvt.exception.InventoryItemNameAlreadyExistsException;
import com.ktsnvt.ktsnvt.model.InventoryItem;
import com.ktsnvt.ktsnvt.model.enums.ItemCategory;
import com.ktsnvt.ktsnvt.repository.InventoryItemRepository;
import com.ktsnvt.ktsnvt.service.InventoryItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

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
    public Page<InventoryItem> read(String query, BigDecimal basePriceFrom, BigDecimal basePriceTo,
                                    ItemCategory itemCategory, Pageable pageable) {
        return inventoryItemRepository.findAll(query.trim().toLowerCase(), basePriceFrom,
                basePriceTo, itemCategory, pageable);
    }
}
