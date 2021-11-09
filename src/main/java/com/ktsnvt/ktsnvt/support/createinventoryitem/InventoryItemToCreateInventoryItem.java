package com.ktsnvt.ktsnvt.support.createinventoryitem;

import com.ktsnvt.ktsnvt.dto.createinventoryitem.CreateInventoryItemResponse;
import com.ktsnvt.ktsnvt.model.InventoryItem;
import com.ktsnvt.ktsnvt.support.AbstractConverter;
import lombok.NonNull;
import org.springframework.stereotype.Component;

@Component
public class InventoryItemToCreateInventoryItem extends AbstractConverter<InventoryItem, CreateInventoryItemResponse> {
    @Override
    public CreateInventoryItemResponse convert(@NonNull InventoryItem inventoryItem) {
        return getModelMapper().map(inventoryItem, CreateInventoryItemResponse.class);
    }
}
