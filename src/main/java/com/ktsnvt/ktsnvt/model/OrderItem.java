package com.ktsnvt.ktsnvt.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.Table;
import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "order_items")
@Where(clause = "is_active = true")
public class OrderItem extends BaseEntity {

    @Column(name = "amount", nullable = false)
    private Integer amount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orderItemGroup_id")
    private OrderItemGroup orderItemGroup;

    @ManyToOne(fetch = FetchType.LAZY)
    private Employee preparedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    private MenuItem item;

    public OrderItem() {
        super();
    }

    public OrderItem(Integer amount, OrderItemGroup group, Employee preparedBy, MenuItem item) {
        this();
        this.amount = amount;
        this.orderItemGroup = group;
        this.preparedBy = preparedBy;
        this.item = item;
    }
}
