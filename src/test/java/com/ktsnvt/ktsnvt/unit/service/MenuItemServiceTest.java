package com.ktsnvt.ktsnvt.unit.service;

import com.ktsnvt.ktsnvt.exception.MenuItemNotFoundException;
import com.ktsnvt.ktsnvt.model.MenuItem;
import com.ktsnvt.ktsnvt.repository.MenuItemRepository;
import com.ktsnvt.ktsnvt.service.InventoryItemService;
import com.ktsnvt.ktsnvt.service.LocalDateTimeService;
import com.ktsnvt.ktsnvt.service.OrderItemService;
import com.ktsnvt.ktsnvt.service.impl.MenuItemServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
class MenuItemServiceTest {

    @Mock
    private MenuItemRepository menuItemRepository;

    @Mock
    private InventoryItemService inventoryItemService;

    @Mock
    private LocalDateTimeService localDateTimeService;

    @Mock
    private OrderItemService orderItemService;

    @InjectMocks
    private MenuItemServiceImpl menuItemService;


    @Test
    void readForUpdate_calledWithExistingId_isSuccess() {
        // GIVEN
        var id = Integer.valueOf(42);
        var menuItem = new MenuItem();
        menuItem.setId(id);

        doReturn(Optional.of(menuItem)).when(menuItemRepository).findOneForUpdate(id);

        // WHEN
        var returnedMenuItem = menuItemService.readForUpdate(id);

        // THEN
        assertEquals(menuItem, returnedMenuItem);
        verify(menuItemRepository, times(1)).findOneForUpdate(id);
    }

    @Test
    void readForUpdate_calledWithNonexistentId_throwsException() {
        // GIVEN
        var id = Integer.valueOf(42);

        doReturn(Optional.empty()).when(menuItemRepository).findOneForUpdate(id);

        // WHEN
        assertThrows(MenuItemNotFoundException.class, () -> menuItemService.readForUpdate(id));

        // THEN
        verify(menuItemRepository, times(1)).findOneForUpdate(id);
    }

}
