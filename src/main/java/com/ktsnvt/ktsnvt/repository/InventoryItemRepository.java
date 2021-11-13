package com.ktsnvt.ktsnvt.repository;

import com.ktsnvt.ktsnvt.model.InventoryItem;
import com.ktsnvt.ktsnvt.model.enums.ItemCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import javax.persistence.LockModeType;
import java.util.Optional;

public interface InventoryItemRepository extends JpaRepository<InventoryItem, Integer> {

    Optional<InventoryItem> findByName(String name);

    @Lock(LockModeType.OPTIMISTIC)
    @Query("select i from InventoryItem i where i.id = :id")
    Optional<InventoryItem> findOneForUpdate(Integer id);

    @Query("select i from InventoryItem i " +
            "where ((lower(i.name) like concat('%', :query, '%'))" +
            " or lower(i.description) like concat('%', :query, '%')" +
            " or lower(i.allergies) like concat('%', :query, '%'))" +
            " and (:basePriceFrom is null or i.currentBasePrice >= :basePriceFrom) " +
            " and (:basePriceTo is null or i.currentBasePrice <= :basePriceTo)" +
            " and (:itemCategory is null or i.category = :itemCategory)")
    Page<InventoryItem> findAll(String query, BigDecimal basePriceFrom, BigDecimal basePriceTo, ItemCategory itemCategory, Pageable pageable);
}
