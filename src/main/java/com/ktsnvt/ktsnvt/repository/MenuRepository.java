package com.ktsnvt.ktsnvt.repository;

import com.ktsnvt.ktsnvt.model.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface MenuRepository extends JpaRepository<Menu, Integer> {
}
