package com.ktsnvt.ktsnvt.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.persistence.Table;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "order_items")
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
}
