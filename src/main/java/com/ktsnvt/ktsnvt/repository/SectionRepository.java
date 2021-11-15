package com.ktsnvt.ktsnvt.repository;

import com.ktsnvt.ktsnvt.model.Section;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.LockModeType;
import java.util.Optional;

public interface SectionRepository extends JpaRepository<Section, Integer> {

    @Query("select s from Section s where s.isActive = true")
    Optional<Section> findByName(String name);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select s from Section s join fetch s.restaurantTables where s.id = :id and s.isActive = true")
    Optional<Section> findByIdWithTablesForUpdate(Integer id);
}
