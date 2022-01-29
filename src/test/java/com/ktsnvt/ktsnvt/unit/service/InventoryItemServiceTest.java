package com.ktsnvt.ktsnvt.unit.service;

import com.ktsnvt.ktsnvt.exception.InventoryItemNameAlreadyExistsException;
import com.ktsnvt.ktsnvt.exception.InventoryItemNotFoundException;
import com.ktsnvt.ktsnvt.exception.UsedInventoryItemDeletionException;
import com.ktsnvt.ktsnvt.model.BasePrice;
import com.ktsnvt.ktsnvt.model.InventoryItem;
import com.ktsnvt.ktsnvt.model.enums.ItemCategory;
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

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
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

        doReturn(Optional.empty()).when(inventoryItemRepository).findByNameAndIsActiveTrue(inventoryItem.getName());
        doReturn(savedItem).when(inventoryItemRepository).save(inventoryItem);

        // WHEN
        var returnedItem = inventoryItemService.createInventoryItem(inventoryItem);

        // THEN
        assertEquals(savedItem, returnedItem);
        verify(inventoryItemRepository, times(1)).findByNameAndIsActiveTrue(inventoryItem.getName());
        verify(inventoryItemRepository, times(1)).save(inventoryItem);
    }

    @Test
    void createInventoryItem_whenCalledWithExistingItemName_throwsException() {
        // GIVEN
        var inventoryItem = new InventoryItem();
        inventoryItem.setName("name taken");
        var sameInventoryItem = new InventoryItem();
        sameInventoryItem.setName("name taken");
        doReturn(Optional.of(sameInventoryItem)).when(inventoryItemRepository).findByNameAndIsActiveTrue(inventoryItem.getName());

        // WHEN
        assertThrows(InventoryItemNameAlreadyExistsException.class,
                () -> inventoryItemService.createInventoryItem(inventoryItem));

        // THEN
        verify(inventoryItemRepository, times(1)).findByNameAndIsActiveTrue(inventoryItem.getName());
        verifyNoMoreInteractions(inventoryItemRepository);
    }

    @Test
    void readForUpdate_whenCalledWithExistingId_isSuccess() {
        // GIVEN
        var id = Integer.valueOf(42);
        var inventoryItem = new InventoryItem();
        inventoryItem.setId(id);
        doReturn(Optional.of(inventoryItem)).when(inventoryItemRepository).findOneForUpdate(id);

        // WHEN
        var returnedItem = inventoryItemService.readForUpdate(id);

        // THEN
        assertEquals(returnedItem.getId(), id);
        verify(inventoryItemRepository, times(1)).findOneForUpdate(id);
    }

    @Test
    void readForUpdate_whenCalledWithNonexistentId_throwsException() {
        // GIVEN
        var id = Integer.valueOf(42);
        doReturn(Optional.empty()).when(inventoryItemRepository).findOneForUpdate(id);

        // WHEN
        assertThrows(InventoryItemNotFoundException.class, () -> inventoryItemService.readForUpdate(id));

        // THEN
        verify(inventoryItemRepository, times(1)).findOneForUpdate(id);
    }

    @Test
    void delete_whenCalledWithInventoryItemWithNoActiveOrders_isSuccess() {
        // GIVEN
        var id = Integer.valueOf(42);
        var inventoryItem = new InventoryItem();
        inventoryItem.setId(id);

        var inventoryItemServiceSpy = spy(inventoryItemService);

        doReturn(inventoryItem).when(inventoryItemServiceSpy).readForUpdate(id);
        doReturn(Boolean.FALSE).when(orderItemService).hasActiveOrderItems(inventoryItem);
        doNothing().when(basePriceService).endActiveBasePriceForInventoryItem(inventoryItem);
        doNothing().when(menuItemService).removeActiveMenuItemForInventoryItem(id);

        // WHEN
        inventoryItemServiceSpy.delete(id);

        // THEN
        assertEquals(Boolean.FALSE, inventoryItem.getIsActive());
        verify(inventoryItemServiceSpy, times(1)).readForUpdate(id);
        verify(orderItemService, times(1)).hasActiveOrderItems(inventoryItem);
        verify(basePriceService, times(1)).endActiveBasePriceForInventoryItem(inventoryItem);
        verify(menuItemService, times(1)).removeActiveMenuItemForInventoryItem(id);
    }

    @Test
    void delete_whenCalledWithInventoryItemWithActiveOrderItems_throwsException() {
        // GIVEN
        var id = Integer.valueOf(42);
        var inventoryItem = new InventoryItem();
        inventoryItem.setId(42);

        var inventoryItemServiceSpy = spy(inventoryItemService);

        doReturn(inventoryItem).when(inventoryItemServiceSpy).readForUpdate(id);
        doReturn(Boolean.TRUE).when(orderItemService).hasActiveOrderItems(inventoryItem);

        // WHEN
        assertThrows(UsedInventoryItemDeletionException.class, () -> inventoryItemServiceSpy.delete(id));

        // THEN
        assertEquals(Boolean.TRUE, inventoryItem.getIsActive());
        verify(inventoryItemServiceSpy, times(1)).readForUpdate(id);
        verify(orderItemService, times(1)).hasActiveOrderItems(inventoryItem);
        verifyNoInteractions(basePriceService);
        verifyNoInteractions(menuItemService);
    }


    @Test
    void update_whenCalledWithUniqueInventoryItemName_isSuccess() {
        // GIVEN
        var id = Integer.valueOf(42);
        var name = "valid name";
        var description = "valid description";
        var allergies = "valid allergies";
        var image = "valid image";
        var itemCategory = ItemCategory.DRINK;
        var basePrice = BigDecimal.valueOf(322);

        var inventoryItem = new InventoryItem("different name", "different description",
                "different image", "different allergies", ItemCategory.FOOD, Boolean.TRUE);
        inventoryItem.setId(id);

        var existingBasePrice = new BasePrice(LocalDateTime.of(2021, 12, 1, 3, 14),
                null, BigDecimal.valueOf(42), inventoryItem);
        inventoryItem.addBasePrice(existingBasePrice);

        var currentDate = LocalDateTime.of(2021, 12, 3, 2, 2);

        var inventoryItemServiceSpy = spy(inventoryItemService);

        doReturn(inventoryItem).when(inventoryItemServiceSpy).readForUpdate(id);
        doReturn(Optional.empty()).when(inventoryItemRepository).findByNameAndIsActiveTrue(name);
        doNothing().when(basePriceService).updateInventoryItemBasePrice(eq(id), any(BasePrice.class));
        doReturn(currentDate).when(localDateTimeService).currentTime();

        // WHEN
        inventoryItemServiceSpy.update(id, name, description, allergies, image, itemCategory, basePrice);

        // THEN
        assertEquals(id, inventoryItem.getId());
        assertEquals(name, inventoryItem.getName());
        assertEquals(description, inventoryItem.getDescription());
        assertEquals(allergies, inventoryItem.getAllergies());
        assertEquals(image, inventoryItem.getImage());
        assertEquals(itemCategory, inventoryItem.getCategory());
        verify(inventoryItemServiceSpy, times(1)).readForUpdate(id);
        verify(inventoryItemRepository, times(1)).findByNameAndIsActiveTrue(name);
        verify(basePriceService, times(1))
                .updateInventoryItemBasePrice(eq(id), any(BasePrice.class));
        verify(localDateTimeService, times(1)).currentTime();
    }

    @Test
    void update_whenCalledWithTakenInventoryItemName_throwsException() {
        // GIVEN
        var id = Integer.valueOf(42);
        var name = "valid name";
        var description = "valid description";
        var allergies = "valid allergies";
        var image = "valid image";
        var itemCategory = ItemCategory.DRINK;
        var basePrice = BigDecimal.valueOf(322);
        var inventoryItem = new InventoryItem("different name", "different description",
                "different image", "different allergies", ItemCategory.FOOD, Boolean.TRUE);
        inventoryItem.setId(id);

        var inventoryItemSameName = new InventoryItem();
        inventoryItemSameName.setId(322);
        inventoryItemSameName.setName(name);

        var inventoryItemServiceSpy = spy(inventoryItemService);
        doReturn(inventoryItem).when(inventoryItemServiceSpy).readForUpdate(id);
        doReturn(Optional.of(inventoryItemSameName)).when(inventoryItemRepository).findByNameAndIsActiveTrue(name);

        // WHEN
        assertThrows(InventoryItemNameAlreadyExistsException.class,
                () -> inventoryItemServiceSpy.update(id, name, description, allergies, image, itemCategory, basePrice));

        // THEN
        assertEquals(id, inventoryItem.getId());
        assertNotEquals(name, inventoryItem.getName());
        assertNotEquals(description, inventoryItem.getDescription());
        assertNotEquals(allergies, inventoryItem.getAllergies());
        assertNotEquals(image, inventoryItem.getImage());
        assertNotEquals(itemCategory, inventoryItem.getCategory());
        verify(inventoryItemServiceSpy, times(1)).readForUpdate(id);
        verify(inventoryItemRepository, times(1)).findByNameAndIsActiveTrue(name);
        verifyNoInteractions(basePriceService);
        verifyNoInteractions(localDateTimeService);
    }

}
