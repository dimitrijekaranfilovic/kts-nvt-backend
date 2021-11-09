package com.ktsnvt.ktsnvt.repository;

import com.ktsnvt.ktsnvt.model.Order;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;

public interface OrderRepository extends JpaRepository<Order, Integer> {

    // Order status: CREATED or IN_PROGRESS 
    @Query("select o from Order o where o.waiter.id = :id and (o.status = 0 or o.status = 1)")
    Stream<Order> streamAssignedActiveOrdersForEmployee(Integer id);

    @Query("select o from Order o where o.id > 1")
    Collection<Order> example(); // za potrebe testiranja

    @Query(value = "select o from Order o join fetch o.restaurantTable where o.id > 0", countQuery = "select count(o) from Order o where o.id > 0")
    Page<Order> examplePage(Pageable pageable); // za potrebe testiranja

    Optional<Order> findById(@NonNull Integer id);
}
