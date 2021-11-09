package com.ktsnvt.ktsnvt.model;

import com.ktsnvt.ktsnvt.model.enums.OrderItemGroupStatus;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.Table;
import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "order_item_groups")
@Where(clause = "is_active = true")
public class OrderItemGroup extends BaseEntity {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "status", nullable = false)
    private OrderItemGroupStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "orderItemGroup")
    private Set<OrderItem> orderItems = new HashSet<>();

    public OrderItemGroup() {
        super();
    }

    public OrderItemGroup(String name, OrderItemGroupStatus status){
        this.name = name;
        this.status = status;
    }

    public OrderItemGroup(String name, OrderItemGroupStatus status, Order order) {
        this();
        this.name = name;
        this.status = status;
        this.order = order;
    }

    public void addItem(OrderItem item){
        orderItems.add(item);
        item.setOrderItemGroup(this);
    }

    public void removeItem(OrderItem item){
        orderItems.remove(item);
        item.setOrderItemGroup(null);
    }
}
