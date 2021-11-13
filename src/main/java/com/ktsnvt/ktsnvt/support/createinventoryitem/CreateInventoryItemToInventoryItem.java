package com.ktsnvt.ktsnvt.support.createinventoryitem;

import com.ktsnvt.ktsnvt.dto.createinventoryitem.CreateInventoryItemRequest;
import com.ktsnvt.ktsnvt.model.BasePrice;
import com.ktsnvt.ktsnvt.model.InventoryItem;
import com.ktsnvt.ktsnvt.service.LocalDateTimeService;
import com.ktsnvt.ktsnvt.support.AbstractConverter;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CreateInventoryItemToInventoryItem extends AbstractConverter<CreateInventoryItemRequest, InventoryItem> {
    private final LocalDateTimeService localDateTimeService;

    @Autowired
    public CreateInventoryItemToInventoryItem(LocalDateTimeService localDateTimeService) {
        this.localDateTimeService = localDateTimeService;
    }

    @Override
    public InventoryItem convert(@NonNull CreateInventoryItemRequest source) {
        var inventoryItem = getModelMapper().map(source, InventoryItem.class);
        inventoryItem.addBasePrice(
                new BasePrice(localDateTimeService.currentTime(), null, source.getBasePrice(), inventoryItem));
        return inventoryItem;
    }
}
