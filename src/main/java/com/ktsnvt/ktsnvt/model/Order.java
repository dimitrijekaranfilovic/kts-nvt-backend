package com.ktsnvt.ktsnvt.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@javax.persistence.Table(name = "orders")
public class Order extends BaseEntity{

    @Column(name = "finished", nullable = false)
    private Boolean finished = false;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "served_at", nullable = false)
    private LocalDateTime servedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "table_id")
    private Table table;

    @ManyToOne(fetch = FetchType.LAZY)
    private Employee waiter;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "order")
    private Set<OrderItemGroup> itemGroups = new HashSet<>();

    public void addOrderItemGroup(OrderItemGroup group){
        itemGroups.add(group);
        group.setOrder(this);
    }

    public void removeOrderItemGroup(OrderItemGroup group){
        itemGroups.remove(group);
        group.setOrder(null);
    }
}
