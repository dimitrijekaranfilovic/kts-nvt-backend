package com.ktsnvt.ktsnvt.repository;

import com.ktsnvt.ktsnvt.model.InventoryItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.LockModeType;
import java.util.Optional;

public interface InventoryItemRepository extends JpaRepository<InventoryItem, Integer> {

    Optional<InventoryItem> findByName(String name);

    @Lock(LockModeType.OPTIMISTIC)
    @Query("select i from InventoryItem i where i.id = :id")
    Optional<InventoryItem> findOneForUpdate(Integer id);
}
