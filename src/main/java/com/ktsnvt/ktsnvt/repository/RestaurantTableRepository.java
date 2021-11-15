package com.ktsnvt.ktsnvt.repository;

import com.ktsnvt.ktsnvt.model.RestaurantTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.LockModeType;
import java.util.Collection;
import java.util.stream.Stream;
import java.util.Optional;

public interface RestaurantTableRepository extends JpaRepository<RestaurantTable, Integer> {

    @Query("select t from RestaurantTable t join t.section where t.section.id = :sectionId and t.isActive = true")
    Collection<RestaurantTable> findAllForSection(Integer sectionId);

    @Query("select t from RestaurantTable t where t.section.id = :sectionId and t.available = false and t.isActive = true")
    Stream<RestaurantTable> streamOccupiedTablesForSection(Integer sectionId);
    
    @Query("select t from RestaurantTable t join t.section where t.section.id = :sectionId and t.number = :number and t.isActive = true")
    RestaurantTable findByNumberInSection(Integer sectionId, Integer number);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select t from RestaurantTable t join fetch t.section where t.id = :id and t.isActive = true")
    Optional<RestaurantTable> findByIdForUpdate(Integer id);
}
