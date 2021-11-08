package com.ktsnvt.ktsnvt.repository;

import com.ktsnvt.ktsnvt.model.OrderItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {

    @Query(value = "select oi from OrderItem oi join fetch oi.item where oi.status = 0 and oi.item.item.category = 1",
            countQuery = "select count(oi) from OrderItem oi where oi.status = 0 and oi.item.item.category = 1")
    Page<OrderItem> findAllDrinkRequests(Pageable pageable);

    @Query(value = "select oi from OrderItem oi join fetch oi.item where oi.status = 0 and oi.item.item.category = 0",
            countQuery = "select count(oi) from OrderItem oi where oi.status = 0 and oi.item.item.category = 0")
    Page<OrderItem> findAllFoodRequests(Pageable pageable);

    @Query("select oi from OrderItem oi join fetch oi.item where oi.id = :id and oi.status = 0")
    Optional<OrderItem> findOneByIdWithItemReference(Integer id);

    @Query("select oi from OrderItem oi join fetch oi.item where oi.id = :id and oi.status = 2")
    Optional<OrderItem> findOneInProgressByIdWithItemReference(Integer id);
}
