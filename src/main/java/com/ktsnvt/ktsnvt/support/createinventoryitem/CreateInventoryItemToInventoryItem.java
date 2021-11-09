package com.ktsnvt.ktsnvt.support.createinventoryitem;

import com.ktsnvt.ktsnvt.dto.createinventoryitem.CreateInventoryItemRequest;
import com.ktsnvt.ktsnvt.model.InventoryItem;
import com.ktsnvt.ktsnvt.support.AbstractConverter;
import lombok.NonNull;
import org.springframework.stereotype.Component;

@Component
public class CreateInventoryItemToInventoryItem extends AbstractConverter<CreateInventoryItemRequest, InventoryItem> {
    @Override
    public InventoryItem convert(@NonNull CreateInventoryItemRequest createInventoryItemRequest) {
        return getModelMapper().map(createInventoryItemRequest, InventoryItem.class);
    }
}
