package com.ktsnvt.ktsnvt.integration.service;

import com.ktsnvt.ktsnvt.exception.EntityAlreadyDeactivatedException;
import com.ktsnvt.ktsnvt.exception.MenuItemNotFoundException;
import com.ktsnvt.ktsnvt.exception.UsedMenuItemDeletionException;
import com.ktsnvt.ktsnvt.service.impl.MenuItemServiceImpl;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MenuItemServiceTest {

    @Autowired
    private MenuItemServiceImpl menuItemService;

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4})
    void readForUpdate_calledWithValidId_isSuccess(Integer id) {
        var menuItem = menuItemService.readForUpdate(id);
        assertEquals(id, menuItem.getId());
    }

    @ParameterizedTest
    @ValueSource(ints = {28, 496, 42, -1})
    void readForUpdate_calledWithNonExistingId_throwsException(Integer id) {
        assertThrows(MenuItemNotFoundException.class, () -> menuItemService.readForUpdate(id));
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 42, -1})
    void removeActiveMenuItemForInventoryItem_calledWithValidId_isSuccess(Integer id) {
        assertDoesNotThrow(() -> menuItemService.removeActiveMenuItemForInventoryItem(id));
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12})
    void createMenuItem_calledWithValidPriceAndValidInventoryItemId_isSuccess(Integer inventoryItemId) {
        var menuItem = menuItemService.createMenuItem(BigDecimal.valueOf(42), inventoryItemId);
        assertEquals(inventoryItemId, menuItem.getItem().getId());
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4})
    void updateMenuItemPrice_calledWithValidArguments_isSuccess(Integer id) {
        var price = BigDecimal.valueOf(42);
        var menuItem = menuItemService.updateMenuItemPrice(price, id);
        assertNotEquals(id, menuItem.getId());
        assertEquals(0, price.compareTo(menuItem.getPrice()));
    }

    @ParameterizedTest
    @ValueSource(ints = {4, 5})
    void deactivateMenuItem_calledWithActiveMenuItem_isSuccess(Integer id) {
        assertDoesNotThrow(() -> menuItemService.deactivateMenuItem(id));
    }

    @ParameterizedTest
    @ValueSource(ints = {6, 7})
    void deactivateMenuItem_calledWithDeactivateMenuItem_throwsException(Integer id) {
        assertThrows(EntityAlreadyDeactivatedException.class, () -> menuItemService.deactivateMenuItem(id));
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3})
    void deactivateMenuItem_calledWithMenuItemWithActiveOrders_throwsException(Integer id) {
        assertThrows(UsedMenuItemDeletionException.class, () -> menuItemService.deactivateMenuItem(id));
    }

}