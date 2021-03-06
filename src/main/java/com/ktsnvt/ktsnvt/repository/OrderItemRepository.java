package com.ktsnvt.ktsnvt.repository;

import com.ktsnvt.ktsnvt.model.OrderItem;
import com.ktsnvt.ktsnvt.model.enums.ItemCategory;
import com.ktsnvt.ktsnvt.model.enums.OrderItemStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.LockModeType;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;

public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {

    @Query(value = "select oi from OrderItem oi where oi.status = :status and oi.item.item.category = :category and oi.isActive = true")
    Page<OrderItem> findAllItemRequests(Pageable pageable, OrderItemStatus status, ItemCategory category);

    @Lock(LockModeType.OPTIMISTIC)
    @Query("select oi from OrderItem oi where oi.id = :id and oi.status = 0 and oi.isActive = true")
    Optional<OrderItem> findOneByIdWithItemReference(Integer id);

    @Lock(LockModeType.OPTIMISTIC)
    @Query("select oi from OrderItem oi where oi.id = :id and (oi.status = 1 or oi.status = 0) and oi.isActive = true")
    Optional<OrderItem> findOneInProgressByIdWithItemReference(Integer id);

    @Query("select oi from OrderItem oi where oi.orderItemGroup.id = :groupId")
    Collection<OrderItem> getAllFromOneGroup(Integer groupId);

    // Order item status != DONE
    @Query("select oi from OrderItem oi where oi.preparedBy.id = :id and oi.status <> 2 and oi.isActive = true")
    Stream<OrderItem> streamActiveOrderItemsForEmployee(Integer id);

    // Order item status != DONE
    @Query("select oi from OrderItem oi where oi.item.id = :id and oi.status <> 2 and oi.isActive = true")
    Stream<OrderItem> streamActiveOrderItemsForMenuItem(Integer id);

    // Order item status != DONE
    @Query("select oi from OrderItem oi where oi.item.item.id = :id and oi.status <> 2 and oi.isActive = true")
    Stream<OrderItem> streamActiveOrderItemsForInventoryItem(Integer id);
}
