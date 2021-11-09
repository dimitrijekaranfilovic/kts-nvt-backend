package com.ktsnvt.ktsnvt.service;

import com.ktsnvt.ktsnvt.model.RestaurantTable;

import java.util.Collection;

public interface RestaurantTableService {
    Collection<RestaurantTable> getAllTablesForSection(Integer sectionId);
    void createRestaurantTable(RestaurantTable newTable, Integer sectionId);
}
