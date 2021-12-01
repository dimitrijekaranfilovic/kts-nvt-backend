package com.ktsnvt.ktsnvt.integration.service;

import com.ktsnvt.ktsnvt.exception.DuplicateTableNumberException;
import com.ktsnvt.ktsnvt.exception.OccupiedTableException;
import com.ktsnvt.ktsnvt.exception.RestaurantTableNotFoundException;
import com.ktsnvt.ktsnvt.exception.TableIntersectionException;
import com.ktsnvt.ktsnvt.model.RestaurantTable;
import com.ktsnvt.ktsnvt.service.impl.RestaurantTableServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
public class RestaurantTableServiceTest {

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
}
