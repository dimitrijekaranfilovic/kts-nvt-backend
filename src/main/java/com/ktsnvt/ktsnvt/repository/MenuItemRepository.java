package com.ktsnvt.ktsnvt.repository;

import com.ktsnvt.ktsnvt.model.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuItemRepository extends JpaRepository<MenuItem, Integer> {
}
