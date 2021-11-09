package com.ktsnvt.ktsnvt.service.impl;

import com.ktsnvt.ktsnvt.exception.SectionNameAlreadyExistsException;
import com.ktsnvt.ktsnvt.model.Section;
import com.ktsnvt.ktsnvt.repository.SectionRepository;
import com.ktsnvt.ktsnvt.service.SectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Service
public class SectionServiceImpl implements SectionService {
    private final SectionRepository sectionRepository;

    @Autowired
    public SectionServiceImpl(SectionRepository sectionRepository) {
        this.sectionRepository = sectionRepository;
    }

    @Override
    public Collection<Section> getAllSections() {
        return sectionRepository.findAll();
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Section create(Section section) {
        var sectionWithSameName = sectionRepository.findByName(section.getName());
        if (sectionWithSameName.isPresent()) {
            throw new SectionNameAlreadyExistsException("Section name: " + section.getName() + " already exists.");
        }
        return sectionRepository.save(section);
    }
}
