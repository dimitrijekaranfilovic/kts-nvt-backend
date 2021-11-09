package com.ktsnvt.ktsnvt.repository;

import com.ktsnvt.ktsnvt.model.RestaurantTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface RestaurantTableRepository extends JpaRepository<RestaurantTable, Integer> {

    @Query("select t from RestaurantTable t join t.section where t.section.id = :sectionId")
    Collection<RestaurantTable> findAllForSection(Integer sectionId);
}
