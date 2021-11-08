package com.ktsnvt.ktsnvt.repository;

import com.ktsnvt.ktsnvt.model.RestaurantTable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TableRepository extends JpaRepository<RestaurantTable, Integer> {
}
