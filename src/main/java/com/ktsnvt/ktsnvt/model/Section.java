package com.ktsnvt.ktsnvt.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "sections")
public class Section extends BaseEntity {

    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "section")
    private Set<RestaurantTable> restaurantTables = new HashSet<>();

    public Section() {
        super();
    }

    public Section(String name) {
        this();
        this.name = name;
    }

    public void addTable(RestaurantTable restaurantTable) {
        restaurantTables.add(restaurantTable);
        restaurantTable.setSection(this);
    }

    public void removeTable(RestaurantTable restaurantTable) {
        restaurantTables.remove(restaurantTable);
        restaurantTable.setSection(null);
    }

}
