package com.ktsnvt.ktsnvt.model;

import com.ktsnvt.ktsnvt.model.enums.OrderItemStatus;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.math.BigDecimal;
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

    @Column(name = "current_base_price", nullable = false)
    private BigDecimal currentBasePrice;

    @Column(name = "current_menu_price", nullable = false)
    private BigDecimal currentMenuPrice;

    @Version
    private Integer version;

    public OrderItem() {
        super();
        this.currentBasePrice = BigDecimal.ZERO;
        this.currentMenuPrice = BigDecimal.ZERO;
    }

    public OrderItem(Integer amount, OrderItemGroup group, Employee preparedBy, MenuItem item, OrderItemStatus status) {
        this();
        this.amount = amount;
        this.orderItemGroup = group;
        this.preparedBy = preparedBy;
        this.item = item;
        this.status = status;
        this.currentBasePrice = item.getItem().getCurrentBasePrice();
        this.currentMenuPrice = item.getPrice();
    }

    public OrderItem(Integer amount, OrderItemGroup group, MenuItem item, OrderItemStatus status) {
        this();
        this.amount = amount;
        this.orderItemGroup = group;
        this.item = item;
        this.status = status;
        this.currentBasePrice = item.getItem().getCurrentBasePrice();
        this.currentMenuPrice = item.getPrice();
    }
}
