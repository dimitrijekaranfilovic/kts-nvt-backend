package com.ktsnvt.ktsnvt.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "base_prices")
public class BasePrice extends BaseEntity {

    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    @Column(name = "end_date", nullable = true)
    private LocalDateTime endDate;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @ManyToOne(fetch = FetchType.EAGER)
    private InventoryItem inventoryItem;

    @Version
    private Integer version;

    public BasePrice() {
        super();
    }

    public BasePrice(LocalDateTime startDate, LocalDateTime endDate, BigDecimal amount, InventoryItem inventoryItem) {
        this();
        this.startDate = startDate;
        this.endDate = endDate;
        this.amount = amount;
        this.inventoryItem = inventoryItem;
    }
}
