package com.ktsnvt.ktsnvt.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "menu_items")
public class MenuItem extends BaseEntity {

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    @Column(name = "end_date", nullable = true)
    private LocalDateTime endDate;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    private InventoryItem item;

    @Version
    private Integer version;

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

    public void deactivateMenuItem(LocalDateTime endDate) {
        this.endDate = endDate;
        this.setIsActive(Boolean.FALSE);
        if (this.item != null) {
            this.item.setIsInMenu(Boolean.FALSE);
        }
    }
}
