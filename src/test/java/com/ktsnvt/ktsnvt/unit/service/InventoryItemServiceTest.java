package com.ktsnvt.ktsnvt.unit.service;

import com.ktsnvt.ktsnvt.exception.InventoryItemNameAlreadyExistsException;
import com.ktsnvt.ktsnvt.model.InventoryItem;
import com.ktsnvt.ktsnvt.repository.InventoryItemRepository;
import com.ktsnvt.ktsnvt.service.BasePriceService;
import com.ktsnvt.ktsnvt.service.LocalDateTimeService;
import com.ktsnvt.ktsnvt.service.MenuItemService;
import com.ktsnvt.ktsnvt.service.OrderItemService;
import com.ktsnvt.ktsnvt.service.impl.InventoryItemServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
class InventoryItemServiceTest {

    @Mock
    private InventoryItemRepository inventoryItemRepository;

    @Mock
    private OrderItemService orderItemService;

    @Mock
    private BasePriceService basePriceService;

    @Mock
    private MenuItemService menuItemService;

    @Mock
    private LocalDateTimeService localDateTimeService;

    @InjectMocks
    private InventoryItemServiceImpl inventoryItemService;

    @Test
    void createInventoryItem_whenCalledWithValidData_isSuccess() {
        // GIVEN
        var inventoryItem = new InventoryItem();
        inventoryItem.setName("valid name");
        var savedItem = new InventoryItem();
        savedItem.setId(42);
        savedItem.setName(inventoryItem.getName());

        doReturn(Optional.empty()).when(inventoryItemRepository).findByName(inventoryItem.getName());
        doReturn(savedItem).when(inventoryItemRepository).save(inventoryItem);

        // WHEN
        var returnedItem = inventoryItemService.createInventoryItem(inventoryItem);

        // THEN
        assertEquals(savedItem, returnedItem);
        verify(inventoryItemRepository, times(1)).findByName(inventoryItem.getName());
        verify(inventoryItemRepository, times(1)).save(inventoryItem);
    }

    @Test
    void createInventoryItem_whenCalledWithExistingItemName_throwsException() {
        // GIVEN
        var inventoryItem = new InventoryItem();
        inventoryItem.setName("name taken");
        var sameInventoryItem = new InventoryItem();
        sameInventoryItem.setName("name taken");
        doReturn(Optional.of(sameInventoryItem)).when(inventoryItemRepository).findByName(inventoryItem.getName());

        // WHEN
        assertThrows(InventoryItemNameAlreadyExistsException.class,
                () -> inventoryItemService.createInventoryItem(inventoryItem));

        // THEN
        verify(inventoryItemRepository, times(1)).findByName(inventoryItem.getName());
        verifyNoMoreInteractions(inventoryItemRepository);
    }

}
