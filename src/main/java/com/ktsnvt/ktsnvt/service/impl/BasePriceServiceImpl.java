package com.ktsnvt.ktsnvt.service.impl;

import com.ktsnvt.ktsnvt.model.InventoryItem;
import com.ktsnvt.ktsnvt.repository.BasePriceRepository;
import com.ktsnvt.ktsnvt.service.BasePriceService;
import com.ktsnvt.ktsnvt.service.LocalDateTimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BasePriceServiceImpl implements BasePriceService {

    private final BasePriceRepository basePriceRepository;
    private final LocalDateTimeService localDateTimeService;

    @Autowired
    public BasePriceServiceImpl(BasePriceRepository basePriceRepository, LocalDateTimeService localDateTimeService) {
        this.basePriceRepository = basePriceRepository;
        this.localDateTimeService = localDateTimeService;
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void endActiveBasePriceForInventoryItem(InventoryItem inventoryItem) {
        this.basePriceRepository.findActiveForInventoryItem(inventoryItem.getId()).ifPresent(
                bp -> bp.setEndDate(localDateTimeService.currentTime())
        );
    }
}
