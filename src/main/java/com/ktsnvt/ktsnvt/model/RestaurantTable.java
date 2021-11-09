package com.ktsnvt.ktsnvt.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "tables")
@Where(clause = "is_active = true")
public class RestaurantTable extends BaseEntity {

    @Column(name = "number", nullable = false)
    private Integer number;

    @Column(name = "x", nullable = false)
    private Integer x;

    @Column(name = "y", nullable = false)
    private Integer y;

    @Column(name = "r", nullable = false)
    private Integer r;

    @Column(name = "ready_groups", nullable = false)
    private Integer readyGroups;

    @Column(name = "avaliable", nullable = false)
    private Boolean available;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "section_id")
    private Section section;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "restaurantTable")
    private Set<Order> orders = new HashSet<>();

    public RestaurantTable() {
        super();
    }

    public RestaurantTable(Integer number, Integer x, Integer y, Integer r, Section section) {
        this();
        this.number = number;
        this.x = x;
        this.y = y;
        this.r = r;
        this.section = section;
        this.readyGroups = 0;
        this.available = true;
    }

    public void addOrder(Order order) {
        orders.add(order);
        order.setRestaurantTable(this);
    }

    public void removeOrder(Order order) {
        orders.remove(order);
        order.setRestaurantTable(null);
    }

}
