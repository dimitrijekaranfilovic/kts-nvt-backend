package com.ktsnvt.ktsnvt.service;

import com.ktsnvt.ktsnvt.model.Section;

import java.util.Collection;

public interface SectionService {
    Collection<Section> getAllSections();

    Section readForUpdate(Integer id);

    Section create(Section section);

    void update(Integer id, String name);
}
