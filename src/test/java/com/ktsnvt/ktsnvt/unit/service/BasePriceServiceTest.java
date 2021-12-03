package com.ktsnvt.ktsnvt.unit.service;


import com.ktsnvt.ktsnvt.exception.InventoryItemHasNoActiveBasePrice;
import com.ktsnvt.ktsnvt.model.BasePrice;
import com.ktsnvt.ktsnvt.model.InventoryItem;
import com.ktsnvt.ktsnvt.repository.BasePriceRepository;
import com.ktsnvt.ktsnvt.service.LocalDateTimeService;
import com.ktsnvt.ktsnvt.service.impl.BasePriceServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
class BasePriceServiceTest {

    @Mock
    private BasePriceRepository basePriceRepository;

    @Mock
    private LocalDateTimeService localDateTimeService;

    @InjectMocks
    private BasePriceServiceImpl basePriceService;

    @Test
    void endActiveBasePriceForInventoryItem_calledWithInventoryItemWithActiveBasePrice_isSuccess() {
        // GIVEN
        var inventoryItem = new InventoryItem();
        inventoryItem.setId(42);
        var currentBasePrice = new BasePrice(LocalDateTime.of(2021, 12, 1, 3, 14),
                null, BigDecimal.valueOf(42), inventoryItem);
        inventoryItem.addBasePrice(currentBasePrice);

        var endDate = LocalDateTime.of(2021, 12, 2, 4, 2);
        doReturn(endDate).when(localDateTimeService).currentTime();
        doReturn(Optional.of(currentBasePrice)).when(basePriceRepository)
                .findActiveForInventoryItem(inventoryItem.getId());

        // WHEN
        basePriceService.endActiveBasePriceForInventoryItem(inventoryItem);

        // THEN
        assertEquals(endDate, currentBasePrice.getEndDate());
        verify(basePriceRepository, times(1)).findActiveForInventoryItem(inventoryItem.getId());
    }


    @Test
    void endActiveBasePriceForInventoryItem_calledWithInventoryItemWithNoActiveBasePRice_throwsException() {
        // GIVEN
        var inventoryItem = new InventoryItem();
        inventoryItem.setId(42);

        doReturn(Optional.empty()).when(basePriceRepository).findActiveForInventoryItem(inventoryItem.getId());

        // WHEN
        assertThrows(InventoryItemHasNoActiveBasePrice.class,
                () -> basePriceService.endActiveBasePriceForInventoryItem(inventoryItem));

        // THEN
        verify(basePriceRepository, times(1)).findActiveForInventoryItem(inventoryItem.getId());
    }
}
