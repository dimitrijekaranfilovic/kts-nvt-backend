package com.ktsnvt.ktsnvt.service.impl;

import com.ktsnvt.ktsnvt.model.BasePrice;
import com.ktsnvt.ktsnvt.model.InventoryItem;
import com.ktsnvt.ktsnvt.repository.BasePriceRepository;
import com.ktsnvt.ktsnvt.service.BasePriceService;
import com.ktsnvt.ktsnvt.service.InventoryItemService;
import com.ktsnvt.ktsnvt.service.LocalDateTimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
public class BasePriceServiceImpl extends TransactionalServiceBase implements BasePriceService {
    private final BasePriceRepository basePriceRepository;

    private final LocalDateTimeService localDateTimeService;
    private final InventoryItemService inventoryItemService;

    @Autowired
    public BasePriceServiceImpl(BasePriceRepository basePriceRepository, LocalDateTimeService localDateTimeService,
                                @Lazy InventoryItemService inventoryItemService) {
        this.basePriceRepository = basePriceRepository;
        this.localDateTimeService = localDateTimeService;
        this.inventoryItemService = inventoryItemService;
    }

    @Override
    public void endActiveBasePriceForInventoryItem(InventoryItem inventoryItem) {
        this.basePriceRepository.findActiveForInventoryItem(inventoryItem.getId()).ifPresent(
                bp -> bp.setEndDate(localDateTimeService.currentTime())
        );
    }

    @Override
    public void updateInventoryItemBasePrice(Integer id, BasePrice basePrice) {
        var inventoryItem = inventoryItemService.readForUpdate(id);
        endActiveBasePriceForInventoryItem(inventoryItem);
        inventoryItem.addBasePrice(basePrice);
    }
}
