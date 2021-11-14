package com.ktsnvt.ktsnvt.model;

import com.ktsnvt.ktsnvt.model.enums.ItemCategory;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "inventory_items")
public class InventoryItem extends BaseEntity {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "image", nullable = false)
    private String image;

    @Column(name = "allergies", nullable = false)
    private String allergies;

    @Column(name = "category", nullable = false)
    private ItemCategory category;

    @Column(name = "current_base_price", nullable = false)
    private BigDecimal currentBasePrice;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<BasePrice> basePrices = new HashSet<>();

    @Column(name = "is_in_menu", nullable = false)
    private Boolean isInMenu;

    @Version
    private Integer version;

    public InventoryItem() {
        super();
        this.currentBasePrice = BigDecimal.ZERO;
        this.isInMenu = Boolean.FALSE;
    }

    public InventoryItem(String name, String description,
                         String image, String allergies, ItemCategory category) {
        this();
        this.name = name;
        this.description = description;
        this.image = image;
        this.allergies = allergies;
        this.category = category;
    }

    public InventoryItem(String name, String description,
                         String image, String allergies, ItemCategory category, Boolean isInMenu) {
        this();
        this.name = name;
        this.description = description;
        this.image = image;
        this.allergies = allergies;
        this.category = category;
        this.isInMenu = isInMenu;
    }

    public void addBasePrice(BasePrice basePrice) {
        basePrices.add(basePrice);
        currentBasePrice = basePrice.getAmount();
        basePrice.setInventoryItem(this);
    }

    public void addMenuItem(MenuItem menuItem) {
        this.isInMenu = Boolean.TRUE;
        menuItem.setItem(this);
    }

}
