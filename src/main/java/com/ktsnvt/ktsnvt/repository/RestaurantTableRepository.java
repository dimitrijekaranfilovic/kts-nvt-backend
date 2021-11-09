package com.ktsnvt.ktsnvt.repository;

import com.ktsnvt.ktsnvt.model.RestaurantTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.stream.Stream;

public interface RestaurantTableRepository extends JpaRepository<RestaurantTable, Integer> {

    @Query("select t from RestaurantTable t join t.section where t.section.id = :sectionId")
    Collection<RestaurantTable> findAllForSection(Integer sectionId);

    @Query("select t from RestaurantTable t where t.section.id = :sectionId and t.available = false")
    Stream<RestaurantTable> streamOccupiedTablesForSection(Integer sectionId);
}
