package com.ktsnvt.ktsnvt.integration.service;

import com.ktsnvt.ktsnvt.exception.InventoryItemHasNoActiveBasePrice;
import com.ktsnvt.ktsnvt.exception.InventoryItemNameAlreadyExistsException;
import com.ktsnvt.ktsnvt.exception.InventoryItemNotFoundException;
import com.ktsnvt.ktsnvt.exception.UsedInventoryItemDeletionException;
import com.ktsnvt.ktsnvt.model.InventoryItem;
import com.ktsnvt.ktsnvt.model.enums.ItemCategory;
import com.ktsnvt.ktsnvt.service.impl.InventoryItemServiceImpl;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class InventoryItemServiceTest {

    @Autowired
    private InventoryItemServiceImpl inventoryItemService;


    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4})
    void readForUpdate_calledWithValidId_isSuccess(Integer id) {
        var inventoryItem = inventoryItemService.readForUpdate(id);
        assertEquals(id, inventoryItem.getId());
    }

    @ParameterizedTest
    @ValueSource(ints = {42, 28, -1})
    void readForUpdate_calledWithInvalidId_throwsException(Integer id) {
        assertThrows(InventoryItemNotFoundException.class, () -> inventoryItemService.readForUpdate(id));
    }

    @ParameterizedTest
    @MethodSource("provideArgumentsAndExpectedValueForReadingPaginatedInventoryItems")
    void read_calledWithValidPaginationArguments_isSuccess(String query, BigDecimal basePriceFrom,
                                                           BigDecimal basePriceTo, ItemCategory itemCategory,
                                                           Pageable pageable, int expectedTotalItems) {

        var returnedPage = inventoryItemService.read(query, basePriceFrom, basePriceTo,
                itemCategory, pageable);
        assertEquals(expectedTotalItems, returnedPage.getTotalElements());
    }

    @ParameterizedTest
    @ValueSource(ints = {4, 9, 10, 11, 12})
    void delete_calledWithValidId_isSuccess(Integer id) {
        assertDoesNotThrow(() -> inventoryItemService.delete(id));
    }

    @ParameterizedTest
    @ValueSource(ints = {42, 28, -1})
    void delete_calledWithNonExistingId_throwsException(Integer id) {
        assertThrows(InventoryItemNotFoundException.class, () -> inventoryItemService.delete(id));
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3})
    void delete_calledWithInventoryItemThatIsContainedInNonFinalizedOrder_throwsException(Integer id) {
        assertThrows(UsedInventoryItemDeletionException.class, () -> inventoryItemService.delete(id));
    }

    @ParameterizedTest
    @ValueSource(ints = {5, 6, 7, 8})
    void delete_calledWithItemsWithNoBasePrice_throwsException(Integer id) {
        assertThrows(InventoryItemHasNoActiveBasePrice.class, () -> inventoryItemService.delete(id));
    }

    @ParameterizedTest
    @EnumSource(ItemCategory.class)
    void createInventoryItem_calledWithValidId_isSuccess(ItemCategory itemCategory) {
        var inventoryItem = new InventoryItem("unique name", "description",
                "image", "allergies", itemCategory, Boolean.FALSE);
        var createdInventoryItem = inventoryItemService.createInventoryItem(inventoryItem);
        assertNotNull(createdInventoryItem.getId());
        assertEquals(inventoryItem.getName(), createdInventoryItem.getName());
    }

    @ParameterizedTest
    @ValueSource(strings = {"Ice cream", "Orange juice", "Pizza", "Steak", "Water", "Cake", "Wine"})
    void createInventoryItem_calledWithNonUniqueName_throwsException(String name) {
        var inventoryItem = new InventoryItem(name, "description",
                "image", "allergies", ItemCategory.FOOD, Boolean.FALSE);
        assertThrows(InventoryItemNameAlreadyExistsException.class,
                () -> inventoryItemService.createInventoryItem(inventoryItem));
    }

    @ParameterizedTest
    @MethodSource("provideValidArgumentsForInventoryItemUpdate")
    void update_calledWithValidArguments_isSuccess(Integer id, String name, String description,
                                                   String allergies, String image, ItemCategory category,
                                                   BigDecimal basePrice) {
        assertDoesNotThrow(
                () -> inventoryItemService.update(id, name, description, allergies, image, category, basePrice));
    }

    @ParameterizedTest
    @MethodSource("provideTakenNameArgumentsForInventoryItemUpdate")
    void update_calledWithTakenName_throwsException(Integer id, String name, String description,
                                                    String allergies, String image, ItemCategory category,
                                                    BigDecimal basePrice) {
        assertThrows(InventoryItemNameAlreadyExistsException.class,
                () -> inventoryItemService.update(id, name, description, allergies, image, category, basePrice));
    }

    @SuppressWarnings("unused")
    private static Stream<Arguments> provideArgumentsAndExpectedValueForReadingPaginatedInventoryItems() {
        var pageable = PageRequest.of(0, 10, Sort.unsorted());
        return Stream.of(
                Arguments.of("", null, null, null, pageable, 12),
                Arguments.of("  ice", null, null, null, pageable, 2),
                Arguments.of("ice  ", null, null, null, pageable, 2),
                Arguments.of(" ice  ", null, null, null, pageable, 2),
                Arguments.of("ICE", null, null, null, pageable, 2),
                Arguments.of("", BigDecimal.valueOf(40), BigDecimal.valueOf(60), null, pageable, 3),
                Arguments.of("", BigDecimal.valueOf(10), BigDecimal.valueOf(30), null, pageable, 3),
                Arguments.of("", BigDecimal.valueOf(440), BigDecimal.valueOf(500), null, pageable, 1),
                Arguments.of("", BigDecimal.valueOf(440), BigDecimal.valueOf(30), null, pageable, 0),
                Arguments.of("", BigDecimal.valueOf(-10), BigDecimal.valueOf(500), null, pageable, 12),
                Arguments.of("", null, null, ItemCategory.FOOD, pageable, 7),
                Arguments.of(" ice ", BigDecimal.valueOf(-100), BigDecimal.valueOf(1000),
                        ItemCategory.DRINK, pageable, 1)
        );
    }

    @SuppressWarnings("unused")
    private static Stream<Arguments> provideValidArgumentsForInventoryItemUpdate() {
        return Stream.of(
                Arguments.of(1, "unique name", "new description", "new allergies", "new image",
                        ItemCategory.DRINK, BigDecimal.valueOf(496)),
                Arguments.of(1, "Ice cream", "new description", "new allergies", "new image",
                        ItemCategory.DRINK, BigDecimal.valueOf(496)),
                Arguments.of(1, "unique name", "new description", "new allergies", "new image",
                        ItemCategory.FOOD, BigDecimal.valueOf(496)),
                Arguments.of(1, "unique name", "new description", "new allergies", "new image",
                        ItemCategory.FOOD, BigDecimal.valueOf(30)),
                Arguments.of(10, "Whiskey", "new description", "new allergies", "new image",
                        ItemCategory.FOOD, BigDecimal.valueOf(42))
        );
    }

    @SuppressWarnings("unused")
    private static Stream<Arguments> provideTakenNameArgumentsForInventoryItemUpdate() {
        return Stream.of(
                Arguments.of(1, "Wine", "new description", "new allergies", "new image",
                        ItemCategory.DRINK, BigDecimal.valueOf(496)),
                Arguments.of(1, "Cake", "new description", "new allergies", "new image",
                        ItemCategory.DRINK, BigDecimal.valueOf(496)),
                Arguments.of(2, "Pizza", "new description", "new allergies", "new image",
                        ItemCategory.FOOD, BigDecimal.valueOf(30)),
                Arguments.of(10, "Ice cream", "new description", "new allergies", "new image",
                        ItemCategory.FOOD, BigDecimal.valueOf(42))
        );
    }

}
