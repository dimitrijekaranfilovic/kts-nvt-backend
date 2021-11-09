package com.ktsnvt.ktsnvt.repository;

import com.ktsnvt.ktsnvt.model.OrderItem;
import com.ktsnvt.ktsnvt.model.OrderItemGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface OrderItemGroupRepository extends JpaRepository<OrderItemGroup, Integer> {
}
