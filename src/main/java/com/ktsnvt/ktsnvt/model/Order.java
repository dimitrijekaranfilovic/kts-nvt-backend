package com.ktsnvt.ktsnvt.model;

import com.ktsnvt.ktsnvt.model.enums.OrderStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "orders")
public class Order extends BaseEntity {

    @Column(name = "status", nullable = false)
    private OrderStatus status;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "served_at", nullable = true)
    private LocalDateTime servedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "table_id")
    private RestaurantTable restaurantTable;

    @ManyToOne(fetch = FetchType.EAGER)
    private Employee waiter;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "order")
    private Set<OrderItemGroup> itemGroups = new HashSet<>();

    @Column(name = "total_income", nullable = true)
    private BigDecimal totalIncome = BigDecimal.ZERO;

    @Column(name = "total_cost", nullable = true)
    private BigDecimal totalCost = BigDecimal.ZERO;

    public Order() {
        super();
    }

    public Order(OrderStatus status, LocalDateTime createdAt, LocalDateTime servedAt, RestaurantTable restaurantTable, Employee waiter) {
        this();
        this.status = status;
        this.createdAt = createdAt;
        this.servedAt = servedAt;
        this.restaurantTable = restaurantTable;
        this.waiter = waiter;
    }

    public void addOrderItemGroup(OrderItemGroup group) {
        itemGroups.add(group);
        group.setOrder(this);
    }
}
