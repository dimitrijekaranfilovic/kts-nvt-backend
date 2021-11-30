package com.ktsnvt.ktsnvt.unit.service;

import com.ktsnvt.ktsnvt.exception.DuplicateTableNumberException;
import com.ktsnvt.ktsnvt.exception.OccupiedTableException;
import com.ktsnvt.ktsnvt.exception.TableIntersectionException;
import com.ktsnvt.ktsnvt.exception.TableSectionMismatchException;
import com.ktsnvt.ktsnvt.model.RestaurantTable;
import com.ktsnvt.ktsnvt.model.Section;
import com.ktsnvt.ktsnvt.repository.RestaurantTableRepository;
import com.ktsnvt.ktsnvt.service.RestaurantTableService;
import com.ktsnvt.ktsnvt.service.SectionService;
import com.ktsnvt.ktsnvt.service.impl.RestaurantTableServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class RestaurantTableServiceTest {
    @Mock
    private RestaurantTableRepository restaurantTableRepository;
    @Mock
    private SectionService sectionService;
    @InjectMocks
    private RestaurantTableServiceImpl restaurantTableService;

    @BeforeEach
    public void setUp(){
        RestaurantTable oldTable = new RestaurantTable(1, 0, 0, 1, null);
        oldTable.setId(1);

        Collection<RestaurantTable> sectionTables = new ArrayList<>();
        sectionTables.add(oldTable);

        doReturn(sectionTables).when(restaurantTableRepository).findAllForSection(anyInt());
    }

    @Test
    void updateTablePosition_whenCalledWithPointsNoIntersection_isSuccess() {
        // GIVEN
        var section = new Section("example");
        var sectionId = 999;
        section.setId(sectionId);
        var table = new RestaurantTable(123, 10, 10, 10,  section);
        var tableId = 999;
        table.setId(tableId);
        RestaurantTableService restaurantTableServiceSpy = spy(restaurantTableService);
        doReturn(section).when(sectionService).readForUpdate(sectionId);
        doReturn(table).when(restaurantTableServiceSpy).readForUpdate(tableId);
        doReturn(List.of(table)).when(restaurantTableRepository).findAllForSection(sectionId);

        // WHEN
        restaurantTableServiceSpy.updateTablePosition(sectionId, tableId, 15, 20);

        // THEN
        assertEquals(15, table.getX());
        assertEquals(20, table.getY());
        verify(restaurantTableRepository, times(1)).findAllForSection(sectionId);
    }

    @Test
    void updateTablePosition_whenCalledWithOccupiedTable_throwsException() {
        // GIVEN
        var section = new Section("example");
        var sectionId = 999;
        section.setId(sectionId);
        var table = new RestaurantTable(123, 10, 10, 10,  section);
        table.setAvailable(false);
        var tableId = 999;
        table.setId(tableId);
        RestaurantTableService restaurantTableServiceSpy = spy(restaurantTableService);
        doReturn(section).when(sectionService).readForUpdate(sectionId);
        doReturn(table).when(restaurantTableServiceSpy).readForUpdate(tableId);
        doReturn(List.of(table)).when(restaurantTableRepository).findAllForSection(sectionId);

        // WHEN
       assertThrows(OccupiedTableException.class, () ->  restaurantTableServiceSpy.updateTablePosition(sectionId, tableId, 15, 20));

        // THEN
        assertNotEquals(15, table.getX());
        assertNotEquals(20, table.getY());
        verify(restaurantTableRepository, times(0)).findAllForSection(sectionId);
    }

    @Test
    void updateTablePosition_whenCalledWithIntersections_throwsException() {
        // GIVEN
        var section = new Section("example");
        var sectionId = 999;
        section.setId(sectionId);
        var table = new RestaurantTable(123, 10, 10, 10,  section);
        var tableId = 999;
        table.setId(tableId);
        var intersectingTable = new RestaurantTable(456, 8, 8, 5, section);
        intersectingTable.setId(888);
        RestaurantTableService restaurantTableServiceSpy = spy(restaurantTableService);
        doReturn(section).when(sectionService).readForUpdate(sectionId);
        doReturn(table).when(restaurantTableServiceSpy).readForUpdate(tableId);
        doReturn(List.of(table, intersectingTable)).when(restaurantTableRepository).findAllForSection(sectionId);

        // WHEN
        assertThrows(TableIntersectionException.class, () ->  restaurantTableServiceSpy.updateTablePosition(sectionId, tableId, 15, 20));

        // THEN
        verify(restaurantTableRepository, times(1)).findAllForSection(sectionId);
    }

    @Test
    void updateTablePosition_whenCalledWithInvalidSectionId_throwsException() {
        // GIVEN
        var section = new Section("example");
        var sectionId = 999;
        section.setId(sectionId);
        var table = new RestaurantTable(123, 10, 10, 10,  section);
        var tableId = 999;
        var invalidSection = new Section("aaaaa");
        var invalidSectionId = 888;
        invalidSection.setId(invalidSectionId);
        table.setId(tableId);
        RestaurantTableService restaurantTableServiceSpy = spy(restaurantTableService);
        doReturn(section).when(sectionService).readForUpdate(sectionId);
        doReturn(new Section()).when(sectionService).readForUpdate(invalidSectionId);
        doReturn(table).when(restaurantTableServiceSpy).readForUpdate(tableId);
        doReturn(List.of(table)).when(restaurantTableRepository).findAllForSection(sectionId);

        // WHEN
        assertThrows(TableSectionMismatchException.class, () -> restaurantTableServiceSpy.updateTablePosition(invalidSectionId, tableId, 15, 20));

        // THEN
        assertNotEquals(15, table.getX());
        assertNotEquals(20, table.getY());
        verify(restaurantTableRepository, times(0)).findAllForSection(sectionId);
    }

    @Test
    void delete_whenCalledWithValidId_isSuccess() {
        // GIVEN
        RestaurantTableService restaurantTableServiceSpy = spy(restaurantTableService);
        RestaurantTable table = new RestaurantTable();
        table.setAvailable(true);

        // WHEN
        doReturn(table).when(restaurantTableServiceSpy).readForUpdate(anyInt());

        restaurantTableServiceSpy.deleteRestaurantTable(1);

        // THEN
        assertEquals(false, table.getIsActive());
    }

    @Test
    void delete_whenTableIsUnavailable_throwsOccupiedTableException() {
        // GIVEN
        RestaurantTableService restaurantTableServiceSpy = spy(restaurantTableService);
        RestaurantTable table = new RestaurantTable();
        table.setAvailable(false);

        // WHEN
        doReturn(table).when(restaurantTableServiceSpy).readForUpdate(anyInt());

        // THEN
        assertThrows(OccupiedTableException.class, () -> restaurantTableServiceSpy.deleteRestaurantTable(1));
    }

    @Test
    void create_whenCalledWithValidData_isSuccess() {
        // GIVEN
        Section section = new Section();
        section.setId(1);
        RestaurantTable newTable = new RestaurantTable(2, 1, 1, 0, section);
        newTable.setId(2);

        // WHEN
        doReturn(section).when(sectionService).readForUpdate(section.getId());
        doReturn(newTable).when(restaurantTableRepository).findByNumberInSection(section.getId(), newTable.getNumber());

        RestaurantTable ret = restaurantTableService.createRestaurantTable(newTable, section.getId());

        // THEN
        assertEquals(newTable.getNumber(), ret.getNumber());
        verify(restaurantTableRepository, times(1)).save(newTable);
    }

    @Test
    void create_whenIntersectsWithOtherTable_throwsTableIntersectionException() {
        // GIVEN
        Section section = new Section();
        section.setId(1);
        RestaurantTable newTable = new RestaurantTable(2, 1, 1, 1, section);
        newTable.setId(2);

        // WHEN
        doReturn(section).when(sectionService).readForUpdate(section.getId());
        doReturn(newTable).when(restaurantTableRepository).findByNumberInSection(section.getId(), newTable.getNumber());

        // THEN
        assertThrows(TableIntersectionException.class, () -> restaurantTableService.createRestaurantTable(newTable, section.getId()));
    }

    @Test
    void create_whenHasNumberAsOtherTable_throwsDuplicateTableNumberException() {
        // GIVEN
        Section section = new Section();
        section.setId(1);
        RestaurantTable newTable = new RestaurantTable(1, 1, 1, 0, section);
        newTable.setId(2);

        // WHEN
        doReturn(section).when(sectionService).readForUpdate(section.getId());
        doReturn(newTable).when(restaurantTableRepository).findByNumberInSection(section.getId(), newTable.getNumber());

        // THEN
        assertThrows(DuplicateTableNumberException.class, () -> restaurantTableService.createRestaurantTable(newTable, section.getId()));
    }
}
