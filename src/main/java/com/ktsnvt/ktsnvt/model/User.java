package com.ktsnvt.ktsnvt.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.Table;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED)
@Where(clause = "is_active = true")
public abstract class User extends BaseEntity {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "surname", nullable = false)
    private String surname;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "authority_id")
    private Authority authority;

    @Column(name = "current_salary", nullable = false)
    private BigDecimal currentSalary;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Salary> salaries = new HashSet<>();

    protected User() {
        super();
        currentSalary = BigDecimal.valueOf(0);
    }

    protected User(String name, String surname, Authority authority) {
        this();
        this.name = name;
        this.surname = surname;
        this.authority = authority;
    }

    public void addSalary(Salary salary) {
        salaries.add(salary);
        currentSalary = salary.getAmount();
        salary.setUser(this);
    }

    public void removeSalary(Salary salary) {
        salaries.remove(salary);
        salary.setUser(null);
    }

}
