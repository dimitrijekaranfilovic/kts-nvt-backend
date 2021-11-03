package com.ktsnvt.ktsnvt.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@javax.persistence.Table(name = "tables")
public class Table extends BaseEntity{

    @Column(name = "number", nullable = false)
    private Integer number;

    @Column(name = "x", nullable = false)
    private Double x;

    @Column(name = "y", nullable = false)
    private Double y;

    @Column(name = "r", nullable = false)
    private Double r;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinTable(name = "section_id")
    private Section section;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "table")
    private Set<Order> orders = new HashSet<>();

    public void addOrder(Order order){
        orders.add(order);
        order.setTable(this);
    }

    public void removeOrder(Order order){
        orders.remove(order);
        order.setTable(null);
    }

}
