package com.ktsnvt.ktsnvt.repository;

import com.ktsnvt.ktsnvt.model.MenuItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.persistence.LockModeType;
import java.util.Optional;

public interface MenuItemRepository extends JpaRepository<MenuItem, Integer> {


    @Query(value = "select mi from MenuItem mi where lower(mi.item.name) like concat('%', lower(:name), '%') or :name is null",
            countQuery = "select count(mi) from MenuItem mi where lower(mi.item.name) like concat('%', lower(:name), '%') or :name is null")
    Page<MenuItem> getMenuItems(@Param("name") String name, Pageable pageable);

    @Lock(LockModeType.OPTIMISTIC)
    @Query(value = "select mi from MenuItem mi" +
            " where mi.item.id = :inventoryItemId" +
            " and mi.endDate is null")
    Optional<MenuItem> findActiveForInventoryItem(Integer inventoryItemId);
}
