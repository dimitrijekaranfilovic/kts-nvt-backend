package com.ktsnvt.ktsnvt.unit.service;

import com.ktsnvt.ktsnvt.exception.SectionNameAlreadyExistsException;
import com.ktsnvt.ktsnvt.model.Section;
import com.ktsnvt.ktsnvt.repository.RestaurantTableRepository;
import com.ktsnvt.ktsnvt.repository.SectionRepository;
import com.ktsnvt.ktsnvt.service.impl.SectionServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

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
}
