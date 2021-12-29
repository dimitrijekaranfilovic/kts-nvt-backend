package com.ktsnvt.ktsnvt.integration.service;

import com.ktsnvt.ktsnvt.exception.EntityAlreadyDeactivatedException;
import com.ktsnvt.ktsnvt.exception.MenuItemNotFoundException;
import com.ktsnvt.ktsnvt.exception.UsedMenuItemDeletionException;
import com.ktsnvt.ktsnvt.model.enums.ItemCategory;
import com.ktsnvt.ktsnvt.service.impl.MenuItemServiceImpl;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Locale;
import java.util.stream.Stream;

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

    @ParameterizedTest
    @MethodSource("provideReadArguments")
    void read_calledWithValidArguments_isSuccess(String query, BigDecimal priceFrom, BigDecimal priceTo,
                                                 ItemCategory itemCategory, Pageable pageable, int expectedResults) {
        var processedQuery = query.trim().toLowerCase();
        var retVal = menuItemService.read(query, priceFrom, priceTo, itemCategory, pageable);
        assertEquals(expectedResults, retVal.getTotalElements());
        assertTrue(retVal.stream().parallel().allMatch(menuItem ->
                menuItem.getItem().getName().trim().toLowerCase(Locale.ROOT).contains(processedQuery)
                        || menuItem.getItem().getAllergies().trim().toLowerCase(Locale.ROOT).contains(processedQuery)
                        || menuItem.getItem().getDescription().trim().toLowerCase(Locale.ROOT).contains(processedQuery))
        );
    }

    @SuppressWarnings("unused")
    private static Stream<Arguments> provideReadArguments() {
        return Stream.of(
                Arguments.of(" iCe ", BigDecimal.valueOf(0), BigDecimal.valueOf(500),
                        ItemCategory.DRINK, PageRequest.of(0, 10), 1),
                Arguments.of(" iCe ", null, null,
                        null, PageRequest.of(0, 10), 2),
                Arguments.of("ice", BigDecimal.valueOf(1000), BigDecimal.valueOf(2000),
                        null, PageRequest.of(0, 10), 0),
                Arguments.of(" iCe ", BigDecimal.valueOf(0), BigDecimal.valueOf(42),
                        null, PageRequest.of(0, 10), 0),
                Arguments.of("ice", BigDecimal.valueOf(0), BigDecimal.valueOf(500),
                        null, PageRequest.of(0, 1), 2),
                Arguments.of("ice", BigDecimal.valueOf(0), BigDecimal.valueOf(500),
                        null, PageRequest.of(0, 10), 2),
                Arguments.of(" aK ", BigDecimal.valueOf(0), BigDecimal.valueOf(1000),
                        ItemCategory.FOOD, PageRequest.of(0, 10), 3),
                Arguments.of("non existing string", BigDecimal.valueOf(0), BigDecimal.valueOf(1000),
                        ItemCategory.FOOD, PageRequest.of(0, 10), 0)
        );
    }

}
