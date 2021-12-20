package com.ktsnvt.ktsnvt.repository;

import com.ktsnvt.ktsnvt.model.Section;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.LockModeType;
import java.util.Collection;
import java.util.Optional;

public interface SectionRepository extends JpaRepository<Section, Integer> {

    @Query("select s from Section s where s.id = :id and s.isActive = true")
    Optional<Section> findOneById(Integer id);

    @Query("select s from Section s where s.name = :name and s.isActive = true")
    Optional<Section> findByName(String name);

    @Query("select s from Section s where s.isActive = true")
    Collection<Section> findAllActive();

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select s from Section s where s.id = :id and s.isActive = true")
    Optional<Section> findByIdWithTablesForUpdate(Integer id);
}
