package com.ktsnvt.ktsnvt.unit.service;

import com.ktsnvt.ktsnvt.exception.DuplicateTableNumberException;
import com.ktsnvt.ktsnvt.exception.OccupiedTableException;
import com.ktsnvt.ktsnvt.exception.TableIntersectionException;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
public class RestaurantTableServiceTest {

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
    public void delete_whenCalledWithValidId_isSuccess() {
        RestaurantTableService restaurantTableServiceSpy = spy(restaurantTableService);

        RestaurantTable table = new RestaurantTable();
        table.setAvailable(true);

        doReturn(table).when(restaurantTableServiceSpy).readForUpdate(anyInt());

        restaurantTableServiceSpy.deleteRestaurantTable(1);

        assertEquals(false, table.getIsActive());
    }

    @Test
    public void delete_whenTableIsUnavailable_ThrowsOccupiedTableException() {
        RestaurantTableService restaurantTableServiceSpy = spy(restaurantTableService);

        RestaurantTable table = new RestaurantTable();
        table.setAvailable(false);

        doReturn(table).when(restaurantTableServiceSpy).readForUpdate(anyInt());

        assertThrows(OccupiedTableException.class, () -> restaurantTableServiceSpy.deleteRestaurantTable(1));
    }

    @Test
    public void create_whenCalledWithValidData_IsSuccess() {
        Section section = new Section();
        section.setId(1);
        RestaurantTable newTable = new RestaurantTable(2, 1, 1, 0, section);
        newTable.setId(2);

        doReturn(section).when(sectionService).readForUpdate(section.getId());
        doReturn(newTable).when(restaurantTableRepository).findByNumberInSection(section.getId(), newTable.getNumber());

        RestaurantTable ret = restaurantTableService.createRestaurantTable(newTable, section.getId());

        assertEquals(newTable.getNumber(), ret.getNumber());
        verify(restaurantTableRepository, times(1)).save(newTable);
    }

    @Test
    public void create_whenIntersectsWithOtherTable_throwsTableIntersectionException() {
        Section section = new Section();
        section.setId(1);
        RestaurantTable newTable = new RestaurantTable(2, 1, 1, 1, section);
        newTable.setId(2);

        doReturn(section).when(sectionService).readForUpdate(section.getId());
        doReturn(newTable).when(restaurantTableRepository).findByNumberInSection(section.getId(), newTable.getNumber());

        assertThrows(TableIntersectionException.class, () ->restaurantTableService.createRestaurantTable(newTable, section.getId()));
    }

    @Test
    public void create_whenHasNumberAsOtherTable_throwsDuplicateTableNumberException() {
        Section section = new Section();
        section.setId(1);
        RestaurantTable newTable = new RestaurantTable(1, 1, 1, 0, section);
        newTable.setId(2);

        doReturn(section).when(sectionService).readForUpdate(section.getId());
        doReturn(newTable).when(restaurantTableRepository).findByNumberInSection(section.getId(), newTable.getNumber());

        assertThrows(DuplicateTableNumberException.class, () ->restaurantTableService.createRestaurantTable(newTable, section.getId()));
    }
}
