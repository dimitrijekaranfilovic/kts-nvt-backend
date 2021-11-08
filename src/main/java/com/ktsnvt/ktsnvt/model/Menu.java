package com.ktsnvt.ktsnvt.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.Table;
import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "menus")
@Where(clause = "is_active = true")
public class Menu extends BaseEntity {

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = true)
    private LocalDate endDate;

    @OneToMany(fetch = FetchType.LAZY)
    private Set<MenuItem> menuItems = new HashSet<>();

    public Menu() {
        super();
    }

    public Menu(LocalDate startDate, LocalDate endDate) {
        this();
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
