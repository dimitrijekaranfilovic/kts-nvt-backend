package com.ktsnvt.ktsnvt.repository;

import com.ktsnvt.ktsnvt.model.InventoryItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryItemRepository extends JpaRepository<InventoryItem, Integer> {
}
