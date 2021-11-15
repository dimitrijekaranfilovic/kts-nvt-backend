package com.ktsnvt.ktsnvt.repository;

import com.ktsnvt.ktsnvt.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.stream.Stream;

public interface OrderRepository extends JpaRepository<Order, Integer> {

    // Order status: CREATED or IN_PROGRESS 
    @Query("select o from Order o where o.waiter.id = :id and (o.status = 0 or o.status = 1) and o.isActive = true")
    Stream<Order> streamAssignedActiveOrdersForEmployee(Integer id);

    // Order status: CHARGED
    @Query("select o from Order o where o.status = 2 and o.servedAt >= :from and o.servedAt < :to and o.isActive = true")
    Stream<Order> streamChargedOrdersInTimeRange(LocalDateTime from, LocalDateTime to);
}
