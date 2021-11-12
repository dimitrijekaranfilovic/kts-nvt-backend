package com.ktsnvt.ktsnvt.model;

import com.ktsnvt.ktsnvt.model.enums.OrderItemStatus;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.Table;
import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "order_items")
@Where(clause = "is_active = true")
public class OrderItem extends BaseEntity {

    @Column(name = "amount", nullable = false)
    private Integer amount;

    @Column(name = "sent_at", nullable = true)
    private LocalDateTime sentAt;

    @Column(name = "taken_at", nullable = true)
    private LocalDateTime takenAt;

    @Column(name = "prepared_at", nullable = true)
    private LocalDateTime preparedAt;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "orderItemGroup_id")
    private OrderItemGroup orderItemGroup;

    @ManyToOne(fetch = FetchType.EAGER)
    private Employee preparedBy;

    @ManyToOne(fetch = FetchType.EAGER)
    private MenuItem item;

    @Column(name = "status", nullable = false)
    private OrderItemStatus status;

    @Version
    private Integer version;

    public OrderItem() {
        super();
    }

    public OrderItem(Integer amount, OrderItemGroup group, Employee preparedBy, MenuItem item, OrderItemStatus status) {
        this();
        this.amount = amount;
        this.orderItemGroup = group;
        this.preparedBy = preparedBy;
        this.item = item;
        this.status = status;
    }

    public OrderItem(Integer amount, OrderItemGroup group, MenuItem item, OrderItemStatus status){
        this.amount = amount;
        this.orderItemGroup = group;
        this.item = item;
        this.status = status;
    }
}
