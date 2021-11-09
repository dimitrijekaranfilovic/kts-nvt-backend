package com.ktsnvt.ktsnvt.repository;

import com.ktsnvt.ktsnvt.model.InventoryItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface InventoryItemRepository extends JpaRepository<InventoryItem, Integer> {

    Optional<InventoryItem> findByName(String name);
}
