package com.ktsnvt.ktsnvt.model;

import com.ktsnvt.ktsnvt.model.enums.ItemCategory;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "inventory_items")
@Where(clause = "is_active = true")
public class InventoryItem extends BaseEntity {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "base_price", nullable = false)
    private BigDecimal basePrice;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "image", nullable = false)
    private String image;

    @Column(name = "allergies", nullable = false)
    private String allergies;

    @Column(name = "category", nullable = false)
    private ItemCategory category;

    public InventoryItem() {
        super();
    }

    public InventoryItem(String name, BigDecimal basePrice, String description, String image, String allergies, ItemCategory category) {
        this();
        this.name = name;
        this.basePrice = basePrice;
        this.description = description;
        this.image = image;
        this.allergies = allergies;
        this.category = category;
    }

}
