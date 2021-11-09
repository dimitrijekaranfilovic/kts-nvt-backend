package com.ktsnvt.ktsnvt.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.Table;
import javax.persistence.*;
import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "menu_items")
@Where(clause = "is_active = true")
public class MenuItem extends BaseEntity {

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    private InventoryItem item;

    public MenuItem() {
        super();
    }

    public MenuItem(BigDecimal price, InventoryItem item) {
        this();
        this.price = price;
        this.item = item;
    }
}
