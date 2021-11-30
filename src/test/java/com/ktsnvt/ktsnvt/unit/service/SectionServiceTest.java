package com.ktsnvt.ktsnvt.unit.service;

import com.ktsnvt.ktsnvt.exception.OccupiedSectionException;
import com.ktsnvt.ktsnvt.exception.SectionNameAlreadyExistsException;
import com.ktsnvt.ktsnvt.model.RestaurantTable;
import com.ktsnvt.ktsnvt.model.Section;
import com.ktsnvt.ktsnvt.repository.RestaurantTableRepository;
import com.ktsnvt.ktsnvt.repository.SectionRepository;
import com.ktsnvt.ktsnvt.service.SectionService;
import com.ktsnvt.ktsnvt.service.impl.SectionServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;
import java.util.stream.Stream;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SectionServiceTest {
    @Mock
    private SectionRepository sectionRepository;
    @Mock
    private RestaurantTableRepository restaurantTableRepository;
    @InjectMocks
    private SectionServiceImpl sectionService;

    @Test
    void create_whenCalledWithUniqueName_isSuccess() {
        // GIVEN
        var section = new Section("something");
        doReturn(Optional.empty()).when(sectionRepository).findByName(section.getName());
        doReturn(section).when(sectionRepository).save(section);

        // WHEN
        sectionService.create(section);

        // THEN
        verify(sectionRepository, times(1)).save(section);
    }

    @Test
    void create_whenCalledWithDuplicateName_throwsException() {
        // GIVEN
        var section = new Section("something");
        doReturn(Optional.of(new Section())).when(sectionRepository).findByName(section.getName());
        doReturn(section).when(sectionRepository).save(section);

        // WHEN
        assertThrows(SectionNameAlreadyExistsException.class, () -> sectionService.create(section));

        // THEN
        verify(sectionRepository, times(0)).save(section);
    }

    @Test
    void update_whenCalledWithValidNameChange_isSuccess() {
        // GIVEN
        var newName = "somethingNew";
        var section = new Section("something");
        var sectionId = 999;
        section.setId(sectionId);
        SectionService sectionServiceSpy = spy(sectionService);
        doReturn(section).when(sectionServiceSpy).readForUpdate(sectionId);
        doReturn(Optional.empty()).when(sectionRepository).findByName(newName);
        doReturn(section).when(sectionRepository).save(section);

        // WHEN
        sectionServiceSpy.update(sectionId, newName);

        // THEN
        assertEquals(newName, section.getName());
        verify(sectionRepository, times(0)).save(section);
    }

    @Test
    void update_whenCalledWithNoNameChange_isSuccess() {
        // GIVEN
        var section = new Section("something");
        var sectionId = 999;
        section.setId(sectionId);
        SectionService sectionServiceSpy = spy(sectionService);
        doReturn(section).when(sectionServiceSpy).readForUpdate(sectionId);
        doReturn(Optional.of(section)).when(sectionRepository).findByName(section.getName());
        doReturn(section).when(sectionRepository).save(section);

        // WHEN
        sectionServiceSpy.update(sectionId, section.getName());

        // THEN
        verify(sectionRepository, times(0)).save(section);
    }

    @Test
    void update_whenCalledWithDuplicateNameChange_isSuccess() {
        // GIVEN
        var newName = "somethingNew";
        var section = new Section("something");
        var sectionId = 999;
        section.setId(sectionId);
        var sectionWithSameName = new Section(newName);
        sectionWithSameName.setId(888);
        SectionService sectionServiceSpy = spy(sectionService);
        doReturn(section).when(sectionServiceSpy).readForUpdate(sectionId);
        doReturn(Optional.of(sectionWithSameName)).when(sectionRepository).findByName(newName);
        doReturn(section).when(sectionRepository).save(section);

        // WHEN
        assertThrows(SectionNameAlreadyExistsException.class, () -> sectionServiceSpy.update(sectionId, newName));

        // THEN
        assertNotEquals(newName, section.getName());
        verify(sectionRepository, times(0)).save(section);
    }

    @Test
    void delete_whenCalledWithSectionWithNoOccupiedTables_isSuccess() {
        // GIVEN
        var section = new Section("something");
        var sectionId = 999;
        section.setId(sectionId);
        SectionService sectionServiceSpy = spy(sectionService);
        doReturn(section).when(sectionServiceSpy).readForUpdate(sectionId);
        doReturn(Stream.empty()).when(restaurantTableRepository).streamOccupiedTablesForSection(sectionId);

        // WHEN
        sectionServiceSpy.delete(sectionId);

        // THEN
        assertFalse(section.getIsActive());
        verify(restaurantTableRepository, times(1)).streamOccupiedTablesForSection(sectionId);
        verifyNoInteractions(sectionRepository);
    }

    @Test
    void delete_whenCalledWithSectionWithOccupiedTables_throwsException() {
        // GIVEN
        var section = new Section("something");
        var sectionId = 999;
        section.setId(sectionId);
        SectionService sectionServiceSpy = spy(sectionService);
        doReturn(section).when(sectionServiceSpy).readForUpdate(sectionId);
        doReturn(Stream.of(new RestaurantTable(), new RestaurantTable())).when(restaurantTableRepository).streamOccupiedTablesForSection(sectionId);

        // WHEN
        assertThrows(OccupiedSectionException.class, () -> sectionServiceSpy.delete(sectionId));

        // THEN
        assertTrue(section.getIsActive());
        verify(restaurantTableRepository, times(1)).streamOccupiedTablesForSection(sectionId);
        verifyNoInteractions(sectionRepository);
    }

}
