package com.ktsnvt.ktsnvt.integration.service;

import com.ktsnvt.ktsnvt.exception.InventoryItemHasNoActiveBasePrice;
import com.ktsnvt.ktsnvt.exception.InventoryItemNotFoundException;
import com.ktsnvt.ktsnvt.model.BasePrice;
import com.ktsnvt.ktsnvt.model.InventoryItem;
import com.ktsnvt.ktsnvt.service.impl.BasePriceServiceImpl;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class BasePriceServiceTest {

    @Autowired
    private BasePriceServiceImpl basePriceService;


    @ParameterizedTest
    @MethodSource("provideInventoryItemsWithActiveBasePrice")
    void endActiveBasePriceForInventoryItem_calledWithInventoryItemWithActiveBasePrice_isSuccess(
            InventoryItem inventoryItem) {
        assertDoesNotThrow(() -> basePriceService.endActiveBasePriceForInventoryItem(inventoryItem));
    }

    @ParameterizedTest
    @MethodSource("provideInventoryItemsWithNoActiveBasePrice")
    void endActiveBasePriceForInventoryItem_calledWithInventoryItemWithNoActiveBasePrice_throwsException(
            InventoryItem inventoryItem) {
        assertThrows(InventoryItemHasNoActiveBasePrice.class,
                () -> basePriceService.endActiveBasePriceForInventoryItem(inventoryItem));
    }

    @ParameterizedTest
    @MethodSource("provideInventoryItemsWithActiveBasePrice")
    void updateInventoryItemBasePrice_calledWithValidIdAndBasePrice_isSuccess(InventoryItem inventoryItem) {
        var basePrice = new BasePrice(LocalDateTime.of(2021, 11, 20, 3, 42), null,
                BigDecimal.valueOf(42), null);
        assertDoesNotThrow(() -> basePriceService.updateInventoryItemBasePrice(inventoryItem.getId(), basePrice));
        assertEquals(inventoryItem.getId(), basePrice.getInventoryItem().getId());
    }

    @ParameterizedTest
    @MethodSource("provideInventoryItemsWithNoActiveBasePrice")
    void updateInventoryItemBasePrice_calledWithInventoryItemWithNoActiveBasePrice_throwsException(
            InventoryItem inventoryItem) {
        var basePrice = new BasePrice(LocalDateTime.of(2021, 11, 20, 3, 42), null,
                BigDecimal.valueOf(42), null);
        var id = inventoryItem.getId();
        assertThrows(InventoryItemHasNoActiveBasePrice.class,
                () -> basePriceService.updateInventoryItemBasePrice(id, basePrice));
        assertNull(basePrice.getInventoryItem());
    }

    @ParameterizedTest
    @MethodSource("provideNonExistingInventoryItems")
    void updateInventoryItemBasePrice_calledWithNonExisingInventoryItem_throwsException(
            InventoryItem inventoryItem) {
        var basePrice = new BasePrice(LocalDateTime.of(2021, 11, 20, 3, 42), null,
                BigDecimal.valueOf(42), null);
        var id = inventoryItem.getId();
        assertThrows(InventoryItemNotFoundException.class,
                () -> basePriceService.updateInventoryItemBasePrice(id, basePrice));
        assertNull(basePrice.getInventoryItem());
    }

    @SuppressWarnings("unused")
    private static Stream<Arguments> provideInventoryItemsWithActiveBasePrice() {
        return Stream.of(
                Arguments.of(createInventoryItem(1)),
                Arguments.of(createInventoryItem(2)),
                Arguments.of(createInventoryItem(3)),
                Arguments.of(createInventoryItem(4))
        );
    }

    @SuppressWarnings("unused")
    private static Stream<Arguments> provideInventoryItemsWithNoActiveBasePrice() {
        return Stream.of(
                Arguments.of(createInventoryItem(5)),
                Arguments.of(createInventoryItem(6)),
                Arguments.of(createInventoryItem(7)),
                Arguments.of(createInventoryItem(8))
        );
    }

    @SuppressWarnings("unused")
    private static Stream<Arguments> provideNonExistingInventoryItems() {
        return Stream.of(
                Arguments.of(createInventoryItem(42)),
                Arguments.of(createInventoryItem(43)),
                Arguments.of(createInventoryItem(44))
        );
    }


    private static InventoryItem createInventoryItem(Integer id) {
        var inventoryItem = new InventoryItem();
        inventoryItem.setId(id);
        return inventoryItem;
    }
}
