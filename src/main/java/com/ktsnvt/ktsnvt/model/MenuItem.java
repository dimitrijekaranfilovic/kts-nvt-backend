package com.ktsnvt.ktsnvt.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.Table;
import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "menu_items")
@Where(clause = "is_active = true")
public class MenuItem extends BaseEntity {

    @Column(name = "price", nullable = false)
    private Double price;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    private InventoryItem item;

    public MenuItem() {
        super();
    }

    public MenuItem(Double price, InventoryItem item) {
        this();
        this.price = price;
        this.item = item;
    }
}
