package com.ktsnvt.ktsnvt.repository;

import com.ktsnvt.ktsnvt.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Integer> {
}
