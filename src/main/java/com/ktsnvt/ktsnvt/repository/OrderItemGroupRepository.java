package com.ktsnvt.ktsnvt.repository;

import com.ktsnvt.ktsnvt.model.OrderItemGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface OrderItemGroupRepository extends JpaRepository<OrderItemGroup, Integer> {

    @Query("select oig from OrderItemGroup oig where oig.name like :groupName and oig.order.id = :orderId")
    Optional<OrderItemGroup> getGroupByNameAndOrderId(@Param("orderId") Integer orderId, @Param("groupName") String groupName);

    @Query("select oig from OrderItemGroup oig join fetch oig.orderItems where oig.id = :groupId and oig.order.id = :orderId")
    Optional<OrderItemGroup> getGroupByIdAndOrderId(@Param("orderId") Integer orderId, @Param("groupId") Integer groupId);

    //if list is used, then when using join fetch hibernate returns duplicates
    @Query("select oig from OrderItemGroup oig join fetch oig.orderItems where oig.order.id = :orderId")
    Set<OrderItemGroup> getOrderItemGroupsForOrder(@Param("orderId") Integer orderId);
}
