package com.ktsnvt.ktsnvt.unit.service;

import com.ktsnvt.ktsnvt.exception.EntityAlreadyDeactivatedException;
import com.ktsnvt.ktsnvt.exception.MenuItemNotFoundException;
import com.ktsnvt.ktsnvt.exception.UsedMenuItemDeletionException;
import com.ktsnvt.ktsnvt.model.InventoryItem;
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

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
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

    @Test
    void createMenuItem_calledWithValidArguments_isSuccess() {
        // GIVEN
        var price = BigDecimal.valueOf(42);
        var inventoryItemId = Integer.valueOf(42);
        var inventoryItem = new InventoryItem();
        inventoryItem.setId(inventoryItemId);
        var currentTime = LocalDateTime.of(2021, 12, 1, 3, 14);
        var menuItem = new MenuItem(price, currentTime, null, inventoryItem);
        menuItem.setId(322);

        var menuItemServiceSpy = spy(menuItemService);
        doReturn(inventoryItem).when(inventoryItemService).readForUpdate(inventoryItemId);
        doNothing().when(menuItemServiceSpy).removeActiveMenuItemForInventoryItem(inventoryItemId);
        doReturn(currentTime).when(localDateTimeService).currentTime();
        doReturn(menuItem).when(menuItemRepository).save(any(MenuItem.class));

        // WHEN
        var returnedMenuItem = menuItemServiceSpy.createMenuItem(price, inventoryItemId);

        // THEN
        assertEquals(menuItem, returnedMenuItem);
        assertEquals(Boolean.TRUE, inventoryItem.getIsInMenu());
        assertEquals(returnedMenuItem, menuItem);
        verify(inventoryItemService, times(1)).readForUpdate(inventoryItemId);
        verify(menuItemServiceSpy, times(1))
                .removeActiveMenuItemForInventoryItem(inventoryItemId);
        verify(localDateTimeService, times(1)).currentTime();
        verify(menuItemRepository, times(1)).save(any(MenuItem.class));
    }

    @Test
    void removeActiveMenuItemForInventoryItem_calledWithExistingInventoryItemId_isSuccess() {
        //GIVEN
        var id = Integer.valueOf(42);
        var inventoryItem = new InventoryItem();
        inventoryItem.setId(322);
        var menuItem = new MenuItem();
        menuItem.setId(id);
        inventoryItem.addMenuItem(menuItem);
        var currentTime = LocalDateTime.of(2021, 12, 1, 3, 14);

        doReturn(Optional.of(menuItem)).when(menuItemRepository).findActiveForInventoryItem(id);
        doReturn(currentTime).when(localDateTimeService).currentTime();

        // WHEN
        menuItemService.removeActiveMenuItemForInventoryItem(id);

        // THEN
        assertEquals(currentTime, menuItem.getEndDate());
        assertEquals(Boolean.FALSE, menuItem.getIsActive());
        assertEquals(Boolean.FALSE, inventoryItem.getIsInMenu());
        verify(menuItemRepository, times(1)).findActiveForInventoryItem(id);
        verify(localDateTimeService, times(1)).currentTime();
    }

    @Test
    void updateMenuItemPrice_whenCalledWithValidArguments_isSuccess() {
        // GIVEN
        var price = BigDecimal.valueOf(42);
        var menuItemId = Integer.valueOf(322);
        var menuItem = new MenuItem();
        menuItem.setId(menuItemId);
        var inventoryItem = new InventoryItem();
        inventoryItem.setId(28);
        inventoryItem.addMenuItem(menuItem);

        var menuItemServiceSpy = spy(menuItemService);
        doReturn(menuItem).when(menuItemServiceSpy).readForUpdate(menuItemId);
        doReturn(menuItem).when(menuItemServiceSpy).createMenuItem(price, inventoryItem.getId());

        // WHEN
        var returnedMenuItem = menuItemServiceSpy.updateMenuItemPrice(price, menuItemId);

        // THEN
        assertEquals(menuItem, returnedMenuItem);
        verify(menuItemServiceSpy, times(1)).readForUpdate(menuItemId);
        verify(menuItemServiceSpy, times(1)).createMenuItem(price, inventoryItem.getId());
    }

    @Test
    void deactivateMenuItem_calledWithActiveInventoryItemWithNoActiveOrders_isSuccess() {
        // GIVEN
        var id = Integer.valueOf(42);
        var menuItem = new MenuItem();
        menuItem.setId(id);
        var inventoryItem = new InventoryItem();
        inventoryItem.addMenuItem(menuItem);

        var currentTime = LocalDateTime.of(2021, 12, 1, 3, 14);

        var menuItemServiceSpy = spy(menuItemService);
        doReturn(menuItem).when(menuItemServiceSpy).readForUpdate(id);
        doReturn(Boolean.FALSE).when(orderItemService).hasActiveOrderItems(menuItem);
        doReturn(currentTime).when(localDateTimeService).currentTime();

        // WHEN
        menuItemServiceSpy.deactivateMenuItem(id);

        // THEN
        assertEquals(currentTime, menuItem.getEndDate());
        assertEquals(Boolean.FALSE, menuItem.getIsActive());
        assertEquals(Boolean.FALSE, inventoryItem.getIsInMenu());
        verify(menuItemServiceSpy, times(1)).readForUpdate(id);
        verify(orderItemService, times(1)).hasActiveOrderItems(menuItem);
    }

    @Test
    void deactivateMenuItem_calledWithAlreadyDeactivatedMenuItem_throwsException() {
        // GIVEN
        var id = Integer.valueOf(42);
        var menuItem = new MenuItem();
        menuItem.setId(id);
        var inventoryItem = new InventoryItem();
        inventoryItem.addMenuItem(menuItem);
        var currentTime = LocalDateTime.of(2021, 12, 1, 3, 14);
        menuItem.deactivateMenuItem(currentTime);
        var newCurrentTime = LocalDateTime.of(2021, 12, 3, 2, 2);

        var menuItemServiceSpy = spy(menuItemService);
        doReturn(menuItem).when(menuItemServiceSpy).readForUpdate(id);
        doReturn(Boolean.FALSE).when(orderItemService).hasActiveOrderItems(menuItem);
        doReturn(newCurrentTime).when(localDateTimeService).currentTime();

        // WHEN
        assertThrows(EntityAlreadyDeactivatedException.class, () -> menuItemServiceSpy.deactivateMenuItem(id));

        // THEN
        assertNotEquals(newCurrentTime, menuItem.getEndDate());
        verify(menuItemServiceSpy, times(1)).readForUpdate(id);
        verifyNoInteractions(orderItemService);
        verifyNoInteractions(localDateTimeService);
    }

    @Test
    void deactivateMenuItem_calledWithMenuItemWithActiveOrderItems_throwsException() {
        // GIVEN
        var id = Integer.valueOf(42);
        var menuItem = new MenuItem();
        menuItem.setId(id);
        var inventoryItem = new InventoryItem();
        inventoryItem.addMenuItem(menuItem);
        var newCurrentTime = LocalDateTime.of(2021, 12, 3, 2, 2);

        var menuItemServiceSpy = spy(menuItemService);
        doReturn(menuItem).when(menuItemServiceSpy).readForUpdate(id);
        doReturn(Boolean.TRUE).when(orderItemService).hasActiveOrderItems(menuItem);
        doReturn(newCurrentTime).when(localDateTimeService).currentTime();

        // WHEN
        assertThrows(UsedMenuItemDeletionException.class, () -> menuItemServiceSpy.deactivateMenuItem(id));

        // THEN
        assertNull(menuItem.getEndDate());
        assertEquals(Boolean.TRUE, menuItem.getIsActive());
        assertEquals(Boolean.TRUE, inventoryItem.getIsInMenu());
        verify(menuItemServiceSpy, times(1)).readForUpdate(id);
        verify(orderItemService, times(1)).hasActiveOrderItems(menuItem);
        verifyNoInteractions(localDateTimeService);
    }

}
