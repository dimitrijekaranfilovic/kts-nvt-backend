package com.ktsnvt.ktsnvt.service.impl;

import com.ktsnvt.ktsnvt.model.RestaurantTable;
import com.ktsnvt.ktsnvt.repository.RestaurantTableRepository;
import com.ktsnvt.ktsnvt.service.RestaurantTableService;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class RestaurantTableServiceImpl implements RestaurantTableService {

    private final RestaurantTableRepository restaurantTableRepository;

    public RestaurantTableServiceImpl(RestaurantTableRepository restaurantTableRepository) {
        this.restaurantTableRepository = restaurantTableRepository;
    }

    @Override
    public Collection<RestaurantTable> getAllTablesForSection(Integer sectionId) {
        return restaurantTableRepository.findAllForSection(sectionId);
    }
}
