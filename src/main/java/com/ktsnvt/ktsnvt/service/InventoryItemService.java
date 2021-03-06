package com.ktsnvt.ktsnvt.service;

import com.ktsnvt.ktsnvt.model.InventoryItem;
import com.ktsnvt.ktsnvt.model.enums.ItemCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;

public interface InventoryItemService {

    InventoryItem createInventoryItem(InventoryItem inventoryItem);

    InventoryItem readForUpdate(Integer id);

    Page<InventoryItem> read(String query, BigDecimal basePriceFrom, BigDecimal basePriceTo,
                             ItemCategory itemCategory, Pageable pageable);

    void delete(Integer id);

    void update(Integer id, String name, String description, String allergies, String image, ItemCategory category,
                BigDecimal basePrice);
}
