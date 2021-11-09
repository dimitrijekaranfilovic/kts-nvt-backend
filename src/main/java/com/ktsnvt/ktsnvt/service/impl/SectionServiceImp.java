package com.ktsnvt.ktsnvt.service.impl;

import com.ktsnvt.ktsnvt.model.Section;
import com.ktsnvt.ktsnvt.repository.SectionRepository;
import com.ktsnvt.ktsnvt.service.SectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class SectionServiceImp implements SectionService {

    private final SectionRepository sectionRepository;

    @Autowired
    public SectionServiceImp(SectionRepository sectionRepository) {
        this.sectionRepository = sectionRepository;
    }

    @Override
    public Collection<Section> getAllSections() {
        return sectionRepository.findAll();
    }
}
