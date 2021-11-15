package com.ktsnvt.ktsnvt.service.impl;

import com.ktsnvt.ktsnvt.exception.*;
import com.ktsnvt.ktsnvt.model.RestaurantTable;
import com.ktsnvt.ktsnvt.repository.RestaurantTableRepository;
import com.ktsnvt.ktsnvt.service.RestaurantTableService;
import com.ktsnvt.ktsnvt.service.SectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Objects;

@Service
public class RestaurantTableServiceImpl implements RestaurantTableService {
    private final RestaurantTableRepository restaurantTableRepository;

    private final SectionService sectionService;

    @Autowired
    public RestaurantTableServiceImpl(RestaurantTableRepository restaurantTableRepository,
                                      SectionService sectionService) {
        this.restaurantTableRepository = restaurantTableRepository;
        this.sectionService = sectionService;
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public RestaurantTable readForUpdate(Integer id) {
        return restaurantTableRepository
                .findByIdForUpdate(id)
                .orElseThrow(() -> new RestaurantTableNotFoundException(String.format("Restaurant table with id: %d not found.", id)));
    }

    @Override
    public Collection<RestaurantTable> getAllTablesForSection(Integer sectionId) {
        var section = sectionService.read(sectionId);
        return restaurantTableRepository.findAllForSection(section.getId());
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public RestaurantTable createRestaurantTable(RestaurantTable newTable, Integer sectionId) {
        var section = sectionService.readForUpdate(sectionId);
        var sectionTables = restaurantTableRepository.findAllForSection(sectionId);
        if(sectionTables.stream().anyMatch(t -> Objects.equals(t.getNumber(), newTable.getNumber()))){
            throw new DuplicateTableNumberException(String.format("Table with number: %d already exists in this section.", newTable.getNumber()));
        }
        if(sectionTables.stream().anyMatch(t -> intersects(t, newTable))) {
            throw new TableIntersectionException("Table is overlapping with another table.");
        }
        section.addTable(newTable);
        newTable.setAvailable(true);
        newTable.setReadyGroups(0);
        restaurantTableRepository.save(newTable);
        return newTable;
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void deleteRestaurantTable(Integer id) {
        var table = readForUpdate(id);
        throwIfNotAvailable(table);
        table.setIsActive(false);
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void updateTablePosition(Integer sectionId, Integer tableId, Integer newX, Integer newY) {
        var section = sectionService.readForUpdate(sectionId);
        var table = readForUpdate(tableId);
        var sectionTables = restaurantTableRepository.findAllForSection(sectionId);
        if (!table.getSection().getId().equals(section.getId())) {
            throw new TableSectionMismatchException(String.format("Table with id: %d does not belong to section with id: %d", tableId, sectionId));
        }
        throwIfNotAvailable(table);
        table.setX(newX);
        table.setY(newY);
        if(sectionTables.stream().anyMatch(t -> intersects(t, table))) {
            throw new TableIntersectionException("Table is overlapping with another table.");
        }
    }

    private void throwIfNotAvailable(RestaurantTable table) {
        if(Boolean.FALSE.equals(table.getAvailable())){
            throw new OccupiedTableException(String.format("Table number: %d is occupied at the moment.", table.getNumber()));
        }
    }

    private Boolean intersects(RestaurantTable t1, RestaurantTable t2) {
        if (t1.getId().equals(t2.getId())) {
            return false;
        }
        return Math.sqrt(Math.pow((t1.getX() - t2.getX()), 2) + Math.pow((t1.getY() - t2.getY()), 2)) <= (t1.getR() + t2.getR());
    }

}
