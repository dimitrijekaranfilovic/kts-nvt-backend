package com.ktsnvt.ktsnvt.service;

import com.ktsnvt.ktsnvt.model.InventoryItem;

public interface InventoryItemService {

    InventoryItem createInventoryItem(InventoryItem inventoryItem);

    InventoryItem readForUpdate(Integer id);
}
