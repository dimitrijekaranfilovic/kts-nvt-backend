package com.ktsnvt.ktsnvt.support.readinventoryitems;

import com.ktsnvt.ktsnvt.dto.readinventoryitems.ReadInventoryItemsResponse;
import com.ktsnvt.ktsnvt.model.InventoryItem;
import com.ktsnvt.ktsnvt.support.AbstractConverter;
import lombok.NonNull;
import org.springframework.stereotype.Component;

@Component
public class InventoryItemToReadInventoryItemsResponse extends AbstractConverter<InventoryItem, ReadInventoryItemsResponse> {
    @Override
    public ReadInventoryItemsResponse convert(@NonNull InventoryItem source) {
        return getModelMapper().map(source, ReadInventoryItemsResponse.class);
    }
}
