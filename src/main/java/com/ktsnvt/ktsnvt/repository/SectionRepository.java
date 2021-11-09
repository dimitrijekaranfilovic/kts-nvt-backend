package com.ktsnvt.ktsnvt.repository;

import com.ktsnvt.ktsnvt.model.Section;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;
import java.util.Optional;

public interface SectionRepository extends JpaRepository<Section, Integer> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select s from Section s join fetch s.restaurantTables where s.id = :id")
    Optional<Section> findByIdWithTables(Integer id);
}
