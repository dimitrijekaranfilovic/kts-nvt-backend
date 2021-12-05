package com.ktsnvt.ktsnvt.integration.service;

import com.ktsnvt.ktsnvt.exception.InventoryItemNotFoundException;
import com.ktsnvt.ktsnvt.service.impl.InventoryItemServiceImpl;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
class InventoryItemServiceTest {

    @Autowired
    private InventoryItemServiceImpl inventoryItemService;


    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4})
    void readForUpdate_whenCalledWithValidId_isSuccess(Integer id) {
        var inventoryItem = inventoryItemService.readForUpdate(id);
        assertEquals(id, inventoryItem.getId());
    }

    @ParameterizedTest
    @ValueSource(ints = {42, 28, -1})
    void readForUpdate_whenCalledWithInvalidId_throwsException(Integer id) {
        assertThrows(InventoryItemNotFoundException.class, () -> inventoryItemService.readForUpdate(id));
    }

}
