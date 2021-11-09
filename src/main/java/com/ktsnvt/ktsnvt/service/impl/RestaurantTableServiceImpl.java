package com.ktsnvt.ktsnvt.service.impl;

import com.ktsnvt.ktsnvt.exception.NotFoundException;
import com.ktsnvt.ktsnvt.model.RestaurantTable;
import com.ktsnvt.ktsnvt.repository.RestaurantTableRepository;
import com.ktsnvt.ktsnvt.repository.SectionRepository;
import com.ktsnvt.ktsnvt.service.RestaurantTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class RestaurantTableServiceImpl implements RestaurantTableService {

    private final RestaurantTableRepository restaurantTableRepository;
    private final SectionRepository sectionRepository;

    @Autowired
    public RestaurantTableServiceImpl(RestaurantTableRepository restaurantTableRepository,
                                      SectionRepository sectionRepository) {
        this.restaurantTableRepository = restaurantTableRepository;
        this.sectionRepository = sectionRepository;
    }

    @Override
    public Collection<RestaurantTable> getAllTablesForSection(Integer sectionId) {
        var section = sectionRepository.findById(sectionId);

        if (section.isEmpty()){
            throw new NotFoundException("Section with id " + sectionId + " does not exist.");
        }

        return restaurantTableRepository.findAllForSection(sectionId);
    }
}
