package com.ktsnvt.ktsnvt.service;

import com.ktsnvt.ktsnvt.model.BasePrice;
import com.ktsnvt.ktsnvt.model.InventoryItem;

public interface BasePriceService {

    void endActiveBasePriceForInventoryItem(InventoryItem inventoryItem);

    void updateInventoryItemBasePrice(Integer id, BasePrice basePrice);
}
