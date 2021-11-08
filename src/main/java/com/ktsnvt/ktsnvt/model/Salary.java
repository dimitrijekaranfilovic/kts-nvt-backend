package com.ktsnvt.ktsnvt.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.Table;
import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "salaries")
@Where(clause = "is_active = true")
public class Salary extends BaseEntity {

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = true)
    private LocalDate endDate;

    @Column(name = "amount", nullable = false)
    private Double amount;

    @ManyToOne(fetch = FetchType.EAGER)
    private User user;

    public Salary() {
        super();
    }

    public Salary(LocalDate startDate, LocalDate endDate, Double amount, User user) {
        this();
        this.startDate = startDate;
        this.endDate = endDate;
        this.amount = amount;
        this.user = user;
    }

}
