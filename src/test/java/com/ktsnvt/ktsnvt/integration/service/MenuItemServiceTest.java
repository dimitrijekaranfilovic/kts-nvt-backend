package com.ktsnvt.ktsnvt.integration.service;

import com.ktsnvt.ktsnvt.exception.MenuItemNotFoundException;
import com.ktsnvt.ktsnvt.service.impl.MenuItemServiceImpl;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
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

}
