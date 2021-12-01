package com.ktsnvt.ktsnvt.integration.service;

import com.ktsnvt.ktsnvt.exception.OccupiedSectionException;
import com.ktsnvt.ktsnvt.exception.SectionNameAlreadyExistsException;
import com.ktsnvt.ktsnvt.exception.SectionNotFoundException;
import com.ktsnvt.ktsnvt.model.Section;
import com.ktsnvt.ktsnvt.service.impl.SectionServiceImpl;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class SectionServiceTest {

    @Autowired
    private SectionServiceImpl sectionService;

    @Test
    @Order(1)
    void getAllSections_isSuccess() {
        var sections = sectionService.getAllSections();
        assertEquals(4, sections.size());
    }

    @Test
    @Order(1)
    void read_calledWithValidId_isSuccess() {
        var section = sectionService.read(1);
        assertEquals(1, section.getId());
    }

    @Test
    @Order(2)
    void read_calledWithInvalidId_throwsSectionNotFoundException() {
        assertThrows(SectionNotFoundException.class, () -> sectionService.read(0));
    }

    @Test
    void readForUpdate_calledWithValidId_isSuccess() {
        var section = sectionService.readForUpdate(1);
        assertEquals(1, section.getId());
    }

    @Test
    void readForUpdate_calledWithInvalidId_throwsSectionNotFoundException() {
        assertThrows(SectionNotFoundException.class, () -> sectionService.readForUpdate(0));
    }

    @Test
    void create_calledWithValidData_isSuccess() {
        var newSection = new Section("New section");
        var createdSection = sectionService.create(newSection);
        assertEquals(newSection.getName(), createdSection.getName());
    }

    @Test
    void create_calledWithExistingName_throwsSectionNameAlreadyExistsException() {
        var newSection = new Section("Terrace");
        assertThrows(SectionNameAlreadyExistsException.class, () -> sectionService.create(newSection));
    }

    @Test
    @Order(3)
    void update_calledWithValidData_isSuccess() {
        sectionService.update(1, "New name");
        assertEquals("New name", sectionService.read(1).getName());
    }

    @Test
    void update_calledWithExistingName_throwsSectionNameAlreadyExistsException() {
        assertThrows(SectionNameAlreadyExistsException.class, () -> sectionService.update(1, "Terrace"));
    }

    @Test
    void update_calledWithInvalidId_throwsSectionNotFoundException() {
        assertThrows(SectionNotFoundException.class, () -> sectionService.update(0, "Irrelevant"));
    }

    @Test
    void delete_calledWhenTableIsOccupied_throwsOccupiedSectionException() {
        assertThrows(OccupiedSectionException.class, () -> sectionService.delete(4));
    }
}
