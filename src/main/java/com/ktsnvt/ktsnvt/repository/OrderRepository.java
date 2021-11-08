package com.ktsnvt.ktsnvt.repository;

import com.ktsnvt.ktsnvt.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;

public interface OrderRepository extends JpaRepository<Order, Integer> {

    @Query("select o from Order o where o.id > 1")
    Collection<Order> example(); // za potrebe testiranja

    @Query(value = "select o from Order o join fetch o.restaurantTable where o.id > 0", countQuery = "select count(o) from Order o where o.id > 0")
    Page<Order> examplePage(Pageable pageable); // za potrebe testiranja
}
