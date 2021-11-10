package com.ktsnvt.ktsnvt.service;

import com.ktsnvt.ktsnvt.model.RestaurantTable;

import java.util.Collection;

public interface RestaurantTableService {
    RestaurantTable readForUpdate(Integer id);

    Collection<RestaurantTable> getAllTablesForSection(Integer sectionId);

    RestaurantTable createRestaurantTable(RestaurantTable newTable, Integer sectionId);

    void deleteRestaurantTable(Integer id);

    void updateTablePosition(Integer sectionId, Integer tableId, Integer newX, Integer newY);
}
