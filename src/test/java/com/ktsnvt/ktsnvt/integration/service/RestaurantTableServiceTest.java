package com.ktsnvt.ktsnvt.integration.service;

import com.ktsnvt.ktsnvt.exception.*;
import com.ktsnvt.ktsnvt.model.RestaurantTable;
import com.ktsnvt.ktsnvt.service.impl.RestaurantTableServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class RestaurantTableServiceTest {
    @Autowired
    private RestaurantTableServiceImpl restaurantTableService;

    @Test
    void readForUpdate_whenCalledWithValidId_isSuccess() {
        var table = restaurantTableService.readForUpdate(1);
        assertEquals(1, table.getId());
    }

    @Test
    void readForUpdate_whenCalledWithInvalidId_throwsRestaurantTableNotFoundException() {
        assertThrows(RestaurantTableNotFoundException.class, () -> restaurantTableService.readForUpdate(0));
    }

    @Test
    void getAllTablesForSection_calledWithValidSectionId_isSuccess() {
        var tables = restaurantTableService.getAllTablesForSection(4);
        assertEquals(2, tables.size());
    }

    @Test
    void createRestaurantTable_calledWithValidData_isSuccess() {
        var newTable = new RestaurantTable(11, 11, 11, 1, null);
        var createdTable = restaurantTableService.createRestaurantTable(newTable, 2);
        assertEquals(newTable.getNumber(), createdTable.getNumber());
    }

    @Test
    void createRestaurantTable_hasSameNumberAsOtherTableInSection_throwsDuplicateTableNumberException() {
        var newTable = new RestaurantTable(1, 11, 11, 1, null);
        assertThrows(DuplicateTableNumberException.class, () -> restaurantTableService.createRestaurantTable(newTable, 3));
    }

    @Test
    void createRestaurantTable_overlappingWithTableInSection_throwsTableIntersectionException() {
        var newTable = new RestaurantTable(0, 0, 0, 1, null);
        assertThrows(TableIntersectionException.class, () -> restaurantTableService.createRestaurantTable(newTable, 3));
    }

    @Test
    void deleteRestaurantTable_calledWithOccupiedTable_throwsOccupiedTableException() {
        assertThrows(OccupiedTableException.class, () -> restaurantTableService.deleteRestaurantTable(10));
    }

    @ParameterizedTest
    @ValueSource(ints = {10, 20, 30, 80})
    void updateTablePosition_whenCalledWithValidData_isSuccess(int position) {
        assertDoesNotThrow(() -> restaurantTableService.updateTablePosition(2, 4, position, position));
    }

    @Test
    void updateTablePosition_whenCalledWithWrongSectionId_throwsException() {
        assertThrows(TableSectionMismatchException.class, () -> restaurantTableService.updateTablePosition(3, 4, 50, 50));
    }

    @Test
    void updateTablePosition_whenCalledOnNotAvailableTable_throwsException() {
        assertThrows(OccupiedTableException.class, () -> restaurantTableService.updateTablePosition(4, 10, 50, 50));
    }

    @Test
    void updateTablePosition_whenCalledWithIntersectingPositions_throwsException() {
        assertThrows(TableIntersectionException.class, () -> restaurantTableService.updateTablePosition(2, 4, 2, 2));
    }
}
