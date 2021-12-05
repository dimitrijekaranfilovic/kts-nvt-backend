package com.ktsnvt.ktsnvt.integration.service;

import com.ktsnvt.ktsnvt.exception.InventoryItemHasNoActiveBasePrice;
import com.ktsnvt.ktsnvt.model.InventoryItem;
import com.ktsnvt.ktsnvt.service.impl.BasePriceServiceImpl;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
                Arguments.of(createInventoryItem(8)),
                Arguments.of(createInventoryItem(9))
        );
    }


    private static InventoryItem createInventoryItem(Integer id) {
        var inventoryItem = new InventoryItem();
        inventoryItem.setId(id);
        return inventoryItem;
    }
}
