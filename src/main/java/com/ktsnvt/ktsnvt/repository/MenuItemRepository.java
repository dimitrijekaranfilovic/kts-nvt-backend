package com.ktsnvt.ktsnvt.repository;

import com.ktsnvt.ktsnvt.model.MenuItem;
import com.ktsnvt.ktsnvt.model.enums.ItemCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.persistence.LockModeType;
import java.math.BigDecimal;
import java.util.Optional;

public interface MenuItemRepository extends JpaRepository<MenuItem, Integer> {


    @Query(value = "select mi from " +
            "MenuItem mi where " +
            "(lower(mi.item.name) like concat('%', lower(:name), '%') or :name is null) and (mi.endDate is null)" +
            " and mi.isActive = true",
            countQuery = "select count(mi) from " +
                    "MenuItem mi where " +
                    "(lower(mi.item.name) like concat('%', lower(:name), '%') or :name is null) and (mi.endDate is null)" +
                    " and mi.isActive = true"
    )
    Page<MenuItem> getMenuItems(@Param("name") String name, Pageable pageable);

    @Lock(LockModeType.OPTIMISTIC)
    @Query(value = "select mi from MenuItem mi" +
            " where mi.item.id = :inventoryItemId" +
            " and mi.endDate is null " +
            " and mi.isActive = true")
    Optional<MenuItem> findActiveForInventoryItem(Integer inventoryItemId);


    @Lock(LockModeType.OPTIMISTIC)
    @Query(value = "select mi from MenuItem mi where mi.id = :id and mi.isActive = true")
    Optional<MenuItem> findOneForUpdate(Integer id);

    @Query("select i from MenuItem i " +
            "where ((lower(i.item.name) like concat('%', :query, '%'))" +
            " or lower(i.item.description) like concat('%', :query, '%')" +
            " or lower(i.item.allergies) like concat('%', :query, '%'))" +
            " and (:priceFrom is null or i.price >= :priceFrom) " +
            " and (:priceTo is null or i.price <= :priceTo)" +
            " and (:itemCategory is null or i.item.category = :itemCategory)" +
            " and i.isActive = true")
    Page<MenuItem> findAll(String query, BigDecimal priceFrom, BigDecimal priceTo,
                           ItemCategory itemCategory, Pageable pageable);
}
