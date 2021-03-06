package com.ktsnvt.ktsnvt.service.impl;

import com.ktsnvt.ktsnvt.exception.OccupiedSectionException;
import com.ktsnvt.ktsnvt.exception.SectionNameAlreadyExistsException;
import com.ktsnvt.ktsnvt.exception.SectionNotFoundException;
import com.ktsnvt.ktsnvt.model.Section;
import com.ktsnvt.ktsnvt.repository.RestaurantTableRepository;
import com.ktsnvt.ktsnvt.repository.SectionRepository;
import com.ktsnvt.ktsnvt.service.SectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class SectionServiceImpl extends TransactionalServiceBase implements SectionService {
    private final SectionRepository sectionRepository;

    private final RestaurantTableRepository restaurantTableRepository;

    @Autowired
    public SectionServiceImpl(SectionRepository sectionRepository, RestaurantTableRepository restaurantTableRepository) {
        this.sectionRepository = sectionRepository;
        this.restaurantTableRepository = restaurantTableRepository;
    }

    @Override
    public Collection<Section> getAllSections() {
        return sectionRepository.findAllActive();
    }

    @Override
    public Section read(Integer id) {
        return sectionRepository
                .findOneById(id)
                .orElseThrow(() -> new SectionNotFoundException("Cannot find section with id: " + id));
    }

    @Override
    public Section readForUpdate(Integer id) {
        return sectionRepository
                .findByIdWithTablesForUpdate(id)
                .orElseThrow(() -> new SectionNotFoundException("Cannot find section with id: " + id));
    }

    @Override
    public Section create(Section section) {
        var sectionWithSameName = sectionRepository.findByName(section.getName());
        if (sectionWithSameName.isPresent()) {
            throw new SectionNameAlreadyExistsException("Section name: " + section.getName() + " already exists.");
        }
        return sectionRepository.save(section);
    }

    @Override
    public void update(Integer id, String name) {
        var section = readForUpdate(id);
        var sectionWithSameName = sectionRepository.findByName(name);
        sectionWithSameName.ifPresent(same -> {
            if (!same.getId().equals(section.getId())) {
                throw new SectionNameAlreadyExistsException("Section name: " + name + " already exists.");
            }
        });
        section.setName(name);
    }

    @Override
    public void delete(Integer id) {
        var section = readForUpdate(id);
        restaurantTableRepository.streamOccupiedTablesForSection(section.getId()).findAny().ifPresent(table -> {
            throw new OccupiedSectionException("Section is occupied so it can't be deleted.");
        });
        section.setIsActive(false);
    }
}
