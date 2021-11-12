package com.ktsnvt.ktsnvt.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "menu_items")
@Where(clause = "is_active = true")
public class MenuItem extends BaseEntity {

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    @Column(name = "end_date", nullable = true)
    private LocalDateTime endDate;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    private InventoryItem item;

    public MenuItem() {
        super();
    }

    public MenuItem(BigDecimal price, LocalDateTime startDate, LocalDateTime endDate, InventoryItem item) {
        this();
        this.price = price;
        this.startDate = startDate;
        this.endDate = endDate;
        this.item = item;
    }
}
