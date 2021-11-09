package com.ktsnvt.ktsnvt.repository;

import com.ktsnvt.ktsnvt.model.Section;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SectionRepository extends JpaRepository<Section, Integer> {
    Optional<Section> findByName(String name);
}
